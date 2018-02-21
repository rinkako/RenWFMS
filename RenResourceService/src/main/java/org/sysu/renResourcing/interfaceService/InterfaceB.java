/*
 * Project Ren @ 2018
 * Rinkako, Ariana, Gordan. SYSU SDCS.
 */
package org.sysu.renResourcing.interfaceService;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.sysu.renResourcing.GlobalContext;
import org.sysu.renResourcing.basic.enums.*;
import org.sysu.renResourcing.consistency.ContextLockManager;
import org.sysu.renResourcing.context.*;
import org.sysu.renResourcing.context.steady.RenRuntimerecordEntity;
import org.sysu.renResourcing.context.steady.RenWorkitemEntity;
import org.sysu.renResourcing.executor.AllocateInteractionExecutor;
import org.sysu.renResourcing.executor.OfferInteractionExecutor;
import org.sysu.renResourcing.plugin.AgentNotifyPlugin;
import org.sysu.renResourcing.plugin.AsyncPluginRunner;
import org.sysu.renResourcing.principle.PrincipleParser;
import org.sysu.renResourcing.principle.RPrinciple;
import org.sysu.renResourcing.utility.HibernateUtil;
import org.sysu.renResourcing.utility.LogUtil;

import java.sql.Timestamp;
import java.util.*;

/**
 * Author: Rinkako
 * Date  : 2018/2/9
 * Usage : Implementation of Interface B of Resource Service.
 *         Interface B is responsible for control workitems life-cycle, and provide
 *         workqueue operations for participants.
 */
public class InterfaceB {

