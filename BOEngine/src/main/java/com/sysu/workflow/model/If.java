
package com.sysu.workflow.model;

import com.sysu.workflow.*;
import com.sysu.workflow.semantics.ErrorConstants;

import java.util.ArrayList;
import java.util.List;

/**
 * The class in this SCXML object model that corresponds to the
 * &lt;if&gt; SCXML element, which serves as a container for conditionally
 * executed elements. &lt;else&gt; and &lt;elseif&gt; can optionally
 * appear within an &lt;if&gt; as immediate children, and serve to partition
 * the elements within an &lt;if&gt;.
 */
public class If extends Action implements ActionsContainer {

    /**
     * Serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * An conditional expression which can be evaluated to true or false.
     */
    private String cond;

    /**
     * The set of executable elements (those that inheriting from
     * Action) that are contained in this &lt;if&gt; element.
     */
    private List<Action> actions;

    /**
     * The boolean value that dictates whether the particular child action
     * should be executed.
     */
    private boolean execute;

    /**
     * Constructor.
     */
    public If() {
        super();
        this.actions = new ArrayList<Action>();
        this.execute = false;
    }

    public final String getContainerElementName() {
        return ELEM_IF;
    }

    /**
     * Get the executable actions contained in this &lt;if&gt;.
     *
     * @return Returns the actions.
     */
    public final List<Action> getActions() {
        return actions;
    }

    /**
     * Add an Action to the list of executable actions contained in
     * this &lt;if&gt;.
     *
     * @param action The action to add.
     */
    public final void addAction(final Action action) {
        if (action != null) {
            this.actions.add(action);
        }
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
    public void execute(ActionExecutionContext exctx) throws ModelException, SCXMLExpressionException {
        EnterableState parentState = getParentEnterableState();
        Context ctx = exctx.getContext(parentState);
        Evaluator eval = exctx.getEvaluator();
        ctx.setLocal(getNamespacesKey(), getNamespaces());
        Boolean rslt;
        try {
            rslt = eval.evalCond(ctx, cond);
            if (rslt == null) {
                if (exctx.getAppLog().isDebugEnabled()) {
                    exctx.getAppLog().debug("Treating as false because the cond expression was evaluated as null: '"
                            + cond + "'");
                }
                rslt = Boolean.FALSE;
            }
        } catch (SCXMLExpressionException e) {
            rslt = Boolean.FALSE;
            exctx.getInternalIOProcessor().addEvent(new TriggerEvent(TriggerEvent.ERROR_EXECUTION, TriggerEvent.ERROR_EVENT));
            exctx.getErrorReporter().onError(ErrorConstants.EXPRESSION_ERROR, "Treating as false due to error: "
                    + e.getMessage(), this);
        }
        execute = rslt;
        ctx.setLocal(getNamespacesKey(), null);
        // The "if" statement is a "container"
        for (Action aa : actions) {
            if (execute && !(aa instanceof ElseIf)) {
                aa.execute(exctx);
            } else if (execute && aa instanceof ElseIf) {
                break;
            } else if (aa instanceof Else) {
                execute = true;
            } else if (aa instanceof ElseIf) {
                ctx.setLocal(getNamespacesKey(), getNamespaces());
                execute = eval.evalCond(ctx, ((ElseIf) aa).getCond());
                ctx.setLocal(getNamespacesKey(), null);
            }
        }
    }

}

