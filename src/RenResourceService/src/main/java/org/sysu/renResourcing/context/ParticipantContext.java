/*
 * Project Ren @ 2018
 * Rinkako, Ariana, Gordan. SYSU SDCS.
 */
package org.sysu.renResourcing.context;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.sysu.renCommon.enums.AgentReentrantType;
import org.sysu.renCommon.enums.LogLevelType;
import org.sysu.renCommon.enums.WorkerType;
import org.sysu.renResourcing.consistency.ContextCachePool;
import org.sysu.renResourcing.context.steady.RenRsparticipantEntity;
import org.sysu.renResourcing.utility.HibernateUtil;
import org.sysu.renResourcing.utility.LogUtil;

import java.io.Serializable;

/**
 * Author: Rinkako
 * Date  : 2018/2/4
 * Usage : Task context is an encapsulation of RenRsparticipant in a
 *         convenient way for resourcing service.
 */
public class ParticipantContext implements Serializable, RCacheablesContext {
    /**
     * Serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Worker global id.
     */
    private String workerId;

    /**
     * User-friendly resource name.
     */
    private String displayName;

    /**
     * Worker type enum.
     */
    private WorkerType workerType;

    /**
     * Agent location, null if Human.
     */
    private String agentLocation;

    /**
     * Agent type enum, only valid when worker type is Agent.
     */
    private AgentReentrantType agentType;

    /**
     * Get a participant context by its global id.
     * @param workerId worker global id
     * @return Participant resourcing context, null if exception occurred or assertion error
     */
    public static ParticipantContext GetContext(String rtid, String workerId) {
        return ParticipantContext.GetContext(rtid, workerId, false);
    }

    /**
     * Get a participant context by its global id.
     * @param workerId worker global id
     * @return Participant resourcing context, null if exception occurred or assertion error
     */
    public static ParticipantContext GetContext(String rtid, String workerId, boolean forceReload) {
        ParticipantContext cachedCtx = ContextCachePool.Retrieve(ParticipantContext.class, workerId);
        // fetch cache
        if (cachedCtx != null && !forceReload) {
            return cachedCtx;
        }
        Session session = HibernateUtil.GetLocalSession();
        Transaction transaction = session.beginTransaction();
        boolean cmtFlag = false;
        try {
            RenRsparticipantEntity rre = session.get(RenRsparticipantEntity.class, workerId);
            transaction.commit();
            cmtFlag = true;
            ParticipantContext retCtx = ParticipantContext.GenerateParticipantContext(rre);
            ContextCachePool.AddOrUpdate(workerId, retCtx);
            return retCtx;
        }
        catch (Exception ex) {
            if (!cmtFlag) {
                transaction.rollback();
            }
            LogUtil.Log("When json serialization exception occurred, transaction rollback. " + ex,
                    TaskContext.class.getName(), LogLevelType.ERROR, rtid);
            return null;
        }
        finally {
            HibernateUtil.CloseLocalSession();
        }
    }

    /**
     * Get worker global id.
     * @return id string
     */
    public String getWorkerId() {
        return this.workerId;
    }

    /**
     * Get a user-friendly display name.
     * @return name string
     */
    public String getDisplayName() {
        return this.displayName;
    }

    /**
     * Get the worker resource type.
     * @return WorkerType enum
     */
    public WorkerType getWorkerType() {
        return this.workerType;
    }

    /**
     * Get the agent type, valid only worker type is Agent.
     * @return AgentReentrantType enum
     */
    public AgentReentrantType getAgentType() {
        return this.agentType;
    }

    /**
     * Get Agent location, null if Human.
     * @return agent location string
     */
    public String getAgentLocation() {
        return this.agentLocation;
    }

    /**
     * Generate a participant context by a steady entity.
     * @param rsparticipantEntity RS participant entity
     * @return equivalent participant context.
     */
    private static ParticipantContext GenerateParticipantContext(RenRsparticipantEntity rsparticipantEntity) {
        assert rsparticipantEntity != null;
        ParticipantContext context = new ParticipantContext(rsparticipantEntity.getWorkerid(),
                WorkerType.values()[rsparticipantEntity.getType()]);
        context.displayName = rsparticipantEntity.getDisplayname();
        if (context.workerType == WorkerType.Agent) {
            context.agentType = AgentReentrantType.values()[rsparticipantEntity.getReentrantType()];
            context.agentLocation = rsparticipantEntity.getAgentLocation();
        }
        return context;
    }

    /**
     * Create a new participant context.
     * Private constructor for preventing create context without using `{@code ParticipantContext.GetContext}`.
     * @param workerId worker global id
     * @param type worker type enum
     */
    private ParticipantContext(String workerId, WorkerType type) {
        this.workerId = workerId;
        this.workerType = type;
    }
}
