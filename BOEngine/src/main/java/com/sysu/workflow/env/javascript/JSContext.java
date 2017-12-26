

package com.sysu.workflow.env.javascript;

import com.sysu.workflow.Context;
import com.sysu.workflow.env.SimpleContext;

import java.util.Map;

/**
 * SCXML Context for use by the JSEvaluator. It is simply a 'no functionality'
 * extension of SimpleContext that has been implemented to reduce the impact
 * if the JSEvaluator requires additional functionality at a later stage.
 * <p/>
 * Could easily be dispensed with.
 */
public class JSContext extends SimpleContext {

    /**
     * Serial version UID.
     */
    private static final long serialVersionUID = 1L;

    // CONSTRUCTORS

    /**
     * Default constructor - just invokes the SimpleContext default
     * constructor.
     */
    public JSContext() {
        super();
    }

    /**
     * Constructor with initial vars.
     *
     * @param parent      The parent context
     * @param initialVars The initial set of variables.
     */
    public JSContext(final Context parent, final Map<String, Object> initialVars) {
        super(parent, initialVars);
    }

    /**
     * Child constructor. Just invokes the identical SimpleContext
     * constructor.
     *
     * @param parent Parent context for this context.
     */
    public JSContext(final Context parent) {
        super(parent);
    }

}

