/*
 * Project Ren @ 2018
 * Rinkako, Ariana, Gordan. SYSU SDCS.
 */
package org.sysu.renResourcing.executor;

import org.sysu.renCommon.enums.InitializationByType;

/**
 * Author: Rinkako
 * Date  : 2018/2/5
 * Usage : This class is responsible for handle resources start phase.
 */
public class StartInteractionExecutor extends InteractionExecutor {

    /**
     * Create a new start interaction executor.
     *
     * @param ownerTaskId id of task to create this
     * @param type        type of service invoker type
     */
    public StartInteractionExecutor(String ownerTaskId, InitializationByType type) {
        super(ownerTaskId, type);
    }

    /**
     * Binding a trigger to this executor by the start type of task.
     *
     * @param startType start type string
     */
    public void BindingTrigger(String startType, String rtid) {
        // todo
    }
}