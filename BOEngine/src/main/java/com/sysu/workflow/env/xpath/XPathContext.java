
package com.sysu.workflow.env.xpath;

import com.sysu.workflow.Context;
import com.sysu.workflow.env.SimpleContext;
import org.apache.commons.jxpath.Variables;

/**
 * A {@link Context} implementation for JXPath environments.
 */
public class XPathContext extends SimpleContext
        implements Context, Variables {

    /**
     * Serial version UID.
     */
    private static final long serialVersionUID = -6803159294612685806L;

    /**
     * No argument constructor.
     */
    public XPathContext() {
        super();
    }

    /**
     * Constructor for cascading contexts.
     *
     * @param parent The parent context. Can be null.
     */
    public XPathContext(final Context parent) {
        super(parent);
    }


    public boolean isDeclaredVariable(final String varName) {
        return has(varName);
    }


    public Object getVariable(final String varName) {
        return get(varName);
    }


    public void declareVariable(final String varName, final Object value) {
        set(varName, value);
    }


    public void undeclareVariable(final String varName) {
        if (has(varName)) {
            Context ctx = this;
            while (!ctx.hasLocal(varName)) {
                ctx = ctx.getParent();
                if (ctx == null) {
                    return;
                }
            }
            ctx.getVars().remove(varName);
        }
    }
}
