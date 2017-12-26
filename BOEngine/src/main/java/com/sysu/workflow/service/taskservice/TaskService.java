package com.sysu.workflow.service.taskservice;

import com.sysu.workflow.entity.GroupWorkItemEntity;
import com.sysu.workflow.entity.UserWorkItemEntity;

/**
 * Created with IntelliJ IDEA
 * Date: 2016/1/20
 * Time: 14:59
 * User: ThinerZQ
 * GitHub: <a>https://github.com/ThinerZQ</a>
 * Blog: <a>http://blog.csdn.net/c601097836</a>
 * Email: 601097836@qq.com
 */
public class TaskService {
    private WorkItemDao workItemDao;

    public static UserTaskQuery createUserTaskQuery() {
        return new UserTaskQuery();
    }

    public static GroupTaskQuery createGroupTaskQuery() {
        return new GroupTaskQuery();
    }

    public long saveUserWorkItem(UserWorkItemEntity workItemEntity) {
        if (workItemDao == null) {
            workItemDao = new WorkItemDao();
        }
        return workItemDao.insertIntoWorkItem(workItemEntity);
    }

    public long saveGroupWorkItem(GroupWorkItemEntity groupWorkItemEntity) {
        if (workItemDao == null) {
            workItemDao = new WorkItemDao();
        }
        return workItemDao.insertIntoWorkItem(groupWorkItemEntity);
    }

    public boolean updateUserWorkItem(UserWorkItemEntity userWorkItemEntity) {
        if (workItemDao == null) {
            workItemDao = new WorkItemDao();
        }
        return workItemDao.updateUserWorkItem(userWorkItemEntity);
    }

    public boolean updateGroupWorkItem(GroupWorkItemEntity groupWorkItemEntity) {
        if (workItemDao == null) {
            workItemDao = new WorkItemDao();
        }
        return workItemDao.updateGroupWorkItem(groupWorkItemEntity);
    }
    public UserWorkItemEntity newWorkItem() {
        return new UserWorkItemEntity();
    }


}
