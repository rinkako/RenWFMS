
package com.sysu.workflow.env.jexl;

import com.sysu.workflow.Context;
import com.sysu.workflow.env.SimpleContext;

import java.util.Map;

/**
 * JEXL Context implementation for Commons SCXML.
 */
public class JexlContext extends SimpleContext
        implements org.apache.commons.jexl2.JexlContext {

    /**
     * Serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Constructor.
     */
    public JexlContext() {
        super();
    }

    /**
     * Constructor with initial vars.
     *
     * @param parent      The parent context
     * @param initialVars The initial set of variables.
     */
    public JexlContext(final Context parent, final Map<String, Object> initialVars) {
        super(parent, initialVars);
    }

    /**
     * Constructor with parent context.
     *
     * @param parent The parent context.
     */
    public JexlContext(final Context parent) {
        super(parent);
    }
}

