
package com.sysu.workflow.model;

import com.sysu.workflow.ActionExecutionContext;
import com.sysu.workflow.Context;
import com.sysu.workflow.Evaluator;
import com.sysu.workflow.SCXMLExpressionException;

/**
 * The class in this SCXML object model that corresponds to the
 * &lt;cancel&gt; SCXML element.
 */
public class Cancel extends Action {

    /**
     * Serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Constructor.
     */
    public Cancel() {
        super();
    }

    /**
     * The ID of the send message that should be cancelled.
     */
    private String sendid;

    /**
     * The expression that evaluates to the ID of the send message that should be cancelled.
     */
    private String sendidexpr;

    /**
     * Get the ID of the send message that should be cancelled.
     *
     * @return Returns the sendid.
     */
    public String getSendid() {
        return sendid;
    }

    /**
     * Set the ID of the send message that should be cancelled.
     *
     * @param sendid The sendid to set.
     */
    public void setSendid(final String sendid) {
        this.sendid = sendid;
    }

    /**
     * Get the expression that evaluates to the ID of the send message that should be cancelled.
     *
     * @return the expression that evaluates to the ID of the send message that should be cancelled.
     */
    public String getSendidexpr() {
        return sendidexpr;
    }

    /**
     * Set the expression that evaluates to the ID of the send message that should be cancelled.
     *
     * @param sendidexpr the expression that evaluates to the ID of the send message that should be cancelled.
     */
    public void setSendidexpr(String sendidexpr) {
        this.sendidexpr = sendidexpr;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute(ActionExecutionContext exctx) throws ModelException, SCXMLExpressionException {
        EnterableState parentState = getParentEnterableState();
        Context ctx = exctx.getContext(parentState);
        ctx.setLocal(getNamespacesKey(), getNamespaces());
        Evaluator eval = exctx.getEvaluator();

        String sendidValue = sendid;
        if (sendidValue == null && sendidexpr != null) {
            sendidValue = (String) getTextContentIfNodeResult(eval.eval(ctx, sendidexpr));
            if ((sendidValue == null || sendidValue.trim().length() == 0)
                    && exctx.getAppLog().isWarnEnabled()) {
                exctx.getAppLog().warn("<send>: sendid expression \"" + sendidexpr
                        + "\" evaluated to null or empty String");
            }
        }

        exctx.getEventDispatcher().cancel(sendidValue);
    }
}

