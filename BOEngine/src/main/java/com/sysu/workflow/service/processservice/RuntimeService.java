package com.sysu.workflow.service.processservice;

import com.sysu.workflow.entity.ProcessInstanceEntity;

/**
 * Created with IntelliJ IDEA
 * Date: 2016/1/18
 * Time: 21:03
 * User: ThinerZQ
 * GitHub: <a>https://github.com/ThinerZQ</a>
 * Blog: <a>http://blog.csdn.net/c601097836</a>
 * Email: 601097836@qq.com
 */
public class RuntimeService {

    ProcessInstanceDao processInstanceDao=null;

    public static ProcessInstanceQuery createProcessInstanceQuery() {
        return new ProcessInstanceQuery();
    }

    public boolean saveProcessInstance(ProcessInstanceEntity processInstanceEntity){

        if (processInstanceDao==null){
            processInstanceDao = new ProcessInstanceDao();
        }

        return  processInstanceDao.addProcessInstance(processInstanceEntity);
    }

    public boolean updateProcessInstance(ProcessInstanceEntity processInstanceEntity){

        if (processInstanceDao==null){
            processInstanceDao = new ProcessInstanceDao();
        }

        return  processInstanceDao.updateProcessInstance(processInstanceEntity);

    }

    public ProcessInstanceEntity newProcessInstance(){
        return new ProcessInstanceEntity();
    }
    public ProcessInstanceEntity newProcessInstance(String processInstanceId,String processInstanceName,String processInstanceCreateTime){
        return new ProcessInstanceEntity(processInstanceId,processInstanceName,processInstanceCreateTime);
    }
}
