package com.sysu.workflow.service.processservice;

import com.sysu.workflow.entity.ProcessInstanceEntity;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA
 * Date: 2016/1/18
 * Time: 21:09
 * User: ThinerZQ
 * GitHub: <a>https://github.com/ThinerZQ</a>
 * Blog: <a>http://blog.csdn.net/c601097836</a>
 * Email: 601097836@qq.com
 */
public class ProcessInstanceQuery {
    private ProcessInstanceEntity processInstanceEntity = null;
    private ProcessInstanceDao processInstanceDao = null;

    public ProcessInstanceQuery(ProcessInstanceEntity processInstanceEntity) {
        this.processInstanceEntity = processInstanceEntity;
    }

    public ProcessInstanceQuery() {
        processInstanceEntity = new ProcessInstanceEntity();
    }

    public ProcessInstanceQuery processInstanceId(String  id) {
        processInstanceEntity.setProcessInstanceId(id);
        return this;
    }

    public ProcessInstanceQuery userId(String processInstanceName) {
        processInstanceEntity.setProcessInstanceName(processInstanceName);
        return this;
    }

    public ProcessInstanceEntity SingleResult() {
        if (processInstanceDao == null) {
            processInstanceDao = new ProcessInstanceDao();
        }
        ArrayList<ProcessInstanceEntity> arrayList = processInstanceDao.findProcessInstance(processInstanceEntity);
        int size = arrayList.size();
        return size >= 1 ? arrayList.get(0) : null;
    }

    public ArrayList<ProcessInstanceEntity> getAllProcessInstance() {
        if (processInstanceDao == null) {
            processInstanceDao = new ProcessInstanceDao();
        }
        return processInstanceDao.getAllProcessInstance();
    }

}

