/*
 * Project Ren @ 2018
 * Rinkako, Ariana, Gordan. SYSU SDCS.
 */
package org.sysu.renResourcing.interfaceService;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.sysu.renResourcing.GlobalContext;
import org.sysu.renResourcing.basic.enums.*;
import org.sysu.renResourcing.consistency.RuntimeContextLockManager;
import org.sysu.renResourcing.context.*;
import org.sysu.renResourcing.context.steady.RenRuntimerecordEntity;
import org.sysu.renResourcing.executor.AllocateInteractionExecutor;
import org.sysu.renResourcing.executor.OfferInteractionExecutor;
import org.sysu.renResourcing.plugin.AgentNotifyPlugin;
import org.sysu.renResourcing.plugin.AsyncPluginRunner;
import org.sysu.renResourcing.principle.PrincipleParser;
import org.sysu.renResourcing.principle.RPrinciple;
import org.sysu.renResourcing.utility.HibernateUtil;
import org.sysu.renResourcing.utility.LogUtil;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

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
        TaskContext taskContext = (TaskContext) ctx.getArgs().get("taskContext");
        // use runtime record to get the admin auth name for admin queue identifier
        RenRuntimerecordEntity runtimeRecord;
        Session session = HibernateUtil.GetLocalThreadSession();
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
        // get auth user name, session like "AUTH_admin_c880d4c9-934c-4d73-9006-22e588400000"
        String adminQueuePostfix = runtimeRecord.getSessionId().split("_")[1];
        assert taskContext != null;
        RPrinciple principle = PrincipleParser.Parse(taskContext.getPrinciple());
        // generate workitem
        WorkitemContext workitem = WorkitemContext.GenerateContext(taskContext, ctx.getRtid(), taskContext.getParameters());
        assert workitem != null;
        // get valid resources
        HashSet<ParticipantContext> validParticipants = InterfaceO.GetCurrentValidParticipant(ctx.getRtid());
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
                InterfaceB.WorkitemChange(workitem, WorkitemStatusType.Fired, WorkitemResourcingStatusType.Allocated);
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
                InterfaceB.WorkitemChange(workitem, WorkitemStatusType.Fired, WorkitemResourcingStatusType.Offered);
                break;
            case AutoAllocateIfOfferFailed:
                // todo not implementation
                break;
            default:
                throw new IllegalArgumentException();
        }
    }

    /**
     * Change a workitem from one status to another status.
     * NOTICE that while changing workitem status, its belonging work queue do NOT be changed.
     * @param workitem workitem context
     * @param preStatus original status
     * @param postStatus destination status
     */
    public static void WorkitemStatusChange(WorkitemContext workitem, WorkitemStatusType preStatus, WorkitemStatusType postStatus) {
        if (preStatus == postStatus) {
            return;
        }
        InterfaceB.WorkitemChange(workitem, postStatus, null);
    }

    /**
     * Change a workitem from one resourcing status to another resourcing status.
     * NOTICE that while changing workitem resourcing status, its belonging work queue do NOT be changed.
     * @param workitem workitem context
     * @param preStatus original status
     * @param postStatus destination status
     */
    public static void WorkitemResourcingStatusChange(WorkitemContext workitem, WorkitemResourcingStatusType preStatus, WorkitemResourcingStatusType postStatus) {
        if (preStatus == postStatus) {
            return;
        }
        InterfaceB.WorkitemChange(workitem, null, postStatus);
    }

    /**
     * Change a workitem from one status to another status.
     * @param workitem workitem context
     * @param toStatus destination status
     * @param toResourcingStatus destination resourcing status
     */
    public static void WorkitemChange(WorkitemContext workitem, WorkitemStatusType toStatus, WorkitemResourcingStatusType toResourcingStatus) {
        RuntimeContextLockManager.WriteLock(workitem.getClass(), workitem.getEntity().getWid());
        try {
            if (toStatus != null) {
                workitem.getEntity().setStatus(toStatus.name());
            }
            if (toResourcingStatus != null) {
                workitem.getEntity().setResourceStatus(toResourcingStatus.name());
            }
        }
        finally {
            RuntimeContextLockManager.WriteUnLock(workitem.getClass(), workitem.getEntity().getWid());
        }
    }
}
