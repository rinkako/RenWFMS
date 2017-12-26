
package com.sysu.workflow.model;

import com.sysu.workflow.ActionExecutionContext;
import com.sysu.workflow.Context;
import com.sysu.workflow.Evaluator;
import com.sysu.workflow.SCXMLExpressionException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * A <code>ParamsContainer</code> represents an element in the SCXML
 * document that may have one or more &lt;param/&gt; children which are used to
 * produce payload for events or external communication.
 */
public abstract class ParamsContainer extends PayloadProvider {

    /**
     * The List of the params to be sent
     */
    private final List<Param> paramsList = new ArrayList<Param>();

    /**
     * Get the list of {@link Param}s.
     *
     * @return List The params list.
     */
    public List<Param> getParams() {
        return paramsList;
    }

    /**
     * Adds data to the payload data map based on the {@link Param}s of this {@link ParamsContainer}
     *
     * @param exctx   The ActionExecutionContext
     * @param payload the payload data map to be updated
     * @throws ModelException           if this action has not an EnterableState as parent
     * @throws SCXMLExpressionException if a malformed or invalid expression is evaluated
     * @see PayloadProvider#addToPayload(String, Object, Map)
     */
    protected void addParamsToPayload(ActionExecutionContext exctx, Map<String, Object> payload)
            throws ModelException, SCXMLExpressionException {
        if (!paramsList.isEmpty()) {
            EnterableState parentState = getParentEnterableState();
            Context ctx = exctx.getContext(parentState);
            try {
                ctx.setLocal(getNamespacesKey(), getNamespaces());
                Evaluator evaluator = exctx.getEvaluator();
                Object paramValue;
                for (Param p : paramsList) {
                    if (p.getExpr() != null) {
                        paramValue = evaluator.eval(ctx, p.getExpr());
                    } else if (p.getLocation() != null) {
                        paramValue = evaluator.eval(ctx, p.getLocation());
                    } else {
                        // ignore invalid param definition
                        continue;
                    }
                    addToPayload(p.getName(), paramValue, payload);
                }
            } finally {
                ctx.setLocal(getNamespacesKey(), null);
            }
        }
    }

    protected void addParamsToForm(ActionExecutionContext exctx, Map<String, Object> payload)
            throws ModelException, SCXMLExpressionException {
        if (!paramsList.isEmpty()) {
            EnterableState parentState = getParentEnterableState();
            Context ctx = exctx.getContext(parentState);
            try {
                ctx.setLocal(getNamespacesKey(), getNamespaces());
                Evaluator evaluator = exctx.getEvaluator();
                Object paramValue = null;
                String paramType = "output";
                for (Param p : paramsList) {
                    if (p.getExpr() != null) {
                        paramValue = evaluator.eval(ctx, p.getExpr());
                    } else if (p.getLocation() != null) {
                        paramValue = evaluator.eval(ctx, p.getLocation());
                    }

                    addToPayload(p.getName(), paramValue, payload);
                    if (p.getType() != null) {
                        paramType = p.getType();
                    }
                    Object[] paramObject = new Object[]{payload.get(p.getName()), paramType};
                    payload.put(p.getName(), paramObject);
                }
            } finally {
                ctx.setLocal(getNamespacesKey(), null);
            }
        }
    }
}
