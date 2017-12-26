
package com.sysu.workflow.model;

import com.sysu.workflow.ActionExecutionContext;
import com.sysu.workflow.Context;
import com.sysu.workflow.Evaluator;
import com.sysu.workflow.SCXMLExpressionException;

/**
 * The class in this SCXML object model that corresponds to the
 * &lt;script&gt; SCXML element.
 * <p/>
 * TODO src attribute support
 */
public class Script extends Action implements BodyContainer {

    /**
     * Serial version UID.
     */
    private static final long serialVersionUID = 1L;

    private boolean globalScript;
    private String body;

    /**
     * Constructor.
     */
    public Script() {
        super();
    }

    public boolean isGlobalScript() {
        return globalScript;
    }

    public void setGlobalScript(final boolean globalScript) {
        this.globalScript = globalScript;
    }


    public String getBody() {
        return body;
    }


    public void setBody(String body) {
        this.body = body;
    }

    /**
     * Get the script to execute.
     *
     * @return The script to execute.
     */
    public String getScript() {
        return body;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute(ActionExecutionContext exctx) throws ModelException, SCXMLExpressionException {
        Context ctx = isGlobalScript() ? exctx.getGlobalContext() : exctx.getContext(getParentEnterableState());
        ctx.setLocal(getNamespacesKey(), getNamespaces());
        Evaluator eval = exctx.getEvaluator();
        eval.evalScript(ctx, getScript());
        ctx.setLocal(getNamespacesKey(), null);
    }

}

