
package com.sysu.workflow.model;

import com.sysu.workflow.ActionExecutionContext;
import com.sysu.workflow.SCXMLExpressionException;
import com.sysu.workflow.TriggerEvent;

/**
 * The class in this SCXML object model that corresponds to the
 * &lt;raise&gt; SCXML element.
 *
 * @since 2.0
 */
public class Raise extends Action {

    /**
     * Serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The event to be generated.
     */
    private String event;

    /**
     * Constructor.
     */
    public Raise() {
        super();
    }

    /**
     * Get the event.
     *
     * @return Returns the event.
     */
    public final String getEvent() {
        return event;
    }

    /**
     * Set the event.
     *
     * @param event The event to set.
     */
    public final void setEvent(final String event) {
        this.event = event;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute(ActionExecutionContext exctx) throws ModelException, SCXMLExpressionException {

        if (exctx.getAppLog().isDebugEnabled()) {
            exctx.getAppLog().debug("<raise>: Adding event '" + event + "' to list of derived events.");
        }
        TriggerEvent ev = new TriggerEvent(event, TriggerEvent.SIGNAL_EVENT);
        exctx.getInternalIOProcessor().addEvent(ev);

    }
}