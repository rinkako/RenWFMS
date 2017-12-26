package com.sysu.workflow.engine;

import com.sysu.workflow.SCXMLExecutionContext;
import com.sysu.workflow.SCXMLExecutor;

import java.util.Hashtable;

/**
 * 实例管理器类：为业务对象的实例树提供公共的上下文访问
 * Created by Rinkako on 2017/3/15.
 */
public class InstanceManager {
    /**
     * 通过唯一编号获取实例树对应节点上的执行器
     * @param tid 树的唯一编号
     * @param nodeId 节点的唯一编号
     * @return 对应节点上的执行器
     */
    public static SCXMLExecutor GetExecutor(String tid, String nodeId) {
        return InstanceManager.GetExecContext(tid, nodeId).getSCXMLExecutor();
    }

    /**
     * 通过唯一编号获取实例树对应节点上的执行器上下文
     * @param tid 树的唯一编号
     * @param nodeId 节点的唯一编号
     * @return 对应节点上的执行器上下文
     */
    public static SCXMLExecutionContext GetExecContext(String tid, String nodeId) {
        return InstanceManager.GetInstanceTree(tid).GetNodeById(nodeId).getExect();
    }

    /**
     * 通过根节点的唯一编号获取实例树
     * @param tid 根节点唯一编号
     * @return 实例树的引用
     */
    public static TimeInstanceTree GetInstanceTree(String tid) {
        if (InstanceManager.InstanceTreeTable.containsKey(tid)) {
            return InstanceManager.InstanceTreeTable.get(tid);
        }
        else {
            System.out.println("WARNING: Instance tree not found: " + tid);
            return null;
        }
    }

    /**
     * 注册一颗实例树
     * @param tree 实例树的引用
     */
    public static void RegisterInstanceTree(TimeInstanceTree tree) {
        if (tree == null || tree.Root == null) {
            System.out.println("ERROR: tree and its Root must not null");
        }
        else if (InstanceManager.InstanceTreeTable.containsKey(tree.Root.getTimeId())) {
            System.out.println("WARNING: duplicated tree id: " + tree.Root.getTimeId());
        }
        else {
            InstanceManager.InstanceTreeTable.put(tree.Root.getTimeId(), tree);
        }
    }

    /**
     * 业务对象的实例树
     */
    private static Hashtable<String, TimeInstanceTree> InstanceTreeTable = new Hashtable<String, TimeInstanceTree>();
}
