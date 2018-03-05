/*
 * Project Ren @ 2018
 * Rinkako, Ariana, Gordan. SYSU SDCS.
 */
package org.sysu.renResourcing.executor;

import org.sysu.renCommon.enums.LogLevelType;
import org.sysu.renResourcing.allocator.RAllocator;
import org.sysu.renCommon.enums.InitializationByType;
import org.sysu.renResourcing.context.ParticipantContext;
import org.sysu.renResourcing.context.WorkitemContext;
import org.sysu.renResourcing.principle.RPrinciple;
import org.sysu.renResourcing.utility.LogUtil;
import org.sysu.renResourcing.utility.ReflectUtil;

import java.util.HashSet;

/**
 * Author: Rinkako
 * Date  : 2018/2/4
 * Usage : This class is responsible for handle resources allocation phase.
 */
public class AllocateInteractionExecutor extends InteractionExecutor {
    /**
     * Binding allocator.
     */
    private RAllocator allocator;

    /**
     * Create a new allocate interaction executor.
     * @param ownerTaskId id of task to create this
     * @param type type of service invoker type
     */
    public AllocateInteractionExecutor(String ownerTaskId, InitializationByType type) {
        super(ownerTaskId, type);
    }

    /**
     * Get the binding allocator.
     * @return get allocator instance.
     */
    public RAllocator GetAllocator() {
        return this.allocator;
    }

    /**
     * Use allocator to handle allocation service.
     * @param candidateSet candidate participant set
     * @param workitem workitem context
     * @return Allocated Participant context.
     */
    public ParticipantContext PerformAllocation(HashSet<ParticipantContext> candidateSet, WorkitemContext workitem) {
        if (this.allocator == null) {
            LogUtil.Log("Perform allocation before binding executor to an allocator.",
                    AllocateInteractionExecutor.class.getName(), LogLevelType.ERROR, workitem.getEntity().getRtid());
            return null;
        }
        return this.allocator.PerformAllocate(candidateSet, workitem);
    }

    /**
     * Binding a allocator to this executor by the principle of task.
     * @param principle allocation principle
     */
    public void BindingAllocator(RPrinciple principle, String rstid, String rtid) throws Exception {
        this.allocator = ReflectUtil.ReflectAllocator(principle.getDistributorName(), rstid, rtid);
        this.allocator.BindingPrinciple(principle);
    }
}
