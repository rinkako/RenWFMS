/*
 * Project Ren @ 2018
 * Rinkako, Ariana, Gordan. SYSU SDCS.
 */
package org.sysu.renResourcing.constraint;

import org.sysu.renResourcing.RSelector;
import org.sysu.renResourcing.context.ParticipantContext;
import org.sysu.renResourcing.context.WorkitemContext;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Author: Rinkako
 * Date  : 2018/2/2
 * Usage : Base allocator for all implemented constraints.
 *         Constraint is used to make the work item allocation in some specific way.
 */
public abstract class RConstraint extends RSelector implements Serializable {
    /**
     * Serial version UID.
     */
    private static final long serialVersionUID = 1L;

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
    public abstract HashSet<ParticipantContext> PerformConstraint(HashSet<ParticipantContext> candidateSet, WorkitemContext context);
}
