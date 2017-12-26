package com.sysu.workflow.engine;

import java.util.ArrayList;
import java.util.Stack;

/**
 * 实例树类：维护业务对象生命周期中的生成层次关系
 * Created by Rinkako on 2017/3/15.
 */
public class TimeInstanceTree {
    /**
     * 构造器
     */
    public TimeInstanceTree() {
        this.Root = null;
    }

    /**
     * 为实例树设置根，该方法应该在执行器第一次执行时，即创建根业务对象时被调用
     * @param root 根节点的引用
     */
    public void SetRoot(TimeTreeNode root) {
        this.Root = root;
    }

    /**
     * 通过节点的唯一编号取得实例树节点
     * @param tid 唯一编号
     * @return 实例树节点的引用
     */
    public TimeTreeNode GetNodeById(String tid) {
        if (this.Root == null) {
            return null;
        }
        Stack<TimeTreeNode> searchStack = new Stack<TimeTreeNode>();
        searchStack.push(this.Root);
        while (!searchStack.empty()) {
            TimeTreeNode curNode = searchStack.pop();
            if (curNode.getTimeId().equals(tid)) {
                return curNode;
            }
            for (int i = 0; i < curNode.Children.size(); i++) {
                searchStack.push(curNode.Children.get(i));
            }
        }
        return null;
    }

    /**
     * 通过节点的描述文件名取得实例树节点集合并序列化成一个向量
     * @param target 要取得的对象的文件名
     * @return 深度优先搜索得到的序列化的树对应的节点向量
     */
    public ArrayList<TimeTreeNode> GetNodeVectorByTarget(String target) {
        if (this.Root == null) {
            return null;
        }
        ArrayList<TimeTreeNode> rVec = new ArrayList<TimeTreeNode>();
        Stack<TimeTreeNode> searchStack = new Stack<TimeTreeNode>();
        searchStack.push(this.Root);
        while (!searchStack.empty()) {
            TimeTreeNode curNode = searchStack.pop();
            if (curNode.getFilename().equals(target)) {
                rVec.add(curNode);
            }
            for (int i = 0; i < curNode.Children.size(); i++) {
                searchStack.push(curNode.Children.get(i));
            }
        }
        return rVec;
    }

    /**
     * 获取整棵树上的节点并序列化成一个向量
     * @return 深度优先搜索得到的序列化的树对应的节点向量
     */
    public ArrayList<TimeTreeNode> GetAllNodeVector() {
        ArrayList<TimeTreeNode> offs = new ArrayList<TimeTreeNode>();
        if (null != this.Root) {
            offs = this.GetOffspringsVector(this.Root.getTimeId());
            offs.add(0, this.Root);
        }
        return offs;
    }

    /**
     * 通过节点的唯一编号取得实例树上该节点和她的后代节点并序列化成一个向量
     * @param tid 唯一编号
     * @return 深度优先搜索得到的序列化的树对应的节点向量
     */
    public ArrayList<TimeTreeNode> GetSelfAndOffspringsVector(String tid) {
        TimeTreeNode rNode = this.GetNodeById(tid);
        ArrayList<TimeTreeNode> offs = new ArrayList<TimeTreeNode>();
        if (rNode != null) {
            offs = this.GetOffspringsVector(tid);
            offs.add(0, rNode);
        }
        return offs;
    }

    /**
     * 通过节点的唯一编号取得实例树上对应节点的后代节点并序列化成一个向量
     * @param tid 唯一编号
     * @return 深度优先搜索得到的序列化的树对应的节点向量
     */
    public ArrayList<TimeTreeNode> GetOffspringsVector(String tid) {
        TimeTreeNode tRoot = this.GetNodeById(tid);
        ArrayList<TimeTreeNode> offsprings = new ArrayList<TimeTreeNode>();
        if (tRoot != null) {
            Stack<TimeTreeNode> searchStack = new Stack<TimeTreeNode>();
            searchStack.push(tRoot);
            while (!searchStack.empty()) {
                TimeTreeNode curNode = searchStack.pop();
                for (int i = 0; i < curNode.Children.size(); i++) {
                    TimeTreeNode sNode = curNode.Children.get(i);
                    offsprings.add(sNode);
                    searchStack.push(sNode);
                }
            }
        }
        return offsprings;
    }

    /**
     * 通过节点的唯一编号取得实例树上对应节点的符合描述文件名的后代节点并序列化成一个向量
     * @param tid 唯一编号
     * @param target 要取得的对象的文件名
     * @return 深度优先搜索得到的序列化的树对应的节点向量
     */
    public ArrayList<TimeTreeNode> GetOffspringsVectorByTarget(String tid, String target) {
        TimeTreeNode tRoot = this.GetNodeById(tid);
        ArrayList<TimeTreeNode> offsprings = new ArrayList<TimeTreeNode>();
        if (tRoot != null) {
            Stack<TimeTreeNode> searchStack = new Stack<TimeTreeNode>();
            searchStack.push(tRoot);
            while (!searchStack.empty()) {
                TimeTreeNode curNode = searchStack.pop();
                for (int i = 0; i < curNode.Children.size(); i++) {
                    TimeTreeNode sNode = curNode.Children.get(i);
                    if (sNode.getFilename().equals(target)) {
                        offsprings.add(sNode);
                    }
                    searchStack.push(sNode);
                }
            }
        }
        return offsprings;
    }

    /**
     * 通过节点的唯一编号取得实例树上对应节点的符合描述文件名的直接一代孩子节点并序列化成一个向量
     * @param tid 唯一编号
     * @param target 要取得的对象的文件名
     * @return 深度优先搜索得到的序列化的树对应的节点向量
     */
    public ArrayList<TimeTreeNode> GetChildrenVectorByTarget(String tid, String target) {
        TimeTreeNode nNode = this.GetNodeById(tid);
        ArrayList<TimeTreeNode> ret = new ArrayList<TimeTreeNode>();
        if (nNode != null) {
            for (TimeTreeNode tn : nNode.Children) {
                if (tn.getFilename().equals(target)) {
                    ret.add(tn);
                }
            }
        }
        return ret;
    }

    /**
     * 实例树的根节点
     */
    public TimeTreeNode Root;
}
