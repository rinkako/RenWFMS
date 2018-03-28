/*
 * Project Ren @ 2018
 * Rinkako, Ariana, Gordan. SYSU SDCS.
 */
package org.sysu.workflow;

import org.sysu.workflow.model.extend.MessageMode;

import java.util.Map;

/**
 * The event controller interface used to send messages containing
 * events or other information directly to another SCXML Interpreter,
 * other external systems using an Event I/O Processor or to raise
 * events in the current SCXML session.
 */
public interface EventDispatcher {

    /**
     * Cancel a unhanded event.
     *
     * @param sendId id of event to be canceled
     */
    void cancel(String sendId);

    /**
     * Send a event to a ioProcessor.
     *
     * @param ioProcessors the available SCXMLIOProcessors, the same map as the SCXML system variable _ioprocessors
     * @param id           The ID of the send message
     * @param target       An expression returning the target location of the event
     * @param type         The type of the Event I/O Processor that the event should
     *                     be dispatched to
     * @param event        The type of event being generated
     * @param data         The event payload
     * @param hints        The data containing information which may be
     *                     used by the implementing platform to configure the event processor
     * @param delay        The event is dispatched after the delay interval elapses
     */
    void send(Map<String, BOXMLIOProcessor> ioProcessors, String id, String target, String type, String event,
              Object data, Object hints, long delay);


    /**
     * Send a event to specific node on the instance tree.
     * @param rtid              process rtid of tree to spread event
     * @param currentId         current tree node id
     * @param id                message id in send label
     * @param messageMode       message spreading mode on instance tree
     * @param targetName        target BO name
     * @param targetState       target state name, empty string means no limitation
     * @param type              The type of the Event I/O Processor that the event should
     *                          be dispatched to
     * @param event             The type of event being generated
     * @param data              The event payload
     * @param hints             The data containing information which may be
     *                          used by the implementing platform to configure the event processor
     * @param delay             The event is dispatched after the delay interval elapses
     */
    void send(String rtid, String currentId, String id, MessageMode messageMode, String targetName, String targetState, String type, String event, Object data, Object hints, long delay);
}

