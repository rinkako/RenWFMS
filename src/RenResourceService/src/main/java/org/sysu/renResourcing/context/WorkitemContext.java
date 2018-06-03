/*
 * Project Ren @ 2018
 * Rinkako, Ariana, Gordan. SYSU SDCS.
 */
package org.sysu.renResourcing.context;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.sysu.renCommon.enums.LogLevelType;
import org.sysu.renCommon.utility.SerializationUtil;
import org.sysu.renCommon.utility.TimestampUtil;
import org.sysu.renResourcing.GlobalContext;
import org.sysu.renCommon.enums.WorkitemResourcingStatusType;
import org.sysu.renCommon.enums.WorkitemStatusType;
import org.sysu.renResourcing.consistency.ContextCachePool;
import org.sysu.renResourcing.context.steady.RenBoEntity;
import org.sysu.renResourcing.context.steady.RenQueueitemsEntity;
import org.sysu.renResourcing.context.steady.RenRstaskEntity;
import org.sysu.renResourcing.context.steady.RenWorkitemEntity;
import org.sysu.renResourcing.interfaceService.InterfaceA;
import org.sysu.renResourcing.utility.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

/**
 * Author: Rinkako
 * Date  : 2018/2/4
 * Usage : Workitem context is an encapsulation of RenWorkitemEntity in a
 *         convenient way for resourcing service.
 */
public class WorkitemContext implements Serializable, RCacheablesContext {

    /**
     * Serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Steady entity.
     */
    private RenWorkitemEntity entity;

    /**
     * Argument dictionary.
     */
    private HashMap<String, String> argsDict;

    /**
     * Template task context.
     */
    private TaskContext taskContext;

    /**
     * Get workitem entity.
     *
     * @return workitem entity object
     */
    public RenWorkitemEntity getEntity() {
        return this.entity;
    }

    /**
     * Get workitem argument dictionary.
     *
     * @return parameter-argument hash map
     */
    public HashMap<String, String> getArgsDict() {
        return this.argsDict;
    }

    /**
     * Get the template task context of this workitem.
     *
     * @return TaskContext
     */
    public TaskContext getTaskContext() {
        return this.taskContext;
    }

    /**
     * Check if this workitem at a specific resourcing status.
     *
     * @param rtype status to be checked
     * @return true if in this resourcing status
     */
    public boolean IsAtResourcingStatus(WorkitemResourcingStatusType rtype) {
        return rtype.name().equalsIgnoreCase(this.entity.getResourceStatus());
    }

    /**
     * Generate a user-friendly workitem package.
     *
     * @param workitem workitem context
     * @return HashMap of workitem descriptor
     */
    public static HashMap<String, String> GenerateResponseWorkitem(WorkitemContext workitem) {
        HashMap<String, String> retMap = new HashMap<>();
        RenWorkitemEntity entity = workitem.getEntity();
        String workitemId = entity.getWid();
        retMap.put("Wid", workitemId);
        retMap.put("Rtid", entity.getRtid());
        retMap.put("CallbackNodeId", entity.getCallbackNodeId());
        retMap.put("TaskName", workitem.taskContext.getTaskName());
        retMap.put("TaskId", workitem.taskContext.getTaskId());
        retMap.put("Role", workitem.taskContext.getBrole());
        retMap.put("Documentation", workitem.taskContext.getDocumentation());
        retMap.put("Argument", SerializationUtil.JsonSerialization(workitem.getArgsDict()));
        retMap.put("Status", entity.getStatus());
        retMap.put("ResourceStatus", entity.getResourceStatus());
        retMap.put("EnablementTime", entity.getEnablementTime() == null ? "" : entity.getEnablementTime().toString());
        retMap.put("FiringTime", entity.getFiringTime() == null ? "" : entity.getFiringTime().toString());
        retMap.put("StartTime", entity.getStartTime() == null ? "" : entity.getStartTime().toString());
        retMap.put("CompletionTime", entity.getCompletionTime() == null ? "" : entity.getCompletionTime().toString());
        retMap.put("ExecuteTime", String.valueOf(entity.getExecuteTime()));
        Session session = HibernateUtil.GetLocalSession();
        Transaction transaction = session.beginTransaction();
        ArrayList<RenQueueitemsEntity> relations = null;
        try {
            relations = (ArrayList<RenQueueitemsEntity>) session.createQuery(String.format("FROM RenQueueitemsEntity WHERE workitemId = '%s'", workitemId)).list();
            transaction.commit();
        } catch (Exception ex) {
            LogUtil.Log("GenerateResponseWorkitem but cannot read relation from steady, " + ex,
                    WorkitemContext.class.getName(), LogLevelType.ERROR, entity.getRtid());
            transaction.rollback();
        } finally {
            HibernateUtil.CloseLocalSession();
        }
        if (relations == null) {
            retMap.put("WorkerIdList", "[]");
        } else {
            StringBuilder workerIdSb = new StringBuilder();
            workerIdSb.append("[");
            for (RenQueueitemsEntity rqe : relations) {
                String[] workqueueIdItem = rqe.getWorkqueueId().split("_");
                String workerId;
                if (workqueueIdItem.length == 4) {
                    workerId = String.format("\"%s_%s\"", workqueueIdItem[2], workqueueIdItem[3]);
                }
                // for admin queue
                else {
                    workerId = String.format("\"%s\"", workqueueIdItem[2]);
                }
                workerIdSb.append(workerId).append(",");
            }
            String workerIdList = workerIdSb.toString();
            if (workerIdList.length() > 1) {
                workerIdList = workerIdList.substring(0, workerIdList.length() - 1);
            }
            workerIdList += "]";
            retMap.put("WorkerIdList", workerIdList);
        }
        return retMap;
    }

