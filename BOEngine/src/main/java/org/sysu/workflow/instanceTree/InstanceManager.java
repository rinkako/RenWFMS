/*
 * Project Ren @ 2018
 * Rinkako, Ariana, Gordan. SYSU SDCS.
 */
package org.sysu.workflow.instanceTree;

import org.sysu.workflow.SCXMLExecutionContext;
import org.sysu.workflow.SCXMLExecutor;

import java.util.Hashtable;

/**
 * Author: Rinkako
 * Date  : 2017/3/15
 * Usage : Maintaining all running BO tree.
 */
public class InstanceManager {

    /**
     * Get the executor of a tree node by tree node global id.
     * @param tid process rtid
     * @param nodeId tree node global id
     * @return executor at fetched node
     */
    public static SCXMLExecutor GetExecutor(String tid, String nodeId) {
        return InstanceManager.GetExecContext(tid, nodeId).getSCXMLExecutor();
    }

    /**
     * Get the execution context of a tree node by tree node global id.
     * @param rtid process rtid
     * @param nodeId tree node global id
     * @return execution context at fetched node
     */
    public static SCXMLExecutionContext GetExecContext(String rtid, String nodeId) {
        return InstanceManager.GetInstanceTree(rtid).GetNodeById(nodeId).getExect();
    }

    /**
     * Get tree by its global id, means rtid.
     * @param rtid process rtid
     * @return tree reference
     */
    public static RInstanceTree GetInstanceTree(String rtid) {
        if (InstanceManager.InstanceTreeTable.containsKey(rtid)) {
            return InstanceManager.InstanceTreeTable.get(rtid);
        }
        else {
            System.out.println("WARNING: Instance tree not found: " + rtid);
            return null;
        }
    }

    /**
     * Register a new tree.
     * @param tree Tree reference
     */
    public static void RegisterInstanceTree(RInstanceTree tree) {
        if (tree == null || tree.Root == null) {
            System.out.println("ERROR: tree and its Root must not null");
        }
        else if (InstanceManager.InstanceTreeTable.containsKey(tree.Root.getGlobalId())) {
            System.out.println("WARNING: duplicated tree id: " + tree.Root.getGlobalId());
        }
        else {
            InstanceManager.InstanceTreeTable.put(tree.Root.getGlobalId(), tree);
        }
    }

    /**
     * Forest of instance tree, mapping (RTID, Tree).
     */
    private static Hashtable<String, RInstanceTree> InstanceTreeTable = new Hashtable<String, RInstanceTree>();
}
