/*
 * Project Ren @ 2018
 * Rinkako, Ariana, Gordan. SYSU SDCS.
 */
package org.sysu.renResourcing.executor;

import org.sysu.renCommon.enums.InitializationByType;

/**
 * Author: Rinkako
 * Date  : 2018/2/4
 * Usage : Base class for Offer, Allocate and Start interaction points.
 */
public abstract class InteractionExecutor {

    /**
     * Create a new Interaction executor.
     */
    public InteractionExecutor() { }

    /**
     * Create a new Interaction executor.
     * @param ownerTaskId belong to task global id
     * @param type initiator type
     */
    public InteractionExecutor(String ownerTaskId, InitializationByType type) {
        this.initiator = type;
        this.ownerTaskId = ownerTaskId;
    }

    /**
     * Get the initiator type.
     * @return initiator type enum
     */
    public InitializationByType getInitiatorType() {
        return this.initiator;
    }

    /**
     * Get the owner task global id.
     * @return task gid string
     */
    public String getOwnerTaskId() {
        return this.ownerTaskId;
    }

    /**
     * Level of initiator.
     */
    protected InitializationByType initiator = InitializationByType.USER_INITIATED;

    /**
     * Belong to Task global id.
     */
    protected String ownerTaskId;
}
