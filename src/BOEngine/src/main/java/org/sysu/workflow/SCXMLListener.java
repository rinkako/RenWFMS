
package org.sysu.workflow;

import org.sysu.workflow.model.*;

/**
 * Listener interface for observable entities in the SCXML model.
 * Observable entities include {@link SCXML}
 * instances (subscribe to all entry, exit and transition notifications),
 * {@link State} instances (subscribe to
 * particular entry and exit notifications) and
 * {@link Transition} instances (subscribe to
 * particular transitions).
 */
public interface SCXMLListener {

    /**
     * Handle the entry into a EnterableState.
     *
     * @param state The EnterableState entered
     */
    void onEntry(EnterableState state);

    /**
     * Handle the exit out of a EnterableState.
     *
     * @param state The EnterableState exited
     */
    void onExit(EnterableState state);

    /**
     * Handle the transition.
     *
     * @param from       The source TransitionTarget
     * @param to         The destination TransitionTarget
     * @param transition The Transition taken
     * @param event      The event name triggering the transition
     */
    void onTransition(TransitionTarget from, TransitionTarget to,
                      Transition transition, String event);

}

