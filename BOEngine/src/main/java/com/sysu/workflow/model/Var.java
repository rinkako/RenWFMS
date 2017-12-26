
package com.sysu.workflow.model;

import com.sysu.workflow.*;

/**
 * The class in this SCXML object model that corresponds to the
 * &lt;var&gt; SCXML element.
 */
public class Var extends Action {

    /**
     * Serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The name of the variable to be created.
     */
    private String name;

    /**
     * The expression that evaluates to the initial value of the variable.
     */
    private String expr;

    /**
     * Constructor.
     */
    public Var() {
        super();
    }

    /**
     * Get the expression that evaluates to the initial value
     * of the variable.
     *
     * @return String Returns the expr.
     */
    public final String getExpr() {
        return expr;
    }

    /**
     * Set the expression that evaluates to the initial value
     * of the variable.
     *
     * @param expr The expr to set.
     */
    public final void setExpr(final String expr) {
        this.expr = expr;
    }

    /**
     * Get the name of the (new) variable.
     *
     * @return String Returns the name.
     */
    public final String getName() {
        return name;
    }

    /**
     * Set the name of the (new) variable.
     *
     * @param name The name to set.
     */
    public final void setName(final String name) {
        this.name = name;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute(ActionExecutionContext exctx) throws ModelException, SCXMLExpressionException {
        Context ctx = exctx.getContext(getParentEnterableState());
        Evaluator eval = exctx.getEvaluator();
        ctx.setLocal(getNamespacesKey(), getNamespaces());
        Object varObj = eval.eval(ctx, expr);
        ctx.setLocal(getNamespacesKey(), null);
        ctx.setLocal(name, varObj);
        if (exctx.getAppLog().isDebugEnabled()) {
            exctx.getAppLog().debug("<var>: Defined variable '" + name
                    + "' with initial value '" + String.valueOf(varObj) + "'");
        }
        TriggerEvent ev = new TriggerEvent(name + ".change", TriggerEvent.CHANGE_EVENT);
        exctx.getInternalIOProcessor().addEvent(ev);
    }
}

