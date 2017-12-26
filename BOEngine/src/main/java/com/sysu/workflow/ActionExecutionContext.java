
package com.sysu.workflow;

import com.sysu.workflow.model.Action;
import com.sysu.workflow.model.EnterableState;
import com.sysu.workflow.model.SCXML;
import org.apache.commons.logging.Log;

/**
 * ActionExecutionContext providing restricted access to the SCXML model, instance and services needed
 * ActionExecutionContext 对SCXML模型，实例，和Action所需要的内容提供了严格的访问
 * for the execution of {@link Action} instances
 */
public class ActionExecutionContext {

    /**
     * The SCXML execution context this action exection context belongs to
     * 当前Action执行上下文所属的  SCXML执行上文
     */
    private final SCXMLExecutionContext exctx;

    /**
     * Constructor
     * 构造函数
     *
     * @param exctx The SCXML execution context this action execution context belongs to
     */
    public ActionExecutionContext(SCXMLExecutionContext exctx) {
        this.exctx = exctx;
    }

    /**
     * @return Returns the state machine
     * 返回状态机
     */
    public SCXML getStateMachine() {
        return exctx.getStateMachine();
    }

    /**
     * @return Returns the global context
     * 返回全局上下文
     */
    public Context getGlobalContext() {
        return exctx.getScInstance().getGlobalContext();
    }

    /**
     * 根据某一个状态，返回状态对应的上下文
     *
     * @param state an EnterableState
     * @return Returns the context for an EnterableState
     */
    public Context getContext(EnterableState state) {
        return exctx.getScInstance().getContext(state);
    }

    /**
     * @return Returns The evaluator.
     * 返回求值器
     */
    public Evaluator getEvaluator() {
        return exctx.getEvaluator();
    }

    /**
     * @return Returns the error reporter
     * 返回错误报告器
     */
    public ErrorReporter getErrorReporter() {
        return exctx.getErrorReporter();
    }

    /**
     * @return Returns the event dispatcher
     * 返回事件派发器
     */
    public EventDispatcher getEventDispatcher() {
        return exctx.getEventDispatcher();
    }

    /**
     * @return Returns the I/O Processor for the internal event queue
     * 返回内部事件的 I/O 处理器 也就是  SCXML执行上下文
     */
    public SCXMLIOProcessor getInternalIOProcessor() {
        return exctx;
    }

    /**
     * @return Returns the SCXML Execution Logger for the application
     * 返回SCXML 执行上下文的日记记录器
     */
    public Log getAppLog() {
        return exctx.getAppLog();
    }
}