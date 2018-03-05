/*
 * Project Ren @ 2018
 * Rinkako, Ariana, Gordan. SYSU SDCS.
 */
package org.sysu.renResourcing.executor;

import org.sysu.renCommon.enums.InitializationByType;
import org.sysu.renCommon.enums.LogLevelType;
import org.sysu.renResourcing.context.ParticipantContext;
import org.sysu.renResourcing.context.WorkitemContext;
import org.sysu.renResourcing.filter.RFilter;
import org.sysu.renResourcing.principle.RPrinciple;
import org.sysu.renResourcing.utility.LogUtil;
import org.sysu.renResourcing.utility.ReflectUtil;

import java.util.HashSet;
import java.util.Set;

/**
 * Author: Rinkako
 * Date  : 2018/2/4
 * Usage :
 */
public class OfferInteractionExecutor extends InteractionExecutor {
    /**
     * Binding allocator.
     */
    private RFilter filter;

    /**
     * Get the binding filter.
     * @return get filter instance.
     */
    public RFilter GetFilter() {
        return this.filter;
    }

    /**
     * Create a new offer interaction executor.
     * @param ownerTaskId id of task to create this
     * @param type type of service invoker type
     */
    public OfferInteractionExecutor(String ownerTaskId, InitializationByType type) {
        super(ownerTaskId, type);
    }

    /**
     * Use allocator to handle allocation service.
     * @return Filtered Participant context set.
     */
    public Set<ParticipantContext> PerformOffer(HashSet<ParticipantContext> candidateSet, WorkitemContext workitem) {
        if (this.filter == null) {
            LogUtil.Log("Perform offer before binding executor to a filter.",
                    OfferInteractionExecutor.class.getName(), LogLevelType.ERROR, "");
            return null;
        }
        return this.filter.PerformFilter(candidateSet, workitem);
    }

    /**
     * Binding a allocator to this executor by the principle of task.
     * @param principle allocation principle
     */
    public void BindingFilter(RPrinciple principle, String rstid, String rtid) throws Exception {
        this.filter = ReflectUtil.ReflectFilter(principle.getDistributorName(), rstid, rtid);
        this.filter.BindingPrinciple(principle);
    }
}
