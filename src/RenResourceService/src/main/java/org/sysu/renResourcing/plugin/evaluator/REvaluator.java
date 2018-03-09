/*
 * Project Ren @ 2018
 * Rinkako, Ariana, Gordan. SYSU SDCS.
 */
package org.sysu.renResourcing.plugin.evaluator;

/**
 * Author: Rinkako
 * Date  : 2018/2/9
 * Usage : Interface for a component that may be used by the
 *         Resource Service to evaluate the expressions.
 */
public interface REvaluator {

    /**
     * Evaluate an expression returning a data value.
     *
     * @param ctx  variable context
     * @param expr expression
     * @return Result of the evaluation
     */
    Object Evaluate(REvaluableContext ctx, String expr);

    /**
     * Evaluate a boolean condition.
     *
     * @param ctx  variable context
     * @param expr expression
     * @return boolean value of condition expression calculated result
     */
    Boolean EvaluateCondition(REvaluableContext ctx, String expr);

    /**
     * Evaluate a script.
     * Manifests as &lt;script&gt; element.
     *
     * @param ctx    variable context
     * @param script The script
     * @return Result of the script execution.
     */
    Object EvaluateScript(REvaluableContext ctx, String script);

    /**
     * Get the internal evaluate engine.
     * @return evaluate engine reference
     */
    Object GetInternalEngine();
}
