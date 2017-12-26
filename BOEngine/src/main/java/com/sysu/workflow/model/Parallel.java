
package com.sysu.workflow.model;

/**
 * The class in this SCXML object model that corresponds to the
 * &lt;parallel&gt; SCXML element, which is a wrapper element to
 * encapsulate parallel state machines. For the &lt;parallel&gt; element
 * to be useful, each of its &lt;state&gt; substates must itself be
 * complex, that is, one with either &lt;state&gt; or &lt;parallel&gt;
 * children.
 */
public class Parallel extends TransitionalState {

    /**
     * Serial version UID.
     */
    private static final long serialVersionUID = 2L;

    /**
     * Constructor.
     */
    public Parallel() {
    }

    /**
     * {@inheritDoc}
     *
     * @return Returns always false (a state of type Parallel is never atomic)
     */
    public final boolean isAtomicState() {
        return false;
    }

    /**
     * Add a TransitionalState (State or Parallel) child
     *
     * @param ts the child to add
     */
    public final void addChild(final TransitionalState ts) {
        super.addChild(ts);
    }
}

