package org.sysu.renResourcing.allocator;

import org.sysu.renCommon.enums.LogLevelType;
import org.sysu.renResourcing.RSelector;
import org.sysu.renResourcing.context.ParticipantContext;
import org.sysu.renResourcing.context.WorkitemContext;
import org.sysu.renResourcing.utility.LogUtil;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 * Author: Rinkako
 * Date  : 2017/11/15
 * Usage : Base allocator for all implemented allocators.
 *         Allocator is used to choose a participant to handle task from candidate set.
 */
public abstract class RAllocator extends RSelector implements Serializable {

    /**
     * Serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Parameter name of allocation instance.
     */
    protected static final String ParameterInstanceCount = "instance";

    /**
     * Apply principle to configure selector.
     */
    @Override
    protected void ApplyPrinciple() {

    }

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
     * @param candidateSet candidate participant set
     * @param context workitem context
     * @return selected participant
     */
    public abstract ParticipantContext PerformAllocate(HashSet<ParticipantContext> candidateSet, WorkitemContext context);
}
