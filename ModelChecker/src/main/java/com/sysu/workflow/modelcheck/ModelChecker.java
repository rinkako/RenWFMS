package com.sysu.workflow.modelcheck;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * User：ThinerZQ
 * Email：thinerzq@gmail.com
 * Date：2017/1/5 19:48
 * Project：WorkflowModelCheck
 * Package：com.sysu.workflow.modelcheck
 */
public class ModelChecker {

    private static StateNodeTree tree;
    private static String pmlPath;

    public static void main(String[] args) {


        for (String path : args) {
            if (path != null && !"".equals(path)) {
                pmlPath = path;
                tree = Paser.startPaser(pmlPath);
                String code = scxml2promela(tree);
                //save to pml file
                saveTofile(path.replace(".xml", ".pml"), code);
            }
        }
    }

    private static void saveTofile(String path, String code) {
        //保存到scxml文件的相同目录，名字也相同
        FileOutputStream fop = null;
        try {
            File file = new File(path);
            fop = new FileOutputStream(file);

            if (!file.exists()) {
                file.createNewFile();
            }
            byte[] contentInBytes = code.getBytes();
            fop.write(contentInBytes);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                fop.flush();
                fop.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }

    public static String scxml2promela(StateNodeTree tree) {
        StringBuilder promelaText = new StringBuilder();
        StringBuilder promelaDoGuard = new StringBuilder();
        StringBuilder promelaElseDoGuard = new StringBuilder();
        int counter = 1;
        StringBuilder variables = init(tree);
        promelaText.append(variables);
        promelaText.append("active proctype ourtype(){" +
                "isactive[1]=true;" +
                "do{" +
                "");

        List<Transition> transitions = getAllTransitions(tree);
        for (Transition t : transitions) {

            Set<StateNode> enterStates;
            Set<StateNode> exitStates;
            // 计算do 里面的守护条件，以及完整的退出状态和进入状态
            // 1. do 里面的守护条件;
            StateNode lcancestor = getLCAncestor(t);
            Set<StateNode> tempNodes = getProperAncestor(t.getSource(), lcancestor);
            //2. 计算完整的退出状态
            if (t.getSource().isParallel() || t.getSource().isComposite() || !tree.isBrother(t.getSource(), t.getTarget())) {
                exitStates = computeExitStates(t);
            } else {
                exitStates = new HashSet<StateNode>();
                exitStates.add(t.getSource());
            }
            //还是要区分转移的类型:yes or no is a problem
            promelaDoGuard.append("::(");
            for (StateNode node : tempNodes) {

                //todo 放的位置应该改一改
                if(node.isParallel() || node.isComposite()){
                    promelaDoGuard.append("cangoout[" + node.getNumber() + "] &&");
                }

                if(t.event!=null && t.event.equals("done.state."+node.getName())) {

                    if(node.isComposite()){
                        promelaDoGuard.append("complexFinish==1 &&");
                    }else if(node.isParallel()){
                        promelaDoGuard.append("parallelFinish==1 &&");
                    }else{
                        //nothing
                    }
                }
                promelaDoGuard.append("isactive[" + node.getNumber() + "] &&");
            }
            promelaDoGuard.delete(promelaDoGuard.lastIndexOf("&&"), promelaDoGuard.length());
            promelaDoGuard.append(")");
            promelaDoGuard.append("->atomic{" +
                    "statetochange=true;" +
                    "list[i]=" + counter + ";" +
                    "i++;");
            //添加cangoout=false,添加parallelFinish,和complexFinish
            if(getLCAncestor(t).isComposite() || getLCAncestor(t).isParallel()){
                promelaDoGuard.append("cangoout["+getLCAncestor(t).getNumber()+"]=false;");
            }
            //表示复合状态的结束节点
            if(t.getTarget().getTransitions().size() ==0){
                if(getLCAncestor(t).getParent()!= null) {
                    if (getLCAncestor(t).getParent().isParallel()) {
                        promelaDoGuard.append("parallelFinish_" + getLCAncestor(t).getParent().getNumber() + "++;");
                        promelaDoGuard.append("complexFinish_" + getLCAncestor(t).getNumber() + "++;");

                    } else {
                        promelaDoGuard.append("complexFinish_" + getLCAncestor(t).getNumber() + "++;");

                    }
                }
            }
            //
            for (StateNode exitState : exitStates) {
                promelaDoGuard.append("isactive[" + exitState.getNumber() + "]=false;");
            }
            promelaDoGuard.append("}");
            //
            if (t.getTarget().isParallel() || t.getTarget().isComposite() || !tree.isBrother(t.getSource(), t.getTarget())) {
                enterStates = computeEntryStates(t);
            } else {
                enterStates = new HashSet<StateNode>();
                enterStates.add(t.getTarget());
            }

            promelaElseDoGuard.append("::(list[actioncount]==" + counter + ")->atomic{" +
                    "actioncount++;");
            //do else里面部分
            for (StateNode node : enterStates) {
                promelaElseDoGuard.append("isactive[" + node.getNumber() + "]=true;");
            }
            promelaElseDoGuard.append("}");
            counter++;
        }
        //开始修补promela代码
        promelaText.append(promelaDoGuard);
        promelaText.append("else->{" +
                "if" +
                "::(statetochange)->" +
                "statetochange=false;" +
                "i=1;");
        promelaText.append("" +
                "do" +
                "::(temp1<stateNumber && cangoout[temp1]==false)->" +
                "cangoout[temp1]=true;" +
                "temp1++;" +
                "::(temp1==stateNumber)->" +
                "break;" +
                "::else->temp1++" +
                "od;" +
                "temp1=1;");
        promelaText.append("do");
        promelaText.append(promelaElseDoGuard);
        promelaText.append("::(list[actioncount]==0)->" +
                "break;" +
                "od;");
        promelaText.append("actioncount=1;");
        promelaText.append("do" +
                "::(temp2<transNumber && list[temp2]!=0)->" +
                "list[temp2]=0;" +
                "temp2++;" +
                "::(temp2==transNumber)->" +
                "break;" +
                "::else->" +
                "temp2++;" +
                "od;" +
                "temp2=1;");
        promelaText.append("::else->goto DeadLock;");
        promelaText.append("fi;" +
                "}" +
                "od;" +
                "DeadLock:printf(\"Dead Lock\");" +
                "TheEnd:skip;");

        //调整格式
        String code = promelaText.toString().replace(";", ";\n");
        code = code.replace("}", "}\n");
        code = code.replace("{", "{\n");

        System.out.println(code);

        return code;

    }

    private static Set<StateNode> computeEntryStates(Transition t) {
        Set<StateNode> enterStates = new HashSet<StateNode>();

        addDescendantStateToEnterstates(t.getTarget(), enterStates);
        StateNode lcAncestor = getLCAncestor(t);
        addAncestorStateToEnterState(t.getTarget(), lcAncestor, enterStates);

        return enterStates;
    }

    private static void addAncestorStateToEnterState(StateNode target, StateNode lcAncestor, Set<StateNode> enterStates) {
        for (StateNode ancestor : getProperAncestor(target, lcAncestor)) {
            enterStates.add(ancestor);
            if (ancestor.isParallel()) {
                for (StateNode child : ancestor.getChildren()) {
                    addDescendantStateToEnterstates(child, enterStates);
                }
            }
        }
    }

    private static Set<StateNode> getProperAncestor(StateNode target, StateNode lcAncestor) {

        Set<StateNode> properAncestor = new HashSet<StateNode>();
        StateNode temp = target;
        while (temp != null && temp != lcAncestor) {
            properAncestor.add(temp);
            temp = temp.getParent();
        }
        return properAncestor;
    }

    private static void addDescendantStateToEnterstates(StateNode target, Set<StateNode> enterStates) {
        enterStates.add(target);
        if (target.isComposite()) {
            //enterStates.add(target.initState);
            //找到初始节点
            for (StateNode temp : target.getChildren()) {
                if (temp.getName().contains("_init")) {
                    enterStates.add(temp);
                }
                break;
            }

        } else if (target.isParallel()) {
            for (StateNode child : target.getChildren()) {
                enterStates.add(child);
                for (StateNode temp : child.getChildren()) {
                    if (temp.getName().contains("_init")) {
                        enterStates.add(temp);
                    }
                    break;
                }
            }
        }
    }

    private static Set<StateNode> computeExitStates(Transition t) {

        Set<StateNode> exitStates = new HashSet<StateNode>();
        StateNode lcancestor = getLCAncestor(t);
        /*If（lcancestor is parallel）{

        }*/
        for (StateNode temp : lcancestor.getChildren()) {
            if (t.getSource().isDescendantOf(temp)) {
                exitStates.addAll(tree.getAllDescendantAndSelf(temp));
                break;
            }
        }
        return exitStates;
    }

    private static StateNode getLCAncestor(Transition t) {

        int sourceLevel = tree.getLayer(t.getSource());
        int targetLevel = tree.getLayer(t.getTarget());

        StateNode lowLeveStateNode = sourceLevel > targetLevel ? t.getTarget() : t.getSource();
        StateNode highLeveStateNode = sourceLevel > targetLevel ? t.getSource() : t.getTarget();

        return getLCAncestor(lowLeveStateNode, highLeveStateNode);
    }

    private static StateNode getLCAncestor(StateNode lowLeveStateNode, StateNode highLevelStateNode) {
        if (lowLeveStateNode.isAncesotrOf(highLevelStateNode)) {
            return lowLeveStateNode;
        } else {
            return getLCAncestor(lowLeveStateNode.getParent(), highLevelStateNode);
        }
    }

    private static StringBuilder init(StateNodeTree tree) {
        StringBuilder variables = new StringBuilder();
        variables.append("int list[" + 100 + "]=0;");
        variables.append("bool isactive[" + tree.getStateNumber() + "]=false;");
        variables.append("statetochange=false;");
        variables.append("actioncount=1;");
        variables.append("temp1=1;");
        variables.append("temp2=1;");
        variables.append("int stateNumber=" + tree.getStateNumber() + ";");
        variables.append("int transNumber=" + tree.getTransitionNumber() + ";");
        variables.append("bool cangoout[" + tree.getStateNumber() + "]=true;");
        variables.append("int i=1;");
        //TODO ：此处还应该根据并发状态，和复合状态的数量来搞一个并发计数器
        List<StateNode> parallelStates = tree.findParallelState();
        List<StateNode> complexStates =tree.findComplexState();


        for(StateNode stateNode: parallelStates){
            variables.append("int parallelFinish_"+stateNode.getNumber()+"=0");
        }
        for(StateNode stateNode: complexStates){
            variables.append("int complexFinish_"+stateNode.getNumber()+"=0");
        }


        return variables;
    }

    private static List<Transition> getAllTransitions(StateNodeTree tree) {
        List<Transition> allTransitions = new ArrayList<Transition>();

        preOrder(tree.getRoot(), allTransitions);
        return allTransitions;
    }

    private static void preOrder(StateNode root, List<Transition> allTransitions) {

        //加添root的转移
        allTransitions.addAll(root.getTransitions());
        for (StateNode temp : root.getChildren()) {
            preOrder(temp, allTransitions);
        }
    }

}