    /**
     * Generate a list of user-friendly workitem packages.
     *
     * @param wList      list of workitem context
     * @param onlyActive whether only get active workitems
     * @return ArrayList of HashMap of workitem descriptor
     */
    public static ArrayList<HashMap<String, String>> GenerateResponseWorkitems(ArrayList<WorkitemContext> wList, boolean onlyActive) {
        ArrayList<HashMap<String, String>> retList = new ArrayList<>();
        for (WorkitemContext workitem : wList) {
            String status = workitem.getEntity().getStatus();
            if (onlyActive && (status.equals(WorkitemStatusType.Complete.name())
                    || status.equals(WorkitemStatusType.ForcedComplete.name())
                    || status.equals(WorkitemStatusType.Discarded.name()))) {
                continue;
            }
            retList.add(WorkitemContext.GenerateResponseWorkitem(workitem));
        }
        return retList;
    }

    /**
     * Get Workitem Context by RTID.
     *
     * @param rtid process rtid
     * @return ArrayList of workitem context
     */
    public static ArrayList<WorkitemContext> GetContextRTID(String rtid) {
        Session session = HibernateUtil.GetLocalSession();
        Transaction transaction = session.beginTransaction();
        boolean cmtFlag = false;
        ArrayList<WorkitemContext> retList = new ArrayList<>();
        try {
            ArrayList<RenWorkitemEntity> workitems = (ArrayList<RenWorkitemEntity>) session.createQuery(String.format("FROM RenWorkitemEntity WHERE rtid = '%s'", rtid)).list();
            transaction.commit();
            cmtFlag = true;
            for (RenWorkitemEntity rwe : workitems) {
                retList.add(WorkitemContext.GetContext(rwe.getWid(), rwe.getRtid()));
            }
            return retList;
        } catch (Exception ex) {
            if (!cmtFlag) {
                transaction.rollback();
            }
            return null;
        } finally {
            HibernateUtil.CloseLocalSession();
        }
    }

    /**
     * Get Workitem Context by Domain.
     *
     * @param domain domain name
     * @return ArrayList of workitem context
     */
    public static ArrayList<WorkitemContext> GetContextInDomain(String domain) {
        Session session = HibernateUtil.GetLocalSession();
        Transaction transaction = session.beginTransaction();
        boolean cmtFlag = false;
        ArrayList<WorkitemContext> retList = new ArrayList<>();
        try {
            ArrayList<RenWorkitemEntity> workitems = (ArrayList<RenWorkitemEntity>) session.createQuery(String.format("FROM RenWorkitemEntity WHERE LOCATE('%s', rtid) > 0", "@" + domain + "_")).list();
            transaction.commit();
            cmtFlag = true;
            for (RenWorkitemEntity rwe : workitems) {
                retList.add(WorkitemContext.GetContext(rwe.getWid(), rwe.getRtid()));
            }
            return retList;
        } catch (Exception ex) {
            if (!cmtFlag) {
                transaction.rollback();
            }
            return null;
        } finally {
            HibernateUtil.CloseLocalSession();
        }
    }

    /**
     * Get an exist workitem context from cache or steady.
     *
     * @param wid workitem global id
     * @return workitem context
     */
    public static WorkitemContext GetContext(String wid, String rtid) {
        return WorkitemContext.GetContext(wid, rtid, false);
    }

