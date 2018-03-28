/*
 * Project Ren @ 2018
 * Rinkako, Ariana, Gordan. SYSU SDCS.
 */
package org.sysu.workflow.env;

import org.sysu.renCommon.enums.LogLevelType;
import org.sysu.workflow.BOXMLExecutor;
import org.sysu.workflow.EventDispatcher;
import org.sysu.workflow.BOXMLIOProcessor;
import org.sysu.workflow.TriggerEvent;
import org.sysu.workflow.instanceTree.InstanceManager;
import org.sysu.workflow.instanceTree.RTreeNode;
import org.sysu.workflow.model.ModelException;
import org.sysu.workflow.model.extend.MessageMode;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sysu.workflow.utility.LogUtil;

import java.io.Serializable;
import java.util.*;

/**
 * Author: Rinkako
 * Date  : 2017/7/20
 * Usage : Handle event dispatching on the instance tree.
 */
public class MultiStateMachineDispatcher extends SimpleDispatcher implements Serializable {

    /**
     * Serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Implementation independent log category.
     */
    private Log log = LogFactory.getLog(EventDispatcher.class);

    /**
     * The Map of active Timers, keyed by send element id.
     */
    private Map<String, Timer> timers = Collections.synchronizedMap(new HashMap<String, Timer>());

    /**
     * Get the log instance.
     *
     * @return The current log instance
     */
    protected Log getLog() {
        return log;
    }

    /**
     * Sets the log instance
     *
     * @param log the new log instance
     */
    protected void setLog(Log log) {
        this.log = log;
    }

    /**
     * Get the current timers.
     *
     * @return The currently scheduled timers
     */
    protected Map<String, Timer> getTimers() {
        return timers;
    }

    /**
     * @see EventDispatcher#cancel(String)
     */
    public void cancel(final String sendId) {
        if (log.isInfoEnabled()) {
            log.info("cancel( sendId: " + sendId + ")");
        }
        if (!timers.containsKey(sendId)) {
            return; // done, we don't track this one or its already expired
        }
        Timer timer = timers.get(sendId);
        if (timer != null) {
            timer.cancel();
            if (log.isDebugEnabled()) {
                log.debug("Cancelled event scheduled by <send> with id '"
                        + sendId + "'");
            }
        }
        timers.remove(sendId);
    }

    /**
     * @see EventDispatcher#send(Map, String, String, String, String, Object, Object, long)
     */
    @Override
    public void send(Map<String, BOXMLIOProcessor> ioProcessors, String id, String target, String type, String event, Object data, Object hints, long delay) {
        super.send(ioProcessors, id, target, type, event, data, hints, delay);
    }

