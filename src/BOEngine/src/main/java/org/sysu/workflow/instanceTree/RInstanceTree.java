/*
 * Project Ren @ 2018
 * Rinkako, Ariana, Gordan. SYSU SDCS.
 */
package org.sysu.workflow.instanceTree;

import java.util.ArrayList;
import java.util.Stack;

/**
 * Author: Rinkako
 * Date  : 2017/3/15
 * Usage : Instance Tree which represent a span instance tree of execution process.
 */
public class RInstanceTree {

    /**
     * Root node of tree, represents root BO instance.
     */
    public RTreeNode Root;

    /**
     * Construct a new BO Tree.
     */
    public RInstanceTree() {
        this.Root = null;
    }

    /**
     * Set root of this tree, it should be called when creating root BO.
     * @param root reference of root BO tree node
     */
    public void SetRoot(RTreeNode root) {
        this.Root = root;
    }

    /**
     * Fetch a tree node by its global id.
     * @param gid node unique global id
     * @return fetching result node reference
     */
    public RTreeNode GetNodeById(String gid) {
        if (this.Root == null) {
            return null;
        }
        Stack<RTreeNode> searchStack = new Stack<RTreeNode>();
        searchStack.push(this.Root);
        // DFS
        while (!searchStack.empty()) {
            RTreeNode curNode = searchStack.pop();
            if (curNode.getGlobalId().equals(gid)) {
                return curNode;
            }
            for (int i = 0; i < curNode.Children.size(); i++) {
                searchStack.push(curNode.Children.get(i));
            }
        }
        return null;
    }

    /**
     * Fetch a tree node by its name.
     * @param targetName target BO name
     * @return vector of flattened fetching result by DFS
     */
    public ArrayList<RTreeNode> GetNodeVectorByTarget(String targetName) {
        if (this.Root == null) {
            return null;
        }
        ArrayList<RTreeNode> rVec = new ArrayList<RTreeNode>();
        Stack<RTreeNode> searchStack = new Stack<RTreeNode>();
        searchStack.push(this.Root);
        while (!searchStack.empty()) {
            RTreeNode curNode = searchStack.pop();
            if (curNode.getName().equals(targetName)) {
                rVec.add(curNode);
            }
            for (int i = 0; i < curNode.Children.size(); i++) {
                searchStack.push(curNode.Children.get(i));
            }
        }
        return rVec;
    }

    /**
     * Fetch the whole tree and get all nodes.
     * @return vector of flattened fetching result by DFS
     */
    public ArrayList<RTreeNode> GetAllNodeVector() {
        ArrayList<RTreeNode> offs = new ArrayList<RTreeNode>();
        if (null != this.Root) {
            offs = this.GetOffspringsVector(this.Root.getGlobalId());
            offs.add(0, this.Root);
        }
        return offs;
    }

    /**
     * Fetch a tree node and its offsprings by its global id.
     * @param gid node unique global id
     * @return vector of flattened fetching result by DFS
     */
    public ArrayList<RTreeNode> GetSelfAndOffspringsVector(String gid) {
        RTreeNode rNode = this.GetNodeById(gid);
        ArrayList<RTreeNode> offs = new ArrayList<RTreeNode>();
        if (rNode != null) {
            offs = this.GetOffspringsVector(gid);
            offs.add(0, rNode);
        }
        return offs;
    }

    /**
     * Fetch a tree node then get its offsprings without itself by its global id.
     * @param gid node unique global id
     * @return vector of flattened fetching result by DFS
     */
    public ArrayList<RTreeNode> GetOffspringsVector(String gid) {
        RTreeNode tRoot = this.GetNodeById(gid);
        ArrayList<RTreeNode> offsprings = new ArrayList<RTreeNode>();
        if (tRoot != null) {
            Stack<RTreeNode> searchStack = new Stack<RTreeNode>();
            searchStack.push(tRoot);
            while (!searchStack.empty()) {
                RTreeNode curNode = searchStack.pop();
                for (int i = 0; i < curNode.Children.size(); i++) {
                    RTreeNode sNode = curNode.Children.get(i);
                    offsprings.add(sNode);
                    searchStack.push(sNode);
                }
            }
        }
        return offsprings;
    }

    /**
     * Fetch a tree and return all nodes with notifiableId.
     * @param notifiableId notifiable id string
     * @return vector of flattened fetching result by DFS
     */
    public ArrayList<RTreeNode> GetVectorByNotifiableId(String notifiableId) {
        ArrayList<RTreeNode> offsprings = this.GetSelfAndOffspringsVector(this.Root.getGlobalId());
        ArrayList<RTreeNode> retList = new ArrayList<>();
        for (RTreeNode node : offsprings) {
            if (node.getExect().NotifiableId.equals(notifiableId)) {
                retList.add(node);
            }
        }
        return retList;
    }

