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
     * Create a new work queue container.
     * @param workerGid owner worker global id
     * @param type container type
     */
    public WorkQueueContainer(String workerGid, WorkQueueContainerType type) {
        this.workerGid = workerGid;
        this.type = type;
    }

    /**
     * Get container owner worker global id.
     * @return worker gid string
     */
    public String getWorkerGid() {
        return workerGid;
    }

    /**
     * Get container type.
     * @return container type enum
     */
    public WorkQueueContainerType getType() {
        return type;
    }

    /**
     * Binding worker global id.
     */
    private String workerGid;

    /**
     * Type of container.
     */
    private WorkQueueContainerType type;


}