    /**
     * Handle perform submit task.
     * @param ctx rs context
     */
    public static void PerformEngineSubmitTask(ResourcingContext ctx) throws Exception {
        LinkedHashMap mapTaskCtx = (LinkedHashMap) ctx.getArgs().get("taskContext");
        TaskContext taskContext = TaskContext.ParseHashMap(mapTaskCtx);
        // use runtime record to get the admin auth name for admin queue identifier
        RenRuntimerecordEntity runtimeRecord;
        Session session = HibernateUtil.GetLocalSession();
        Transaction transaction = session.beginTransaction();
        try {
            runtimeRecord = session.get(RenRuntimerecordEntity.class, ctx.getRtid());
            assert runtimeRecord != null;
            transaction.commit();
        }
        catch (Exception ex) {
            transaction.rollback();
            LogUtil.Log("PerformEngineSubmitTask get Runtime record failed. " + ex,
                    InterfaceB.class.getName(), LogUtil.LogLevelType.ERROR, ctx.getRtid());
            throw ex;
        }
        finally {
            HibernateUtil.CloseLocalSession();
        }
        // get auth user name, session like "AUTH_admin_c880d4c9-934c-4d73-9006-22e588400000"
        String adminQueuePostfix = runtimeRecord.getSessionId().split("_")[1];
        assert taskContext != null;
        RPrinciple principle = PrincipleParser.Parse(taskContext.getPrinciple());
        if (principle == null) {
            LogUtil.Log(String.format("Cannot parse principle %s", taskContext.getPrinciple()), InterfaceB.class.getName(),
                    LogUtil.LogLevelType.ERROR, ctx.getRtid());
            return;
        }
        // generate workitem
        WorkitemContext workitem = WorkitemContext.GenerateContext(taskContext, ctx.getRtid(), (ArrayList) ctx.getArgs().get("taskArgumentsVector"));
        assert workitem != null;
        // get valid resources
        HashSet<ParticipantContext> validParticipants = InterfaceO.GetParticipantByBRole(ctx.getRtid(), taskContext.getBrole());
        if (validParticipants.isEmpty()) {
            LogUtil.Log("A task cannot be allocated to any valid resources, so it will be put into admin unoffered queue.",
                    InterfaceB.class.getName(), LogUtil.LogLevelType.WARNING, ctx.getRtid());
            // move to admin queue
            WorkQueueContainer adminContainer = WorkQueueContainer.GetContext(GlobalContext.WORKQUEUE_ADMIN_PREFIX + adminQueuePostfix);
            adminContainer.AddToQueue(workitem, WorkQueueType.UNOFFERED);
            return;
        }
        switch (principle.getDistributionType()) {
            case Allocate:
                // create an allocate interaction
                AllocateInteractionExecutor allocateInteraction = new AllocateInteractionExecutor(
                        taskContext.getTaskId(), InitializationByType.SYSTEM_INITIATED);
                // create an allocator for task principle
                allocateInteraction.BindingAllocator(principle, ctx.getRstid(), ctx.getRtid());
                // do allocate to select a participant for handle this workitem
                ParticipantContext chosenOne = allocateInteraction.PerformAllocation(validParticipants, workitem);
                // put workitem to the chosen participant allocated queue
                WorkQueueContainer container = WorkQueueContainer.GetContext(chosenOne.getWorkerId());
                container.AddToQueue(workitem, WorkQueueType.ALLOCATED);
                // notify if agent
                if (chosenOne.getWorkerType() == WorkerType.Agent) {
                    AgentNotifyPlugin allocateAnp = new AgentNotifyPlugin();
                    HashMap<String, String> allocateNotifyMap = new HashMap<>();
                    allocateNotifyMap.put(GlobalContext.NOTIFICATION_AGENT_ACTION, WorkitemDistributionType.Allocate.name());
                    allocateAnp.AddNotification(chosenOne, allocateNotifyMap, ctx.getRtid());
                    AsyncPluginRunner.AsyncRun(allocateAnp);
                }
                // change workitem status
                workitem.getEntity().setFiringTime(new Timestamp(System.currentTimeMillis()));
                WorkitemContext.SaveToSteady(workitem);
                InterfaceB.WorkitemChanged(workitem, WorkitemStatusType.Fired, WorkitemResourcingStatusType.Allocated);
                break;
            case Offer:
                // create a filter interaction
                OfferInteractionExecutor offerInteraction = new OfferInteractionExecutor(
                        taskContext.getTaskId(), InitializationByType.SYSTEM_INITIATED);
                // create a filter for task principle
                offerInteraction.BindingFilter(principle, ctx.getRstid(), ctx.getRtid());
                // do filter to select a set of participants for this workitem
                Set<ParticipantContext> chosenSet = offerInteraction.PerformOffer(validParticipants, workitem);
                // put workitem to chosen participants offered queue
                AgentNotifyPlugin offerAnp = new AgentNotifyPlugin();
                HashMap<String, String> offerNotifyMap = new HashMap<>();
                offerNotifyMap.put(GlobalContext.NOTIFICATION_AGENT_ACTION, WorkitemDistributionType.Offer.name());
                for (ParticipantContext oneInSet : chosenSet) {
                    WorkQueueContainer oneInSetContainer = WorkQueueContainer.GetContext(oneInSet.getWorkerId());
                    oneInSetContainer.AddToQueue(workitem, WorkQueueType.OFFERED);
                    // notify if agent
                    if (oneInSet.getWorkerType() == WorkerType.Agent) {
                        offerAnp.AddNotification(oneInSet, offerNotifyMap, ctx.getRtid());
                    }
                }
                if (offerAnp.Count(ctx.getRtid()) > 0) {
                    AsyncPluginRunner.AsyncRun(offerAnp);
                }
                // change workitem status
                workitem.getEntity().setFiringTime(new Timestamp(System.currentTimeMillis()));
                WorkitemContext.SaveToSteady(workitem);
                InterfaceB.WorkitemChanged(workitem, WorkitemStatusType.Fired, WorkitemResourcingStatusType.Offered);
                break;
            case AutoAllocateIfOfferFailed:
                // todo not implementation
                break;
            default:
                throw new IllegalArgumentException();
        }
    }

    /**
     * Handle a participant accept a workitem.
     * @param participant participant context
     * @param workitem workitem context
     * @param initType initialization type, a flag for engine internal call
     * @return true for a successful workitem accept
     */
    public static boolean AcceptOfferedWorkitem(ParticipantContext participant, WorkitemContext workitem, InitializationByType initType) {
        // if internal call, means accept and start
        if (initType == InitializationByType.SYSTEM_INITIATED) {
            boolean result = InterfaceB.StartWorkitem(participant, workitem);
            if (!result) {
                // todo use interfaceE log to workitem log and interfaceX for exception handle
                return false;
            }
        }
        // otherwise workitem should be put to allocated queue
        else {
            WorkQueueContainer container = WorkQueueContainer.GetContext(participant.getWorkerId());
            container.RemoveFromQueue(workitem, WorkQueueType.OFFERED);
            container.AddToQueue(workitem, WorkQueueType.ALLOCATED);
            InterfaceB.WorkitemChanged(workitem, WorkitemStatusType.Fired, WorkitemResourcingStatusType.Allocated);
        }
        // todo notify if agent
        return true;
    }

