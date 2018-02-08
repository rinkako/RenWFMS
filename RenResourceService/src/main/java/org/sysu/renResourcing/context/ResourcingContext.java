package org.sysu.renResourcing.context;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.sysu.renResourcing.GlobalContext;
import org.sysu.renResourcing.basic.enums.RServiceType;
import org.sysu.renResourcing.context.steady.RenRsrecordEntity;
import org.sysu.renResourcing.utility.CommonUtil;
import org.sysu.renResourcing.utility.HibernateUtil;
import org.sysu.renResourcing.utility.LogUtil;
import org.sysu.renResourcing.utility.SerializationUtil;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Hashtable;
import java.util.UUID;

/**
 * Author: Rinkako
 * Date  : 2017/2/5
 * Usage : Task context is an encapsulation of RenRsrecordEntity in a
 *         convenient way for resourcing service.
 */
public class ResourcingContext implements Comparable<ResourcingContext>, Serializable, RCacheablesContext {
    /**
     * Serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Resourcing record global id.
     */
    private String rstid;

    /**
     * Process runtime record id.
     */
    private String rtid;

    /**
     * Priority, bigger schedule faster
     */
    private int priority = 0;

    /**
     * Service type enum.
     */
    private RServiceType service;

    /**
     * Service argument dictionary.
     */
    private Hashtable<String, Object> argsDict;

    /**
     * Timestamp of request received.
     */
    private Timestamp receivedTimestamp;

    /**
     * Timestamp of request scheduled.
     */
    private Timestamp scheduledTimestamp;

    /**
     * Timestamp of request finished.
     */
    private Timestamp finishTimestamp;

    /**
     * Execution result descriptor, not save to steady.
     */
    private String executionResult;

    /**
     * Get a resourcing request context.
     * @param rstid resourcing request global id, null if create a new one
     * @param rtid process rtid
     * @param service service type enum
     * @param argsDict service argument dict
     * @return Resourcing request context, null if exception occurred or assertion error
     */
    public static ResourcingContext GetContext(String rstid, String rtid, RServiceType service, Hashtable<String, Object> argsDict) {
        Session session = HibernateUtil.GetLocalThreadSession();
        Transaction transaction = session.beginTransaction();
        boolean cmtFlag = false;
        try {
            RenRsrecordEntity renRsrecordEntity;
            // create new
            if (rstid == null) {
                renRsrecordEntity = new RenRsrecordEntity();
                renRsrecordEntity.setRstid(String.format("RSR_%s", UUID.randomUUID().toString()));
                renRsrecordEntity.setRtid(rtid);
                renRsrecordEntity.setPriority(0);
                renRsrecordEntity.setReceiveTimestamp(new Timestamp(System.currentTimeMillis()));
                renRsrecordEntity.setResourcingId(GlobalContext.RESOURCE_SERVICE_GLOBAL_ID);
                renRsrecordEntity.setService(service.name());
                renRsrecordEntity.setArgs(SerializationUtil.JsonSerialization(argsDict));
                session.saveOrUpdate(renRsrecordEntity);
                transaction.commit();
                cmtFlag = true;
            }
            // exist from steady
            else {
                renRsrecordEntity = session.get(RenRsrecordEntity.class, rstid);
                assert renRsrecordEntity != null;
                transaction.commit();
                cmtFlag = true;
            }
            return ResourcingContext.GenerateResourcingContext(renRsrecordEntity);
        }
        catch (Exception ex) {
            if (!cmtFlag) {
                transaction.rollback();
            }
            LogUtil.Log("Get resourcing request context but exception occurred, " + ex,
                    TaskContext.class.getName(), LogUtil.LogLevelType.ERROR, rtid);
            return null;
        }
    }

    /**
     * Save changes context to steady memory.
     * @param context context to be saved
     */
    public static void SaveToSteady(ResourcingContext context) {
        if (context == null) {
            LogUtil.Log("Ignore null resourcing context saving.", TaskContext.class.getName(),
                    LogUtil.LogLevelType.WARNING, "");
            return;
        }
        Session session = HibernateUtil.GetLocalThreadSession();
        Transaction transaction = session.beginTransaction();
        try {
            RenRsrecordEntity rre = session.get(RenRsrecordEntity.class, context.rstid);
            assert rre != null;
            rre.setReceiveTimestamp(context.receivedTimestamp);
            rre.setScheduledTimestamp(context.scheduledTimestamp);
            rre.setFinishTimestamp(context.finishTimestamp);
            session.update(rre);
            transaction.commit();
        }
        catch (Exception ex) {
            transaction.rollback();
            LogUtil.Log("Save resourcing request context but exception occurred, " + ex,
                    TaskContext.class.getName(), LogUtil.LogLevelType.ERROR, context.getRtid());
        }
    }

