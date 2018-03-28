/*
 * Project Ren @ 2018
 * Rinkako, Ariana, Gordan. SYSU SDCS.
 */
package org.sysu.workflow.instanceTree;

import org.sysu.workflow.BOXMLExecutionContext;

import java.util.ArrayList;

/**
 * Author: Rinkako
 * Date  : 2017/3/15
 * Usage : Instance Tree node, which represents a BO.
 */
public class RTreeNode {

    /**
     * Construct a new BO tree node.
     * @param filename BO name
     * @param globalId unique id
     * @param exect binding execution context
     * @param parent parent tree node reference
     */
    public RTreeNode(String filename, String globalId, BOXMLExecutionContext exect, RTreeNode parent) {
        this.name = filename;
        this.Parent = parent;
        this.globalId = globalId;
        this.exect = exect;
        this.Children = new ArrayList<RTreeNode>();
    }

    /**
     * Add a child.
     * @param ttn tree node to be added
     */
    public void AddChild(RTreeNode ttn) {
        ttn.Parent = this;
        this.Children.add(ttn);
    }

    /**
     * Get global id of this node.
     * @return global id string
     */
    public String getGlobalId() {
        return globalId;
    }

    /**
     * Get binding BO execution context.
     * @return BOXMLExecutionContext instance
     */
    public BOXMLExecutionContext getExect() {
        return exect;
    }

    /**
     * Get binding BO name.
     * @return BO name string
     */
    public String getName() {
        return name;
    }

    /**
     * Children vector of this node.
     */
    public ArrayList<RTreeNode> Children;

    /**
     * Parent of this node.
     */
    public RTreeNode Parent;

    /**
     * Node global id.
     */
    private String globalId;

    /**
     * BO name.
     */
    private String name;

    /**
     * Binding execution context.
     */
    private BOXMLExecutionContext exect;
}