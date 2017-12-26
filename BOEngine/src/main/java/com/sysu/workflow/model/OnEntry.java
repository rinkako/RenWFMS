
package com.sysu.workflow.model;

/**
 * The class in this SCXML object model that corresponds to the
 * &lt;onentry&gt; SCXML element, which is an optional property
 * holding executable content to be run upon entering the parent
 * State or Parallel.
 */
public class OnEntry extends Executable {

    /**
     * Serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * An indicator whether to raise the non-standard "entry.state.id" internal event after executing this OnEntry
     */
    private Boolean raiseEvent;

    /**
     * Constructor.
     */
    public OnEntry() {
        super();
    }

    /**
     * Set the EnterableState parent.
     *
     * @param parent The parent to set.
     */
    @Override
    public final void setParent(final EnterableState parent) {
        super.setParent(parent);
    }

    /**
     * @return true if the non-standard internal "entry.state.id" event will be raised after executing this OnEntry
     */
    public final boolean isRaiseEvent() {
        return raiseEvent != null && raiseEvent;
    }

    /**
     * @return The indicator whether to raise the non-standard "entry.state.id" internal event after executing
     * this OnEntry. When null no event will be raised
     */
    public final Boolean getRaiseEvent() {
        return raiseEvent;
    }

    /**
     * Set the indicator whether to raise the non-standard "entry.state.id" internal event after executing this OnEntry.
     *
     * @param raiseEvent The indicator, when null no event will be raised
     */
    public final void setRaiseEvent(final Boolean raiseEvent) {
        this.raiseEvent = raiseEvent;
    }
}

