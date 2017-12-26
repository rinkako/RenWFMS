
package com.sysu.workflow.env.minimal;

import com.sysu.workflow.*;
import com.sysu.workflow.model.SCXML;

import java.io.Serializable;

/**
 * Minimal Evaluator implementing and providing support for the SCXML Null Data Model.
 * <p>
 * The SCXML Null Data Model only supports the SCXML "In(stateId)" builtin function.
 * </p>
 */
public class MinimalEvaluator implements Evaluator, Serializable {

    /**
     * Serial version UID.
     */
    private static final long serialVersionUID = 1L;

    public static final String SUPPORTED_DATA_MODEL = Evaluator.NULL_DATA_MODEL;

    public static class MinimalEvaluatorProvider implements EvaluatorProvider {


        public String getSupportedDatamodel() {
            return SUPPORTED_DATA_MODEL;
        }


        public Evaluator getEvaluator() {
            return new MinimalEvaluator();
        }


        public Evaluator getEvaluator(final SCXML document) {
            return new MinimalEvaluator();
        }
    }


    public String getSupportedDatamodel() {
        return SUPPORTED_DATA_MODEL;
    }


    public Object eval(final Context ctx, final String expr) throws SCXMLExpressionException {
        return expr;
    }


    public Boolean evalCond(final Context ctx, final String expr) throws SCXMLExpressionException {
        // only support the "In(stateId)" predicate
        String predicate = expr != null ? expr.trim() : "";
        if (predicate.startsWith("In(") && predicate.endsWith(")")) {
            String stateId = predicate.substring(3, predicate.length() - 1);
            return Builtin.isMember(ctx, stateId);
        }
        return false;
    }


    public Object evalLocation(final Context ctx, final String expr) throws SCXMLExpressionException {
        return expr;
    }


    public void evalAssign(final Context ctx, final String location, final Object data, final AssignType type, final String attr) throws SCXMLExpressionException {
        throw new UnsupportedOperationException("Assign expressions are not supported by the \"null\" datamodel");
    }


    public Object evalScript(final Context ctx, final String script) throws SCXMLExpressionException {
        throw new UnsupportedOperationException("Scripts are not supported by the \"null\" datamodel");
    }


    public Context newContext(final Context parent) {
        return parent instanceof MinimalContext ? parent : new MinimalContext(parent);
    }
}
