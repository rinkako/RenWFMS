package com.sysu.workflow.modelcheck;

import java.util.ArrayList;
import java.util.List;

/**
 * User：ThinerZQ
 * Email：thinerzq@gmail.com
 * Date：2017/1/5 11:26
 * Project：WorkflowModelCheck
 * Package：com.sysu.workflow.modelcheck
 */
public class StateNode {
    int number;
    String name;
    boolean parallel;
    boolean composite;

    List<StateNode> children = new ArrayList<StateNode>();
    StateNode parent = null;
    List<Transition> transitions = new ArrayList<Transition>();

    public StateNode(String name, int number) {
        this.name = name;
        this.number = number;
    }

    public StateNode() {
    }

    boolean isDescendantOf(StateNode stateNode) {
        if (stateNode == this) {
            return true;
        } else {
            for (StateNode child : stateNode.getChildren()) {
                if (isDescendantOf(child)) {
                    return true;
                }
            }
            return false;
        }
    }

    boolean isAncesotrOf(StateNode stateNode) {
        return stateNode.isDescendantOf(this);
    }

    public List<StateNode> getChildren() {
        return children;
    }

    public void setChildren(List<StateNode> children) {
        this.children = children;
    }

    public StateNode getParent() {
        return parent;
    }

    public void setParent(StateNode parent) {
        this.parent = parent;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public boolean isParallel() {
        return parallel;
    }

    public void setParallel(boolean parallel) {
        this.parallel = parallel;
    }

    public boolean isComposite() {
        return composite;
    }

    public void setComposite(boolean composite) {
        this.composite = composite;
    }

    public List<Transition> getTransitions() {
        return transitions;
    }

    public void setTransitions(List<Transition> transitions) {
        this.transitions = transitions;
    }

    public void addTransition(Transition transition) {
        this.transitions.add(transition);
    }

    @Override
    public String toString() {
        return "StateNode{" +
                "number=" + number +
                ", name='" + name + '\'' +
                ", parallel=" + parallel +
                ", composite=" + composite +
                ", childrenNumber=" + children.size() +
                ", parentNumber=" + parent.getNumber() +
                ", transitionsNumber=" + transitions.size() +
                '}';
    }
}
