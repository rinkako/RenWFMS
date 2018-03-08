/*
 * Project Ren @ 2018
 * Rinkako, Ariana, Gordan. SYSU SDCS.
 */
package org.sysu.renResourcing.allocator;

import org.sysu.renCommon.enums.WorkQueueType;
import org.sysu.renResourcing.context.ParticipantContext;
import org.sysu.renResourcing.context.WorkQueueContainer;
import org.sysu.renResourcing.context.WorkitemContext;

import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;

/**
 * Author: Rinkako
 * Date  : 2018/2/9
 * Usage : Performs allocation based on work queue length, prefer shortest.
 */
public class ShortestQueueAllocator extends RAllocator {

    /**
     * Allocator description.
     */
    public static final String Descriptor = "The Shortest-Queue allocator " +
            "chooses from a distribution set the participant with the " +
            "shortest Allocated queue (i.e. the least number of workitems " +
            "in the Allocated queue).";

    /**
     * Constructor for reflect.
     */
    public ShortestQueueAllocator() {
        this("Allocator_" + UUID.randomUUID().toString(), ShortestQueueAllocator.class.getName(), null);
    }

    /**
     * Create a new allocator.
     * Allocator should not be created directly, use {@code AllocateInteractionExecutor} instead.
     *
     * @param id          unique id for selector fetching
     * @param type        type name string
     * @param args        parameter dictionary in HashMap
     */
    public ShortestQueueAllocator(String id, String type, HashMap<String, String> args) {
        super(id, type, ShortestQueueAllocator.Descriptor, args);
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
        int currentShortest = Integer.MAX_VALUE;
        int currentLength;
        ParticipantContext retCtx = null;
        for (ParticipantContext p : candidateSet) {
            WorkQueueContainer container = WorkQueueContainer.GetContext(p.getWorkerId());
            if (container.IsNullOrEmptyQueue(WorkQueueType.ALLOCATED)) {
                return p;
            }
            else {
                currentLength = container.DirectlyGetQueue(WorkQueueType.ALLOCATED).Count();
            }
            if (currentLength < currentShortest) {
                currentShortest = currentLength;
                retCtx = p;
            }
        }
        return retCtx;
    }
}
