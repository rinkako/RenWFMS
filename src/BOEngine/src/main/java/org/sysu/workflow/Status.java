
package org.sysu.workflow;

import org.sysu.workflow.model.EnterableState;
import org.sysu.workflow.model.Final;

import java.io.Serializable;
import java.util.Set;

/**
 * The immutable encapsulation of the current state of a state machine.
 */
public class Status implements Serializable {

    /**
     * Serial version UID.
     */
    private static final long serialVersionUID = 1L;

    private final StateConfiguration configuration;


    public Status(StateConfiguration configuration) {
        this.configuration = configuration;
    }

    /**
     * @return Whether the state machine terminated AND we reached a top level Final state.
     */
    public boolean isFinal() {
        return getFinalState() != null;
    }

    /**
     * @return Returns the single top level final state in which the state machine terminated, or null otherwise
     */
    public Final getFinalState() {
        if (configuration.getStates().size() == 1) {
            EnterableState es = configuration.getStates().iterator().next();
            if (es instanceof Final && es.getParent() == null) {
                return (Final) es;
            }
        }
        return null;
    }

    /**
     * Get the atomic states configuration (leaf only).
     *
     * @return Returns the atomic states configuration - simple (leaf) states only.
     */
    public Set<EnterableState> getStates() {
        return configuration.getStates();
    }

    /**
     * Get the active states configuration.
     *
     * @return active states configuration including simple states and their
     * complex ancestors up to the root.
     */
    public Set<EnterableState> getActiveStates() {
        return configuration.getActiveStates();
    }

    public boolean isInState(final String state) {
        for (EnterableState es : configuration.getActiveStates()) {
            if (state.equals(es.getId())) {
                return true;
            }
        }
        return false;
    }
}

