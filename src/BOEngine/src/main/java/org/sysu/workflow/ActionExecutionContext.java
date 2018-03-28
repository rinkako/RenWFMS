
package org.sysu.workflow;

import org.sysu.workflow.model.Action;
import org.sysu.workflow.model.EnterableState;
import org.sysu.workflow.model.SCXML;
import org.apache.commons.logging.Log;

/**
 * ActionExecutionContext providing restricted access to the SCXML model, instance and services needed
 * for the execution of {@link Action} instances
 */
public class ActionExecutionContext {

    /**
     * The SCXML execution context this action exection context belongs to
     */
    private final BOXMLExecutionContext exctx;

    /**
     * Constructor
     *
     * @param exctx The SCXML execution context this action execution context belongs to
     */
    public ActionExecutionContext(BOXMLExecutionContext exctx) {
        this.exctx = exctx;
    }

    /**
     * @return Returns the state machine
     */
    public SCXML getStateMachine() {
        return exctx.getStateMachine();
    }

    /**
     * @return Returns the global context
     */
    public Context getGlobalContext() {
        return exctx.getScInstance().getGlobalContext();
    }

    /**
     * Get context by state.
     * @param state an EnterableState
     * @return Returns the context for an EnterableState
     */
    public Context getContext(EnterableState state) {
        return exctx.getScInstance().getContext(state);
    }

    /**
     * @return Returns The evaluator.
     */
    public Evaluator getEvaluator() {
        return exctx.getEvaluator();
    }

    /**
     * @return Returns the error reporter
     */
    public ErrorReporter getErrorReporter() {
        return exctx.getErrorReporter();
    }

    /**
     * @return Returns the event dispatcher
     */
    public EventDispatcher getEventDispatcher() {
        return exctx.getEventDispatcher();
    }

    /**
     * @return Returns the I/O Processor for the internal event queue
     */
    public BOXMLIOProcessor getInternalIOProcessor() {
        return exctx;
    }

    /**
     * @return Returns the SCXML Execution Logger for the application
     */
    public Log getAppLog() {
        return exctx.getAppLog();
    }
}