    /**
     * Get the resourcing request global id.
     * @return resourcing record id string
     */
    public String getRstid() {
        return rstid;
    }

    /**
     * Get process runtime record global id.
     * @return process rtid string
     */
    public String getRtid() {
        return rtid;
    }

    /**
     * Get the priority of resourcing request.
     * Bigger schedule faster.
     * @return priority int, default `0`
     */
    public int getPriority() {
        return this.priority;
    }

    /**
     * Get service enum type of this request.
     * @return service enum type
     */
    public RServiceType getService() {
        return this.service;
    }

    /**
     * Get argument dict of this request.
     * @return argument HashTable
     */
    public Hashtable<String, Object> getArgs() {
        return this.argsDict;
    }

    /**
     * Get request received timestamp.
     * @return timestamp
     */
    public Timestamp getReceivedTimestamp() {
        return this.receivedTimestamp;
    }

    /**
     * Get request scheduled timestamp.
     * @return timestamp
     */
    public Timestamp getScheduledTimestamp() {
        return this.scheduledTimestamp;
    }

    /**
     * Get request finish timestamp.
     * @return timestamp
     */
    public Timestamp getFinishTimestamp() {
        return this.finishTimestamp;
    }

    /**
     * Set resourcing request is scheduled.
     */
    public void SetScheduled() {
        this.scheduledTimestamp = new Timestamp(System.currentTimeMillis());
    }

    /**
     * Set resourcing request is finished.
     */
    public void SetFinish() {
        this.finishTimestamp = new Timestamp(System.currentTimeMillis());
    }

    /**
     * Get the execution result.
     * @return result descriptor string.
     */
    public String getExecutionResult() {
        return executionResult;
    }

    /**
     * Set the execution result.
     * @param executionResult result descriptor string.
     */
    public void setExecutionResult(String executionResult) {
        this.executionResult = executionResult;
    }

    /**
     * Create a new resourcing request context.
     * Private constructor for preventing create outside.
     * @param rstid resourcing request id
     * @param rtid process rtid
     * @param service service descriptor
     * @param argsDict service argument dictionary
     */
    private ResourcingContext(String rstid, String rtid, String service, Hashtable<String, Object> argsDict) {
        this.rstid = rstid;
        this.rtid = rtid;
        this.service = RServiceType.valueOf(service);
        this.argsDict = argsDict;
    }

    /**
     * Generate a resourcing context by a steady entity.
     * @param rsrecordEntity RS Record entity
     * @return equivalent resourcing context.
     */
    @SuppressWarnings("unchecked")
    private static ResourcingContext GenerateResourcingContext(RenRsrecordEntity rsrecordEntity) {
        assert rsrecordEntity != null;
        String argdDescriptor = rsrecordEntity.getArgs();
        Hashtable<String, Object> dict;
        if (CommonUtil.IsNullOrEmpty(argdDescriptor)) {
            dict = new Hashtable<>();
        }
        else {
            dict = SerializationUtil.JsonDeserialization(rsrecordEntity.getArgs(), Hashtable.class);
        }
        ResourcingContext context = new ResourcingContext(rsrecordEntity.getRstid(),
                rsrecordEntity.getRtid(), rsrecordEntity.getService(), dict);
        context.receivedTimestamp = rsrecordEntity.getReceiveTimestamp();
        context.scheduledTimestamp = rsrecordEntity.getScheduledTimestamp();
        context.finishTimestamp = rsrecordEntity.getFinishTimestamp();
        return context;
    }

    /**
     * Compares this object with the specified object for order.  Returns a
     * negative integer, zero, or a positive integer as this object is less
     * than, equal to, or greater than the specified object.
     * @param o the object to be compared.
     * @return a negative integer, zero, or a positive integer as this object
     * is less than, equal to, or greater than the specified object.
     */
    @Override
    public int compareTo(ResourcingContext o) {
        return Integer.compare(this.priority, o.priority);
    }
}
