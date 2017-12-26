package com.sysu.workflow.modelcheck;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

/**
 * User：ThinerZQ
 * Email：thinerzq@gmail.com
 * Date：2017/1/5 10:51
 * Project：WorkflowModelCheck
 * Package：com.sysu.workflow.modelcheck
 */
public class Paser {
    private static int i_state = 0;
    private static int t_state = 0;
    private static List<Transition> tempTransitions = new ArrayList<Transition>();

    public static StateNodeTree startPaser(String pmlPath) {
        SAXReader reader = new SAXReader();
        Document document = null;
        try {
            document = reader.read(new File(pmlPath));
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        Element scxmlRoot = document.getRootElement();
        StateNode stateRoot = new StateNode("scxml", i_state++);
        StateNodeTree tree = new StateNodeTree(stateRoot);


        recursivePaser(scxmlRoot, scxmlRoot, tree);
        tree.printTree(tree.getRoot());

        return tree;
    }

    public static void recursivePaser(Element root, Element parent, StateNodeTree tree) {

        if (root.getName().equals("scxml")) {
            //构造开始节点
            StateNode initNode = new StateNode();
            initNode.setParent(tree.getRoot());
            initNode.setName("scxml_init");
            initNode.setNumber(i_state++);

            Transition initTransition = new Transition();
            initTransition.setSource(initNode);
            StateNode initTarget = new StateNode(root.attributeValue("initial"), -1);
            initTransition.setTarget(initTarget);
            initTransition.setNumber(++t_state);
            initNode.addTransition(initTransition);
            tempTransitions.add(initTransition);
            tree.insert(initNode, "scxml");
            //tree.getRoot().getChildren().add(initNode);


            //取出所有子节点
            List<Element> elements = root.elements();
            for (Element e : elements) {
                recursivePaser(e, root, tree);
            }
            //修复转移
            for (Transition t : tempTransitions) {
                String targetName = t.getTarget().getName();
                if (targetName == null) {
                    //自身转移怎么处理

                } else {
                    t.setTarget(tree.findStateNode(targetName));
                }

            }
        } else if (root.getName().equals("state")) {
            StateNode stateNode = new StateNode();
            stateNode.setName(root.attributeValue("id"));
            stateNode.setNumber(i_state++);

            List<Element> transElements = root.elements("transition");
            for (Element e : transElements) {
                Transition t = new Transition();
                t.setNumber(++t_state);
                t.setEvent(e.attributeValue("event"));
                t.setCond(e.attributeValue("cond"));
                t.setSource(stateNode);
                t.setTarget(new StateNode(e.attributeValue("target"), -1));
                stateNode.addTransition(t);
                tempTransitions.add(t);
            }
            if (parent.getName().equals("scxml")) {
                tree.insert(stateNode, "scxml");
            } else {
                tree.insert(stateNode, parent.attributeValue("id"));
            }
            List<Element> elements = root.elements();

            boolean b = elements.removeIf(new Predicate<Element>() {
                public boolean test(Element element) {
                    if (element.getName().equals("state") || element.getName().equals("parallel") || element.getName().equals("final") || element.getName().equals("initial")) {
                        return false;
                    } else
                        return true;
                }
            });
            if (elements.size() > 0) {
                stateNode.setComposite(true);
                for (Element e : elements) {
                    recursivePaser(e, root, tree);
                }
            }
        } else if (root.getName().equals("parallel")) {
            StateNode stateNode = new StateNode();
            stateNode.setName(root.attributeValue("id"));
            stateNode.setNumber(i_state++);
            stateNode.setParallel(true);

            List<Element> transElements = root.elements("transition");
            for (Element e : transElements) {
                Transition t = new Transition();
                t.setNumber(++t_state);
                t.setEvent(e.attributeValue("event"));
                t.setCond(e.attributeValue("cond"));
                t.setSource(stateNode);
                t.setTarget(new StateNode(e.attributeValue("target"), -1));
                stateNode.addTransition(t);
                tempTransitions.add(t);
            }
            if (parent.getName().equals("scxml")) {
                tree.insert(stateNode, "scxml");
            } else {
                tree.insert(stateNode, parent.attributeValue("id"));
            }
            List<Element> elements = root.elements("state");
            if (elements.size() > 0) {
                //stateNode.setComposite(true);
                for (Element e : elements) {
                    recursivePaser(e, root, tree);
                }
            }
        } else if (root.getName().equals("initial")) {
            StateNode stateNode = new StateNode();
            stateNode.setName(parent.attributeValue("id") + "_init");
            stateNode.setNumber(i_state++);

            //给这样的节点添加转移
            List<Element> transElements = root.elements("transition");
            for (Element e : transElements) {
                Transition t = new Transition();
                t.setNumber(++t_state);
                t.setSource(stateNode);
                t.setTarget(new StateNode(e.attributeValue("target"), -1));
                stateNode.addTransition(t);
                tempTransitions.add(t);
            }
            if (parent.getName().equals("scxml")) {
                tree.insert(stateNode, "scxml");
            } else {
                tree.insert(stateNode, parent.attributeValue("id"));
            }
        } else if (root.getName().equals("final")) {
            StateNode stateNode = new StateNode();
            stateNode.setName(root.attributeValue("id"));
            stateNode.setNumber(i_state++);
            if (parent.getName().equals("scxml")) {
                tree.insert(stateNode, "scxml");
            } else {
                tree.insert(stateNode, parent.attributeValue("id"));
            }
        }
    }


}
