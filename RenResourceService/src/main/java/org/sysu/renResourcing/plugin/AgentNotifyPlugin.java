/*
 * Project Ren @ 2018
 * Rinkako, Ariana, Gordan. SYSU SDCS.
 */
package org.sysu.renResourcing.plugin;

import org.sysu.renResourcing.basic.enums.AgentReentrantType;
import org.sysu.renResourcing.context.ParticipantContext;
import org.sysu.renResourcing.utility.HttpClientUtil;
import org.sysu.renResourcing.utility.LogUtil;

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
     * Post data to a agent binding URL.
     * @param agentContext agent participant context
     */
    public void PostToAgent(ParticipantContext agentContext, Map<String, String> args, String rtid) {
        try {
            // reentrant agent: send post directly.
            if (agentContext.getAgentType() == AgentReentrantType.Reentrant) {
                HttpClientUtil.SendPost(agentContext.getAgentLocation(), args, rtid);
                LogUtil.Log(String.format("Send notification to agent %s(%s): %s", agentContext.getDisplayName(), agentContext.getWorkerId(), args),
                        AgentNotifyPlugin.class.getName(), LogUtil.LogLevelType.INFO, rtid);
            }
            // not reentrant agent: queue the request.
            else {
                // todo here should use queue to send asynchronously only when the agent is free (include other RS)
                HttpClientUtil.SendPost(agentContext.getAgentLocation(), args, rtid);
            }
        }
        catch (Exception ex) {
            // do not rethrown the exception, since this is tolerable.
            LogUtil.Log("Send notification to agent failed, " + ex, AgentNotifyPlugin.class.getName(),
                    LogUtil.LogLevelType.ERROR, rtid);
        }
    }

    /**
     * Global static queue, in pattern (NotReentrantAgentGlobalId, NotificationPackageQueue).
     * NOTICE this is only a queue of current RS, other RS may send notification at the same time.
     */
    private static ConcurrentHashMap<String, ConcurrentLinkedQueue<Object>> pendingQueue = new ConcurrentHashMap<>();
}
