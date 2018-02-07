/*
 * Project Ren @ 2018
 * Rinkako, Ariana, Gordan. SYSU SDCS.
 */
package org.sysu.renResourcing;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.sysu.renResourcing.basic.enums.InitializationByType;
import org.sysu.renResourcing.basic.enums.RServiceType;
import org.sysu.renResourcing.context.ParticipantContext;
import org.sysu.renResourcing.context.ResourcingContext;
import org.sysu.renResourcing.context.TaskContext;
import org.sysu.renResourcing.context.WorkitemContext;
import org.sysu.renResourcing.context.steady.RenRuntimerecordEntity;
import org.sysu.renResourcing.executor.AllocateInteractionExecutor;
import org.sysu.renResourcing.executor.InteractionExecutor;
import org.sysu.renResourcing.principle.PrincipleParser;
import org.sysu.renResourcing.principle.RPrinciple;
import org.sysu.renResourcing.utility.CommonUtil;
import org.sysu.renResourcing.utility.HibernateUtil;
import org.sysu.renResourcing.utility.LogUtil;

import java.util.HashSet;
import java.util.Hashtable;

/**
 * Author: Rinkako
 * Date  : 2018/2/4
 * Usage : This class performs the engine controller role. All service requests are
 *         passed to engine here from RESTful API by Spring router controllers, all
 *         service requests here are in a same view, means there no any concept of
 *         HTTP request, etc. LEngine is responsible for solving the request and
 *         give a response result to return immediately.
 */
public class ResourcingEngine {

    /**
     * Handle resourcing submission request from BO Engine.
     * @param rtid process runtime record id
     * @param boName task belong to BO name
     * @param polymorphismName task name defined in BO XML.
     * @return response package
     */
    public static String EngineSubmitTask(String rtid, String boName, String polymorphismName) {
        Session session = HibernateUtil.GetLocalThreadSession();
        Transaction transaction = session.beginTransaction();
        boolean cmtFlag = false;
        try {
            RenRuntimerecordEntity rre = session.get(RenRuntimerecordEntity.class, rtid);
            assert rre != null;
            transaction.commit();
            cmtFlag = true;
            TaskContext taskContext = TaskContext.GetContext(rtid, boName, polymorphismName);
            assert taskContext != null;
            // todo task context can be cached
            Hashtable<String, Object> args = new Hashtable<>();
            args.put("taskContext", taskContext);
            ResourcingContext ctx = ResourcingContext.GetContext(null, rtid, RServiceType.SubmitResourcingTask, args);
            ResourcingEngine.mainScheduler.Schedule(ctx);
            return GlobalContext.RESPONSE_SUCCESS;
        }
        catch (Exception ex) {
            if (!cmtFlag) {
                transaction.rollback();
            }
            LogUtil.Log("Exception in EngineSubmitTask, " + ex, ResourcingEngine.class.getName(),
                    LogUtil.LogLevelType.ERROR, rtid);
            throw ex;  // rethrow to cause exception response
        }
    }


    /**
     * Internal: Handle perform submit task.
     * @param ctx rs context
     */
    public static void PerformEngineSubmitTask(ResourcingContext ctx) throws Exception {
        TaskContext taskContext = (TaskContext) ctx.getArgs().get("taskContext");
        assert taskContext != null;
        RPrinciple principle = PrincipleParser.Parse(taskContext.getPrinciple());
        switch (principle.getDistributionType()) {
            case Allocate:
                // create an allocate interaction
                AllocateInteractionExecutor allocateInteraction = new AllocateInteractionExecutor(
                        taskContext.getTaskId(), InitializationByType.SYSTEM_INITIATED);
                // create an allocator for task principle
                allocateInteraction.BindingAllocator(principle, ctx.getRstid(), ctx.getRtid());
                // get valid resources
                HashSet<ParticipantContext> validParticipants = ResourcingEngine.GetCurrentValidParticipant(ctx.getRtid());
                if (validParticipants.isEmpty()) {
                    LogUtil.Log("A task cannot be allocated to any valid resources, so it will be put into admin unoffered queue.",
                            ResourcingEngine.class.getName(), LogUtil.LogLevelType.WARNING, ctx.getRtid());
                    // todo move to admin queue -rinkako
                    return;
                }
                // generate workitem
                WorkitemContext workitem = WorkitemContext.GenerateContext(taskContext, ctx.getRtid(), taskContext.getParameters());
                // do allocate to select a participant for handle this workitem
                ParticipantContext chosenOne = allocateInteraction.PerformAllocation(validParticipants, workitem);
                // put workitem to the chosen participant allocated queue

                break;
            case Offer:
                break;
            case AutoAllocateIfOfferFailed:
                break;
            default:
                throw new IllegalArgumentException();
        }
    }

    /**
     * Get current valid participant context set.
     * Current valid means that current resources set in Name Service according to process COrgan isolation type.
     * NOTICE that participant load and unload is handled in Name Service.
     * @param rtid process rtid
     * @return a Hash set for current valid participant context
     */
    public static HashSet<ParticipantContext> GetCurrentValidParticipant(String rtid) {
        HashSet<ParticipantContext> retSet = new HashSet<>();
        Session session = HibernateUtil.GetLocalThreadSession();
        Transaction transaction = session.beginTransaction();
        boolean cmtFlag = false;
        try {
            RenRuntimerecordEntity runtimeCtx = session.get(RenRuntimerecordEntity.class, rtid);
            String participants = runtimeCtx.getParticipantCache();
            transaction.commit();
            cmtFlag = true;
            if (CommonUtil.IsNullOrEmpty(participants)) {
                return retSet;
            }
            String[] participantItem = participants.split(",");
            for (String workerId : participantItem) {
                retSet.add(ParticipantContext.GetContext(rtid, workerId));
            }
            return retSet;
        }
        catch (Exception ex) {
            if (!cmtFlag) {
                transaction.rollback();
            }
            throw ex;
        }
    }

    /**
     * Main scheduler reference.
     */
    private static RScheduler mainScheduler = RScheduler.GetInstance();
}