    /**
     * @see EventDispatcher#send(String, String, String, MessageMode, String, String, String, String, Object, Object, long)
     */
    @Override
    public void send(String rtid, String currentId, String id, MessageMode messageMode, String targetName, String targetState, String type, String event, Object data, Object hints, long delay) {
        // log send action
        if (log.isInfoEnabled()) {
            String buf = "send ( id: " + id +
                    ", messageMode: " + messageMode +
                    ", targetName: " + targetName +
                    ", targetState: " + targetState +
                    ", type: " + type +
                    ", event: " + event +
                    ", data: " + String.valueOf(data) +
                    ", hints: " + String.valueOf(hints) +
                    ", delay: " + delay + ')';
            log.info(buf);
        }
        if (type == null || type.equalsIgnoreCase(BOXMLIOProcessor.SCXML_EVENT_PROCESSOR) ||
                type.equals(BOXMLIOProcessor.DEFAULT_EVENT_PROCESSOR)) {
            BOXMLIOProcessor ioProcessor = InstanceManager.GetExecutor(rtid, currentId);
            // null event handle
            if (event == null) {
                if (log.isWarnEnabled()) {
                    log.warn("<send>: Cannot send without event name");
                }
                ioProcessor.addEvent(new TriggerEvent(TriggerEvent.ERROR_EXECUTION, TriggerEvent.ERROR_EVENT));

            }
            // delay send
            else if (delay > 0L) {
                Timer timer = new Timer(true);
                timer.schedule(new DelayedEventTask(id, event, data, ioProcessor), delay);
                timers.put(id, timer);
                if (log.isDebugEnabled()) {
                    log.debug("Scheduled event '" + event + "' with delay " + delay +
                            "ms, as specified by <send> with id '" + id + "'");
                }
            }
            // do send
            switch (messageMode) {
                case BROADCAST:
                    sendBroadCast(rtid, currentId, targetName, targetState, event, data, hints, delay);
                    break;
                case MULTICAST:
                    sendMulticast(rtid, currentId, targetName, targetState, event, data, hints, delay);
                    break;
                case TO_PARENT:
                    sendToParent(rtid, currentId, targetName, targetState, event, data, hints, delay);
                    break;
                case TO_CHILD:
                    sendToChild(rtid, currentId, targetName, targetState, event, data, hints, delay);
                    break;
                case TO_ANCESTOR:
                    sendToAncestor(rtid, currentId, targetName, targetState, event, data, hints, delay);
                    break;
                case TO_OFFSPRING:
                    sendToOffSpring(rtid, currentId, targetName, targetState, event, data, hints, delay);
                    break;
                case TO_SIBLING:
                    sendToSibling(rtid, currentId, targetName, targetState, event, data, hints, delay);
                    break;
                case UNICAST:
                    sendUnicast(rtid, currentId, targetName, targetState, event, data, hints, delay);
                    break;
                case TO_NOTIFIABLE_ID:
                    sendToNotifiableId(rtid, currentId, targetName, targetState, event, data, hints, delay);
                    break;
                default:
                    System.out.println("Unknown message mode");
                    LogUtil.Log("Dispatcher unknown message mode: " + messageMode,
                            MultiStateMachineDispatcher.class.getName(), LogLevelType.ERROR, rtid);
                    break;
            }
        }
    }

    /**
     * @param treeId        the tree id equals to the root id of the instance tree
     * @param currentNodeId the id of the current node
     * @param targetName    the target name of the parent tree node
     * @param targetState   the target state of the parent tree node
     * @param event         type of event being generated
     * @param data          event payload
     * @param hints         The data containing information which may be used by the implementing platform to configure the event processor
     * @param delay         The event is dispatched after the delay interval elapses
     */
    @SuppressWarnings("all")
    private void sendToParent(String treeId, String currentNodeId, String targetName, String targetState, String event, Object data, Object hints, long delay) {
        //get the current tree node by currentNodeId
        RTreeNode currentTreeNode = InstanceManager.GetInstanceTree(treeId).GetNodeById(currentNodeId);
        //get the parent node of the current tree node
        RTreeNode parentNode = currentTreeNode.Parent;
        if (parentNode == null) {
            System.out.println("it is root , no parent.");
        }
        String eventPrefix = this.normalizeEventPrefix(treeId, currentTreeNode);
        sendToTarget(parentNode, targetState, eventPrefix + event, data);
    }

    /**
     * send the event to the children of the current node
     *
     * @param treeId        the tree id equals to the root id of the instance tree
     * @param currentNodeId the id of the current node
     * @param targetName    target BO name
     * @param targetState   target state name, empty string means no limitation
     * @param event         type of event being generated
     * @param data          event payload
     * @param hints         The data containing information which may be used by the implementing platform to configure the event processor
     * @param delay         The event is dispatched after the delay interval elapses
     */
    @SuppressWarnings("all")
    private void sendToChild(String treeId, String currentNodeId, String targetName, String targetState, String event, Object data, Object hints, long delay) {
        ArrayList<RTreeNode> targetTreeNodeList;
        //get the current tree node by currentNodeId
        RTreeNode currentTreeNode = InstanceManager.GetInstanceTree(treeId).GetNodeById(currentNodeId);
        if (targetName != null && !"".equals(targetName)) {
            //only get those children whose name is equals to the targetName
            targetTreeNodeList = InstanceManager.GetInstanceTree(treeId).GetChildrenVectorByTarget(currentNodeId, targetName);
        } else {
            //get all the children
            targetTreeNodeList = currentTreeNode.Children;
        }
        String eventPrefix = this.normalizeEventPrefix(treeId, currentTreeNode);
        sendToTarget(targetTreeNodeList, targetState, eventPrefix + event, data);
    }

