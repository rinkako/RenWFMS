
package com.sysu.workflow.model;

import java.util.List;

/**
 * An <code>ActionsContainer</code> is an entity that holds a list of <code>Action</code> elements.
 */
public interface ActionsContainer {

    /**
     * The &lt;if&gt; ActionsContainer element name
     */
    String ELEM_IF = "if";

    /**
     * The &lt;foreach&gt; ActionsContainer element name
     */
    String ELEM_FOREACH = "foreach";

    /**
     * the substateMachine actionContainer element name
     */
    String ELEM_SUBSTATEMACHINE = "subStateMachine";

    /**
     * Get the Document element type for this &lt;container&gt;.
     *
     * @return Returns the element type
     */
    String getContainerElementName();

    /**
     * Get the executable actions contained in this &lt;container&gt;.
     *
     * @return Returns the actions.
     */
    List<Action> getActions();

    /**
     * Add an Action to the list of executable actions contained in
     * this &lt;container&gt;.
     *
     * @param action The action to add.
     */
    void addAction(final Action action);
}
