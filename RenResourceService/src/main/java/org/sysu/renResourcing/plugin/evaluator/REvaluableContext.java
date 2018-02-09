/*
 * Project Ren @ 2018
 * Rinkako, Ariana, Gordan. SYSU SDCS.
 */
package org.sysu.renResourcing.plugin.evaluator;

/**
 * Author: Rinkako
 * Date  : 2018/2/9
 * Usage : A scope for storing variables.
 */
public interface REvaluableContext {
    /**
     * Add a variable to context.
     * @param name variable name
     * @param value variable value
     */
    void Add(String name, Object value);

    /**
     * Remove a variable and return it.
     * @param name variable name
     * @return removed variable object, null if not exist.
     */
    Object Remove(String name);

    /**
     * Update a variable in context.
     * @param name variable name
     * @param value variable value
     */
    void Update(String name, Object value);

    /**
     * Retrieve a variable in context.
     * @param name variable name
     * @return variable object, null if not exist.
     */
    Object Retrieve(String name);

    /**
     * Check if a variable name is defined in context.
     * @param name variable name
     * @return true if exist
     */
    boolean Contains(String name);

    /**
     * Clear all variables in context.
     */
    void Clear();

    /**
     * Count variables number in context.
     * @return number of variables
     */
    int Count();

    /**
     * Return internal context reference.
     * @return internal context
     */
    Object GetInternalContext();
}
