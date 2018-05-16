package org.sysu.workflow.stateless;

/**
 * Author: Rinkako
 * Date  : 2018/5/16
 * Usage : Methods for BO Engine cluster interaction.
 */
public class EngineClusterService {

    /**
     * Delegate a running process instance to another BO Engine.
     *
     * @param rtid process runtime record id for delegation
     * @param toInstance global id of another BO Engine to delegate to
     */
    public static void RequestDelegate(String rtid, String toInstance) {
        // todo
    }

    /**
     * Receive delegation request and handle it.
     *
     * @param rtid process runtime record id for delegation
     */
    public static void ReceiveDelegate(String rtid) {
        // todo
    }
}
