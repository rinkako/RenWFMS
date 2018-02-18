/*
 * Project Ren @ 2018
 * Rinkako, Ariana, Gordan. SYSU SDCS.
 */
package org.sysu.renResourcing.context;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.sysu.renResourcing.GlobalContext;
import org.sysu.renResourcing.basic.enums.RSEventType;
import org.sysu.renResourcing.basic.enums.WorkQueueType;
import org.sysu.renResourcing.consistency.ContextCachePool;
import org.sysu.renResourcing.context.steady.RenQueueitemsEntity;
import org.sysu.renResourcing.context.steady.RenWorkqueueEntity;
import org.sysu.renResourcing.interfaceService.InterfaceE;
import org.sysu.renResourcing.utility.HibernateUtil;
import org.sysu.renResourcing.utility.LogUtil;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Author: Rinkako
 * Date  : 2018/2/7
 * Usage : WorkQueueContext context is an encapsulation of RenWorkqueueEntity in a
 *         convenient way for resourcing service.
 */
public class WorkQueueContext implements Serializable, RCacheablesContext {
    /**
     * Queue global id.
     */
    private String queueId;

    /**
     * Queue owner worker id, {@code GlobalContext.WORKQUEUE_ADMIN_PREFIX} if an admin queue.
     */
    private String ownerWorkerId;

    /**
     * Queue type enum.
     */
    private WorkQueueType type;

    /**
     * Workitem in this queue, map in pattern (workitemId, workitemObject)
     */
    private ConcurrentHashMap<String, WorkitemContext> workitems = new ConcurrentHashMap<>();

    /**
     * Add or update a workitem to this queue.
     * @param workitem workitem entity.
     */
    public synchronized void AddOrUpdate(WorkitemContext workitem) {
        this.workitems.put(workitem.getEntity().getWid(), workitem);
        this.AddChangesToSteady(workitem);
        this.LogEvent(workitem);
    }

    /**
     * Add or update all entries of a work item queue Map.
     * @param queueMap Map in pattern (workitemId, workitemObject)
     */
    public synchronized void AddQueue(Map<String, WorkitemContext> queueMap) {
        this.workitems.putAll(queueMap);
        this.AddChangesToSteady(new HashSet<>(queueMap.values()));
        this.LogEvent(queueMap);
    }

    /**
     * Add or update all item in the queue passed by another queue.
     * Entries in another queue will be copied and append to this queue.
     * @param queue queue to be added
     */
    public synchronized void AddQueue(WorkQueueContext queue) {
        Map<String, WorkitemContext> qMap = queue.GetQueueAsMap();
        this.workitems.putAll(qMap);
        this.AddChangesToSteady(queue.GetQueueAsSet());
        this.LogEvent(qMap);
    }

    /**
     * Remove a workitem from this queue.
     * @param workitemId workitem global id
     */
    public synchronized void Remove(String workitemId) {
        this.workitems.remove(workitemId);
        this.RemoveChangesToSteady(workitemId);
    }

    /**
     * Remove a workitem from this queue.
     * @param workitem workitem context
     */
    public synchronized void Remove(WorkitemContext workitem) {
        String wid = workitem.getEntity().getWid();
        this.workitems.remove(wid);
        this.RemoveChangesToSteady(wid);
    }

    /**
     * Removes all entries in a queue.
     * @param queue the queue of items to remove
     */
    public synchronized void RemoveQueue(WorkQueueContext queue) {
        Set<WorkitemContext> qSet = queue.GetQueueAsSet();
        HashSet<String> idSet = new HashSet<>();
        for (WorkitemContext ctx : qSet) {
            String wid = ctx.getEntity().getWid();
            this.Remove(wid);
            idSet.add(wid);
        }
        this.RemoveChangesToSteady(idSet);
    }

    /**
     * Remove all entries from a specific process runtime.
     * @param rtid process rtid
     */
    public synchronized void RemoveByRtid(String rtid) {
        // clone queue prevent iteration fault
        Set<WorkitemContext> clonedQueue = new HashSet<>(this.workitems.values());
        HashSet<String> idSet = new HashSet<>();
        for (WorkitemContext workitem : clonedQueue) {
            if (workitem.getEntity().getRtid().equals(rtid)) {
                String wid = workitem.getEntity().getWid();
                this.workitems.remove(wid);
                idSet.add(wid);
            }
        }
        if (!idSet.isEmpty()) {
            this.RemoveChangesToSteady(idSet);
        }
    }

