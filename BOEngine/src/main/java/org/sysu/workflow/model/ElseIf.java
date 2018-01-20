
package org.sysu.workflow.model;

import org.sysu.workflow.ActionExecutionContext;
import org.sysu.workflow.SCXMLExpressionException;

/**
 * The class in this SCXML object model that corresponds to the
 * &lt;elseif&gt; SCXML element.
 */
public class ElseIf extends Action {

    /**
     * Serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * An conditional expression which can be evaluated to true or false.
     */
    private String cond;

    /**
     * Constructor.
     */
    public ElseIf() {
        super();
    }

    /**
     * Get the conditional expression.
     *
     * @return Returns the cond.
     */
    public final String getCond() {
        return cond;
    }

    /**
     * Set the conditional expression.
     *
     * @param cond The cond to set.
     */
    public final void setCond(final String cond) {
        this.cond = cond;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute(ActionExecutionContext exctx) {
        // nothing to do, the <if> container will take care of this
    }

}

