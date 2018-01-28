package org.sysu.workflow.bridge;

import org.sysu.workflow.model.Data;
import org.sysu.workflow.model.Datamodel;
import org.sysu.workflow.model.extend.SubProcess;
import org.sysu.workflow.model.extend.Task;
import org.sysu.workflow.model.extend.Tasks;

import java.io.Serializable;
import java.util.List;
/**
 * Business Object Inheritable Context
 * This class is used to generate InheritableContext of the BO for the runtime.
 */
public class InheritableContext implements Serializable {

    /**
     * Serial version UID.
     */
    private static final long serialVersionUID = 2L;

    private Datamodel InheritedDatamodel;
    private Tasks InheritedTasks;

    public boolean UpdateDataModel(Datamodel dm) {
        boolean newFlag = false;
        if (this.InheritedDatamodel == null) {
            this.InheritedDatamodel = dm;
            newFlag = true;
        }
        else {
            List<Data> data = dm.getData();
            List<Data> orgData = this.InheritedDatamodel.getData();
            for (Data d : data) {
                //如果可继承上下文的datamodel中拥有同名的data,就用当前步所得到的新data对象替换掉旧data对象
                boolean temp = false;
                for (int i = 0; i < orgData.size(); i++) {
                    if (orgData.get(i).getId().equals(d.getId())) {
                        orgData.set(i, d);
                        temp = true;
                        break;
                    }
                }
                //否则将新data插入到可继承上下文的datamodel中。
                if(temp == false){
                    orgData.add(d);
                }
            }
            //使用新的orgData更新可继承上下文的datamodel
            Datamodel datamodel = new Datamodel();
            for(Data d:orgData){
                datamodel.addData(d);
            }
            this.InheritedDatamodel = datamodel;
        }
        return newFlag;
    }

    public Datamodel getInheritedDatamodel() {
        return InheritedDatamodel;
    }

    public boolean UpdateTasks(Tasks ts) {
        boolean newFlag = false;
        if (this.InheritedTasks == null) {
            this.InheritedTasks = ts;
            newFlag = true;
        }
        else {
            List<Task> taskList = ts.getTaskList();
            List<Task> orgTaskList = this.InheritedTasks.getTaskList();
            for (Task t : taskList) {
                //如果可继承上下文的tasks中拥有同名的task,就用当前步所得到的新task对象替换掉旧task对象
                boolean temp = false;
                for (int i = 0; i < orgTaskList.size(); i++) {
                    if (orgTaskList.get(i).getName().equals(t.getName())) {
                        orgTaskList.set(i, t);
                        temp = true;
                        break;
                    }
                }
                //否则将新task插入到可继承上下文的tasks中。
                if(temp == false){
                    orgTaskList.add(t);
                }
            }

            List<SubProcess> procList = ts.getProcessList();
            List<SubProcess> orgProcList = this.InheritedTasks.getProcessList();
            for (SubProcess p : procList) {
                boolean temp = false;

                for (int i = 0; i < orgProcList.size(); i++) {
                    if (orgProcList.get(i).getName().equals(p.getName())) {
                        orgProcList.set(i, p);
                        temp = true;
                        break;
                    }
                }
                //否则将新subprocess插入到可继承上下文的subprocess中。
                if(temp == false){
                    orgProcList.add(p);
                }
            }

            //更新可继承上下文的tasks
            Tasks tasks = new Tasks();
            for(Task t:orgTaskList){
                tasks.addTask(t);
            }
            for(SubProcess p:orgProcList){
                tasks.addProcess(p);
            }
            this.InheritedTasks = tasks;
        }
        return newFlag;
    }
    public void setInheritedDatamodel(Datamodel inheritedDatamodel) {
        InheritedDatamodel = inheritedDatamodel;
    }

    public Tasks getInheritedTasks() {
        return InheritedTasks;
    }

    public void setInheritedTasks(Tasks inheritedTasks) {
        InheritedTasks = inheritedTasks;
    }
}
