package org.sysu.workflow.model.extend;

import org.sysu.workflow.SCXMLExecutionContext;
import org.sysu.workflow.SCXMLExpressionException;
import org.sysu.workflow.bridge.EngineBridge;
import org.sysu.workflow.ActionExecutionContext;
import org.sysu.workflow.Context;
import org.sysu.workflow.model.EnterableState;
import org.sysu.workflow.model.ModelException;
import org.sysu.workflow.model.NamelistHolder;
import org.sysu.workflow.model.ParamsContainer;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Call标签类
 * Created by Rinkako on 2017/3/8.
 */
public class Call extends NamelistHolder implements Serializable {
    /**
     * Serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The name of the task or the sub process to call
     */
    private String name;

    /**
     * How many task instances ought to be create
     */
    private int instances = 1;

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
     * Get the value of instance
     * @return value of instance property
     */
    public int getInstances() {
        return instances;
    }

    /**
     * Set the value of instance
     * @param instances the instance value to set, represent how many task instances ought to be created
     */
    public void setInstances(int instances) {
        this.instances = instances;
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
        Map<String, Object> payloadDataMap = new LinkedHashMap();
        addParamsToPayload(exctx, payloadDataMap);

        //获取当前BO实例的tasks列表
        Tasks tasks = scxmlExecContext.getSCXMLExecutor().getStateMachine().getTasks();
        if (tasks != null) {
            List<Task> taskList = tasks.getTaskList();
            List<SubProcess> processList = tasks.getProcessList();
            boolean successFlag = false;
            if(!taskList.isEmpty()){
                for (Task t : taskList) {
                    //判断一个task的名字与当前call标签的name是否相同
                    //todo
                    if (t.getName().equals(this.name)) {
                        // Send Message to APP
                        EngineBridge.QuickEnqueueBOMessage(scxmlExecContext.getSCXMLExecutor().getExecutorIndex(),
                                this.name, payloadDataMap, t.getBrole(), t.getEvent());
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
