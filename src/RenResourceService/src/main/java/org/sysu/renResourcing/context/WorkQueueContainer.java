/*
 * Project Ren @ 2018
 * Rinkako, Ariana, Gordan. SYSU SDCS.
 */
package org.sysu.renResourcing.context;

import org.sysu.renResourcing.GlobalContext;
import org.sysu.renCommon.enums.WorkQueueContainerType;
import org.sysu.renCommon.enums.WorkQueueType;
import org.sysu.renResourcing.consistency.ContextCachePool;

import java.util.Collections;
import java.util.Set;

/**
 * Author: Rinkako
 * Date  : 2018/2/3
 * Usage : Maintaining all work queues belonging to a Participant.
 */
public class WorkQueueContainer implements RCacheablesContext {

    /**
     * Participant offered workitem queue.
     */
    private WorkQueueContext offeredQueue;

    /**
     * Participant allocated workitem queue.
     */
    private WorkQueueContext allocatedQueue;

    /**
     * Participant started workitem queue.
     */
    private WorkQueueContext startedQueue;

    /**
     * Participant suspended workitem queue.
     */
    private WorkQueueContext suspendedQueue;

    /**
     * Admin unoffered workitem queue.
     */
    private WorkQueueContext unofferedQueue;

    /**
     * Admin worklisted workitem queue.
     */
    private WorkQueueContext worklistedQueue;

    /**
     * Queue container owner worker global id.
     */
    private String ownerWorkerId;

    /**
     * Type of Queue container owner.
     */
    private WorkQueueContainerType type;

    /**
     * Get the queue container of a specific worker.
     *
     * @param workerId worker global id, {@code GlobalContext.WORKQUEUE_ADMIN_PREFIX} if admin user
     * @return Work queue container of this worker
     */
    public static WorkQueueContainer GetContext(String workerId) {
        return WorkQueueContainer.GetContext(workerId, false);
    }

    /**
     * Get the queue container of a specific worker.
     *
     * @param workerId    worker global id, {@code GlobalContext.WORKQUEUE_ADMIN_PREFIX} if admin user
     * @param forceReload force reload from steady and refresh cache
     * @return Work queue container of this worker
     */
    public static WorkQueueContainer GetContext(String workerId, boolean forceReload) {
        WorkQueueContainer retContainer = ContextCachePool.Retrieve(WorkQueueContainer.class, workerId);
        // fetch cache
        if (retContainer != null && !forceReload) {
            return retContainer;
        }
        // admin queue
        if (workerId.startsWith(GlobalContext.WORKQUEUE_ADMIN_PREFIX)) {
            retContainer = new WorkQueueContainer(workerId, WorkQueueContainerType.AdminSet);
        }
        // participant queue
        else {
            retContainer = new WorkQueueContainer(workerId, WorkQueueContainerType.ParticipantSet);
        }
        ContextCachePool.AddOrUpdate(workerId, retContainer);
        return retContainer;
    }

    /**
     * Move workitem queue: OFFERED -> ALLOCATED
     *
     * @param workitem workitem context
     */
    public void MoveOfferedToAllocated(WorkitemContext workitem) {
        this.Move(workitem, WorkQueueType.OFFERED, WorkQueueType.ALLOCATED);
    }

    /**
     * Move workitem queue: ALLOCATED -> OFFERED
     *
     * @param workitem workitem context
     */
    public void MoveAllocatedToOffered(WorkitemContext workitem) {
        this.Move(workitem, WorkQueueType.ALLOCATED, WorkQueueType.OFFERED);
    }

    /**
     * Move workitem queue: OFFERED -> STARTED
     *
     * @param workitem workitem context
     */
    public void MoveOfferedToStarted(WorkitemContext workitem) {
        this.Move(workitem, WorkQueueType.OFFERED, WorkQueueType.STARTED);
    }

    /**
     * Move workitem queue: OFFERED -> STARTED
     *
     * @param workitem workitem context
     */
    public void MoveStartedToOffered(WorkitemContext workitem) {
        this.Move(workitem, WorkQueueType.STARTED, WorkQueueType.OFFERED);
    }

    /**
     * Move workitem queue: ALLOCATED -> STARTED
     *
     * @param workitem workitem context
     */
    public void MoveAllocatedToStarted(WorkitemContext workitem) {
        this.Move(workitem, WorkQueueType.ALLOCATED, WorkQueueType.STARTED);
    }

