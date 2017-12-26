
package com.sysu.workflow;

import java.util.Map;

/**
 * 一个Context 或者 scope ，用来存储SCXML根或者某一个状态对象中的变量
 * A Context or &quot;scope&quot; for storing variables; usually tied to
 * a SCXML root or State object.
 */
public interface Context {

    /**
     * Current namespaces are saved under this key in the context.
     * 当前的命名空间被保存在这个Key之下
     */
    String NAMESPACES_KEY = "_ALL_NAMESPACES";

    /**
     * Assigns a new value to an existing variable or creates a new one.
     * The method searches the chain of parent Contexts for variable
     * existence.
     * <p/>
     * 赋一个新值，给一个已经存在的变量，或者创建一个新的变量，
     * 这个变量的查找，会向上搜索父亲上下文
     *
     * @param name  The variable name
     * @param value The variable value
     */
    void set(String name, Object value);

    /**
     * Assigns a new value to an existing variable or creates a new one.
     * The method allows to shaddow a variable of the same name up the
     * Context chain.
     * <p/>
     * 赋一个新值，给一个已经存在的变量，或者创建一个新的变量，
     * 这个变量的查找，不会向上搜索父亲上下文
     *
     * @param name  The variable name
     * @param value The variable value
     */
    void setLocal(String name, Object value);

    /**
     * Get the value of this variable; delegating to parent.
     * 查找一个变量，会搜索父亲上下文
     *
     * @param name The name of the variable
     * @return The value (or null)
     */
    Object get(String name);

    /**
     * Check if this variable exists, delegating to parent.
     * 检查一个变量是否存在，会搜索父亲上下文
     *
     * @param name The name of the variable
     * @return Whether a variable with the name exists in this Context
     */
    boolean has(String name);

    /**
     * Check if this variable exists, only checking this Context
     * 检查一个变量是否存在，不会搜索父亲上下文
     *
     * @param name The name of the variable
     * @return Whether a variable with the name exists in this Context
     */
    boolean hasLocal(String name);

    /**
     * Get the Map of all variables in this Context.
     * 得到这个上下文中的所有变量
     *
     * @return Local variable entries Map
     * To get variables in parent Context, call getParent().getVars().
     * @see #getParent()
     */
    Map<String, Object> getVars();

    /**
     * Clear this Context.
     * 清除当前上下文
     */
    void reset();

    /**
     * Get the parent Context, may be null.
     * 得到父亲上下文，可能是空的
     *
     * @return The parent Context in a chained Context environment
     */
    Context getParent();

    /**
     * Get the SCXMLSystemContext for this Context, should not be null unless this is the root Context
     * 得到当前上下文所在的SCXML 系统上下文，不应该是空的，除非当前是  root Context
     *
     * @return The SCXMLSystemContext in a chained Context environment
     */
    SCXMLSystemContext getSystemContext();

}
