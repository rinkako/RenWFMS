
package com.sysu.workflow;

import com.sysu.workflow.env.xpath.XPathEvaluator;

/**
 * Implementation and support of Commons SCXML builtin predicates to support XPath based datamodel operations
 * for non-XPath languages.
 * <p/>
 * These static builtin functions delegate to a static {@link }XPathEvaluator} instance.
 */
public class XPathBuiltin {

    private static XPathEvaluator evaluator = new XPathEvaluator();

    /**
     * Optional static setter to change and override the default {@link XPathEvaluator}
     *
     * @param evaluator A custom evaluator to be used
     */
    public static void setEvaluator(XPathEvaluator evaluator) {
        XPathBuiltin.evaluator = evaluator;
    }

    /**
     * Evaluate an xpath expression returning a data value
     *
     * @param ctx        variable context
     * @param expression xpath expression
     * @return the result of the evaluation
     * @throws SCXMLExpressionException A malformed expression exception
     * @see Evaluator#eval(Context, String)
     */
    public static Object eval(Context ctx, String expression) throws SCXMLExpressionException {
        return evaluator.eval(ctx, expression);
    }

    /**
     * Evaluate an xpath location that returns a data assignable reference or list of references.
     * Manifests as "location" attributes of &lt;assign&gt; element.
     *
     * @param ctx        variable context
     * @param expression expression
     * @return The location result.
     * @throws SCXMLExpressionException A malformed expression exception
     * @see Evaluator#evalLocation(Context, String)
     */
    public static Object evalLocation(Context ctx, String expression) throws SCXMLExpressionException {
        return evaluator.evalLocation(ctx, expression);
    }

    /**
     * Determine if an {@link Evaluator#evalLocation(Context, String)} returned result represents an XPath location
     *
     * @param ctx  variable context
     * @param data result data from {@link Evaluator#evalLocation(Context, String)}
     * @return true if the data represents an XPath location
     * @see XPathEvaluator#isXPathLocation(Context, Object)
     */
    public static boolean isXPathLocation(Context ctx, Object data) {
        return evaluator.isXPathLocation(ctx, data);
    }

    /**
     * Assigns data to a location
     *
     * @param ctx      variable context
     * @param location location expression
     * @param data     the data to assign.
     * @param type     the type of assignment to perform, null assumes {@link Evaluator.AssignType#REPLACE_CHILDREN}
     * @param attr     the name of the attribute to add when using type {@link Evaluator.AssignType#ADD_ATTRIBUTE}
     * @throws SCXMLExpressionException A malformed expression exception
     * @see Evaluator#evalAssign(Context, String, Object, Evaluator.AssignType, String)
     * @see XPathEvaluator#assign(Context, Object, Object, Evaluator.AssignType, String)
     */
    public static void assign(Context ctx, Object location, Object data, Evaluator.AssignType type, String attr)
            throws SCXMLExpressionException {
        evaluator.assign(ctx, location, data, type, attr);
    }
}
