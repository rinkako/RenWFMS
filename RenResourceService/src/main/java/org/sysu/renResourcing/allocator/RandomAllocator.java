/*
 * Project Ren @ 2018
 * Rinkako, Ariana, Gordan. SYSU SDCS.
 */
package org.sysu.renResourcing.allocator;

import org.sysu.renResourcing.context.ParticipantContext;
import org.sysu.renResourcing.context.WorkitemContext;
import org.sysu.renCommon.utility.CommonUtil;

import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;

/**
 * Author: Rinkako
 * Date  : 2018/2/13
 * Usage : Performs allocation based on random choose.
 */
public class RandomAllocator extends RAllocator {

    /**
     * Allocator description.
     */
    public static final String Descriptor = "The random allocator chooses " +
            "from a distribution set the participant randomly.";

    /**
     * Constructor for reflect.
     */
    public RandomAllocator() {
        this("Allocator_" + UUID.randomUUID().toString(), RandomAllocator.class.getName(), null);
    }

    /**
     * Create a new allocator.
     * Allocator should not be created directly, use {@code AllocateInteractionExecutor} instead.
     *
     * @param id          unique id for selector fetching
     * @param type        type name string
     * @param args        parameter dictionary in HashMap
     */
    public RandomAllocator(String id, String type, HashMap<String, String> args) {
        super(id, type, RandomAllocator.Descriptor, args);
    }

    /**
     * Perform allocation on the candidate set.
     *
     * @param candidateSet candidate participant set
     * @param context workitem context
     * @return selected participant
     */
    @Override
    public ParticipantContext PerformAllocate(HashSet<ParticipantContext> candidateSet, WorkitemContext context) {
        int chosenIdx = CommonUtil.GenerateRandomNumber(0, candidateSet.size());
        return (ParticipantContext) candidateSet.toArray()[chosenIdx];
    }
}
