/*
 * Project Ren @ 2018
 * Rinkako, Ariana, Gordan. SYSU SDCS.
 */
package org.sysu.renResourcing.allocator;

import org.sysu.renResourcing.context.ParticipantContext;
import org.sysu.renResourcing.context.WorkitemContext;

import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;

/**
 * Author: Rinkako
 * Date  : 2018/3/8
 * Usage : Performs allocation on rounding choose.
 */
public class RoundAllocator extends RAllocator {

    /**
     * Allocator description.
     */
    public static final String Descriptor = "The round allocator is special allocator. It will " +
            "allocate the workitem to a participant never receive this type of workitem.";

    /**
     * Constructor for reflect.
     */
    public RoundAllocator() {
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
    public RoundAllocator(String id, String type, HashMap<String, String> args) {
        super(id, type, RandomAllocator.Descriptor, args);
    }

    /**
     * Perform allocation on the candidate set.
     *
     * @param candidateSet candidate participant set
     * @param context      workitem context
     * @return selected participant
     */
    @Override
    public ParticipantContext PerformAllocate(HashSet<ParticipantContext> candidateSet, WorkitemContext context) {
        synchronized (RoundAllocator.roundingSetMutex) {
            HashSet<String> roundSet = roundingCloseSet.computeIfAbsent(String.format("%s_%s", context.getTaskContext().getTaskGlobalId(), context.getEntity().getRtid()), k -> new HashSet<>());
            for (ParticipantContext participant : candidateSet) {
                if (!roundSet.contains(participant.getWorkerId())) {
                    roundSet.add(participant.getWorkerId());
                    return participant;
                }
            }
            // round but no one satisfied, re round
            roundSet.clear();
            ParticipantContext reRounded = candidateSet.iterator().next();
            roundSet.add(reRounded.getWorkerId());
            return reRounded;
        }
    }

    /**
     * Rounding set, pattern (taskGlobalId_rtid, Set of allocated workerId).
     */
    private static final HashMap<String, HashSet<String>> roundingCloseSet = new HashMap<>();

    /**
     * Mutex for rounding set.
     */
    private static final Object roundingSetMutex = new Object();
}