    /**
     * Move workitem queue: STARTED -> ALLOCATED
     *
     * @param workitem workitem context
     */
    public void MoveStartedToAllocated(WorkitemContext workitem) {
        this.Move(workitem, WorkQueueType.STARTED, WorkQueueType.ALLOCATED);
    }

    /**
     * Move workitem queue: STARTED -> SUSPENDED
     *
     * @param workitem workitem context
     */
    public void MoveStartedToSuspend(WorkitemContext workitem) {
        this.Move(workitem, WorkQueueType.STARTED, WorkQueueType.SUSPENDED);
    }

    /**
     * Move workitem queue: SUSPENDED -> STARTED
     *
     * @param workitem workitem context
     */
    public void MoveSuspendToStarted(WorkitemContext workitem) {
        this.Move(workitem, WorkQueueType.SUSPENDED, WorkQueueType.STARTED);
    }

    /**
     * Move a workitem from a queue to another queue.
     * NOTICE that this method usually should NOT be called outside, since not all move is valid.
     *
     * @param workitem workitem context to be moved
     * @param from     from queue type
     * @param to       to queue type
     */
    public void Move(WorkitemContext workitem, WorkQueueType from, WorkQueueType to) {
        this.RemoveFromQueue(workitem, from);
        this.AddToQueue(workitem, to);
    }

    /**
     * Add or update a workitem to a queue.
     *
     * @param workitem workitem context
     * @param type     queue type
     */
    public void AddToQueue(WorkitemContext workitem, WorkQueueType type) {
        WorkQueueContext wq = this.GetQueue(type);
        wq.AddOrUpdate(workitem);
    }

    /**
     * Add or update workitems to a queue.
     *
     * @param addQueue workitem context queue to add
     * @param type     queue type
     */
    public void AddToQueue(WorkQueueContext addQueue, WorkQueueType type) {
        WorkQueueContext wq = this.GetQueue(type);
        wq.AddQueue(addQueue);
    }

    /**
     * Remove a workitem from a queue.
     *
     * @param workitem workitem context
     * @param type     queue type
     */
    public void RemoveFromQueue(WorkitemContext workitem, WorkQueueType type) {
        this.GetQueue(type).Remove(workitem);
    }

    /**
     * Remove workitems from a queue.
     *
     * @param removeQueue workitem context queue to remove
     * @param type        queue type
     */
    public void RemoveFromQueue(WorkQueueContext removeQueue, WorkQueueType type) {
        this.GetQueue(type).RemoveQueue(removeQueue);
    }

    /**
     * Remove workitems from a queue belong to a specific rtid.
     *
     * @param rtid process rtid.
     * @param type queue type
     */
    public void RemoveFromQueueByRTID(String rtid, WorkQueueType type) {
        this.GetQueue(type).RemoveByRtid(rtid);
    }

    /**
     * Remove workitems from all queues belong to a specific rtid.
     *
     * @param rtid process rtid.
     */
    public void RemoveFromAllQueueByRTID(String rtid) {
        if (this.type == WorkQueueContainerType.AdminSet) {
            this.RemoveFromQueueByRTID(rtid, WorkQueueType.UNOFFERED);
            this.RemoveFromQueueByRTID(rtid, WorkQueueType.WORKLISTED);
        } else {
            this.RemoveFromQueueByRTID(rtid, WorkQueueType.OFFERED);
            this.RemoveFromQueueByRTID(rtid, WorkQueueType.ALLOCATED);
            this.RemoveFromQueueByRTID(rtid, WorkQueueType.STARTED);
            this.RemoveFromQueueByRTID(rtid, WorkQueueType.SUSPENDED);
        }
    }

    /**
     * Get all workitem in a queue.
     *
     * @param type queue type
     * @return workitem hash set.
     */
    public Set<WorkitemContext> GetQueuedWorkitem(WorkQueueType type) {
        return this.GetQueue(type).GetQueueAsSet();
    }

    /**
     * Get worklisted workitem queue.
     *
     * @return worklisted queue
     */
    public WorkQueueContext GetWorklistedQueue() {
        String wlid = String.format("WQ_WL_%s", this.ownerWorkerId);
        WorkQueueContext worklisted = new WorkQueueContext(wlid, this.ownerWorkerId, WorkQueueType.WORKLISTED);
        for (int qType = WorkQueueType.OFFERED.ordinal(); qType <= WorkQueueType.SUSPENDED.ordinal(); qType++) {
            WorkQueueType qt = WorkQueueType.values()[qType];
            worklisted.AddQueue(this.GetQueue(qt));
        }
        return worklisted;
    }

