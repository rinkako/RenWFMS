
package com.sysu.workflow.model;

/**
 * The class in this SCXML object model that corresponds to the
 * &lt;history&gt; SCXML pseudo state element.
 */
public class History extends TransitionTarget {

    /**
     * Serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Whether this is a shallow or deep history, the default is shallow.
     */
    private boolean isDeep;

    /**
     * A conditionless transition representing the default history state
     * and indicates the state to transition to if the parent state has
     * never been entered before.
     */
    private SimpleTransition transition;

    /**
     * Default no-args constructor
     */
    public History() {
        super();
    }

    /**
     * Get the transition.
     *
     * @return Returns the transition.
     */
    public final SimpleTransition getTransition() {
        return transition;
    }

    /**
     * Set the transition.
     *
     * @param transition The transition to set.
     */
    public final void setTransition(final SimpleTransition transition) {
        if (getParent() == null) {
            throw new IllegalStateException("History transition cannot be set before setting its parent");
        }
        this.transition = transition;
        this.transition.setParent(getParent());
    }

    /**
     * Is this history &quot;deep&quot; (as against &quot;shallow&quot;).
     *
     * @return Returns whether this is a &quot;deep&quot; history
     */
    public final boolean isDeep() {
        return isDeep;
    }

    /**
     * @param type The history type, which can be &quot;shallow&quot; or
     *             &quot;deep&quot;
     */
    public final void setType(final String type) {
        if ("deep".equals(type)) {
            isDeep = true;
        }
        //shallow is by default
    }

    /**
     * @return Returns the TransitionalState parent
     */
    @Override
    public TransitionalState getParent() {
        return (TransitionalState) super.getParent();
    }

    /**
     * Set the TransitionalState parent.
     *
     * @param parent The parent to set.
     */
    public final void setParent(final TransitionalState parent) {
        super.setParent(parent);
    }
}

