package com.sysu.workflow;

import com.sysu.workflow.model.TaskState;

import java.util.Date;

/**
 * Created with IntelliJ IDEA
 * Date: 2016/2/2
 * Time: 14:09
 * User: ThinerZQ
 * GitHub: <a>https://github.com/ThinerZQ</a>
 * Blog: <a>http://www.thinerzq.me</a>
 * Email: 601097836@qq.com
 */

public class CrowdSourcingTaskEntity {


    private long taskId;
    private String taskName;

    private String taskDescription;

    private Date taskReleaseTime;

    private Date taskDeadlineTime;

    private Date taskCompleteTime;

    private String taskPrice;

    private int taskJudgeCount = 3;

    private int taskDecomposeCount = 2;

    private int taskDecomposeVoteCount = 3;

    private int taskSolveCount = 2;

    private int taskSolveVoteCount = 3;



    private TaskState taskState;


    public void updateTaskState(String taskState){
        this.setTaskState(TaskState.valueOf(taskState));
    }

    public boolean merge(){


        System.out.println("merge");

        return true;
    }


    public long getTaskId() {
        return taskId;
    }

    public void setTaskId(long taskId) {
        this.taskId = taskId;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getTaskDescription() {
        return taskDescription;
    }

    public void setTaskDescription(String taskDescription) {
        this.taskDescription = taskDescription;
    }

    public Date getTaskReleaseTime() {
        return taskReleaseTime;
    }

    public void setTaskReleaseTime(Date taskReleaseTime) {
        this.taskReleaseTime = taskReleaseTime;
    }

    public Date getTaskDeadlineTime() {
        return taskDeadlineTime;
    }

    public void setTaskDeadlineTime(Date taskDeadlineTime) {
        this.taskDeadlineTime = taskDeadlineTime;
    }

    public Date getTaskCompleteTime() {
        return taskCompleteTime;
    }

    public void setTaskCompleteTime(Date taskCompleteTime) {
        this.taskCompleteTime = taskCompleteTime;
    }

    public String getTaskPrice() {
        return taskPrice;
    }

    public void setTaskPrice(String taskPrice) {
        this.taskPrice = taskPrice;
    }

    public int getTaskJudgeCount() {
        return taskJudgeCount;
    }

    public void setTaskJudgeCount(int taskJudgeCount) {
        this.taskJudgeCount = taskJudgeCount;
    }

    public int getTaskDecomposeCount() {
        return taskDecomposeCount;
    }

    public void setTaskDecomposeCount(int taskDecomposeCount) {
        this.taskDecomposeCount = taskDecomposeCount;
    }

    public int getTaskDecomposeVoteCount() {
        return taskDecomposeVoteCount;
    }

    public void setTaskDecomposeVoteCount(int taskDecomposeVoteCount) {
        this.taskDecomposeVoteCount = taskDecomposeVoteCount;
    }

    public int getTaskSolveCount() {
        return taskSolveCount;
    }

    public void setTaskSolveCount(int taskSolveCount) {
        this.taskSolveCount = taskSolveCount;
    }

    public int getTaskSolveVoteCount() {
        return taskSolveVoteCount;
    }

    public void setTaskSolveVoteCount(int taskSolveVoteCount) {
        this.taskSolveVoteCount = taskSolveVoteCount;
    }

    public TaskState getTaskState() {
        return taskState;
    }

    public void setTaskState(TaskState taskState) {
        this.taskState = taskState;
    }

    @Override
    public String toString() {
        return "CrowdSourcingTaskEntity{" +
                "taskId=" + taskId +
                ", taskName='" + taskName + '\'' +
                ", taskDescription='" + taskDescription + '\'' +
                ", taskReleaseTime=" + taskReleaseTime +
                ", taskDeadlineTime=" + taskDeadlineTime +
                ", taskCompleteTime=" + taskCompleteTime +
                ", taskPrice='" + taskPrice + '\'' +
                ", taskJudgeCount=" + taskJudgeCount +
                ", taskDecomposeCount=" + taskDecomposeCount +
                ", taskDecomposeVoteCount=" + taskDecomposeVoteCount +
                ", taskSolveCount=" + taskSolveCount +
                ", taskSolveVoteCount=" + taskSolveVoteCount +
                ", taskState=" + taskState +
                '}';
    }
}
