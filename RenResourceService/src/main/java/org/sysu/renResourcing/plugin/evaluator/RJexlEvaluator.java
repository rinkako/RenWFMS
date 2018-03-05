/*
 * Project Ren @ 2018
 * Rinkako, Ariana, Gordan. SYSU SDCS.
 */
package org.sysu.renResourcing.plugin.evaluator;

import org.apache.commons.jexl2.Expression;
import org.apache.commons.jexl2.JexlContext;
import org.apache.commons.jexl2.JexlEngine;
import org.apache.commons.jexl2.Script;
import org.sysu.renCommon.enums.LogLevelType;
import org.sysu.renResourcing.utility.LogUtil;

/**
 * Author: Rinkako
 * Date  : 2018/2/9
 * Usage : Evaluator implementation enabling use of JEXL expressions.
 */
public class RJexlEvaluator implements REvaluator {

    /**
     * Global common engine.
     */
    public static JexlEngine CommonEngine = new JexlEngine();

    /**
     * Create a new evaluator.
     */
    public RJexlEvaluator() {
        this.evaluatorEngine = new JexlEngine();
    }

    /**
     * Internal evaluator.
     */
    private JexlEngine evaluatorEngine;

    /**
     * Evaluate an expression returning a data value.
     *
     * @param ctx  variable context
     * @param expr expression
     * @return Result of the evaluation
     */
    @Override
    public Object Evaluate(REvaluableContext ctx, String expr) {
        try {
            Expression jexlExpr = this.evaluatorEngine.createExpression(expr);
            return jexlExpr.evaluate((JexlContext) ctx.GetInternalContext());
        }
        catch (Exception ex) {
            LogUtil.Echo(String.format("Evaluate failed (Expr: %s), %s", expr, ex),
                    RJexlEvaluator.class.getName(), LogLevelType.ERROR);
            throw ex;
        }
    }

    /**
     * Evaluate a boolean condition.
     *
     * @param ctx  variable context
     * @param expr expression
     * @return boolean value of condition expression calculated result
     */
    @Override
    public Boolean EvaluateCondition(REvaluableContext ctx, String expr) {
        try {
            Expression jexlExpr = this.evaluatorEngine.createExpression(expr);
            Object result = jexlExpr.evaluate((JexlContext) ctx.GetInternalContext());
            return result == null ? Boolean.FALSE : (Boolean) result;
        }
        catch (Exception ex) {
            LogUtil.Echo(String.format("Evaluate condition failed (Expr: %s), %s", expr, ex),
                    RJexlEvaluator.class.getName(), LogLevelType.ERROR);
            throw ex;
        }
    }

    /**
     * Evaluate a script.
     * Manifests as &lt;script&gt; element.
     *
     * @param ctx    variable context
     * @param script The script
     * @return Result of the script execution.
     */
    @Override
    public Object EvaluateScript(REvaluableContext ctx, String script) {
        try {
            final Script jexlScript = this.evaluatorEngine.createScript(script);
            return jexlScript.execute((JexlContext) ctx.GetInternalContext());
        } catch (Exception e) {
            String exMessage = e.getMessage() != null ? e.getMessage() : e.getClass().getCanonicalName();
            try {
                LogUtil.Echo(String.format("Evaluate condition failed (Expr: %s), %s", script, e),
                        RJexlEvaluator.class.getName(), LogLevelType.ERROR);
                throw new Exception("evalScript('" + script + "'): " + exMessage, e);
            } catch (Exception e1) {
                LogUtil.Echo(String.format("Evaluate condition failed, %s", e1),
                        RJexlEvaluator.class.getName(), LogLevelType.ERROR);
                return null;
            }
        }
    }

    /**
     * Get the internal evaluate engine.
     *
     * @return evaluate engine reference
     */
    @Override
    public Object GetInternalEngine() {
        return this.evaluatorEngine;
    }
}
