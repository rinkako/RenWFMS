
package org.sysu.workflow;

import org.sysu.workflow.model.EnterableState;
import org.sysu.workflow.model.ModelException;
import org.sysu.workflow.model.SCXML;
import org.sysu.workflow.semantics.SCXMLSemanticsImpl;

import java.util.Set;

/**
 * <p>The purpose of this interface is to separate the the
 * <a href="http://www.w3.org/TR/2014/CR-scxml-20140313/#AlgorithmforSCXMLInterpretation">
 * W3C SCXML Algorithm for SCXML Interpretation</a>
 * from the <code>BOXMLExecutor</code> and therefore make it pluggable.</p>
 * <p>
 * From an SCXML execution POV, there are only three entry points needed into the Algorithm, namely:
 * </p>
 * <ul>
 * <li>Performing the initialization of the state machine and completing a first macro step,
 * see: {@link #firstStep(BOXMLExecutionContext)}. The state machine thereafter should be ready
 * for processing external events (or be terminated already)</li>
 * <li>Processing a single external event and completing the macro step for it, after which the
 * state machine should be ready for processing another external event (if any), or be terminated already.
 * See: {@link #nextStep(BOXMLExecutionContext, TriggerEvent)}.
 * </li>
 * <li>Finally, if the state machine terminated ({@link BOXMLExecutionContext#isRunning()} == false), after either
 * of the above steps, finalize the state machine by performing the final step.
 * See: {@link #finalStep(BOXMLExecutionContext)}.
 * </li>
 * </ul>
 * <p>After a state machine has been terminated you can re-initialize the execution context, and start again.</p>
 * <p>
 * Except for the loading of the SCXML document and (re)initializing the {@link BOXMLExecutionContext}, the above steps
 * represent the <b>interpret</b>,<b>mainEventLoop</b> and <b>exitInterpreter</b> entry points specified in Algorithm
 * for SCXML Interpretation, but more practically and logically broken into separate steps so that the blocking wait
 * for external events can be handled externally.
 * </p>
 * <p>
 * These three entry points are the only interface methods used by the BOXMLExecutor. It is up to the
 * specific BOXMLSemantics implementation to provide the concrete handling for these according to the Algorithm in
 * the SCXML specification (or possibly something else/different).
 * </p>
 * <p>
 * The default {@link SCXMLSemanticsImpl} provides an implementation of the
 * specification, and can easily be overridden/customized as a whole or only on specific parts of the Algorithm
 * implementation.
 * </p>
 * <p>
 * Note that both the {@link #firstStep(BOXMLExecutionContext)} and {@link #nextStep(BOXMLExecutionContext, TriggerEvent)}
 * first run to completion for any internal events raised before returning, as expected and required by the SCXML
 * specification, so it is currently not possible to 'manage' internal event processing externally.
 * </p>
 * <p/>
 * <p>Specific semantics can be created by subclassing
 * <code>SCXMLSemanticsImpl</code>.</p>
 */
public interface BOXMLSemantics {

    /**
     * Optional post processing immediately following BOXMLReader. May be used
     * for removing pseudo-states etc.
     *
     * @param input  SCXML state machine
     * @param errRep ErrorReporter callback
     * @return normalized SCXML state machine, pseudo states are removed, etc.
     */
    SCXML normalizeStateMachine(final SCXML input, final ErrorReporter errRep);

    /**
     * <p>
     * First step in the execution of an SCXML state machine.
     * <p/>
     * <p>
     * In the default implementation, this will first (re)initialize the state machine instance, destroying any existing
     * state!
     * <p/>
     * <p>
     * The first step is corresponding to the Algorithm for SCXML processing from the interpret() procedure to the
     * mainLoop() procedure up to the blocking wait for an external event.
     * </p>
     * <p>
     * This step should complete the SCXML initial execution and a subsequent macroStep to stabilize the state machine
     * again before returning.
     * </p>
     * <p>
     * If the state machine no longer is running after all this, first the {@link #finalStep(BOXMLExecutionContext)}
     * should be called for cleanup before returning.
     * </p>
     *
     * @param exctx The execution context for this step
     * @throws ModelException if the state machine instance failed to initialize or a SCXML model error occurred during
     *                        the execution.
     */
    void firstStep(final BOXMLExecutionContext exctx) throws ModelException;

    /**
     * Next step in the execution of an SCXML state machine.
     * <p>
     * The next step is corresponding to the Algorithm for SCXML processing mainEventLoop() procedure after receiving an
     * external event, up to the blocking wait for another external event.
     * </p>
     * <p>
     * If the state machine isn't {@link BOXMLExecutionContext#isRunning()} (any more), this method should do nothing.
     * </p>
     * <p>
     * If the provided event is a {@link TriggerEvent#CANCEL_EVENT}, the state machine should stop running.
     * </p>
     * <p>
     * Otherwise, the event must be set in the {@link BOXMLSystemContext} and processing of the event then should start,
     * and if the event leads to any transitions a microStep for this event should be performed, followed up by a
     * macroStep to stabilize the state machine again before returning.
     * </p>
     * <p>
     * If the state machine no longer is running after all this, first the {@link #finalStep(BOXMLExecutionContext)}
     * should be called for cleanup before returning.
     * </p>
     *
     * @param exctx The execution context for this step
     * @param event The event to process
     * @throws ModelException if a SCXML model error occurred during the execution.
     */
    void nextStep(final BOXMLExecutionContext exctx, final TriggerEvent event) throws ModelException;

    /**
     * The final step in the execution of an SCXML state machine.
     * <p>
     * This final step is corresponding to the Algorithm for SCXML processing exitInterpreter() procedure, after the
     * state machine stopped running.
     * </p>
     * <p>
     * If the state machine still is {@link BOXMLExecutionContext#isRunning()} invoking this method should simply
     * do nothing.
     * </p>
     * <p>
     * This final step should first exit all remaining active states and cancel any active invokers, before handling
     * the possible donedata element for the last final state.
     * </p>
     * <p>
     * <em>NOTE: the current implementation does not yet provide final donedata handling.</em>
     * </p>
     *
     * @param exctx The execution context for this step
     * @throws ModelException if a SCXML model error occurred during the execution.
     */
    void finalStep(final BOXMLExecutionContext exctx) throws ModelException;

    /**
     * Checks whether a given set of states is a legal Harel State Table
     * configuration (with the respect to the definition of the OR and AND
     * states).
     * <p>
     * When {@link BOXMLExecutionContext#isCheckLegalConfiguration()} is true (default) the BOXMLSemantics implementation
     * <em>should</em> invoke this method before executing a step, and throw a ModelException if a non-legal
     * configuration is encountered.
     * </p>
     * <p>
     * This method is also first invoked when manually initializing the status of a state machine through
     * {@link BOXMLExecutor#setConfiguration(Set)}.
     * </p>
     *
     * @param states a set of states
     * @param errRep ErrorReporter to report detailed error info if needed
     * @return true if a given state configuration is legal, false otherwise
     */
    boolean isLegalConfiguration(final Set<EnterableState> states, final ErrorReporter errRep);
}
