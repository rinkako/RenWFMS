
package org.sysu.workflow;

import java.io.Serializable;

/**
 * Implementation of the SCXML specification required In() builtin predicate.
 */
public class Builtin implements Serializable {

    /**
     * Serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Implements the In() predicate for SCXML documents. The method
     * name chosen is different since &quot;in&quot; is a reserved token
     * in some expression languages.
     * <p/>
     * Simple ID based comparator, assumes IDs are unique.
     *
     * @param ctx   variable context
     * @param state The State ID to compare with
     * @return Whether this State is current active
     */
    @SuppressWarnings("unchecked")
    public static boolean isMember(final Context ctx, final String state) {
        return ((Status) ctx.getSystemContext().getPlatformVariables().get(BOXMLSystemContext.STATUS_KEY)).isInState(state);
    }
}

