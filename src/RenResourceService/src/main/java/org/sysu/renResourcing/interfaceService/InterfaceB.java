/*
 * Project Ren @ 2018
 * Rinkako, Ariana, Gordan. SYSU SDCS.
 */
package org.sysu.renResourcing.interfaceService;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.sysu.renCommon.utility.AuthDomainHelper;
import org.sysu.renResourcing.GlobalContext;
import org.sysu.renCommon.enums.*;
import org.sysu.renResourcing.consistency.ContextLockManager;
import org.sysu.renResourcing.context.*;
import org.sysu.renResourcing.context.steady.RenProcessEntity;
import org.sysu.renResourcing.context.steady.RenRsparticipantEntity;
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
import org.sysu.renCommon.utility.TimestampUtil;

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
     *
     * @param ctx rs context
     */
    public static void PerformEngineSubmitTask(ResourcingContext ctx) throws Exception {
        LinkedHashMap mapTaskCtx = (LinkedHashMap) ctx.getArgs().get("taskContext");
        String nodeId = (String) ctx.getArgs().get("nodeId");
        TaskContext taskContext = TaskContext.ParseHashMap(mapTaskCtx);
        // use runtime record to get the admin auth name for admin queue identifier
        RenRuntimerecordEntity runtimeRecord;
        Session session = HibernateUtil.GetLocalSession();
        Transaction transaction = session.beginTransaction();
        try {
            runtimeRecord = session.get(RenRuntimerecordEntity.class, ctx.getRtid());
            transaction.commit();
        } catch (Exception ex) {
            transaction.rollback();
            LogUtil.Log("PerformEngineSubmitTask get Runtime record failed. " + ex,
                    InterfaceB.class.getName(), LogLevelType.ERROR, ctx.getRtid());
            throw ex;
        } finally {
            HibernateUtil.CloseLocalSession();
        }
        String domain = AuthDomainHelper.GetDomainByRTID(runtimeRecord.getRtid());
        // generate workitem
        WorkitemContext workitem = WorkitemContext.GenerateContext(taskContext, ctx.getRtid(), (HashMap) ctx.getArgs().get("taskArgumentsVector"), nodeId);
        // parse resourcing principle
        RPrinciple principle = PrincipleParser.Parse(taskContext.getPrinciple());
        if (principle == null) {
            LogUtil.Log(String.format("Cannot parse principle %s", taskContext.getPrinciple()), InterfaceB.class.getName(),
                    LogLevelType.ERROR, ctx.getRtid());
            InterfaceX.PrincipleParseFailedRedirectToDomainPool(workitem);
            return;
        }
        // get valid resources
        HashSet<ParticipantContext> validParticipants = InterfaceO.GetParticipantByBRole(ctx.getRtid(), taskContext.getBrole());
        if (validParticipants.isEmpty()) {
            LogUtil.Log("A task cannot be allocated to any valid resources, so it will be put into admin unoffered queue.",
                    InterfaceB.class.getName(), LogLevelType.WARNING, ctx.getRtid());
            // move workitem to admin unoffered queue
            WorkQueueContainer adminContainer = WorkQueueContainer.GetContext(GlobalContext.WORKQUEUE_ADMIN_PREFIX + domain);
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
                // change workitem status
                workitem.getEntity().setFiringTime(TimestampUtil.GetCurrentTimestamp());
                InterfaceB.WorkitemChanged(workitem, WorkitemStatusType.Fired, WorkitemResourcingStatusType.Allocated, null);
                // notify if agent
                if (chosenOne.getWorkerType() == WorkerType.Agent) {
                    AgentNotifyPlugin allocateAnp = new AgentNotifyPlugin();
                    HashMap<String, String> allocateNotifyMap = new HashMap<>(WorkitemContext.GenerateResponseWorkitem(workitem));
                    allocateAnp.AddNotification(chosenOne, allocateNotifyMap, ctx.getRtid());
                    AsyncPluginRunner.AsyncRun(allocateAnp);
                }
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
                HashMap<String, String> offerNotifyMap = new HashMap<>(WorkitemContext.GenerateResponseWorkitem(workitem));
                for (ParticipantContext oneInSet : chosenSet) {
                    WorkQueueContainer oneInSetContainer = WorkQueueContainer.GetContext(oneInSet.getWorkerId());
                    oneInSetContainer.AddToQueue(workitem, WorkQueueType.OFFERED);
                    // notify if agent
                    if (oneInSet.getWorkerType() == WorkerType.Agent) {
                        offerAnp.AddNotification(oneInSet, offerNotifyMap, ctx.getRtid());
                    }
                }
                // change workitem status
                workitem.getEntity().setFiringTime(TimestampUtil.GetCurrentTimestamp());
                InterfaceB.WorkitemChanged(workitem, WorkitemStatusType.Fired, WorkitemResourcingStatusType.Offered, null);
                // do notify
                if (offerAnp.Count(ctx.getRtid()) > 0) {
                    AsyncPluginRunner.AsyncRun(offerAnp);
                }
               break;
            case AutoAllocateIfOfferFailed:
                // todo not implementation
                break;
            default:
                throw new IllegalArgumentException();
        }
    }

    /**
     * Handle perform submit task.
     *
     * @param ctx rs context
     */
    public static void PerformEngineFinishProcess(ResourcingContext ctx) {
        String rtid = (String) ctx.getArgs().get("rtid");
        String successFlag = (String) ctx.getArgs().get("successFlag");
        Session session = HibernateUtil.GetLocalSession();
        Transaction transaction = session.beginTransaction();
        try {
            RenRuntimerecordEntity rre = session.get(RenRuntimerecordEntity.class, rtid);
            rre.setFinishTimestamp(TimestampUtil.GetCurrentTimestamp());
            rre.setIsSucceed(Integer.parseInt(successFlag));
            String participantCache = rre.getParticipantCache();
            String[] participantItem = participantCache.split(",");
            for (String participantGid : participantItem) {
                // Gid is in pattern of "WorkerGlobalId:BRoleName"
                String workerId = participantGid.split(":")[0];
                RenRsparticipantEntity rpe = session.get(RenRsparticipantEntity.class, workerId);
                if (rpe != null) {
                    rpe.setReferenceCounter(rpe.getReferenceCounter() - 1);
                    if (rpe.getReferenceCounter() <= 0) {
                        session.delete(rpe);
                    }
                }
            }
            RenProcessEntity processEntity = session.get(RenProcessEntity.class, rre.getProcessId());
            processEntity.setSuccessCount(processEntity.getSuccessCount() + 1);
            transaction.commit();
        } catch (Exception ex) {
            transaction.rollback();
            LogUtil.Log("PerformEngineFinishProcess but exception occurred, " + ex, InterfaceB.class.getName(),
                    LogLevelType.ERROR, rtid);
        } finally {
            HibernateUtil.CloseLocalSession();
        }
    }

    /**
     * Handle a participant accept a workitem.
     *
     * @param participant participant context
     * @param workitem    workitem context
     * @param initType    initialization type, a flag for engine internal call
     * @param payload     payload in JSON encoded string
     * @return true for a successful workitem accept
     */
    public static boolean AcceptOfferedWorkitem(ParticipantContext participant, WorkitemContext workitem, String payload, InitializationByType initType) {
        // remove from all queue
        WorkQueueContext.RemoveFromAllQueue(workitem);
        // if internal call, means accept and start
        if (initType == InitializationByType.SYSTEM_INITIATED) {
            // write an allocated event without notification
            InterfaceB.WorkitemChanged(workitem, WorkitemStatusType.Fired, WorkitemResourcingStatusType.Allocated, payload, false);
            boolean result = InterfaceB.StartWorkitem(participant, workitem, payload);
            if (!result) {
                InterfaceX.FailedRedirectToLauncherDomainPool(workitem, "AcceptOffered by System but failed to start");
                return false;
            }
        }
        // otherwise workitem should be put to allocated queue
        else {
            WorkQueueContainer container = WorkQueueContainer.GetContext(participant.getWorkerId());
            container.MoveOfferedToAllocated(workitem);
            InterfaceB.WorkitemChanged(workitem, WorkitemStatusType.Fired, WorkitemResourcingStatusType.Allocated, payload);
        }
        // todo notify if agent
        return true;
    }

    /**
     * Handle a participant deallocate a workitem.
     *
     * @param participant participant context
     * @param workitem    workitem context
     * @param payload     payload in JSON encoded string
     * @return true for a successful workitem deallocate
     */
    public static boolean DeallocateWorkitem(ParticipantContext participant, WorkitemContext workitem, String payload) {
        if (InterfaceO.CheckPrivilege(participant, workitem, PrivilegeType.CAN_DEALLOCATE)) {
            try {
                WorkQueueContainer container = WorkQueueContainer.GetContext(participant.getWorkerId());
                container.MoveAllocatedToOffered(workitem);
                InterfaceB.WorkitemChanged(workitem, WorkitemStatusType.Fired, WorkitemResourcingStatusType.Offered, payload);
                return true;
            } catch (Exception ex) {
                InterfaceX.FailedRedirectToLauncherDomainPool(workitem, "Deallocate but exception occurred: " + ex);
                return false;
            }
        } else {
            LogUtil.Log(String.format("Participant %s(%s) try to deallocate %s, but no privilege.", participant.getDisplayName(), participant.getWorkerId(), workitem.getEntity().getWid()),
                    InterfaceB.class.getName(), LogLevelType.UNAUTHORIZED, workitem.getEntity().getRtid());
            return false;
        }
    }

    /**
     * Handle a participant start a workitem.
     *
     * @param participant participant context
     * @param workitem    workitem context
     * @param payload     payload in JSON encoded string
     * @return true for a successful workitem start
     */
    public static boolean StartWorkitem(ParticipantContext participant, WorkitemContext workitem, String payload) {
        try {
            WorkQueueContainer container = WorkQueueContainer.GetContext(participant.getWorkerId());
            container.MoveAllocatedToStarted(workitem);
            RenWorkitemEntity rwe = workitem.getEntity();
            rwe.setLatestStartTime(TimestampUtil.GetCurrentTimestamp());
            rwe.setStartTime(TimestampUtil.GetCurrentTimestamp());
            rwe.setStartedBy(participant.getWorkerId());
            WorkitemContext.SaveToSteady(workitem);
            // already started
            if (workitem.getEntity().getStatus().equals(WorkitemStatusType.Executing.name())) {
                InterfaceB.WorkitemChanged(workitem, WorkitemStatusType.Executing, WorkitemResourcingStatusType.Started, payload);
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
                            InterfaceB.class.getName(), LogLevelType.ERROR, workitem.getEntity().getRtid());
                    return false;
                } finally {
                    HibernateUtil.CloseLocalSession();
                }
                // get admin queue for this auth user
                String adminQueuePostfix = runtimeRecord.getSessionId().split("_")[1];
                WorkQueueContainer adminContainer = WorkQueueContainer.GetContext(GlobalContext.WORKQUEUE_ADMIN_PREFIX + adminQueuePostfix);
                adminContainer.RemoveFromQueue(workitem, WorkQueueType.UNOFFERED);
            }
            InterfaceB.WorkitemChanged(workitem, WorkitemStatusType.Executing, WorkitemResourcingStatusType.Started, payload);
            return true;
        } catch (Exception ex) {
            LogUtil.Log("ParticipantStart get Runtime record failed. " + ex,
                    InterfaceB.class.getName(), LogLevelType.ERROR, workitem.getEntity().getRtid());
            return false;
        }
    }

    /**
     * Handle a participant reallocate a workitem.
     *
     * @param participant participant context
     * @param workitem    workitem context
     * @param payload     payload in JSON encoded string
     * @return true for a successful workitem reallocate
     */
    public static boolean ReallocateWorkitem(ParticipantContext participant, WorkitemContext workitem, String payload) {
        if (InterfaceO.CheckPrivilege(participant, workitem, PrivilegeType.CAN_REALLOCATE)) {
            try {
                WorkQueueContainer container = WorkQueueContainer.GetContext(participant.getWorkerId());
                container.MoveStartedToAllocated(workitem);
                InterfaceB.WorkitemChanged(workitem, WorkitemStatusType.Fired, WorkitemResourcingStatusType.Allocated, payload);
                return true;
            } catch (Exception ex) {
                InterfaceX.FailedRedirectToLauncherDomainPool(workitem, "Reallocate but exception occurred: " + ex);
                return false;
            }
        } else {
            LogUtil.Log(String.format("Participant %s(%s) try to reallocate %s, but no privilege.", participant.getDisplayName(), participant.getWorkerId(), workitem.getEntity().getWid()),
                    InterfaceB.class.getName(), LogLevelType.UNAUTHORIZED, workitem.getEntity().getRtid());
            return false;
        }
    }

    /**
     * Handle a participant suspend a workitem.
     *
     * @param participant participant context
     * @param workitem    workitem context
     * @param payload     payload in JSON encoded string
     * @return true for a successful workitem suspend
     */
    public static boolean SuspendWorkitem(ParticipantContext participant, WorkitemContext workitem, String payload) {
        if (InterfaceO.CheckPrivilege(participant, workitem, PrivilegeType.CAN_SUSPEND)) {
            try {
                WorkQueueContainer container = WorkQueueContainer.GetContext(participant.getWorkerId());
                container.MoveStartedToSuspend(workitem);
                InterfaceB.WorkitemChanged(workitem, WorkitemStatusType.Suspended, WorkitemResourcingStatusType.Suspended, payload);
                return true;
            } catch (Exception ex) {
                InterfaceX.FailedRedirectToLauncherDomainPool(workitem, "Suspend but exception occurred: " + ex);
                return false;
            }
        } else {
            LogUtil.Log(String.format("Participant %s(%s) try to suspend %s, but no privilege.", participant.getDisplayName(), participant.getWorkerId(), workitem.getEntity().getWid()),
                    InterfaceB.class.getName(), LogLevelType.UNAUTHORIZED, workitem.getEntity().getRtid());
            return false;
        }
    }

    /**
     * Handle a participant unsuspend a workitem.
     *
     * @param participant participant context
     * @param workitem    workitem context
     * @param payload     payload in JSON encoded string
     * @return true for a successful workitem unsuspend
     */
    public static boolean UnsuspendWorkitem(ParticipantContext participant, WorkitemContext workitem, String payload) {
        try {
            WorkQueueContainer container = WorkQueueContainer.GetContext(participant.getWorkerId());
            container.MoveSuspendToStarted(workitem);
            InterfaceB.WorkitemChanged(workitem, WorkitemStatusType.Executing, WorkitemResourcingStatusType.Started, payload);
            return true;
        } catch (Exception ex) {
            InterfaceX.FailedRedirectToLauncherDomainPool(workitem, "Unsuspend but exception occurred: " + ex);
            return false;
        }
    }

    /**
     * Handle a participant skip a workitem.
     *
     * @param participant participant context
     * @param workitem    workitem context
     * @param payload     payload in JSON encoded string
     * @return true for a successful workitem skip
     */
    public static boolean SkipWorkitem(ParticipantContext participant, WorkitemContext workitem, String payload) {
        if (InterfaceO.CheckPrivilege(participant, workitem, PrivilegeType.CAN_SKIP)) {
            try {
                WorkQueueContainer container = WorkQueueContainer.GetContext(participant.getWorkerId());
                container.RemoveFromQueue(workitem, WorkQueueType.ALLOCATED);
                InterfaceB.WorkitemChanged(workitem, WorkitemStatusType.ForcedComplete, WorkitemResourcingStatusType.Skipped, payload);
                InterfaceE.WriteLog(workitem, participant.getWorkerId(), RSEventType.skip);
                return true;
            } catch (Exception ex) {
                InterfaceX.FailedRedirectToLauncherDomainPool(workitem, "Skip but exception occurred: " + ex);
                return false;
            }
        } else {
            LogUtil.Log(String.format("Participant %s(%s) try to skip %s, but no privilege.", participant.getDisplayName(), participant.getWorkerId(), workitem.getEntity().getWid()),
                    InterfaceB.class.getName(), LogLevelType.UNAUTHORIZED, workitem.getEntity().getRtid());
            return false;
        }
    }

    /**
     * Handle a participant complete a workitem.
     *
     * @param participant participant context
     * @param workitem    workitem context
     * @param payload     payload in JSON encoded string
     * @return true for a successful workitem complete
     */
    public static boolean CompleteWorkitem(ParticipantContext participant, WorkitemContext workitem, String payload) {
        try {
            RenWorkitemEntity rwe = workitem.getEntity();
            Timestamp currentTS = TimestampUtil.GetCurrentTimestamp();
            Timestamp startTS = rwe.getStartTime();
            rwe.setExecuteTime(currentTS.getTime() - startTS.getTime());
            rwe.setCompletionTime(currentTS);
            rwe.setCompletedBy(participant.getWorkerId());
            WorkitemContext.SaveToSteady(workitem);
            WorkQueueContainer container = WorkQueueContainer.GetContext(participant.getWorkerId());
            container.RemoveFromQueue(workitem, WorkQueueType.STARTED);
            InterfaceB.WorkitemChanged(workitem, WorkitemStatusType.Complete, WorkitemResourcingStatusType.Completed, payload);
            InterfaceE.WriteLog(workitem, participant.getWorkerId(), RSEventType.complete);
            return true;
        } catch (Exception ex) {
            InterfaceX.FailedRedirectToLauncherDomainPool(workitem, "Complete but exception occurred: " + ex);
            return false;
        }
    }

    /**
     * Change a workitem from one status to another status.
     * NOTICE that while changing workitem status, its belonging work queue do NOT be changed.
     *
     * @param workitem   workitem context
     * @param preStatus  original status
     * @param postStatus destination status
     * @param payload    payload in JSON encoded string
     */
    public static void WorkitemStatusChanged(WorkitemContext workitem, WorkitemStatusType preStatus, WorkitemStatusType postStatus, String payload) {
        if (preStatus == postStatus) {
            return;
        }
        InterfaceB.WorkitemChanged(workitem, postStatus, null, payload);
    }

    /**
     * Change a workitem from one resourcing status to another resourcing status.
     * NOTICE that while changing workitem resourcing status, its belonging work queue do NOT be changed.
     *
     * @param workitem   workitem context
     * @param preStatus  original status
     * @param postStatus destination status
     * @param payload    payload in JSON encoded string
     */
    public static void WorkitemResourcingStatusChanged(WorkitemContext workitem, WorkitemResourcingStatusType preStatus, WorkitemResourcingStatusType postStatus, String payload) {
        if (preStatus == postStatus) {
            return;
        }
        InterfaceB.WorkitemChanged(workitem, null, postStatus, payload);
    }

    /**
     * Change a workitem from one status to another status.
     *
     * @param workitem           workitem context
     * @param toStatus           destination status
     * @param toResourcingStatus destination resourcing status
     * @param payload            payload in JSON encoded string
     */
    private static void WorkitemChanged(WorkitemContext workitem, WorkitemStatusType toStatus, WorkitemResourcingStatusType toResourcingStatus, String payload) {
        InterfaceB.WorkitemChanged(workitem, toStatus, toResourcingStatus, payload, true);
    }

    /**
     * Change a workitem from one status to another status.
     *
     * @param workitem           workitem context
     * @param toStatus           destination status
     * @param toResourcingStatus destination resourcing status
     * @param payload            payload in JSON encoded string
     * @param notify             whether need to process callback and hook
     */
    private static void WorkitemChanged(WorkitemContext workitem, WorkitemStatusType toStatus, WorkitemResourcingStatusType toResourcingStatus, String payload, boolean notify) {
        // refresh changed to steady
        ContextLockManager.WriteLock(workitem.getClass(), workitem.getEntity().getWid());
        try {
            if (toStatus != null) {
                workitem.getEntity().setStatus(toStatus.name());
            }
            if (toResourcingStatus != null) {
                workitem.getEntity().setResourceStatus(toResourcingStatus.name());
            }
            WorkitemContext.SaveToSteady(workitem);
        } finally {
            ContextLockManager.WriteUnLock(workitem.getClass(), workitem.getEntity().getWid());
        }
        // handle callbacks and hooks
        if (notify) {
            try {
                InterfaceA.HandleCallbackAndHook(toStatus, workitem, workitem.getTaskContext(), payload);
            } catch (Exception ex) {
                LogUtil.Log(String.format("Workitem(%s) status changed but failed to handle callbacks and hooks, %s", workitem.getEntity().getWid(), ex),
                        InterfaceB.class.getName(), LogLevelType.ERROR, workitem.getEntity().getRtid());
                InterfaceX.NotifyException(workitem);
            }
        }
    }
}