    /**
     * Check this queue is empty.
     * @return true if queue empty
     */
    public synchronized boolean IsEmpty() {
        this.RefreshFromSteady();
        return this.workitems.isEmpty();
    }

    /**
     * Count the queue length.
     * @return number of workitems in this queue
     */
    public synchronized int Count() {
        this.RefreshFromSteady();
        return this.workitems.size();
    }

    /**
     * Check if a workitem in queue.
     * @param workitemId workitem global id
     * @return true if exist in queue
     */
    public synchronized boolean Contains(String workitemId) {
        this.RefreshFromSteady();
        return this.workitems.containsKey(workitemId);
    }

    /**
     * Get a workitem context from this queue by its global id.
     * @param workitemId workitem global id
     * @return workitem context, null if not exist
     */
    public synchronized WorkitemContext Retrieve(String workitemId) {
        this.RefreshFromSteady();
        return this.workitems.get(workitemId);
    }

    /**
     * Clear the queue.
     */
    public synchronized void Clear() {
        HashSet<String> idSet = new HashSet<>();
        for (WorkitemContext ctx : this.workitems.values()) {
            idSet.add(ctx.getEntity().getWid());
        }
        this.workitems.clear();
        this.RemoveChangesToSteady(idSet);
    }

    /**
     * Get a concurrent hash map of all workitem in queue.
     * @return all members of the queue as a HashMap of (workitemId, workitemObject)
     */
    public synchronized Map<String, WorkitemContext> GetQueueAsMap() {
        this.RefreshFromSteady();
        return new HashMap<>(this.workitems);
    }

    /**
     * Get a copied hash set of all workitem in queue.
     * @return all members of the queue as a HashSet
     */
    public synchronized Set<WorkitemContext> GetQueueAsSet() {
        this.RefreshFromSteady();
        Set<WorkitemContext> retSet = new HashSet<>();
        for (WorkitemContext ctx : this.workitems.values()) {
            if (ctx != null) {
                retSet.add(ctx);
            }
        }
        return retSet;
    }

    /**
     * Get the specific queue context and store to steady.
     * @param ownerWorkerId queue owner worker id
     * @param queueType queue type enum
     * @return a workqueue context
     */
    public synchronized static WorkQueueContext GetContext(String ownerWorkerId, WorkQueueType queueType) {
        return WorkQueueContext.GetContext(ownerWorkerId, queueType, false);
    }

    /**
     * Get the specific queue context and store to steady.
     * @param ownerWorkerId queue owner worker id
     * @param queueType queue type enum
     * @param forceReload force reload from steady and refresh cache
     * @return a workqueue context
     */
    public synchronized static WorkQueueContext GetContext(String ownerWorkerId, WorkQueueType queueType, boolean forceReload) {
        String wqid = String.format("WQ_%s_%s", queueType.name(), ownerWorkerId);
        WorkQueueContext retCtx = ContextCachePool.Retrieve(WorkQueueContext.class, wqid);
        // fetch cache
        if (retCtx != null && !forceReload) {
            return retCtx;
        }
        Session session = HibernateUtil.GetLocalSession();
        Transaction transaction = session.beginTransaction();
        boolean cmtFlag = false;
        try {
            RenWorkqueueEntity rwqe = (RenWorkqueueEntity) session.createQuery(String.format("FROM RenWorkqueueEntity WHERE ownerId = '%s' AND type = %s",
                    ownerWorkerId, queueType.ordinal())).uniqueResult();
            // if not exist in steady then create a new one
            if (rwqe == null) {
                rwqe = new RenWorkqueueEntity();
                rwqe.setQueueId(wqid);
                rwqe.setOwnerId(ownerWorkerId);
                rwqe.setType(queueType.ordinal());
                session.saveOrUpdate(rwqe);
            }
            transaction.commit();
            cmtFlag = true;
            WorkQueueContext generateCtx = WorkQueueContext.GenerateContext(rwqe);
            ContextCachePool.AddOrUpdate(wqid, generateCtx);
            return generateCtx;
        }
        catch (Exception ex) {
            if (!cmtFlag) {
                transaction.rollback();
            }
            LogUtil.Log(String.format("Get WorkQueueContext (owner: %s, type: %s) exception occurred, %s", ownerWorkerId, queueType.name(), ex),
                    WorkQueueContext.class.getName(), LogUtil.LogLevelType.ERROR, "");
            throw ex;
        }
        finally {
            HibernateUtil.CloseLocalSession();
        }
    }

