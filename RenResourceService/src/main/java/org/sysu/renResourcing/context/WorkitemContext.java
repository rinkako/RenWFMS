/*
 * Project Ren @ 2018
 * Rinkako, Ariana, Gordan. SYSU SDCS.
 */
package org.sysu.renResourcing.context;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.sysu.renCommon.utility.CommonUtil;
import org.sysu.renCommon.utility.SerializationUtil;
import org.sysu.renCommon.utility.TimestampUtil;
import org.sysu.renResourcing.GlobalContext;
import org.sysu.renCommon.enums.WorkitemResourcingStatusType;
import org.sysu.renCommon.enums.WorkitemStatusType;
import org.sysu.renResourcing.consistency.ContextCachePool;
import org.sysu.renResourcing.context.steady.RenWorkitemEntity;
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
     * Get workitem entity.
     * @return workitem entity object
     */
    public RenWorkitemEntity getEntity() {
        return this.entity;
    }

    /**
     * Get workitem argument dictionary.
     * @return parameter-argument hash map
     */
    public HashMap<String, String> getArgsDict() {
        return this.argsDict;
    }

    /**
     * Get an exist workitem context from cache or steady.
     * @param wid workitem global id
     * @return workitem context
     */
    public static WorkitemContext GetContext(String wid, String rtid) {
        return WorkitemContext.GetContext(wid, rtid, false);
    }

    /**
     * Get an exist workitem context.
     * @param wid workitem global id
     * @param rtid process rtid
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
            transaction.commit();
            cmtFlag = true;
            WorkitemContext retCtx = new WorkitemContext();
            retCtx.entity = rwe;
            ContextCachePool.AddOrUpdate(wid, retCtx);
            return retCtx;
        }
        catch (Exception ex) {
            if (!cmtFlag) {
                transaction.rollback();
            }
            LogUtil.Log("Get workitem context but exception occurred, " + ex,
                    WorkitemContext.class.getName(), LogUtil.LogLevelType.ERROR, rtid);
            throw ex;
        }
        finally {
            HibernateUtil.CloseLocalSession();
        }
    }

    /**
     * Generate a workitem context and save it to steady by a task context.
     * @param taskContext task context to be the generation template
     * @param args arguments vector
     * @return workitem context
     */
    public static WorkitemContext GenerateContext(TaskContext taskContext, String rtid, ArrayList args) {
        assert args != null && taskContext.getParameters() != null;
        if (args.size() != taskContext.getParameters().size()) {
            LogUtil.Log(String.format("Generate workitem for task %s, but arguments(%s) and parameters(%s) not equal",
                    taskContext.getTaskName(), args.size(), taskContext.getParameters().size()),
                    WorkitemContext.class.getName(), LogUtil.LogLevelType.ERROR, rtid);
            return null;
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
            rwe.setEnablementTime(TimestampUtil.GetCurrentTimestamp());
            HashMap<String, String> taskArgsSign = CommonUtil.ZipVectorConvertString(taskContext.getParameters(), args);
            rwe.setArguments(SerializationUtil.JsonSerialization(taskArgsSign));
            session.save(rwe);
            transaction.commit();
            cmtFlag = true;
            WorkitemContext wCtx = new WorkitemContext();
            wCtx.entity = rwe;
            wCtx.argsDict = taskArgsSign;
            return wCtx;
        }
        catch (Exception ex) {
            if (!cmtFlag) {
                transaction.rollback();
            }
            LogUtil.Log("Generate workitem context but exception occurred, " + ex,
                    WorkitemContext.class.getName(), LogUtil.LogLevelType.ERROR, rtid);
            throw ex;
        }
        finally {
            HibernateUtil.CloseLocalSession();
        }
    }

    /**
     * Save changes context to steady memory.
     * @param context context to be saved
     */
    public static void SaveToSteady(WorkitemContext context) {
        if (context == null) {
            LogUtil.Log("Ignore null workitem context saving.", WorkitemContext.class.getName(),
                    LogUtil.LogLevelType.WARNING, "");
            return;
        }
        Session session = HibernateUtil.GetLocalSession();
        Transaction transaction = session.beginTransaction();
        try {
            session.update(context.entity);
            transaction.commit();
        }
        catch (Exception ex) {
            transaction.rollback();
            LogUtil.Log("Save workitem context but exception occurred, " + ex,
                    WorkitemContext.class.getName(), LogUtil.LogLevelType.ERROR, context.getEntity().getRtid());
        }
        finally {
            HibernateUtil.CloseLocalSession();
        }
    }

    /**
     * Create a new workitem context.
     * Private constructor for preventing create context outside.
     */
    private WorkitemContext() { }
}
