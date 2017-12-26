
package com.sysu.workflow.semantics;

import com.sysu.workflow.model.SCXML;
import com.sysu.workflow.model.State;

/**
 * 错误的变量
 * Errors reported by the default SCXMLSemantics implementation.
 */
public class ErrorConstants {

    /**
     * Missing initial state for a composite state or for the scxml root.
     * SCXML文档中或者一个组合状态中 缺少初始化状态，
     *
     *
     * @see SCXML#getInitialTransition()
     * @see State#getInitial()
     */
    public static final String NO_INITIAL = "NO_INITIAL";

    /**
     * An initial state for a composite state whose Transition does not.
     * Map to a descendant of the composite state.
     * 组合状态中的初始化状态的转移不是指向这个组合状态的子孙状态
     *
     *
     */
    public static final String ILLEGAL_INITIAL = "ILLEGAL_INITIAL";

    /**
     * Unknown action - unsupported executable content. List of supported.
     * actions: assign, cancel, elseif, else, if, log, send, var
     *
     * 不支持的可执行内容，
     *
     */
    public static final String UNKNOWN_ACTION = "UNKNOWN_ACTION";

    /**
     * Illegal state machine configuration.
     * Either a parallel exists which does not have all its AND sub-states
     * active or there are multiple enabled OR states on the same level.
     *
     * 非法的状态机配置
     * 并行状态不美多个子状态活跃，或者OR state里面有多个活跃子状态
     */
    public static final String ILLEGAL_CONFIG = "ILLEGAL_CONFIG";

    /**
     * A variable referred to by assign name attribute is undefined.
     *
     * assign操作的属性名未定义
     */
    public static final String UNDEFINED_VARIABLE = "UNDEFINED_VARIABLE";

    /**
     * An expression language error.
     *
     * 表达式错误
     */
    public static final String EXPRESSION_ERROR = "EXPRESSION_ERROR";

    /**
     * An execution error.
     * 执行错误
     *
     */
    public static final String EXECUTION_ERROR = "EXECUTION_ERROR";

    //---------------------------------------------- STATIC CONSTANTS ONLY

    /**
     * Discourage instantiation.
     * 阻止实例化
     */
    private ErrorConstants() {
        super(); // humor checkstyle
    }

}