    /**
     * Get queue global id.
     * @return global id string
     */
    public String getQueueId() {
        return this.queueId;
    }

    /**
     * Get owner worker global id, {@code GlobalContext.WORKQUEUE_ADMIN_PREFIX} if an admin queue.
     * @return global id string
     */
    public String getOwnerWorkerId() {
        return this.ownerWorkerId;
    }

    /**
     * Get queue type.
     * @return queue type enum
     */
    public WorkQueueType getType() {
        return this.type;
    }

    /**
     * Write resource event log to steady.
     * @param workitem workitem context
     */
    private void LogEvent(WorkitemContext workitem) {
        if (this.type != WorkQueueType.WORKLISTED) {
            RSEventType evtType = null;
            switch (this.type) {
                case OFFERED:
                    evtType = RSEventType.offer;
                    break;
                case ALLOCATED:
                    evtType = RSEventType.allocate;
                    break;
                case STARTED:
                    evtType = RSEventType.start;
                    break;
                case SUSPENDED:
                    evtType = RSEventType.suspend;
                    break;
                case UNOFFERED:
                    evtType = RSEventType.unoffer;
                    break;
            }
            assert evtType != null;
            InterfaceE.WriteLog(workitem, this.ownerWorkerId, evtType);
        }
    }

    /**
     * Write resource event log to steady.
     * @param workitems workitem context map
     */
    private void LogEvent(Map<String, WorkitemContext> workitems) {
        for (WorkitemContext workitem : workitems.values()) {
            this.LogEvent(workitem);
        }
    }

    /**
     * Refresh remove workitem changes to steady.
     * NOTICE this method will be called when perform SET DATA type of queue context
     * to make sure data consistency among all RS.
     * @param removeItemId id of context to remove
     */
    private synchronized void RemoveChangesToSteady(String removeItemId) {
        HashSet<String> oneSet = new HashSet<>();
        oneSet.add(removeItemId);
        this.RemoveChangesToSteady(oneSet);
    }

    /**
     * Refresh remove workitem changes to steady.
     * NOTICE this method will be called when perform SET DATA type of queue context
     * to make sure data consistency among all RS.
     * @param removeSet id of contexts to remove
     */
    private synchronized void RemoveChangesToSteady(Set<String> removeSet) {
        if (this.type == WorkQueueType.WORKLISTED) {
            return;
        }
        Session session = HibernateUtil.GetLocalSession();
        Transaction transaction = session.beginTransaction();
        try {
            for (String workitemId : removeSet) {
                RenQueueitemsEntity rqe = (RenQueueitemsEntity) session.createQuery(String.format("FROM RenQueueitemsEntity WHERE workqueueId = '%s' AND workitemId = '%s'", this.queueId, workitemId)).uniqueResult();
                if (rqe != null) {
                    session.delete(rqe);
                }
            }
            transaction.commit();
        }
        catch (Exception ex) {
            transaction.rollback();
            LogUtil.Log(String.format("When WorkQueue(%s of %s, %s) flush remove changes to steady exception occurred, %s",
                        this.queueId, this.ownerWorkerId, this.type.name(), ex), WorkQueueContext.class.getName(),
                        LogUtil.LogLevelType.ERROR, "");
            throw ex;
        }
        finally {
            HibernateUtil.CloseLocalSession();
        }
    }

    /**
     * Refresh add workitem changes to steady.
     * NOTICE this method will be called when perform SET DATA type of queue context
     * to make sure data consistency among all RS.
     * @param addItem context to add
     */
    private synchronized void AddChangesToSteady(WorkitemContext addItem) {
        HashSet<WorkitemContext> oneSet = new HashSet<>();
        oneSet.add(addItem);
        this.AddChangesToSteady(oneSet);
    }

