
package com.sysu.workflow.model;

import com.sysu.workflow.ActionExecutionContext;
import com.sysu.workflow.Context;
import com.sysu.workflow.Evaluator;
import com.sysu.workflow.SCXMLExpressionException;
import com.sysu.workflow.semantics.ErrorConstants;

import java.util.Map;
import java.util.StringTokenizer;

/**
 * A <code>NamelistHolder</code> represents an element in the SCXML
 * document that may have a namelist attribute to
 * produce payload for events or external communication.
 *
 * NamelistHolder 呈现了一个SCXML文档中有一个namelist属性的容器，
 */
public abstract class NamelistHolder extends ParamsContainer {

    /**
     * The namelist.
     */
    private String namelist;

    /**
     * Get the namelist.
     *
     * @return String Returns the namelist.
     */
    public final String getNamelist() {
        return namelist;
    }

    /**
     * Set the namelist.
     *
     * @param namelist The namelist to set.
     */
    public final void setNamelist(final String namelist) {
        this.namelist = namelist;
    }

    /**
     * Adds data to the payload data map based on the namelist which names are location expressions
     * (typically data ids or for example XPath variables). The names and the values they 'point' at
     * are added to the payload data map.
     *
     * @param exctx   The ActionExecutionContext
     * @param payload the payload data map to be updated
     * @throws ModelException           if this action has not an EnterableState as parent
     * @throws SCXMLExpressionException if a malformed or invalid expression is evaluated
     * @see PayloadProvider#addToPayload(String, Object, Map)
     */
    protected void addNamelistDataToPayload(ActionExecutionContext exctx, Map<String, Object> payload)
            throws ModelException, SCXMLExpressionException {
        if (namelist != null) {
            EnterableState parentState = getParentEnterableState();
            Context ctx = exctx.getContext(parentState);
            try {
                ctx.setLocal(getNamespacesKey(), getNamespaces());
                Evaluator evaluator = exctx.getEvaluator();
                StringTokenizer tkn = new StringTokenizer(namelist);
                boolean xpathEvaluator = Evaluator.XPATH_DATA_MODEL.equals(evaluator.getSupportedDatamodel());
                while (tkn.hasMoreTokens()) {
                    String varName = tkn.nextToken();
                    Object varObj = evaluator.eval(ctx, varName);
                    if (varObj == null) {
                        //considered as a warning here
                        exctx.getErrorReporter().onError(ErrorConstants.UNDEFINED_VARIABLE,
                                varName + " = null", parentState);
                    }
                    if (xpathEvaluator && varName.startsWith("$")) {
                        varName = varName.substring(1);
                    }
                    addToPayload(varName, varObj, payload);
                }
            } finally {
                ctx.setLocal(getNamespacesKey(), null);
            }
        }
    }
}