    /**
     * Handle a participant deallocate a workitem.
     * @param participant participant context
     * @param workitem workitem context
     * @return true for a successful workitem deallocate
     */
    public static boolean DeallocateWorkitem(ParticipantContext participant, WorkitemContext workitem) {
        if (InterfaceO.CheckPrivilege(participant, workitem, PrivilegeType.CAN_DEALLOCATE)) {
            try {
                WorkQueueContainer container = WorkQueueContainer.GetContext(participant.getWorkerId());
                container.RemoveFromQueue(workitem, WorkQueueType.ALLOCATED);
                container.AddToQueue(workitem, WorkQueueType.OFFERED);
                InterfaceB.WorkitemChanged(workitem, WorkitemStatusType.Fired, WorkitemResourcingStatusType.Offered);
                return true;
            }
            catch (Exception ex) {
                // todo use interfaceE log to workitem log and interfaceX for exception handle
                return false;
            }
        }
        else {
            LogUtil.Log(String.format("Participant %s(%s) try to deallocate %s, but no privilege.", participant.getDisplayName(), participant.getWorkerId(), workitem.getEntity().getWid()),
                    InterfaceB.class.getName(), LogUtil.LogLevelType.UNAUTHORIZED, workitem.getEntity().getRtid());
            return false;
        }
    }

    /**
     * Handle a participant start a workitem.
     * @param participant participant context
     * @param workitem workitem context
     * @return true for a successful workitem start
     */
    public static boolean StartWorkitem(ParticipantContext participant, WorkitemContext workitem) {
        try {
            WorkQueueContainer container = WorkQueueContainer.GetContext(participant.getWorkerId());
            container.RemoveFromQueue(workitem, WorkQueueType.ALLOCATED);
            container.AddToQueue(workitem, WorkQueueType.STARTED);
            RenWorkitemEntity rwe = workitem.getEntity();
            rwe.setLatestStartTime(new Timestamp(System.currentTimeMillis()));
            rwe.setStartTime(new Timestamp(System.currentTimeMillis()));
            rwe.setStartedBy(participant.getWorkerId());
            WorkitemContext.SaveToSteady(workitem);
            // already started
            if (workitem.getEntity().getStatus().equals(WorkitemStatusType.Executing.name())) {
                InterfaceB.WorkitemChanged(workitem, WorkitemStatusType.Executing, WorkitemResourcingStatusType.Started);
                return true;
            }
            // start by admin
            if (workitem.getEntity().getResourceStatus().equals(WorkitemResourcingStatusType.Unoffered.name())) {
                RenRuntimerecordEntity runtimeRecord;
                Session session = HibernateUtil.GetLocalSession();
                Transaction transaction = session.beginTransaction();
                try {
                    runtimeRecord = session.get(RenRuntimerecordEntity.class, workitem.getEntity().getRtid());
                    assert runtimeRecord != null;
                    transaction.commit();
                } catch (Exception ex2) {
                    transaction.rollback();
                    LogUtil.Log("ParticipantStart get Runtime record failed. " + ex2,
                            InterfaceB.class.getName(), LogUtil.LogLevelType.ERROR, workitem.getEntity().getRtid());
                    return false;
                }
                finally {
                    HibernateUtil.CloseLocalSession();
                }
                // get admin queue for this auth user
                String adminQueuePostfix = runtimeRecord.getSessionId().split("_")[1];
                WorkQueueContainer adminContainer = WorkQueueContainer.GetContext(GlobalContext.WORKQUEUE_ADMIN_PREFIX + adminQueuePostfix);
                adminContainer.RemoveFromQueue(workitem, WorkQueueType.UNOFFERED);
            }
            InterfaceB.WorkitemChanged(workitem, WorkitemStatusType.Executing, WorkitemResourcingStatusType.Started);
            return true;
        }
        catch (Exception ex) {
            LogUtil.Log("ParticipantStart get Runtime record failed. " + ex,
                    InterfaceB.class.getName(), LogUtil.LogLevelType.ERROR, workitem.getEntity().getRtid());
            return false;
        }
    }

