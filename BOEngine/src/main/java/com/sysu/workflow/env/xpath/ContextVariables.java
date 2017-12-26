
package com.sysu.workflow.env.xpath;

import com.sysu.workflow.Context;
import org.apache.commons.jxpath.Variables;

/**
 * JXPath Variables mapping for SCXML Context
 */
public class ContextVariables implements Variables {

    private final Context ctx;

    public ContextVariables(Context ctx) {
        this.ctx = ctx;
    }


    public boolean isDeclaredVariable(final String varName) {
        return ctx.has(varName);
    }


    public Object getVariable(final String varName) {
        return ctx.get(varName);
    }


    public void declareVariable(final String varName, final Object value) {
        ctx.set(varName, value);
    }


    public void undeclareVariable(final String varName) {
        if (ctx.has(varName)) {
            Context cctx = ctx;
            while (!cctx.hasLocal(varName)) {
                cctx = cctx.getParent();
                if (cctx == null) {
                    return;
                }
            }
            cctx.getVars().remove(varName);
        }
    }
}
