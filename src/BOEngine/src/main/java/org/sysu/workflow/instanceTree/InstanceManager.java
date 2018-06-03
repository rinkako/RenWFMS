/*
 * Project Ren @ 2018
 * Rinkako, Ariana, Gordan. SYSU SDCS.
 */
package org.sysu.workflow.instanceTree;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.sysu.renCommon.enums.LogLevelType;
import org.sysu.workflow.BOXMLExecutionContext;
import org.sysu.workflow.BOXMLExecutor;
import org.sysu.workflow.utility.HibernateUtil;
import org.sysu.workflow.utility.LogUtil;

import java.util.Hashtable;

/**
 * Author: Rinkako
 * Date  : 2017/3/15
 * Usage : Maintaining all running BO trees and providing methods for stateless operations.
 */
public class InstanceManager {

    /**
     * Get the executor of a tree node by tree node global id.
     *
     * @param tid    process rtid
     * @param nodeId tree node global id
     * @return executor at fetched node
     */
    public static BOXMLExecutor GetExecutor(String tid, String nodeId) {
        return InstanceManager.GetExecContext(tid, nodeId).getSCXMLExecutor();
    }

    /**
     * Get the execution context of a tree node by tree node global id.
     *
     * @param rtid   process rtid
     * @param nodeId tree node global id
     * @return execution context at fetched node
     */
    public static BOXMLExecutionContext GetExecContext(String rtid, String nodeId) {
        return InstanceManager.GetInstanceTree(rtid).GetNodeById(nodeId).getExect();
    }

    /**
     * Get tree by its global id, means rtid.
     *
     * @param rtid process rtid
     * @return tree reference
     */
    public static RInstanceTree GetInstanceTree(String rtid) {
        return InstanceManager.GetInstanceTree(rtid, true);
    }

    /**
     * Get tree by its global id, means rtid.
     *
     * @param rtid    process rtid
     * @param warning produce warning message when missing tree
     * @return tree reference
     */
    public static RInstanceTree GetInstanceTree(String rtid, boolean warning) {
        if (InstanceManager.InstanceTreeTable.containsKey(rtid)) {
            return InstanceManager.InstanceTreeTable.get(rtid);
        } else {
            if (warning) {
                LogUtil.Log("Instance tree not found: " + rtid, InstanceManager.class.getName(),
                        LogLevelType.WARNING, rtid);
            }
            return null;
        }
    }

    /**
     * Check if a instance tree is exist.
     *
     * @param rtid process rtid
     * @return whether tree exist or not
     */
    public static boolean ContainsInstanceTree(String rtid) {
        return InstanceManager.InstanceTreeTable.containsKey(rtid);
    }


    /**
     * Register a new tree.
     *
     * @param rtid process rtid
     * @param tree Tree reference
     */
    public static void RegisterInstanceTree(String rtid, RInstanceTree tree) {
        if (tree == null || tree.Root == null) {
            LogUtil.Log("Instance tree must not null: " + rtid, InstanceManager.class.getName(),
                    LogLevelType.ERROR, rtid);
        } else if (InstanceManager.InstanceTreeTable.containsKey(rtid)) {
            LogUtil.Log("Duplicated Instance tree: " + rtid, InstanceManager.class.getName(),
                    LogLevelType.WARNING, rtid);
        } else {
            InstanceManager.InstanceTreeTable.put(rtid, tree);
        }
    }

    /**
     * Signal a process runtime over, and remove its instance tree.
     *
     * @param rtid process rtid
     */
    public static void UnregisterInstanceTree(String rtid) {
        InstanceManager.InstanceTreeTable.remove(rtid);
    }

    /**
     * Forest of instance tree, mapping (Rtid, Tree).
     */
    private static Hashtable<String, RInstanceTree> InstanceTreeTable = new Hashtable<String, RInstanceTree>();
}