    /**
     * Handle a participant reallocate a workitem.
     * @param participant participant context
     * @param workitem workitem context
     * @return true for a successful workitem reallocate
     */
    public static boolean ReallocateWorkitem(ParticipantContext participant, WorkitemContext workitem) {
        if (InterfaceO.CheckPrivilege(participant, workitem, PrivilegeType.CAN_REALLOCATE)) {
            try {
                WorkQueueContainer container = WorkQueueContainer.GetContext(participant.getWorkerId());
                container.RemoveFromQueue(workitem, WorkQueueType.STARTED);
                container.AddToQueue(workitem, WorkQueueType.ALLOCATED);
                InterfaceB.WorkitemChanged(workitem, WorkitemStatusType.Fired, WorkitemResourcingStatusType.Allocated);
                return true;
            }
            catch (Exception ex) {
                // todo use interfaceE log to workitem log and interfaceX for exception handle
                return false;
            }
        }
        else {
            LogUtil.Log(String.format("Participant %s(%s) try to reallocate %s, but no privilege.", participant.getDisplayName(), participant.getWorkerId(), workitem.getEntity().getWid()),
                    InterfaceB.class.getName(), LogUtil.LogLevelType.UNAUTHORIZED, workitem.getEntity().getRtid());
            return false;
        }
    }

    /**
     * Handle a participant suspend a workitem.
     * @param participant participant context
     * @param workitem workitem context
     * @return true for a successful workitem suspend
     */
    public static boolean SuspendWorkitem(ParticipantContext participant, WorkitemContext workitem) {
        if (InterfaceO.CheckPrivilege(participant, workitem, PrivilegeType.CAN_SUSPEND)) {
            try {
                WorkQueueContainer container = WorkQueueContainer.GetContext(participant.getWorkerId());
                container.RemoveFromQueue(workitem, WorkQueueType.STARTED);
                container.AddToQueue(workitem, WorkQueueType.SUSPENDED);
                InterfaceB.WorkitemChanged(workitem, WorkitemStatusType.Suspended, WorkitemResourcingStatusType.Suspended);
                return true;
            }
            catch (Exception ex) {
                // todo use interfaceE log to workitem log and interfaceX for exception handle
                return false;
            }
        }
        else {
            LogUtil.Log(String.format("Participant %s(%s) try to suspend %s, but no privilege.", participant.getDisplayName(), participant.getWorkerId(), workitem.getEntity().getWid()),
                    InterfaceB.class.getName(), LogUtil.LogLevelType.UNAUTHORIZED, workitem.getEntity().getRtid());
            return false;
        }
    }

    /**
     * Handle a participant unsuspend a workitem.
     * @param participant participant context
     * @param workitem workitem context
     * @return true for a successful workitem unsuspend
     */
    public static boolean UnsuspendWorkitem(ParticipantContext participant, WorkitemContext workitem) {
        try {
            WorkQueueContainer container = WorkQueueContainer.GetContext(participant.getWorkerId());
            container.RemoveFromQueue(workitem, WorkQueueType.SUSPENDED);
            container.AddToQueue(workitem, WorkQueueType.STARTED);
            InterfaceB.WorkitemChanged(workitem, WorkitemStatusType.Executing, WorkitemResourcingStatusType.Started);
            return true;
        }
        catch (Exception ex) {
            // todo use interfaceE log to workitem log and interfaceX for exception handle
            return false;
        }
    }