    /**
     * send the event to the ancestor of the current node
     *
     * @param treeId        the tree id equals to the root id of the instance tree
     * @param currentNodeId the id of the current node
     * @param targetName    target BO name
     * @param targetState   target state name, empty string means no limitation
     * @param event         type of event being generated
     * @param data          event payload
     * @param hints         The data containing information which may be used by the implementing platform to configure the event processor
     * @param delay         The event is dispatched after the delay interval elapses
     */
    @SuppressWarnings("all")
    private void sendToAncestor(String treeId, String currentNodeId, String targetName, String targetState, String event, Object data, Object hints, long delay) {
        ArrayList<RTreeNode> targetTreeNodeList;
        if (targetName != null && !"".equals(targetName)) {
            //only get those ancestors whose name is equals to the targetName
            targetTreeNodeList = InstanceManager.GetInstanceTree(treeId).GetAncestorsVectorByTarget(currentNodeId, targetName);
        } else {
            //get all the ancestors
            targetTreeNodeList = InstanceManager.GetInstanceTree(treeId).GetAncestorsVector(currentNodeId);
        }
        //get the current tree node by currentNodeId
        String eventPrefix = this.normalizeEventPrefix(treeId, currentNodeId);
        sendToTarget(targetTreeNodeList, targetState, eventPrefix + event, data);
    }

    /**
     * send the event to all the offspring of the current node
     *
     * @param treeId        the tree id equals to the root id of the instance tree
     * @param currentNodeId the id of the current node
     * @param targetName    target BO name
     * @param targetState   target state name, empty string means no limitation
     * @param event         type of event being generated
     * @param data          event payload
     * @param hints         The data containing information which may be used by the implementing platform to configure the event processor
     * @param delay         The event is dispatched after the delay interval elapses
     */
    @SuppressWarnings("all")
    private void sendToOffSpring(String treeId, String currentNodeId, String targetName, String targetState, String event, Object data, Object hints, long delay) {
        ArrayList<RTreeNode> targetTreeNodeList;
        if (targetName != null && !"".equals(targetName)) {
            //only get those offsprings whose name is equals to the targetName
            targetTreeNodeList = InstanceManager.GetInstanceTree(treeId).GetOffspringsVectorByTarget(currentNodeId, targetName);
        } else {
            //get all the offsprings
            targetTreeNodeList = InstanceManager.GetInstanceTree(treeId).GetOffspringsVector(currentNodeId);
        }
        //get the current tree node by currentNodeId
        String eventPrefix = this.normalizeEventPrefix(treeId, currentNodeId);
        sendToTarget(targetTreeNodeList, targetState, eventPrefix + event, data);
    }

    /**
     * send the event to all the siblings of the current node
     *
     * @param treeId        the tree id equals to the root id of the instance tree
     * @param currentNodeId the id of the current node
     * @param targetName    target BO name
     * @param targetState   target state name, empty string means no limitation
     * @param event         type of event being generated
     * @param data          event payload
     * @param hints         The data containing information which may be used by the implementing platform to configure the event processor
     * @param delay         The event is dispatched after the delay interval elapses
     */
    @SuppressWarnings("all")
    private void sendToSibling(String treeId, String currentNodeId, String targetName, String targetState, String event, Object data, Object hints, long delay) {
        ArrayList<RTreeNode> targetTreeNodeList;
        if (targetName != null && !"".equals(targetName)) {
            //only get those siblings whose name is equals to the targetName
            targetTreeNodeList = InstanceManager.GetInstanceTree(treeId).GetSiblingsVectorByTarget(currentNodeId, targetName);
        } else {
            //get all the siblings
            targetTreeNodeList = InstanceManager.GetInstanceTree(treeId).GetSiblingsVector(currentNodeId);
        }
        // get the current tree node by currentNodeId
        String eventPrefix = this.normalizeEventPrefix(treeId, currentNodeId);
        sendToTarget(targetTreeNodeList, targetState, eventPrefix + event, data);
    }

