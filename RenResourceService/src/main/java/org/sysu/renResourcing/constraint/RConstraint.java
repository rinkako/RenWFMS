/*
 * Project Ren @ 2018
 * Rinkako, Ariana, Gordan. SYSU SDCS.
 */
package org.sysu.renResourcing.constraint;

import org.sysu.renResourcing.basic.RSelector;
import org.sysu.renResourcing.context.RParticipant;
import org.sysu.renResourcing.context.ResourcingContext;

import java.util.HashMap;
import java.util.HashSet;

/**
 * Author: Rinkako
 * Date  : 2018/2/2
 * Usage : Base allocator for all implemented constraints.
 *         Constraint is used to make the work item allocation in some specific way.
 */
public abstract class RConstraint extends RSelector {
    /**
     * Create a new filter.
     * @param id unique id for selector fetching
     * @param type type name string
     * @param description selector description text
     * @param args parameter dictionary in HashMap
     */
    public RConstraint(String id, String type, String description, HashMap<String, String> args) {
        super(id, type, description, args);
    }

    /**
     * Perform constraint on the candidate set.
     * @return filtered participant set
     */
    public abstract HashSet<RParticipant> PerformConstraint(HashSet<RParticipant> candidateSet, ResourcingContext context);
}
