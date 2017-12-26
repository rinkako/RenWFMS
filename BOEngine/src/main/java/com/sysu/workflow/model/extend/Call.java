package com.sysu.workflow.model.extend;

import com.sysu.workflow.*;
import com.sysu.workflow.bridge.EngineBridge;
import com.sysu.workflow.model.*;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Call标签类
 * Created by Rinkako on 2017/3/8.
 */
public class Call extends ParamsContainer implements Serializable {
    /**
     * Serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The name of the task or the sub process to call
     */
    private String name;

    /**
     * Get the value of name
     * @return the task name to call
     */
    public String getName() {
        return name;
    }

    /**
     * Set the value of name
     * @param name the task name to call
     */
    public void setName(String name) {
        this.name = name;
    }


    /**
     * Execute RPC
     * @param exctx The ActionExecutionContext for this execution instance
     * @throws ModelException
     * @throws SCXMLExpressionException
     */
    @Override
    public void execute(ActionExecutionContext exctx) throws ModelException, SCXMLExpressionException {
        SCXMLExecutionContext scxmlExecContext = (SCXMLExecutionContext)exctx.getInternalIOProcessor();
        EnterableState parentState = getParentEnterableState();
        Context ctx = exctx.getContext(parentState);
        ctx.setLocal(getNamespacesKey(), getNamespaces());
        Map<String, Object> payloadDataMap = new LinkedHashMap<String,Object>();
        addParamsToPayload(exctx, payloadDataMap);


        Tasks tasks = scxmlExecContext.getSCXMLExecutor().getStateMachine().getTasks();
        if (tasks != null) {
            List<Task> taskList = tasks.getTaskList();
            List<SubProcess> processList = tasks.getProcessList();
            boolean successFlag = false;
            if(!taskList.isEmpty()){
                for (Task t : taskList) {
                    //判断一个task的名字与当前call标签的name是否相同
                    if (t.getName().equals(this.name)) {
                        // Send Message to APP
                        String dasher = "";
                        if (t.getAgent() != null) {
                            dasher = t.getAgent();
                        } else if (t.getAssignee() != null) {
                            dasher = t.getAssignee();
                        }
                        EngineBridge.QuickEnqueueBOMessage(scxmlExecContext.getSCXMLExecutor().getExecutorIndex(),
                                this.name, payloadDataMap, dasher, t.getEvent());
                        successFlag = true;
                        break;
                    }
                }
            } else if(!processList.isEmpty()){
                for(SubProcess subProcess : processList){
                    //判断一个subprocess的名字与当前call标签的subprocess的name是否相同
                    if(subProcess.getName().equals(this.name)){
                        EngineBridge.QuickEnqueueBOMessage(scxmlExecContext.getSCXMLExecutor().getExecutorIndex(),
                                this.name, subProcess.getSrc(),payloadDataMap, subProcess.getEvents());
                        System.out.println("test : begin invoking a sub process!!!!");
                        successFlag = true;
                        break;
                    }
                }
            }

            if (successFlag == false) {
                throw new ModelException();
            }
        }
        else {
            throw new ModelException();
        }
    }
}