    /**
     * send the event to all the siblings of the current node
     *
     * @param treeId        the tree id equals to the root id of the instance tree
     * @param currentNodeId the id of the current node
     * @param targetGid     target node global id
     * @param targetState   target state name, empty string means no limitation
     * @param event         type of event being generated
     * @param data          event payload
     * @param hints         The data containing information which may be used by the implementing platform to configure the event processor
     * @param delay         The event is dispatched after the delay interval elapses
     */
    @SuppressWarnings("all")
    private void sendUnicast(String treeId, String currentNodeId, String targetGid, String targetState, String event, Object data, Object hints, long delay) {
        RTreeNode destination = InstanceManager.GetInstanceTree(treeId).GetNodeById(targetGid);
        String eventPrefix = this.normalizeEventPrefix(treeId, currentNodeId);
        sendToTarget(destination, targetState, eventPrefix + event, data);
    }

    /**
     * send the event to nodes with notifiable id.
     *
     * @param treeId             the tree id equals to the root id of the instance tree
     * @param currentNodeId      the id of the current node
     * @param targetNotifiableId target node targetNotifiable id
     * @param targetState        target state name, empty string means no limitation
     * @param event              type of event being generated
     * @param data               event payload
     * @param hints              The data containing information which may be used by the implementing platform to configure the event processor
     * @param delay              The event is dispatched after the delay interval elapses
     */
    @SuppressWarnings("all")
    private void sendToNotifiableId(String treeId, String currentNodeId, String targetNotifiableId, String targetState, String event, Object data, Object hints, long delay) {
        ArrayList<RTreeNode> destination = InstanceManager.GetInstanceTree(treeId).GetVectorByNotifiableId(targetNotifiableId);
        String eventPrefix = this.normalizeEventPrefix(treeId, currentNodeId);
        sendToTarget(destination, targetState, eventPrefix + event, data);
    }

    /**
     * send the event to all tree nodes
     *
     * @param treeId        the tree id equals to the root id of the instance tree
     * @param currentNodeId the id of the current node
     * @param targetName    target BO name
     * @param targetState   target state name, empty string means no limitation
     * @param event         type of event being generated
     * @param data          event payload
     * @param hints         The data containing information which may be used by the implementing platform to configure the event processor
     * @param delay         The event is dispatched after the delay interval elapses
     */
    @SuppressWarnings("all")
    private void sendBroadCast(String treeId, String currentNodeId, String targetName, String targetState, String event, Object data, Object hints, long delay) {
        ArrayList<RTreeNode> targetTreeNodeList;
        if (targetName != null && !"".equals(targetName)) {
            //only get those tree nodes whose name is equals to the targetName
            //treeNodeArrayList = scxmlInstanceTree.getAllTreeNodeByTargetName(scxmlInstanceTree.getRoot(), targetName);
            targetTreeNodeList = InstanceManager.GetInstanceTree(treeId).GetNodeVectorByTarget(targetName);
        } else {
            //get all tree nodes
            //treeNodeArrayList = scxmlInstanceTree.getAllTreeNode(scxmlInstanceTree.getRoot());
            targetTreeNodeList = InstanceManager.GetInstanceTree(treeId).GetAllNodeVector();
        }
        //get the current tree node by currentNodeId
        String eventPrefix = this.normalizeEventPrefix(treeId, currentNodeId);
        sendToTarget(targetTreeNodeList, targetState, eventPrefix + event, data);
    }

    /**
     * send the event to the tree node according to the specified target name.
     *
     * @param treeId        tree rtid
     * @param currentNodeId the id of the current node
     * @param targetName    target BO name
     * @param targetState   target state name, empty string means no limitation
     * @param event         type of event being generated
     * @param data          event payload
     * @param hints         The data containing information which may be used by the implementing platform to configure the event processor
     * @param delay         The event is dispatched after the delay interval elapses
     */
    @SuppressWarnings("all")
    private void sendMulticast(String treeId, String currentNodeId, String targetName, String targetState, String event, Object data, Object hints, long delay) {
        ArrayList<RTreeNode> targetTreeNodeList;
        // get the target node list by the target name
        if (targetName != null && !"".equals(targetName)) {
            targetTreeNodeList = InstanceManager.GetInstanceTree(treeId).GetNodeVectorByTarget(targetName);
        } else {
            return;
        }
        //get the current tree node by currentNodeId
        String eventPrefix = this.normalizeEventPrefix(treeId, currentNodeId);
        sendToTarget(targetTreeNodeList, targetState, eventPrefix + event, data);
    }