    /**
     * Refresh add workitem changes to steady.
     * NOTICE this method will be called when perform SET DATA type of queue context
     * to make sure data consistency among all RS.
     * @param addSet set of context to add
     */
    private synchronized void AddChangesToSteady(Set<WorkitemContext> addSet) {
        if (this.type == WorkQueueType.WORKLISTED) {
            return;
        }
        Session session = HibernateUtil.GetLocalSession();
        Transaction transaction = session.beginTransaction();
        try {
            for (WorkitemContext workitem : addSet) {
                String workitemId = workitem.getEntity().getWid();
                RenQueueitemsEntity rqe = (RenQueueitemsEntity) session.createQuery(String.format("FROM RenQueueitemsEntity WHERE workqueueId = '%s' AND workitemId = '%s'", this.queueId, workitemId)).uniqueResult();
                if (rqe == null) {
                    rqe = new RenQueueitemsEntity();
                    rqe.setWorkitemId(workitemId);
                    rqe.setWorkqueueId(this.queueId);
                    session.saveOrUpdate(rqe);
                }
            }
            transaction.commit();
        }
        catch (Exception ex) {
            transaction.rollback();
            LogUtil.Log(String.format("When WorkQueue(%s of %s, %s) flush add changes to steady exception occurred, %s",
                    this.queueId, this.ownerWorkerId, this.type.name(), ex), WorkQueueContext.class.getName(),
                    LogUtil.LogLevelType.ERROR, "");
            throw ex;
        }
        finally {
            HibernateUtil.CloseLocalSession();
        }
    }

    /**
     * Refresh work queue from steady.
     * NOTICE this method will be called when perform GET DATA type of queue context
     * to make sure data consistency among all RS.
     */
    @SuppressWarnings("unchecked")
    private synchronized void RefreshFromSteady() {
        if (this.type == WorkQueueType.WORKLISTED) {
            return;
        }
        Session session = HibernateUtil.GetLocalSession();
        Transaction transaction = session.beginTransaction();
        boolean cmtFlag = false;
        try {
            ArrayList<RenQueueitemsEntity> inSteady = (ArrayList<RenQueueitemsEntity>) session.createQuery(String.format("FROM RenQueueitemsEntity WHERE workqueueId = '%s'", this.queueId)).list();
            transaction.commit();
            cmtFlag = true;
            ConcurrentHashMap<String, WorkitemContext> newMaps = new ConcurrentHashMap<>();
            for (RenQueueitemsEntity rqe : inSteady) {
                WorkitemContext workitem = WorkitemContext.GetContext(rqe.getWorkitemId(), "#RS_INTERNAL_" + GlobalContext.RESOURCE_SERVICE_GLOBAL_ID);
                newMaps.put(rqe.getWorkitemId(), workitem);
            }
            this.workitems = newMaps;
        }
        catch (Exception ex) {
            if (!cmtFlag) {
                transaction.rollback();
            }
            LogUtil.Log(String.format("When WorkQueue(%s of %s, %s) refresh from steady exception occurred, %s",
                    this.queueId, this.ownerWorkerId, this.type.name(), ex), WorkQueueContext.class.getName(),
                    LogUtil.LogLevelType.ERROR, "");
            throw ex;
        }
        finally {
            HibernateUtil.CloseLocalSession();
        }
    }

    /**
     * Generate a context by its steady entity.
     * @param workqueueEntity WorkQueue entity
     * @return WorkQueue context
     */
    private static WorkQueueContext GenerateContext(RenWorkqueueEntity workqueueEntity) {
        assert workqueueEntity != null;
        return new WorkQueueContext(workqueueEntity.getQueueId(),
                workqueueEntity.getOwnerId(), WorkQueueType.values()[workqueueEntity.getType()]);
    }


    /**
     * Create a new work queue context.
     * NOTICE that usually this method should not be called unless <b>worklisted</b> queue.
     * @param id work queue global id
     * @param ownerWorkerId owner worker global id
     * @param type queue type enum
     */
    public WorkQueueContext(String id, String ownerWorkerId, WorkQueueType type) {
        this.queueId = id;
        this.ownerWorkerId = ownerWorkerId;
        this.type = type;
    }
}
