package com.sysu.workflow.service.taskservice;

import com.sysu.workflow.entity.UserEntity;
import com.sysu.workflow.entity.UserWorkItemEntity;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA
 * Date: 2016/1/20
 * Time: 15:06
 * User: ThinerZQ
 * GitHub: <a>https://github.com/ThinerZQ</a>
 * Blog: <a>http://blog.csdn.net/c601097836</a>
 * Email: 601097836@qq.com
 */
public class UserTaskQuery {
    private UserWorkItemEntity userWorkItemEntity = null;
    private WorkItemDao workItemDao = null;

    public UserTaskQuery(UserWorkItemEntity workItemEntity) {
        this.userWorkItemEntity = workItemEntity;
    }

    public UserTaskQuery() {
        userWorkItemEntity = new UserWorkItemEntity();
    }

    public UserTaskQuery taskName(String taskName) {
        userWorkItemEntity.setItemName(taskName);
        return this;
    }

    public UserTaskQuery taskId(int taskId) {
        userWorkItemEntity.setItemId(taskId);
        return this;
    }
    public UserTaskQuery taskFinish(String itemFinish) {
        userWorkItemEntity.setItemFinish(itemFinish);
        return this;
    }

    public UserTaskQuery taskProcessInstanceId(String taskProcessInstanceId) {
        userWorkItemEntity.setItemProcessId(taskProcessInstanceId);
        return this;
    }

    public UserTaskQuery taskStateId(String taskStateId) {
        userWorkItemEntity.setItemStateId(taskStateId);
        return this;
    }

    public UserTaskQuery taskAssignee(UserEntity userEntity) {
        userWorkItemEntity.setItemAssigneeEntity(userEntity);
        return this;
    }

    public UserWorkItemEntity SingleResult() {
        if (workItemDao == null) {
            workItemDao = new WorkItemDao();
        }
        ArrayList<UserWorkItemEntity> arrayList = workItemDao.findUserWorkItem(userWorkItemEntity);
        int size = arrayList.size();
        return size >= 1 ? arrayList.get(0) : null;
    }

    public ArrayList<UserWorkItemEntity> list() {
        if (workItemDao == null) {
            workItemDao = new WorkItemDao();
        }

        return workItemDao.findUserWorkItem(userWorkItemEntity);
    }
}
