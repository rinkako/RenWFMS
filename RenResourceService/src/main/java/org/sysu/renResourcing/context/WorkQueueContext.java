/*
 * Project Ren @ 2018
 * Rinkako, Ariana, Gordan. SYSU SDCS.
 */
package org.sysu.renResourcing.context;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.sysu.renResourcing.basic.enums.WorkQueueType;
import org.sysu.renResourcing.context.steady.RenQueueitemsEntity;
import org.sysu.renResourcing.context.steady.RenWorkqueueEntity;
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
        this.AddRemoveChanged();
    }

    /**
     * Add or update all entries of a work item queue Map.
     * @param queueMap Map in pattern (workitemId, workitemObject)
     */
    public synchronized void AddQueue(Map<String, WorkitemContext> queueMap) {
        this.workitems.putAll(queueMap);
        this.AddRemoveChanged();
    }

    /**
     * Add or update all item in the queue passed by another queue.
     * Entries in another queue will be copied and append to this queue.
     * @param queue queue to be added
     */
    public synchronized void AddQueue(WorkQueueContext queue) {
        this.workitems.putAll(queue.GetQueueAsMap());
        this.AddRemoveChanged();
    }

    /**
     * Remove a workitem from this queue.
     * @param workitemId workitem global id
     */
    public synchronized void Remove(String workitemId) {
        this.workitems.remove(workitemId);
        this.AddRemoveChanged();
    }

    /**
     * Remove a workitem from this queue.
     * @param workitem workitem context
     */
    public synchronized void Remove(WorkitemContext workitem) {
        this.workitems.remove(workitem.getEntity().getWid());
        this.AddRemoveChanged();
    }

    /**
     * Removes all entries in a queue.
     * @param queue the queue of items to remove
     */
    public synchronized void RemoveQueue(WorkQueueContext queue) {
        Set<WorkitemContext> qSet = queue.GetQueueAsSet();
        for (WorkitemContext ctx : qSet) {
            this.Remove(ctx);
        }
        this.AddRemoveChanged();
    }

    /**
     * Remove all entries from a specific process runtime.
     * @param rtid process rtid
     */
    public synchronized void RemoveByRtid(String rtid) {
        // clone queue prevent iteration fault
        Set<WorkitemContext> clonedQueue = new HashSet<>(this.workitems.values());
        for (WorkitemContext workitem : clonedQueue) {
            if (workitem.getEntity().getRtid().equals(rtid)) {
                this.workitems.remove(workitem.getEntity().getWid());
            }
        }
        if (clonedQueue.size() != this.workitems.size()) {
            this.AddRemoveChanged();
        }
    }

    /**
     * Check this queue is empty.
     * @return true if queue empty
     */
    public synchronized boolean IsEmpty() {
        return this.workitems.isEmpty();
    }

    /**
     * Count the queue length.
     * @return number of workitems in this queue
     */
    public synchronized int Count() {
        return this.workitems.size();
    }

    /**
     * Check if a workitem in queue.
     * @param workitemId workitem global id
     * @return true if exist in queue
     */
    public synchronized boolean Contains(String workitemId) {
        return this.workitems.containsKey(workitemId);
    }

    /**
     * Get a workitem context from this queue by its global id.
     * @param workitemId workitem global id
     * @return workitem context, null if not exist
     */
    public synchronized WorkitemContext Retrieve(String workitemId) {
        return this.workitems.get(workitemId);
    }

    /**
     * Clear the queue.
     */
    public synchronized void Clear() {
        this.workitems.clear();
        this.AddRemoveChanged();
    }

    /**
     * Get a concurrent hash map of all workitem in queue.
     * @return all members of the queue as a HashMap of (workitemId, workitemObject)
     */
    public synchronized Map<String, WorkitemContext> GetQueueAsMap() {
        return this.workitems;
    }

    /**
     * Get a copied hash set of all workitem in queue.
     * @return all members of the queue as a HashSet
     */
    public synchronized Set<WorkitemContext> GetQueueAsSet() {
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
     * @return a workqueue context
     */
    public synchronized static WorkQueueContext GetContext(String ownerWorkerId, WorkQueueType queueType) {
        Session session = HibernateUtil.GetLocalThreadSession();
        Transaction transaction = session.beginTransaction();
        boolean cmtFlag = false;
        try {
            RenWorkqueueEntity rwqe = (RenWorkqueueEntity) session.createQuery(String.format("FROM RenWorkqueueEntity WHERE ownerId = '%s' AND type = %s",
                    ownerWorkerId, queueType.ordinal())).uniqueResult();
            // if not exist in steady then create a new one
            if (rwqe == null) {
                rwqe = new RenWorkqueueEntity();
                rwqe.setQueueId(String.format("WQ_%s", UUID.randomUUID().toString()));
                rwqe.setOwnerId(ownerWorkerId);
                rwqe.setType(queueType.ordinal());
                session.saveOrUpdate(rwqe);
            }
            transaction.commit();
            cmtFlag = true;
            return WorkQueueContext.GenerateContext(rwqe);
        }
        catch (Exception ex) {
            if (!cmtFlag) {
                transaction.rollback();
            }
            LogUtil.Log(String.format("Get WorkQueueContext (owner: %s, type: %s) exception occurred, %s", ownerWorkerId, queueType.name(), ex),
                    WorkQueueContext.class.getName(), LogUtil.LogLevelType.ERROR, "");
            throw ex;
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
     * Set this queue is changed and flush changes to steady.
     * NOTICE that the admin worklisted queue is dynamically construct so
     * it is no need for persist.
     */
    private synchronized void AddRemoveChanged() {
        if (this.type != WorkQueueType.WORKLISTED) {
            Session session = HibernateUtil.GetLocalThreadSession();
            Transaction transaction = session.beginTransaction();
            try {
                session.createQuery(String.format("DELETE FROM RenQueueitemsEntity WHERE workqueueId = '%s'", this.queueId)).executeUpdate();
                for (WorkitemContext workitem : this.workitems.values()) {
                    RenQueueitemsEntity rqe = new RenQueueitemsEntity();
                    rqe.setWorkqueueId(this.queueId);
                    rqe.setWorkitemId(workitem.getEntity().getWid());
                    session.saveOrUpdate(rqe);
                }
                transaction.commit();
            }
            catch (Exception ex) {
                transaction.rollback();
                LogUtil.Log(String.format("When WorkQueue(%s of %s, %s) flush changes to steady exception occurred, %s",
                        this.queueId, this.ownerWorkerId, this.type.name(), ex), WorkQueueContext.class.getName(),
                        LogUtil.LogLevelType.ERROR, "");
                throw ex;
            }
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
     * NOTICE that usually this method should not be called unless worklisted queue.
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