    /**
     * Check if a queue contains a workitem.
     *
     * @param workitemId workitem global id
     * @param type       queue type
     * @return true if contains
     */
    public boolean Contains(String workitemId, WorkQueueType type) {
        return this.GetQueue(type).Contains(workitemId);
    }

    /**
     * Check if any queue contains a workitem.
     *
     * @param workitemId workitem global id
     * @return true if contains
     */
    public boolean ContainsAny(String workitemId) {
        for (int qType = WorkQueueType.OFFERED.ordinal(); qType <= WorkQueueType.SUSPENDED.ordinal(); qType++) {
            if (this.Contains(workitemId, WorkQueueType.values()[qType])) {
                return true;
            }
        }
        return false;
    }

    /**
     * Get a workitem from a queue by its global id.
     *
     * @param workitemId workitem global id
     * @param type       queue type
     * @return workitem context
     */
    public WorkitemContext Retrieve(String workitemId, WorkQueueType type) {
        return this.GetQueue(type).Retrieve(workitemId);
    }

    /**
     * Check if a queue is null.
     *
     * @param type queue type
     * @return true if queue is null
     */
    public boolean IsNullQueue(WorkQueueType type) {
        return this.DirectlyGetQueue(type) == null;
    }

    /**
     * Check if a queue is null or empty.
     *
     * @param type queue type
     * @return true if queue is null or empty
     */
    public boolean IsNullOrEmptyQueue(WorkQueueType type) {
        WorkQueueContext wq = this.DirectlyGetQueue(type);
        return wq == null || wq.IsEmpty();
    }

    /**
     * Directly get the queue reference in the container.
     *
     * @param type queue type
     * @return queue reference.
     */
    public WorkQueueContext DirectlyGetQueue(WorkQueueType type) {
        switch (type) {
            case UNDEFINED:
                return this.unofferedQueue;
            case OFFERED:
                return this.offeredQueue;
            case ALLOCATED:
                return this.allocatedQueue;
            case STARTED:
                return this.startedQueue;
            case SUSPENDED:
                return this.suspendedQueue;
            case UNOFFERED:
                return this.unofferedQueue;
            case WORKLISTED:
                return this.worklistedQueue;
        }
        return null;
    }

    /**
     * Get the queue reference in the container, if a queue is null then
     * it will be generated by using {@code WorkQueueContext.GetContext}.
     *
     * @param type queue type
     * @return queue reference
     */
    public WorkQueueContext GetQueue(WorkQueueType type) {
        switch (type) {
            case UNDEFINED:
                if (this.unofferedQueue == null) {
                    this.unofferedQueue = WorkQueueContext.GetContext(this.ownerWorkerId, type);
                }
                return this.unofferedQueue;
            case OFFERED:
                if (this.offeredQueue == null) {
                    this.offeredQueue = WorkQueueContext.GetContext(this.ownerWorkerId, type);
                }
                return this.offeredQueue;
            case ALLOCATED:
                if (this.allocatedQueue == null) {
                    this.allocatedQueue = WorkQueueContext.GetContext(this.ownerWorkerId, type);
                }
                return this.allocatedQueue;
            case STARTED:
                if (this.startedQueue == null) {
                    this.startedQueue = WorkQueueContext.GetContext(this.ownerWorkerId, type);
                }
                return this.startedQueue;
            case SUSPENDED:
                if (this.suspendedQueue == null) {
                    this.suspendedQueue = WorkQueueContext.GetContext(this.ownerWorkerId, type);
                }
                return this.suspendedQueue;
            case UNOFFERED:
                if (this.unofferedQueue == null) {
                    this.unofferedQueue = WorkQueueContext.GetContext(this.ownerWorkerId, type);
                }
                return this.unofferedQueue;
            case WORKLISTED:
                if (this.worklistedQueue == null) {
                    this.worklistedQueue = WorkQueueContext.GetContext(this.ownerWorkerId, type);
                }
                return this.worklistedQueue;
        }
        return null;
    }

    /**
     * Create a new work queue container.
     * Private constructor for prevent creating new instance outside.
     *
     * @param workerGid owner worker global id
     * @param type      container type
     */
    private WorkQueueContainer(String workerGid, WorkQueueContainerType type) {
        this.ownerWorkerId = workerGid;
        this.type = type;
    }

    /**
     * Get container owner worker global id.
     *
     * @return worker gid string
     */
    public String getWorkerGid() {
        return this.ownerWorkerId;
    }

    /**
     * Get container type.
     *
     * @return container type enum
     */
    public WorkQueueContainerType getType() {
        return this.type;
    }
}
