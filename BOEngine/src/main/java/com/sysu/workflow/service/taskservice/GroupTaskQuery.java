package com.sysu.workflow.service.taskservice;

import com.sysu.workflow.entity.GroupEntity;
import com.sysu.workflow.entity.GroupWorkItemEntity;

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
public class GroupTaskQuery {

    private GroupWorkItemEntity groupWorkItemEntity = null;
    private WorkItemDao workItemDao = null;

    public GroupTaskQuery(GroupWorkItemEntity groupWorkItemEntity) {
        this.groupWorkItemEntity = groupWorkItemEntity;
    }

    public GroupTaskQuery() {
        groupWorkItemEntity = new GroupWorkItemEntity();
    }

    public GroupTaskQuery taskName(String taskName) {
        groupWorkItemEntity.setItemName(taskName);
        return this;
    }

    public GroupTaskQuery taskId(int taskId) {
        groupWorkItemEntity.setItemId(taskId);
        return this;
    }

    public GroupTaskQuery taskProcessInstanceId(String taskProcessInstanceId) {
        groupWorkItemEntity.setItemProcessId(taskProcessInstanceId);
        return this;
    }

    public GroupTaskQuery taskStateId(String taskStateId) {
        groupWorkItemEntity.setItemStateId(taskStateId);
        return this;
    }

    public GroupTaskQuery taskCandidateGroup(GroupEntity groupEntity) {
        groupWorkItemEntity.setItemCandidateGroupEntity(groupEntity);
        return this;
    }

    public GroupWorkItemEntity SingleResult() {
        if (workItemDao == null) {
            workItemDao = new WorkItemDao();
        }
        ArrayList<GroupWorkItemEntity> arrayList = workItemDao.findGroupWorkItem(groupWorkItemEntity);
        int size = arrayList.size();
        return size >= 1 ? arrayList.get(0) : null;
    }

    public ArrayList<GroupWorkItemEntity> list() {
        if (workItemDao == null) {
            workItemDao = new WorkItemDao();
        }

        return workItemDao.findGroupWorkItem(groupWorkItemEntity);
    }
}
