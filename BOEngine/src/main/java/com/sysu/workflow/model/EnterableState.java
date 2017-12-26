
package com.sysu.workflow.model;

import java.util.ArrayList;
import java.util.List;

/**
 * An abstract base class for state elements in SCXML that can be entered, such as State, Parallel or Final.
 */
public abstract class EnterableState extends TransitionTarget implements DocumentOrder {

    /**
     * The document order of this state
     */
    private int order;

    /**
     * List of optional OnEntry elements holding executable content to be run upon
     * entering this transition target.
     */
    private List<OnEntry> onEntries;

    /**
     * List of optional OnExit elements holding executable content to be run upon
     * exiting this transition target.
     */
    private List<OnExit> onExits;

    public EnterableState() {
        super();
        onEntries = new ArrayList<OnEntry>();
        onExits = new ArrayList<OnExit>();
    }

    /**
     * @return the document order of this state
     * @see DocumentOrder
     */

    public final int getOrder() {
        return order;
    }

    /**
     * Sets the document order of this state
     *
     * @param order the document order
     * @see DocumentOrder
     */
    public final void setOrder(int order) {
        this.order = order;
    }

    /**
     * Get the OnEntry elements.
     *
     * @return Returns the onEntry elements
     */
    public final List<OnEntry> getOnEntries() {
        return onEntries;
    }

    /**
     * Adds an OnEntry element
     *
     * @param onEntry The onEntry to add.
     */
    public final void addOnEntry(final OnEntry onEntry) {
        onEntry.setParent(this);
        onEntries.add(onEntry);
    }

    /**
     * Get the OnExit elements
     *
     * @return Returns the onExit elements
     */
    public final List<OnExit> getOnExits() {
        return onExits;
    }

    /**
     * Add an OnExit element
     *
     * @param onExit The onExit to add.
     */
    public final void addOnExit(final OnExit onExit) {
        onExit.setParent(this);
        onExits.add(onExit);
    }

    /**
     * Check whether this is an atomic state.
     * <p>
     * An atomic state is a state of type Final or of type State without children,
     * </p>
     *
     * @return Returns true if this is an atomic state.
     */
    public abstract boolean isAtomicState();
}
