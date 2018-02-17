package org.sysu.workflow.env;

import org.sysu.workflow.EventDispatcher;
import org.sysu.workflow.SCXMLExecutor;
import org.sysu.workflow.SCXMLIOProcessor;
import org.sysu.workflow.TriggerEvent;
import org.sysu.workflow.instanceTree.InstanceManager;
import org.sysu.workflow.instanceTree.TimeTreeNode;
import org.sysu.workflow.model.ModelException;
import org.sysu.workflow.model.extend.MessageMode;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.Serializable;
import java.util.*;

/**
 * Created with IntelliJ IDEA
 * Date: 2016/1/4
 * Time: 20:29
 * User: zhengshouzi
 * GitHub: <a>https://github.com/ThinerZQ</a>
 * Blog: <a>http://blog.csdn.net/c601097836</a>
 * Email: 601097836@qq.com
 * <p/>
 * ?????????????????????????? ????????????
 * ?????????????????????????????????????????????scxml?????????????????? J2SE ?? Timer
 * ????????????????-
 */
public class MulitStateMachineDispatcher extends SimpleDispatcher implements Serializable {


    /**
     * Serial version UID.
     */
    private static final long serialVersionUID = 1L;
    /**
     * Implementation independent log category.
     */
    private Log log = LogFactory.getLog(EventDispatcher.class);
    /**
     * The <code>Map</code> of active <code>Timer</code>s, keyed by
     * &lt;send&gt; element <code>id</code>s.
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

    @Override
    public void send(Map<String, SCXMLIOProcessor> ioProcessors, String id, String target, String type, String event, Object data, Object hints, long delay) {
        super.send(ioProcessors, id, target, type, event, data, hints, delay);
    }

    @Override
    public void send(String treeId, String currentSessionId, String id, String target, MessageMode messageMode, String targetName, String targetState, String type, String event, Object data, Object hints, long delay) {

        if (log.isInfoEnabled()) {
            StringBuilder buf = new StringBuilder();
            buf.append("send ( id: ").append(id);
            buf.append(", target: ").append(target);
            buf.append(", messageMode: ").append(messageMode);
            buf.append(", targetName: ").append(targetName);
            buf.append(", targetState: ").append(targetState);
            buf.append(", type: ").append(type);
            buf.append(", event: ").append(event);
            buf.append(", data: ").append(String.valueOf(data));
            buf.append(", hints: ").append(String.valueOf(hints));
            buf.append(", delay: ").append(delay);
            buf.append(')');
            log.info(buf.toString());
        }

        if (type == null || type.equalsIgnoreCase(SCXMLIOProcessor.SCXML_EVENT_PROCESSOR) ||
                type.equals(SCXMLIOProcessor.DEFAULT_EVENT_PROCESSOR)) {

            boolean internal = false;
            SCXMLIOProcessor ioProcessor = InstanceManager.GetExecutor(treeId, currentSessionId);

            if (event == null) {
                if (log.isWarnEnabled()) {
                    log.warn("<send>: Cannot send without event name");
                }
                ioProcessor.addEvent(new TriggerEvent(TriggerEvent.ERROR_EXECUTION, TriggerEvent.ERROR_EVENT));

            } else if (!internal && delay > 0L) {
                // Need to schedule this one
                Timer timer = new Timer(true);
                timer.schedule(new DelayedEventTask(id, event, data, ioProcessor), delay);
                timers.put(id, timer);
                if (log.isDebugEnabled()) {
                    log.debug("Scheduled event '" + event + "' with delay "
                            + delay + "ms, as specified by <send> with id '"
                            + id + "'");
                }
            } else {
                //ioProcessor.addEvent(new TriggerEvent(event, TriggerEvent.SIGNAL_EVENT, data));
            }

            switch (messageMode) {
                case BROADCAST:
                    sendBroadCast(treeId, currentSessionId, targetName, targetState, event, data, hints, delay);
                    break;
                case MULTICAST:
                    // TODO:
                    break;
                case UNICAST:
                    sendUniCast(treeId, currentSessionId, targetName, targetState, event, data, hints, delay);
                    break;
                case TO_PARENT:
                    sendToParent(treeId, currentSessionId, targetName, targetState, event, data, hints, delay);
                    break;
                case TO_CHILD:
                    sendToChild(treeId, currentSessionId, targetName, targetState, event, data, hints, delay);
                    break;
                case TO_ANCESTOR:
                    sendToAncestor(treeId, currentSessionId, targetName, targetState, event, data, hints, delay);
                    break;
                case TO_OFFSPRING:
                    sendToOffSpring(treeId, currentSessionId, targetName, targetState, event, data, hints, delay);
                    break;
                case TO_SIBLING:
                    // TODO:
                    break;
                default:
                    //???
                    System.out.println("???????????");
                    break;
            }
        } else {
            //????????????? I/O ??????
          /*  if (log.isWarnEnabled()) {
                log.warn("<send>: Unsupported type - " + type);
            }
            ioProcessors.get(SCXMLIOProcessor.INTERNAL_EVENT_PROCESSOR).
                    addEvent(new TriggerEvent(TriggerEvent.ERROR_EXECUTION, TriggerEvent.ERROR_EVENT));*/
        }
    }

    /**
     *
     * @param treeId the tree id equals to the root id of the instance tree
     * @param currentSessionId the id of the current node
     * @param targetName the target name of the parent tree node
     * @param targetState the target state of the parent tree node
     * @param event the event name
     * @param data the payload to carry parameters
     * @param hints
     * @param delay
     * @return
     */
    private boolean sendToParent(String treeId, String currentSessionId, String targetName, String targetState, String event, Object data, Object hints, long delay) {
        //get the current tree node by currentSessionId
        TimeTreeNode currentNode = InstanceManager.GetInstanceTree(treeId).GetNodeById(currentSessionId);
        //get the parent node of the current tree node
        TimeTreeNode parentNode = currentNode.Parent;
        if(parentNode == null) {
            System.out.println("it is root , no parent.");
            return true;
        }
        String eventPrefix = currentNode != InstanceManager.GetInstanceTree(treeId).Root ? currentNode.getFilename() + "." : "";
        sendToTarget(parentNode, targetState, eventPrefix + event, data);
        return true;
    }

    /**
     * send the event to the children of the current node
     * @param treeId the tree id equals to the root id of the instance tree
     * @param currentSessionId the id of the current node
     * @param targetName
     * @param targetState
     * @param event
     * @param data
     * @param hints
     * @param delay
     * @return
     */
    private boolean sendToChild(String treeId, String currentSessionId, String targetName, String targetState, String event, Object data, Object hints, long delay) {
        ArrayList<TimeTreeNode> targetTreeNodeList;
        //get the current tree node by currentSessionId
        TimeTreeNode currentTreeNode = InstanceManager.GetInstanceTree(treeId).GetNodeById(currentSessionId);
        if (targetName != null && !"".equals(targetName)) {
            //only get those children whose name is equals to the targetName
            targetTreeNodeList = InstanceManager.GetInstanceTree(treeId).GetChildrenVectorByTarget(currentSessionId, targetName);
        } else {
            //get all the children
            targetTreeNodeList = currentTreeNode.Children;
        }
        String eventPrefix = currentTreeNode != InstanceManager.GetInstanceTree(treeId).Root ? currentTreeNode.getFilename() + "." : "";
        sendToTarget(targetTreeNodeList, targetState, eventPrefix + event, data);
        return true;
    }

    /**
     * send the event to the ancestor of the current node
     * @param treeId the tree id equals to the root id of the instance tree
     * @param currentSessionId the id of the current node
     * @param targetName
     * @param targetState
     * @param event
     * @param data
     * @param hints
     * @param delay
     * @return
     */
    private boolean sendToAncestor(String treeId, String currentSessionId, String targetName, String targetState, String event, Object data, Object hints, long delay) {
        ArrayList<TimeTreeNode> targetTreeNodeList;
        if (targetName != null && !"".equals(targetName)) {
            //only get those ancestors whose name is equals to the targetName
            targetTreeNodeList = InstanceManager.GetInstanceTree(treeId).GetAncestorsVectorByTarget(currentSessionId, targetName);
        } else {
            //get all the ancestors
            targetTreeNodeList = InstanceManager.GetInstanceTree(treeId).GetAncestorsVector(currentSessionId);
        }
        //get the current tree node by currentSessionId
        TimeTreeNode currentTreeNode = InstanceManager.GetInstanceTree(treeId).GetNodeById(currentSessionId);
        String eventPrefix = currentTreeNode != InstanceManager.GetInstanceTree(treeId).Root ? currentTreeNode.getFilename() + "." : "";
        sendToTarget(targetTreeNodeList, targetState, eventPrefix + event, data);
        return true;
    }

    /**
     * send the event to all the offspring of the current node
     * @param treeId the tree id equals to the root id of the instance tree
     * @param currentSessionId the id of the current node
     * @param targetName
     * @param targetState
     * @param event
     * @param data
     * @param hints
     * @param delay
     * @return
     */
    private boolean sendToOffSpring(String treeId, String currentSessionId, String targetName, String targetState, String event, Object data, Object hints, long delay) {
        ArrayList<TimeTreeNode> targetTreeNodeList;
        //get the current tree node by currentSessionId
        if (targetName != null && !"".equals(targetName)) {
            //only get those offsprings whose name is equals to the targetName
            targetTreeNodeList = InstanceManager.GetInstanceTree(treeId).GetOffspringsVectorByTarget(currentSessionId, targetName);
        } else {
            //get all the offsprings
            targetTreeNodeList = InstanceManager.GetInstanceTree(treeId).GetOffspringsVector(currentSessionId);
        }
        //get the current tree node by currentSessionId
        TimeTreeNode currentTreeNode = InstanceManager.GetInstanceTree(treeId).GetNodeById(currentSessionId);
        String eventPrefix = currentTreeNode != InstanceManager.GetInstanceTree(treeId).Root ? currentTreeNode.getFilename() + "." : "";
        sendToTarget(targetTreeNodeList, targetState, eventPrefix + event, data);
        return true;
    }

    /**
     * send the event to all tree nodes
     * @param treeId the tree id equals to the root id of the instance tree
     * @param currentSessionId the id of the current node
     * @param targetName
     * @param targetState
     * @param event
     * @param data
     * @param hints
     * @param delay
     * @return
     */
    private boolean sendBroadCast(String treeId, String currentSessionId, String targetName, String targetState, String event, Object data, Object hints, long delay) {
        ArrayList<TimeTreeNode> targetTreeNodeList;
        if (targetName != null && !"".equals(targetName)) {
            //only get those tree nodes whose name is equals to the targetName
            //treeNodeArrayList = scxmlInstanceTree.getAllTreeNodeByTargetName(scxmlInstanceTree.getRoot(), targetName);
            targetTreeNodeList = InstanceManager.GetInstanceTree(treeId).GetNodeVectorByTarget(targetName);
        } else {
            //get all tree nodes
            //treeNodeArrayList = scxmlInstanceTree.getAllTreeNode(scxmlInstanceTree.getRoot());
            targetTreeNodeList = InstanceManager.GetInstanceTree(treeId).GetAllNodeVector();
        }
        //get the current tree node by currentSessionId
        TimeTreeNode currentTreeNode = InstanceManager.GetInstanceTree(treeId).GetNodeById(currentSessionId);
        String eventPrefix = currentTreeNode != InstanceManager.GetInstanceTree(treeId).Root ? currentTreeNode.getFilename() + "." : "";
        sendToTarget(targetTreeNodeList, targetState, eventPrefix + event, data);
        return true;
    }

    /**
     * send the event to the tree node according to the specified target name
     * @param treeId
     * @param currentSessionId
     * @param targetName
     * @param targetState
     * @param event
     * @param data
     * @param hints
     * @param delay
     * @return
     */
    private boolean sendUniCast(String treeId, String currentSessionId, String targetName, String targetState, String event, Object data, Object hints, long delay) {
        ArrayList<TimeTreeNode> targetTreeNodeList;
        //get the target node list by the target name
        if(targetName != null && !"".equals(targetName)){
            targetTreeNodeList = InstanceManager.GetInstanceTree(treeId).GetNodeVectorByTarget(targetName);
        }else{
            return true;
        }
        //get the current tree node by currentSessionId
        TimeTreeNode currentTreeNode = InstanceManager.GetInstanceTree(treeId).GetNodeById(currentSessionId);
        String eventPrefix = currentTreeNode != InstanceManager.GetInstanceTree(treeId).Root ? currentTreeNode.getFilename() + "." : "";
        sendToTarget(targetTreeNodeList, targetState, eventPrefix + event, data);
        return true;
    }

    /**
     * send event to all the target tree nodes
     * @param targetTreeNodeList the target node list
     * @param targetState the current state of the target node
     * @param event
     * @param data
     */
    private void sendToTarget(ArrayList<TimeTreeNode> targetTreeNodeList, String targetState, String event, Object data) {
        //if the target state is not null
        if (targetState != null && !"".equals(targetState)) {
            for (TimeTreeNode treeNode : targetTreeNodeList) {
                //get the unique scxml executor for each tree node
                SCXMLExecutor scxmlExecutor = treeNode.getExect().getSCXMLExecutor();
                if (scxmlExecutor != null) {
                    if (scxmlExecutor.getStatus().isInState(targetState)) {
                        try {
                            //send the event to the current state of the target node
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
            for (TimeTreeNode treeNode : targetTreeNodeList) {
                SCXMLExecutor scxmlExecutor = treeNode.getExect().getSCXMLExecutor();
                //put the event to the external queue no matter which state the node is in currently
                if (scxmlExecutor != null) {
                    try {
                        scxmlExecutor.triggerEvent(new TriggerEvent(event, TriggerEvent.SIGNAL_EVENT, data));
                    } catch (ModelException e) {
                        e.printStackTrace();
                    }
                } else {
                    System.out.println("no this  scxml executor");
                }
            }
        }
    }

    /**
     * send an event to the target tree node
     * @param treeNode the target tree node
     * @param targetState the target state of the target tree node
     * @param event
     * @param data
     */
    private void sendToTarget(TimeTreeNode treeNode, String targetState, String event, Object data) {
        ArrayList<TimeTreeNode> treeNodeArrayList = new ArrayList<TimeTreeNode>();
        treeNodeArrayList.add(treeNode);
        sendToTarget(treeNodeArrayList, targetState, event, data);
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
        private SCXMLIOProcessor target;

        /**
         * Constructor for events with payload.
         *
         * @param id      The ID of the send element.
         * @param event   The name of the event to be triggered.
         * @param payload The event payload, if any.
         * @param target  The target io processor
         */
        DelayedEventTask(final String id, final String event, final Object payload, SCXMLIOProcessor target) {
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
                log.debug("Fired event '" + event + "' as scheduled by "
                        + "<send> with id '" + id + "'");
            }
        }
    }
}