    /**
     * send event to all the target tree nodes
     *
     * @param targetTreeNodeList the target node list
     * @param targetState        target state name, empty string means no limitation
     * @param event              type of event being generated
     * @param data               event payload
     */
    private void sendToTarget(ArrayList<RTreeNode> targetTreeNodeList, String targetState, String event, Object data) {
        // if the target state is not null
        if (targetState != null && !"".equals(targetState)) {
            for (RTreeNode treeNode : targetTreeNodeList) {
                // get the unique scxml executor for each tree node
                BOXMLExecutor scxmlExecutor = treeNode.getExect().getSCXMLExecutor();
                if (scxmlExecutor != null) {
                    if (scxmlExecutor.getStatus().isInState(targetState)) {
                        try {
                            // send the event to the current state of the target node
                            scxmlExecutor.triggerEvent(new TriggerEvent(event, TriggerEvent.SIGNAL_EVENT, data));
                        } catch (ModelException e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    System.out.println("no this scxml executor");
                }
            }
        } else {
            for (RTreeNode treeNode : targetTreeNodeList) {
                BOXMLExecutor scxmlExecutor = treeNode.getExect().getSCXMLExecutor();
                // put the event to the external queue no matter which state the node is in currently
                if (scxmlExecutor != null) {
                    try {
                        scxmlExecutor.triggerEvent(new TriggerEvent(event, TriggerEvent.SIGNAL_EVENT, data));
                    } catch (ModelException e) {
                        e.printStackTrace();
                    }
                } else {
                    System.out.println("no this scxml executor");
                }
            }
        }
    }

    /**
     * send an event to the target tree node
     *
     * @param treeNode    the target tree node
     * @param targetState target state name, empty string means no limitation
     * @param event       type of event being generated
     * @param data        event payload
     */
    private void sendToTarget(RTreeNode treeNode, String targetState, String event, Object data) {
        ArrayList<RTreeNode> treeNodeArrayList = new ArrayList<>();
        treeNodeArrayList.add(treeNode);
        sendToTarget(treeNodeArrayList, targetState, event, data);
    }

    /**
     * Generate normalized event prefix
     * @param treeId        the target tree node
     * @param currentNodeId the id of the current node
     * @return              normalized event prefix
     */
    @SuppressWarnings("all")
    private String normalizeEventPrefix(String treeId, String currentNodeId) {
        RTreeNode currentTreeNode = InstanceManager.GetInstanceTree(treeId).GetNodeById(currentNodeId);
        return currentTreeNode != InstanceManager.GetInstanceTree(treeId).Root ? currentTreeNode.getExect().NotifiableId + "." : "";
    }

    /**
     * Generate normalized event prefix
     * @param treeId        the target tree node
     * @param currentNode   current node
     * @return              normalized event prefix
     */
    @SuppressWarnings("all")
    private String normalizeEventPrefix(String treeId, RTreeNode currentNode) {
        return currentNode != InstanceManager.GetInstanceTree(treeId).Root ? currentNode.getName() + "." : "";
    }

    /**
     * TimerTask implementation.
     */
    class DelayedEventTask extends TimerTask {

        /**
         * The ID of the &lt;send&gt; element.
         */
        private String id;

        /**
         * The event name.
         */
        private String event;

        /**
         * The event payload, if any.
         */
        private Object payload;

        /**
         * The target io processor
         */
        private BOXMLIOProcessor target;

        /**
         * Constructor for events with payload.
         *
         * @param id      The ID of the send element.
         * @param event   type of event being generated
         * @param payload The event payload, if any.
         * @param target  The target io processor
         */
        DelayedEventTask(final String id, final String event, final Object payload, BOXMLIOProcessor target) {
            super();
            this.id = id;
            this.event = event;
            this.payload = payload;
            this.target = target;
        }

        /**
         * What to do when timer expires.
         */
        @Override
        public void run() {
            timers.remove(id);
            target.addEvent(new TriggerEvent(event, TriggerEvent.SIGNAL_EVENT, payload));
            if (log.isDebugEnabled()) {
                log.debug("Fired event '" + event + "' as scheduled by " + "<send> with id '" + id + "'");
            }
        }
    }
}