    /**
     * Get an exist workitem context.
     *
     * @param wid         workitem global id
     * @param rtid        process rtid
     * @param forceReload force reload from steady and refresh cache
     * @return workitem context
     */
    public static WorkitemContext GetContext(String wid, String rtid, boolean forceReload) {
        WorkitemContext cachedCtx = ContextCachePool.Retrieve(WorkitemContext.class, wid);
        // fetch cache
        if (cachedCtx != null && !forceReload) {
            return cachedCtx;
        }
        Session session = HibernateUtil.GetLocalSession();
        Transaction transaction = session.beginTransaction();
        boolean cmtFlag = false;
        try {
            RenWorkitemEntity rwe = session.get(RenWorkitemEntity.class, wid);
            assert rwe != null;
            String taskId = rwe.getTaskid();
            RenRstaskEntity rte = session.get(RenRstaskEntity.class, taskId);
            String taskName = rte.getPolymorphismName();
            String boId = rte.getBoid();
            RenBoEntity rbe = session.get(RenBoEntity.class, boId);
            String boName = rbe.getBoName();
            transaction.commit();
            cmtFlag = true;
            WorkitemContext retCtx = new WorkitemContext();
            retCtx.entity = rwe;
            retCtx.argsDict = SerializationUtil.JsonDeserialization(rwe.getArguments(), HashMap.class);
            retCtx.taskContext = TaskContext.GetContext(rwe.getRtid(), boName, taskName);
            ContextCachePool.AddOrUpdate(wid, retCtx);
            return retCtx;
        } catch (Exception ex) {
            if (!cmtFlag) {
                transaction.rollback();
            }
            LogUtil.Log("Get workitem context but exception occurred, " + ex,
                    WorkitemContext.class.getName(), LogLevelType.ERROR, rtid);
            throw ex;
        } finally {
            HibernateUtil.CloseLocalSession();
        }
    }

    /**
     * Generate a workitem context and save it to steady by a task context.
     *
     * @param taskContext    task context to be the generation template
     * @param rtid           process rtid
     * @param args           parameter-arguments map
     * @param callbackNodeId producer instance tree node global id for callback
     * @return workitem context
     */
    public static WorkitemContext GenerateContext(TaskContext taskContext, String rtid, HashMap args, String callbackNodeId) throws Exception {
        assert args != null && taskContext.getParameters() != null;
        //HashMap parameterMap = SerializationUtil.JsonDeserialization(taskContext.getParameters(), HashMap.class);
        if (args.size() != taskContext.getParameters().size()) {
            LogUtil.Log(String.format("Generate workitem for task %s, but arguments(%s) and parameters(%s) not equal",
                    taskContext.getTaskName(), args.size(), taskContext.getParameters().size()),
                    WorkitemContext.class.getName(), LogLevelType.WARNING, rtid);
        }
        Session session = HibernateUtil.GetLocalSession();
        Transaction transaction = session.beginTransaction();
        boolean cmtFlag = false;
        try {
            RenWorkitemEntity rwe = new RenWorkitemEntity();
            rwe.setWid(String.format("WI_%s", UUID.randomUUID().toString()));
            rwe.setRtid(rtid);
            rwe.setResourcingId(GlobalContext.RESOURCE_SERVICE_GLOBAL_ID);
            rwe.setProcessId(taskContext.getPid());
            rwe.setBoId(taskContext.getBoid());
            rwe.setTaskid(taskContext.getTaskGlobalId());
            rwe.setTaskPolymorphismId(taskContext.getTaskId());
            rwe.setStatus(WorkitemStatusType.Enabled.name());
            rwe.setResourceStatus(WorkitemResourcingStatusType.Unoffered.name());
            rwe.setExecuteTime(0L);
            rwe.setCallbackNodeId(callbackNodeId);
            rwe.setEnablementTime(TimestampUtil.GetCurrentTimestamp());
            rwe.setArguments(SerializationUtil.JsonSerialization(args));
            session.save(rwe);
            transaction.commit();
            cmtFlag = true;
            WorkitemContext wCtx = new WorkitemContext();
            wCtx.entity = rwe;
            wCtx.argsDict = (HashMap<String, String>) args;
            wCtx.taskContext = taskContext;
            // handle callback and hook
            InterfaceA.HandleCallbackAndHook(WorkitemStatusType.Enabled, wCtx, taskContext, null);
            return wCtx;
        } catch (Exception ex) {
            if (!cmtFlag) {
                transaction.rollback();
            }
            LogUtil.Log("Generate workitem context but exception occurred, " + ex,
                    WorkitemContext.class.getName(), LogLevelType.ERROR, rtid);
            throw ex;
        } finally {
            HibernateUtil.CloseLocalSession();
        }
    }

    /**
     * Save changes context to steady memory.
     *
     * @param context context to be saved
     */
    public static void SaveToSteady(WorkitemContext context) {
        if (context == null) {
            LogUtil.Log("Ignore null workitem context saving.", WorkitemContext.class.getName(),
                    LogLevelType.WARNING, "");
            return;
        }
        Session session = HibernateUtil.GetLocalSession();
        Transaction transaction = session.beginTransaction();
        try {
            session.update(context.entity);
            transaction.commit();
        } catch (Exception ex) {
            transaction.rollback();
            LogUtil.Log("Save workitem context but exception occurred, " + ex,
                    WorkitemContext.class.getName(), LogLevelType.ERROR, context.getEntity().getRtid());
        } finally {
            HibernateUtil.CloseLocalSession();
        }
    }

    /**
     * Create a new workitem context.
     * Private constructor for preventing create context outside.
     */
    private WorkitemContext() { }
}
