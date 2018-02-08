/*
 * Project Ren @ 2018
 * Rinkako, Ariana, Gordan. SYSU SDCS.
 */
package org.sysu.renResourcing.context;

import org.sysu.renResourcing.basic.enums.WorkQueueContainerType;

/**
 * Author: Rinkako
 * Date  : 2018/2/3
 * Usage : Maintaining all work queues belonging to a Participant.
 */
public class WorkQueueContainer {

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
     * @param workerId worker global id, `admin` if admin user
     * @return Work queue container of this worker
     */
    public static WorkQueueContext GetContext(String workerId) {
        if (workerId.equals("admin")) {
            // todo
        }
        else {

        }
    }



    /**
     * Create a new work queue container.
     * @param workerGid owner worker global id
     * @param type container type
     */
    public WorkQueueContainer(String workerGid, WorkQueueContainerType type) {
        this.ownerWorkerId = workerGid;
        this.type = type;
    }

    /**
     * Get container owner worker global id.
     * @return worker gid string
     */
    public String getWorkerGid() {
        return this.ownerWorkerId;
    }

    /**
     * Get container type.
     * @return container type enum
     */
    public WorkQueueContainerType getType() {
        return this.type;
    }
}
