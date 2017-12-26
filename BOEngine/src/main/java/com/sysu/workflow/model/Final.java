
package com.sysu.workflow.model;

/**
 * The class in this SCXML object model that corresponds to the
 * &lt;final&gt; SCXML element.
 *
 * @since 0.7
 */
public class Final extends EnterableState {

    /**
     * Serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Default no-args constructor.
     */
    public Final() {
        super();
    }

    /**
     * @return Returns the State parent
     */
    @Override
    public State getParent() {
        return (State) super.getParent();
    }

    /**
     * Set the parent State.
     *
     * @param parent The parent state to set
     */
    public final void setParent(State parent) {
        super.setParent(parent);
    }

    /**
     * {@inheritDoc}
     *
     * @return Returns always true (a state of type Final is always atomic)
     */
    public final boolean isAtomicState() {
        return true;
    }
}

