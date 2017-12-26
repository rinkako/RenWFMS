package org.sysu.workflow.bridge;

import org.sysu.workflow.model.Data;
import org.sysu.workflow.model.Datamodel;
import org.sysu.workflow.model.extend.SubProcess;
import org.sysu.workflow.model.extend.Task;
import org.sysu.workflow.model.extend.Tasks;

import java.util.List;

public class InheritableContext {
    public Datamodel InheritedDatamodel;

    public Tasks InheritedTasks;

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
                for (int i = 0; i < orgData.size(); i++) {
                    if (orgData.get(i).getId().equals(d.getId())) {
                        orgData.set(i, d);
                        break;
                    }
                }
            }
        }
        return newFlag;
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
                for (int i = 0; i < orgTaskList.size(); i++) {
                    if (orgTaskList.get(i).getName().equals(t.getName())) {
                        orgTaskList.set(i, t);
                        break;
                    }
                }
            }

            List<SubProcess> procList = ts.getProcessList();
            List<SubProcess> orgProcList = this.InheritedTasks.getProcessList();
            for (SubProcess p : procList) {
                for (int i = 0; i < orgProcList.size(); i++) {
                    if (orgProcList.get(i).getName().equals(p.getName())) {
                        orgProcList.set(i, p);
                        break;
                    }
                }
            }

        }
        return newFlag;
    }
}