    /**
     * Fetch a tree node then get its offsprings which BO name is target and without itself by its global id.
     * @param gid node unique global id
     * @param target target BO name
     * @return vector of flattened fetching result by DFS
     */
    public ArrayList<RTreeNode> GetOffspringsVectorByTarget(String gid, String target) {
        RTreeNode tRoot = this.GetNodeById(gid);
        ArrayList<RTreeNode> offsprings = new ArrayList<RTreeNode>();
        if (tRoot != null) {
            Stack<RTreeNode> searchStack = new Stack<RTreeNode>();
            searchStack.push(tRoot);
            while (!searchStack.empty()) {
                RTreeNode curNode = searchStack.pop();
                for (int i = 0; i < curNode.Children.size(); i++) {
                    RTreeNode sNode = curNode.Children.get(i);
                    if (sNode.getName().equals(target)) {
                        offsprings.add(sNode);
                    }
                    searchStack.push(sNode);
                }
            }
        }
        return offsprings;
    }

    /**
     * Fetch a tree node then get its children which BO name is target and without itself by its global id.
     * @param gid node unique global id
     * @param target target BO name
     * @return vector of flattened fetching result by DFS
     */
    public ArrayList<RTreeNode> GetChildrenVectorByTarget(String gid, String target) {
        RTreeNode nNode = this.GetNodeById(gid);
        ArrayList<RTreeNode> ret = new ArrayList();
        if (nNode != null) {
            for (RTreeNode tn : nNode.Children) {
                if (tn.getName().equals(target)) {
                    ret.add(tn);
                }
            }
        }
        return ret;
    }

    /**
     * Fetch a tree node then get its ancestors which BO name is target and without itself by its global id.
     * @param gid node unique global id
     * @param target target BO name
     * @return vector of flattened fetching result
     */
    public ArrayList<RTreeNode> GetAncestorsVectorByTarget(String gid, String target) {
        ArrayList<RTreeNode>  ancestors = new ArrayList<RTreeNode>();
        RTreeNode currentNode = this.GetNodeById(gid);
        while(currentNode != null && currentNode.Parent != null) {
            currentNode = currentNode.Parent;
            if(currentNode.getName().equals(target)){
                ancestors.add(currentNode);
            }
        }
        return ancestors;
    }

    /**
     * Fetch a tree node then get its siblings without itself by its global id.
     * @param gid node unique global id
     * @return vector of flattened fetching result
     */
    public ArrayList<RTreeNode> GetSiblingsVector(String gid) {
        ArrayList<RTreeNode>  siblings = new ArrayList<RTreeNode>();
        RTreeNode currentNode = this.GetNodeById(gid);
        ArrayList<RTreeNode> children = new ArrayList<>();
        if(currentNode != null && currentNode.Parent != null) {
            children = currentNode.Parent.Children;
        }
        if(children.size() > 1) {
            for(RTreeNode node : children) {
                if(!node.getGlobalId().equals(gid)) {
                    siblings.add(node);
                }
            }
        }
        return siblings;
    }

    /**
     * Fetch a tree node then get its siblings which BO name is target and without itself by its global id.
     * @param gid node unique global id
     * @param target target BO name
     * @return vector of flattened fetching result
     */
    public ArrayList<RTreeNode> GetSiblingsVectorByTarget(String gid, String target) {
        ArrayList<RTreeNode>  siblings = new ArrayList<RTreeNode>();
        RTreeNode currentNode = this.GetNodeById(gid);
        ArrayList<RTreeNode> children = new ArrayList<>();
        if(currentNode != null && currentNode.Parent != null) {
            children = currentNode.Parent.Children;
        }
        if(children.size() > 1) {
            for(RTreeNode node : children) {
                if(!node.getGlobalId().equals(gid) && node.getName().equals(target)) {
                    siblings.add(node);
                }
            }
        }
        return siblings;
    }

    /**
     * Fetch a tree node then get its ancestors without itself by its global id.
     * @param gid node unique global id
     * @return vector of flattened fetching result
     */
    public ArrayList<RTreeNode> GetAncestorsVector(String gid) {
        ArrayList<RTreeNode>  ancestors = new ArrayList<RTreeNode>();
        RTreeNode currentNode = this.GetNodeById(gid);
        while(currentNode != null && currentNode.Parent != null) {
            currentNode = currentNode.Parent;
            ancestors.add(currentNode);
        }
        return ancestors;
    }
}
