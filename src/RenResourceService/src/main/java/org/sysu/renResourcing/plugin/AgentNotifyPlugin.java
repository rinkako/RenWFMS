/*
 * Project Ren @ 2018
 * Rinkako, Ariana, Gordan. SYSU SDCS.
 */
package org.sysu.renResourcing.plugin;

import org.sysu.renCommon.enums.AgentReentrantType;
import org.sysu.renCommon.enums.LogLevelType;
import org.sysu.renResourcing.GlobalContext;
import org.sysu.renResourcing.context.ParticipantContext;
import org.sysu.renResourcing.utility.LogUtil;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Author: Rinkako
 * Date  : 2018/2/8
 * Usage : Plugin for acting as a Client to send HTTP request to agent URL.
 */
public class AgentNotifyPlugin extends AsyncRunnablePlugin {

    /**
     * Run the plugin.
     * This method will be the asynchronously running entry point.
     */
    @Override
    public void run() {
        this.HandlePendingMessage();
    }

    /**
     * Create a new agent notification send plugin.
     */
    public AgentNotifyPlugin() {
    }

    /**
     * Add a notification to post.
     *
     * @param agentContext agent participant context
     * @param args         arguments to post
     * @param rtid         process rtid
     */
    public void AddNotification(ParticipantContext agentContext, Map<String, String> args, String rtid) {
        if (!this.IsBeginSending) {
            NotifyMessagePackage nmp = new NotifyMessagePackage();
            nmp.Context = agentContext;
            nmp.Args = args;
            nmp.RtId = rtid;
            this.pendingMessage.add(nmp);
        } else {
            LogUtil.Log("Add item to a sending AgentNotifyPlugin, ignored.",
                    AgentNotifyPlugin.class.getName(), LogLevelType.WARNING, rtid);
        }
    }

    /**
     * Clear all pending message.
     */
    public void Clear(String rtid) {
        if (!this.IsBeginSending) {
            this.pendingMessage.clear();
        } else {
            LogUtil.Log("Try to clear items in a sending AgentNotifyPlugin, ignored.",
                    AgentNotifyPlugin.class.getName(), LogLevelType.WARNING, rtid);
        }
    }

    /**
     * Count pending message.
     *
     * @return number of pending queue, -1 if begin to send.
     */
    public int Count(String rtid) {
        if (!this.IsBeginSending) {
            return this.pendingMessage.size();
        } else {
            LogUtil.Log("Try to count items in a sending AgentNotifyPlugin, ignored.",
                    AgentNotifyPlugin.class.getName(), LogLevelType.WARNING, rtid);
            return -1;
        }
    }

    /**
     * Handle pending message queue and post them.
     */
    private void HandlePendingMessage() {
        this.IsBeginSending = true;
        for (NotifyMessagePackage nmp : this.pendingMessage) {
            this.PostToAgent(nmp.Context, nmp.Args, nmp.RtId);
        }
    }

    /**
     * Post data to a agent binding URL.
     *
     * @param agentContext agent participant context
     * @param args         arguments to post
     * @param rtid         process rtid
     */
    private void PostToAgent(ParticipantContext agentContext, Map<String, String> args, String rtid) {
        try {
            // reentrant agent: send post directly.
            if (agentContext.getAgentType() == AgentReentrantType.Reentrant) {
                GlobalContext.Interaction.Send(agentContext.getAgentLocation(), args, rtid);
                LogUtil.Log(String.format("Send notification to agent %s(%s): %s", agentContext.getDisplayName(), agentContext.getWorkerId(), args),
                        AgentNotifyPlugin.class.getName(), LogLevelType.INFO, rtid);
            }
            // not reentrant agent: queue the request.
            else {
                // todo here should use queue to send asynchronously only when the agent is free (include other RS)
                GlobalContext.Interaction.Send(agentContext.getAgentLocation(), args, rtid);
            }
        } catch (Exception ex) {
            // do not rethrown the exception, since this is tolerable.
            LogUtil.Log("Send notification to agent failed, " + ex, AgentNotifyPlugin.class.getName(),
                    LogLevelType.WARNING, rtid);
        }
    }

    /**
     * Message to be sent.
     */
    private ArrayList<NotifyMessagePackage> pendingMessage = new ArrayList<>();

    /**
     * Flag for sending.
     */
    private boolean IsBeginSending = false;

    /**
     * Global static queue, in pattern (NotReentrantAgentGlobalId, NotificationPackageQueue).
     * NOTICE this is only a queue of current RS, other RS may send notification at the same time.
     */
    private static ConcurrentHashMap<String, ConcurrentLinkedQueue<Object>> pendingQueue = new ConcurrentHashMap<>();

    /**
     * Data package for notification.
     */
    private class NotifyMessagePackage {
        /**
         * Get or set notification send to agent context.
         */
        ParticipantContext Context;

        /**
         * Get or set notification arguments map.
         */
        Map<String, String> Args;

        /**
         * Get or set notification process rtid.
         */
        String RtId;
    }
}
