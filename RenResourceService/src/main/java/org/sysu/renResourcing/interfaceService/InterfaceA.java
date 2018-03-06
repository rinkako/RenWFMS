/*
 * Project Ren @ 2018
 * Rinkako, Ariana, Gordan. SYSU SDCS.
 */
package org.sysu.renResourcing.interfaceService;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.sysu.renCommon.enums.LogLevelType;
import org.sysu.renCommon.enums.RServiceType;
import org.sysu.renCommon.enums.WorkitemDistributionType;
import org.sysu.renCommon.enums.WorkitemStatusType;
import org.sysu.renCommon.interactionRouter.LocationContext;
import org.sysu.renCommon.utility.HttpClientUtil;
import org.sysu.renCommon.utility.SerializationUtil;
import org.sysu.renResourcing.GlobalContext;
import org.sysu.renResourcing.RScheduler;
import org.sysu.renResourcing.context.ResourcingContext;
import org.sysu.renResourcing.context.TaskContext;
import org.sysu.renResourcing.context.WorkitemContext;
import org.sysu.renResourcing.context.steady.RenRuntimerecordEntity;
import org.sysu.renResourcing.plugin.AgentNotifyPlugin;
import org.sysu.renResourcing.plugin.AsyncPluginRunner;
import org.sysu.renResourcing.utility.HibernateUtil;
import org.sysu.renResourcing.utility.LogUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

/**
 * Author: Rinkako
 * Date  : 2018/2/9
 * Usage : Implementation of Interface A of Resource Service.
 *         Interface A is responsible for process load and unload, launching, and other
 *         service requests passing from NameService or BOEngine.
 */
public class InterfaceA {

    /**
     * Handle resourcing submission request from BO Engine.
     * @param rtid process runtime record id
     * @param boName task belong to BO name
     * @param nodeId producer tree node global id
     * @param polymorphismName task name defined in BO XML.
     * @param arguments arguments list in JSON string
     * @return response package
     */
    public static String EngineSubmitTask(String rtid, String boName, String nodeId, String polymorphismName, String arguments) {
        Session session = HibernateUtil.GetLocalSession();
        Transaction transaction = session.beginTransaction();
        boolean cmtFlag = false;
        try {
            RenRuntimerecordEntity rre = session.get(RenRuntimerecordEntity.class, rtid);
            assert rre != null;
            transaction.commit();
            cmtFlag = true;
            TaskContext taskContext = TaskContext.GetContext(rtid, boName, polymorphismName);
            assert taskContext != null;
            Hashtable<String, Object> args = new Hashtable<>();
            ArrayList argVector = SerializationUtil.JsonDeserialization(arguments, ArrayList.class);
            assert argVector != null;
            args.put("taskContext", taskContext);
            args.put("nodeId", nodeId);
            args.put("taskArgumentsVector", argVector);
            ResourcingContext ctx = ResourcingContext.GetContext(null, rtid, RServiceType.SubmitResourcingTask, args);
            InterfaceA.mainScheduler.Schedule(ctx);
            return GlobalContext.RESPONSE_SUCCESS;
        }
        catch (Exception ex) {
            if (!cmtFlag) {
                transaction.rollback();
            }
            LogUtil.Log("Exception in EngineSubmitTask, " + ex, InterfaceA.class.getName(),
                    LogLevelType.ERROR, rtid);
            throw ex;  // rethrow to cause exception response
        }
        finally {
            HibernateUtil.CloseLocalSession();
        }
    }

    /**
     * Handle resourcing submission request from BO Engine.
     * @param rtid process runtime record id
     * @param successFlag process finish status
     * @return response package
     */
    public static String EngineFinishProcess(String rtid, String successFlag) {
        Hashtable<String, Object> args = new Hashtable<>();
        args.put("rtid", rtid);
        args.put("successFlag", successFlag == null ? "1" : successFlag);
        ResourcingContext ctx = ResourcingContext.GetContext(null, rtid, RServiceType.FinishProcess, args);
        InterfaceA.mainScheduler.Schedule(ctx);
        return GlobalContext.RESPONSE_SUCCESS;
    }

    /**
     * Handle callback and hook notification when workitem status changed.
     * @param statusType destination status type
     * @param workitem workitem context
     */
    public static void HandleCallbackAndHook(WorkitemStatusType statusType, WorkitemContext workitem, TaskContext task) throws Exception {
        String rtid = workitem.getEntity().getRtid();
        String bo = workitem.getEntity().getCallbackNodeId();
        // events
        List<String> callbacks = task.getCallbackEventsOfStatus(statusType);
        for (String cb : callbacks) {
            HashMap<String, String> argsMap = new HashMap<>();
            argsMap.put("rtid", rtid);
            argsMap.put("bo", bo);
            argsMap.put("on", statusType.name());
            argsMap.put("event", cb);
            argsMap.put("payload", "");  // todo
            GlobalContext.Interaction.Send(LocationContext.URL_BOENGINE_CALLBACK, argsMap, rtid);
        }
        // hooks
        List<String> hooks = task.getCallbackHooksOfStatus(statusType);
        for (String hk : hooks) {
            HashMap<String, String> argsMap = new HashMap<>();
            argsMap.put("rtid", rtid);
            argsMap.put("bo", bo);
            argsMap.put("on", statusType.name());
            argsMap.put("payload", "");  // todo
            // NOTICE: here does not internal interaction, DO NOT use interaction router!
            try {
                HttpClientUtil.SendPost(hk, argsMap, rtid);
            }
            // just jump those failed to send, do not throw exception to stop all
            catch (Exception ex) {
                LogUtil.Log(String.format("Cannot handle hook (%s) for workitem %s, %s", hk, workitem.getEntity().getWid(), ex),
                        InterfaceA.class.getName(), LogLevelType.ERROR, rtid);
            }
        }
    }

    /**
     * Main scheduler reference.
     */
    private static RScheduler mainScheduler = RScheduler.GetInstance();
}
