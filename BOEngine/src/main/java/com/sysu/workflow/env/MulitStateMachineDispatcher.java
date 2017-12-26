package com.sysu.workflow.env;

import com.sysu.workflow.EventDispatcher;
import com.sysu.workflow.SCXMLExecutor;
import com.sysu.workflow.SCXMLIOProcessor;
import com.sysu.workflow.TriggerEvent;
import com.sysu.workflow.engine.InstanceManager;
import com.sysu.workflow.engine.TimeTreeNode;
import com.sysu.workflow.model.ModelException;
import com.sysu.workflow.model.extend.MessageMode;
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
 * ????????????????
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
                case TO_ANCESTOR:
                    // TODO:
                    //sendToAncestor(scxmlInstanceTree, currentSessionId, targetName, targetState, event, data, hints, delay);
                    break;
                case TO_CHILD:
                    // TODO:
                    sendToChild(treeId, currentSessionId, targetName, targetState, event, data, hints, delay);
                    break;
                case TO_OFFSPRING:
                    // TODO:
                    sendToOffSpring(treeId, currentSessionId, targetName, targetState, event, data, hints, delay);
                    break;
                case TO_PARENT:
                    sendToParent(treeId, currentSessionId, targetName, targetState, event, data, hints, delay);
                    break;
                case TO_SIBLING:
                    //????????target????????
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

    private boolean sendToParent(String treeId, String currentSessionId, String targetName, String targetState, String event, Object data, Object hints, long delay) {
        TimeTreeNode currentNode = InstanceManager.GetInstanceTree(treeId).GetNodeById(currentSessionId);
        TimeTreeNode parentNode = currentNode.Parent;
        if(parentNode == null) {
            System.out.println("it is root , no parent.");
            return true;
        }
        String eventPrefix = currentNode != InstanceManager.GetInstanceTree(treeId).Root ? currentNode.getFilename() + "." : "";
        sendToTarget(parentNode, targetState, eventPrefix + event, data);
        return true;
    }

    private boolean sendToOffSpring(String treeId, String currentSessionId, String targetName, String targetState, String event, Object data, Object hints, long delay) {
        ArrayList<TimeTreeNode> treeNodeArrayList;
        if (targetName != null && !"".equals(targetName)) {
            treeNodeArrayList = InstanceManager.GetInstanceTree(treeId).GetOffspringsVectorByTarget(currentSessionId, targetName);
        } else {
            treeNodeArrayList = InstanceManager.GetInstanceTree(treeId).GetOffspringsVector(currentSessionId);
        }
        TimeTreeNode curNode = InstanceManager.GetInstanceTree(treeId).GetNodeById(currentSessionId);
        String eventPrefix = curNode != InstanceManager.GetInstanceTree(treeId).Root ? curNode.getFilename() + "." : "";
        sendToTarget(treeNodeArrayList, targetState, eventPrefix + event, data);
        return true;
    }

    private boolean sendToChild(String treeId, String currentSessionId, String targetName, String targetState, String event, Object data, Object hints, long delay) {
        ArrayList<TimeTreeNode> treeNodeArrayList;
        TimeTreeNode currentTreeNode = InstanceManager.GetInstanceTree(treeId).GetNodeById(currentSessionId);
        if (targetName != null && !"".equals(targetName)) {
            treeNodeArrayList = InstanceManager.GetInstanceTree(treeId).GetChildrenVectorByTarget(currentSessionId, targetName);
        } else {
            treeNodeArrayList = currentTreeNode.Children;
        }
        String eventPrefix = currentTreeNode != InstanceManager.GetInstanceTree(treeId).Root ? currentTreeNode.getFilename() + "." : "";
        sendToTarget(treeNodeArrayList, targetState, eventPrefix + event, data);

        return true;
    }

//    private boolean sendToAncestor(SCXMLInstanceTree scxmlInstanceTree, String currentSessionId, String targetName, String targetState, String event, Object data, Object hints, long delay) {
//
//        //?????????????????????????????
//        ArrayList<SCXMLInstanceTree.TreeNode> treeNodeArrayList;
//        SCXMLInstanceTree.TreeNode currentTreeNode = scxmlInstanceTree.getNode(currentSessionId);
//        if (targetName != null && !"".equals(targetName)) {
//            treeNodeArrayList = scxmlInstanceTree.getAllAncestorTreeNodeByTargetName(currentTreeNode, targetName);
//
//        } else {
//            treeNodeArrayList = scxmlInstanceTree.getAllAncestorTreeNode(currentTreeNode);
//
//        }
//        sendToTarget(treeNodeArrayList, targetState, event, data);
//
//        return true;
//    }

    private boolean sendBroadCast(String treeId, String currentSessionId, String targetName, String targetState, String event, Object data, Object hints, long delay) {

        ArrayList<TimeTreeNode> treeNodeArrayList;
        if (targetName != null && !"".equals(targetName)) {
            //treeNodeArrayList = scxmlInstanceTree.getAllTreeNodeByTargetName(scxmlInstanceTree.getRoot(), targetName);
            treeNodeArrayList = InstanceManager.GetInstanceTree(treeId).GetNodeVectorByTarget(targetName);
        } else {
            //treeNodeArrayList = scxmlInstanceTree.getAllTreeNode(scxmlInstanceTree.getRoot());
            treeNodeArrayList = InstanceManager.GetInstanceTree(treeId).GetAllNodeVector();
        }
        TimeTreeNode curNode = InstanceManager.GetInstanceTree(treeId).GetNodeById(currentSessionId);
        String eventPrefix = curNode != InstanceManager.GetInstanceTree(treeId).Root ? curNode.getFilename() + "." : "";
        sendToTarget(treeNodeArrayList, targetState, eventPrefix + event, data);

        return true;
    }

    private void sendToTarget(ArrayList<TimeTreeNode> treeNodeArrayList, String targetState, String event, Object data) {
        if (targetState != null && !"".equals(targetState)) {
            //?????????????
            for (TimeTreeNode treeNode : treeNodeArrayList) {
                //?????????sessionId???????????????
                SCXMLExecutor scxmlExecutor = treeNode.getExect().getSCXMLExecutor();
                if (scxmlExecutor != null) {
                    if (scxmlExecutor.getStatus().isInState(targetState)) {
                        try {
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
            for (TimeTreeNode treeNode : treeNodeArrayList) {
                SCXMLExecutor scxmlExecutor = treeNode.getExect().getSCXMLExecutor();
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