    /**
     * Handle a participant skip a workitem.
     * @param participant participant context
     * @param workitem workitem context
     * @return true for a successful workitem skip
     */
    public static boolean SkipWorkitem(ParticipantContext participant, WorkitemContext workitem) {
        if (InterfaceO.CheckPrivilege(participant, workitem, PrivilegeType.CAN_SKIP)) {
            try {
                WorkQueueContainer container = WorkQueueContainer.GetContext(participant.getWorkerId());
                container.RemoveFromQueue(workitem, WorkQueueType.ALLOCATED);
                InterfaceB.WorkitemStatusChanged(workitem, WorkitemStatusType.valueOf(workitem.getEntity().getStatus()), WorkitemStatusType.Complete);
                InterfaceE.WriteLog(workitem, participant.getWorkerId(), RSEventType.skip);
                return true;
            }
            catch (Exception ex) {
                // todo use interfaceE to log workitem log and interfaceX for exception handle
                return false;
            }
        }
        else {
            LogUtil.Log(String.format("Participant %s(%s) try to skip %s, but no privilege.", participant.getDisplayName(), participant.getWorkerId(), workitem.getEntity().getWid()),
                    InterfaceB.class.getName(), LogUtil.LogLevelType.UNAUTHORIZED, workitem.getEntity().getRtid());
            return false;
        }
    }

    /**
     * Handle a participant complete a workitem.
     * @param participant participant context
     * @param workitem workitem context
     * @return true for a successful workitem complete
     */
    public static boolean CompleteWorkitem(ParticipantContext participant, WorkitemContext workitem) {
        try {
            RenWorkitemEntity rwe = workitem.getEntity();
            Timestamp currentTS = new Timestamp(System.currentTimeMillis());
            Timestamp startTS = rwe.getStartTime();
            rwe.setExecuteTime(currentTS.getTime() - startTS.getTime());
            rwe.setCompletionTime(currentTS);
            rwe.setCompletedBy(participant.getWorkerId());
            WorkitemContext.SaveToSteady(workitem);
            WorkQueueContainer container = WorkQueueContainer.GetContext(participant.getWorkerId());
            container.RemoveFromQueue(workitem, WorkQueueType.STARTED);
            InterfaceB.WorkitemStatusChanged(workitem, WorkitemStatusType.valueOf(workitem.getEntity().getStatus()), WorkitemStatusType.Complete);
            InterfaceE.WriteLog(workitem, participant.getWorkerId(), RSEventType.complete);
            return true;
        }
        catch (Exception ex) {
            // todo use interfaceE to log workitem log and interfaceX for exception handle
            return false;
        }
    }

    /**
     * Change a workitem from one status to another status.
     * NOTICE that while changing workitem status, its belonging work queue do NOT be changed.
     * @param workitem workitem context
     * @param preStatus original status
     * @param postStatus destination status
     */
    public static void WorkitemStatusChanged(WorkitemContext workitem, WorkitemStatusType preStatus, WorkitemStatusType postStatus) {
        if (preStatus == postStatus) {
            return;
        }
        InterfaceB.WorkitemChanged(workitem, postStatus, null);
    }

    /**
     * Change a workitem from one resourcing status to another resourcing status.
     * NOTICE that while changing workitem resourcing status, its belonging work queue do NOT be changed.
     * @param workitem workitem context
     * @param preStatus original status
     * @param postStatus destination status
     */
    public static void WorkitemResourcingStatusChanged(WorkitemContext workitem, WorkitemResourcingStatusType preStatus, WorkitemResourcingStatusType postStatus) {
        if (preStatus == postStatus) {
            return;
        }
        InterfaceB.WorkitemChanged(workitem, null, postStatus);
    }

    /**
     * Change a workitem from one status to another status.
     * @param workitem workitem context
     * @param toStatus destination status
     * @param toResourcingStatus destination resourcing status
     */
    public static void WorkitemChanged(WorkitemContext workitem, WorkitemStatusType toStatus, WorkitemResourcingStatusType toResourcingStatus) {
        ContextLockManager.WriteLock(workitem.getClass(), workitem.getEntity().getWid());
        try {
            if (toStatus != null) {
                workitem.getEntity().setStatus(toStatus.name());
            }
            if (toResourcingStatus != null) {
                workitem.getEntity().setResourceStatus(toResourcingStatus.name());
            }
            WorkitemContext.SaveToSteady(workitem);
            // todo use InterfaceE for logging
        }
        finally {
            ContextLockManager.WriteUnLock(workitem.getClass(), workitem.getEntity().getWid());
        }
    }
}
