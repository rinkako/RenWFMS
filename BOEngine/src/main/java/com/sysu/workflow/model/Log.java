
package com.sysu.workflow.model;

import com.sysu.workflow.*;

/**
 * The class in this SCXML object model that corresponds to the
 * &lt;log&gt; SCXML element.
 */
public class Log extends Action {

    /**
     * Serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * An expression evaluating to a string to be logged.
     */
    private String expr;

    /**
     * An expression which returns string which may be used, for example,
     * to indicate the purpose of the log.
     */
    private String label;

    /**
     * Constructor.
     */
    public Log() {
        super();
    }

    /**
     * Get the log expression.
     *
     * @return Returns the expression.
     */
    public final String getExpr() {
        return expr;
    }

    /**
     * Set the log expression.
     *
     * @param expr The expr to set.
     */
    public final void setExpr(final String expr) {
        this.expr = expr;
    }

    /**
     * Get the log label.
     *
     * @return Returns the label.
     */
    public final String getLabel() {
        return label;
    }

    /**
     * Set the log label.
     *
     * @param label The label to set.
     */
    public final void setLabel(final String label) {
        this.label = label;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute(ActionExecutionContext exctx) throws ModelException, SCXMLExpressionException {
        Context ctx = exctx.getContext(getParentEnterableState());
        Evaluator eval = exctx.getEvaluator();
        ctx.setLocal(getNamespacesKey(), getNamespaces());
        String logMsg = String.valueOf(getTextContentIfNodeResult(eval.eval(ctx, expr)));
        exctx.getAppLog().info(label + ": " + logMsg);
        ctx.setLocal(getNamespacesKey(), null);

        // Debug test send Msg To App
        //EngineBridge.QuickEnqueueBOMessage(logMsg);
    }
}

