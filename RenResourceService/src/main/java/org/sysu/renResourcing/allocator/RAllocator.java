package org.sysu.renResourcing.allocator;

import org.sysu.renResourcing.basic.RSelector;
import org.sysu.renResourcing.context.RParticipant;
import org.sysu.renResourcing.context.ResourcingContext;

import java.util.HashMap;
import java.util.HashSet;

/**
 * Author: Rinkako
 * Date  : 2017/11/15
 * Usage : Base allocator for all implemented allocators.
 *         Allocator is used to choose a participant to handle task from candidate set.
 */
public abstract class RAllocator extends RSelector {
    /**
     * Create a new allocator.
     * @param id unique id for selector fetching
     * @param type type name string
     * @param description selector description text
     * @param args parameter dictionary in HashMap
     */
    public RAllocator(String id, String type, String description, HashMap<String, String> args) {
        super(id, type, description, args);
    }

    /**
     * Perform allocation on the candidate set.
     * @return selected participant
     */
    public abstract RParticipant PerformAllocate(HashSet<RParticipant> candidateSet, ResourcingContext context);


}
