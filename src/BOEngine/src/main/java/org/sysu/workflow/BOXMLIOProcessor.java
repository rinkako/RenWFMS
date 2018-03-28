
package org.sysu.workflow;

import org.sysu.workflow.invoke.Invoker;

/**
 * The SCXML I/O Processor provides the interface for either an internal process or an external system or invoked child
 * SCXML process ({@link Invoker}) to send events into the SCXML processor queue.
 */
public interface BOXMLIOProcessor {

    /**
     * The name of the default SCXML I/O Event Processor
     */
    String DEFAULT_EVENT_PROCESSOR = "http://www.w3.org/TR/scxml/#SCXMLEventProcessor";

    /**
     * Prefix for SCXML I/O Event Processor aliases
     */
    String EVENT_PROCESSOR_ALIAS_PREFIX = "#_";

    /**
     * Default SCXML I/O Event Processor alias
     */
    String SCXML_EVENT_PROCESSOR = "scxml";

    /**
     * Prefix for SCXML I/O (own) Session external Event processor
     */
    String SCXML_SESSION_EVENT_PROCESSOR_PREFIX = EVENT_PROCESSOR_ALIAS_PREFIX + "scxml_";

    /**
     * The name of the internal Event Processor
     */
    String INTERNAL_EVENT_PROCESSOR = EVENT_PROCESSOR_ALIAS_PREFIX + "internal";

    /**
     * The name of the parent Event Processor
     */
    String PARENT_EVENT_PROCESSOR = EVENT_PROCESSOR_ALIAS_PREFIX + "parent";

    /**
     * Add a message to pending event queue.
     * @param event the event to send
     */
    void addEvent(TriggerEvent event);
}
