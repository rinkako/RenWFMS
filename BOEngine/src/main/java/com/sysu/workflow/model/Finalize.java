
package com.sysu.workflow.model;

/**
 * The class in this SCXML object model that corresponds to the
 * &lt;finalize&gt; SCXML element.
 */
public class Finalize extends Executable {

    /**
     * Serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Default no-args constructor.
     */
    public Finalize() {
        super();
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

