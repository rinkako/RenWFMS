
package com.sysu.workflow.env.xpath;

import com.sysu.workflow.SCXMLSystemContext;
import com.sysu.workflow.Status;
import org.apache.commons.jxpath.ExpressionContext;
import org.apache.commons.jxpath.Variables;

import java.util.Map;

/**
 * Commons JXPath custom extension function providing the SCXML In() predicate
 */
public class XPathFunctions {

    /**
     * Provides the SCXML standard In() predicate for SCXML documents.
     *
     * @param expressionContext The context currently in use for evaluation
     * @param state             The State ID to compare with
     * @return true if this state is currently active
     */
    @SuppressWarnings("unchecked")
    public static boolean In(ExpressionContext expressionContext, String state) {
        Variables variables = expressionContext.getJXPathContext().getVariables();
        Map<String, Object> platformVariables = (Map<String, Object>) variables.getVariable(SCXMLSystemContext.X_KEY);
        return ((Status) platformVariables.get(SCXMLSystemContext.STATUS_KEY)).isInState(state);
    }
}
