package com.sysu.workflow.modelcheck;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * User：ThinerZQ
 * Email：thinerzq@gmail.com
 * Date：2017/1/5 14:11
 * Project：WorkflowModelCheck
 * Package：com.sysu.workflow.modelcheck
 */
public class StateNodeTree {
    private int stateNumber;
    private int transitionNumber;
    private StateNode root;

    public StateNodeTree(StateNode root) {
        this.root = root;
    }

    public int getStateNumber() {

        return stateNumber;
    }

    public int getTransitionNumber() {

        return transitionNumber;
    }

    public void insert(StateNode node, int parentNumber) {
        //广度优先遍历，找到这样的一个元素
        StateNode parentNode = findStateNode(parentNumber);
        parentNode.getChildren().add(node);
        node.setParent(parentNode);
        stateNumber++;
        transitionNumber += node.getTransitions().size();
    }

    public void insert(StateNode node, String parentName) {
        //广度优先遍历，找到这样的一个元素
        StateNode parentNode = findStateNode(parentName);
        parentNode.getChildren().add(node);
        node.setParent(parentNode);
        stateNumber++;
        //System.out.println(node.getNumber()+","+node.getTransitions().size());
        transitionNumber += node.getTransitions().size();
    }


    StateNode findStateNode(int targetStateNumber) {
        Queue<StateNode> queue = new LinkedList<StateNode>();
        queue.add(root);
        while (!queue.isEmpty()) {
            StateNode temp = queue.poll();
            if (temp.getNumber() == targetStateNumber) {
                return temp;
            } else {
                for (StateNode tmp : temp.getChildren()) {
                    queue.add(tmp);
                }
            }
        }
        return null;
    }

    StateNode findStateNode(String targetStateName) {
        Queue<StateNode> queue = new LinkedList<StateNode>();
        queue.add(root);
        while (!queue.isEmpty()) {
            StateNode temp = queue.poll();
            if (temp.getName().equals(targetStateName)) {
                return temp;
            } else {
                for (StateNode tmp : temp.getChildren()) {
                    queue.add(tmp);
                }
            }
        }
        return null;
    }

    public void printTree(StateNode root) {
        //前序遍历树
        for (StateNode temp : root.getChildren()) {
            System.out.println("状态: [编号：" + temp.getNumber() + " ，名字：" + temp.getName() + " , 并发：" + temp.isParallel() + " , 复合：" + temp.isComposite() + "]");
            for (Transition t : temp.getTransitions()) {
                System.out.println(t);

            }
            System.out.println();
            printTree(temp);
        }


    }

    public StateNode getRoot() {
        return root;
    }

    public void setRoot(StateNode root) {
        this.root = root;
    }

    public List<StateNode> getAllDescendantAndSelf(StateNode root) {
        List<StateNode> tempNodes = new ArrayList<StateNode>();
        Queue<StateNode> queue = new LinkedList<StateNode>();
        queue.add(root);
        while (!queue.isEmpty()) {
            StateNode temp = queue.poll();
            tempNodes.add(temp);
            for (StateNode tmp : temp.getChildren()) {
                queue.add(tmp);
            }
        }
        return tempNodes;
    }

    //根节点为第0层
    public int getLayer(StateNode node) {
        if (node == this.getRoot()) {
            return 0;
        }
        if (node.getParent() == this.getRoot()) {
            return 1;
        } else {
            return getLayer(node.getParent()) + 1;
        }
    }

    public boolean isBrother(StateNode node1, StateNode node2) {
        if (node1.getParent() == node2.getParent())
            return true;
        else
            return false;
    }
    public List<StateNode> findParallelState(){

        List<StateNode> parallelStates = new ArrayList<StateNode>();

        Queue<StateNode> queue = new LinkedList<StateNode>();
        queue.add(root);
        while (!queue.isEmpty()){
            StateNode temp = queue.poll();
            if (temp.isParallel()){
                parallelStates.add(temp);
            }
            for(StateNode node : temp.getChildren()){
                queue.add(node);
            }
        }


        return parallelStates;
    }
    public List<StateNode> findComplexState(){
        List<StateNode> complexStates = new ArrayList<StateNode>();

        Queue<StateNode> queue = new LinkedList<StateNode>();
        queue.add(root);
        while (!queue.isEmpty()){
            StateNode temp = queue.poll();
            if (temp.isComposite()){
                complexStates.add(temp);
            }
            for(StateNode node : temp.getChildren()){
                queue.add(node);
            }
        }
        return complexStates;
    }
}
