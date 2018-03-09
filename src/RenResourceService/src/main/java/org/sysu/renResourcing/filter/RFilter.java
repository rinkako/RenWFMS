/*
 * Project Ren @ 2018
 * Rinkako, Ariana, Gordan. SYSU SDCS.
 */
package org.sysu.renResourcing.filter;

import org.sysu.renResourcing.RSelector;
import org.sysu.renResourcing.context.ParticipantContext;
import org.sysu.renResourcing.context.WorkitemContext;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Author: Rinkako
 * Date  : 2018/2/2
 * Usage : Base allocator for all implemented filters.
 *         Filter is used to remove participants in candidate set who cannot map the filter conditions.
 */
public abstract class RFilter extends RSelector implements Serializable {

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
    public RFilter(String id, String type, String description, HashMap<String, String> args) {
        super(id, type, description, args);
    }

    /**
     * Perform filter on the candidate set.
     * @param candidateSet candidate participant set
     * @param context workitem context
     * @return filtered participant set
     */
    public abstract HashSet<ParticipantContext> PerformFilter(HashSet<ParticipantContext> candidateSet, WorkitemContext context);
}
