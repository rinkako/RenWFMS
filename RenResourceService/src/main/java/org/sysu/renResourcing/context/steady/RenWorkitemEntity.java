/*
 * Project Ren @ 2018
 * Rinkako, Ariana, Gordan. SYSU SDCS.
 */
package org.sysu.renResourcing.context.steady;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

/**
 * Author: Rinkako
 * Date  : 2018/2/4
 * Usage :
 */
@Entity
@Table(name = "ren_workitem", schema = "renboengine", catalog = "")
public class RenWorkitemEntity {
    private String wid;
    private String rtid;
    private String processId;
    private String boId;
    private String taskName;
    private String taskId;
    private String documentation;
    private String attributes;
    private Timestamp firingTime;
    private Timestamp enablementTime;
    private Timestamp startTime;
    private Timestamp completionTime;
    private String status;
    private String resourceStatus;
    private String startedBy;
    private String completedBy;
    private String timertrigger;
    private String timerexpiry;
    private Timestamp latestStartTime;
    private String executeTime;
    private String tag;

    @Id
    @Column(name = "wid", nullable = false, length = 64)
    public String getWid() {
        return wid;
    }

    public void setWid(String wid) {
        this.wid = wid;
    }

    @Basic
    @Column(name = "rtid", nullable = false, length = 64)
    public String getRtid() {
        return rtid;
    }

    public void setRtid(String rtid) {
        this.rtid = rtid;
    }

    @Basic
    @Column(name = "processId", nullable = false, length = 64)
    public String getProcessId() {
        return processId;
    }

    public void setProcessId(String processId) {
        this.processId = processId;
    }

    @Basic
    @Column(name = "boId", nullable = false, length = 64)
    public String getBoId() {
        return boId;
    }

    public void setBoId(String boId) {
        this.boId = boId;
    }

    @Basic
    @Column(name = "taskName", nullable = false, length = -1)
    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    @Basic
    @Column(name = "taskID", nullable = false, length = -1)
    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    @Basic
    @Column(name = "documentation", nullable = true, length = -1)
    public String getDocumentation() {
        return documentation;
    }

    public void setDocumentation(String documentation) {
        this.documentation = documentation;
    }

    @Basic
    @Column(name = "attributes", nullable = true, length = -1)
    public String getAttributes() {
        return attributes;
    }

    public void setAttributes(String attributes) {
        this.attributes = attributes;
    }

    @Basic
    @Column(name = "firingTime", nullable = true)
    public Timestamp getFiringTime() {
        return firingTime;
    }

    public void setFiringTime(Timestamp firingTime) {
        this.firingTime = firingTime;
    }

    @Basic
    @Column(name = "enablementTime", nullable = true)
    public Timestamp getEnablementTime() {
        return enablementTime;
    }

    public void setEnablementTime(Timestamp enablementTime) {
        this.enablementTime = enablementTime;
    }

    @Basic
    @Column(name = "startTime", nullable = true)
    public Timestamp getStartTime() {
        return startTime;
    }

    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }

    @Basic
    @Column(name = "completionTime", nullable = true)
    public Timestamp getCompletionTime() {
        return completionTime;
    }

    public void setCompletionTime(Timestamp completionTime) {
        this.completionTime = completionTime;
    }

    @Basic
    @Column(name = "status", nullable = true, length = 128)
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Basic
    @Column(name = "resourceStatus", nullable = true, length = 128)
    public String getResourceStatus() {
        return resourceStatus;
    }

    public void setResourceStatus(String resourceStatus) {
        this.resourceStatus = resourceStatus;
    }

    @Basic
    @Column(name = "startedBy", nullable = true, length = 64)
    public String getStartedBy() {
        return startedBy;
    }

    public void setStartedBy(String startedBy) {
        this.startedBy = startedBy;
    }

    @Basic
    @Column(name = "completedBy", nullable = true, length = 64)
    public String getCompletedBy() {
        return completedBy;
    }

    public void setCompletedBy(String completedBy) {
        this.completedBy = completedBy;
    }

    @Basic
    @Column(name = "timertrigger", nullable = true, length = 64)
    public String getTimertrigger() {
        return timertrigger;
    }

    public void setTimertrigger(String timertrigger) {
        this.timertrigger = timertrigger;
    }

    @Basic
    @Column(name = "timerexpiry", nullable = true, length = 64)
    public String getTimerexpiry() {
        return timerexpiry;
    }

    public void setTimerexpiry(String timerexpiry) {
        this.timerexpiry = timerexpiry;
    }

    @Basic
    @Column(name = "latestStartTime", nullable = true)
    public Timestamp getLatestStartTime() {
        return latestStartTime;
    }

    public void setLatestStartTime(Timestamp latestStartTime) {
        this.latestStartTime = latestStartTime;
    }

    @Basic
    @Column(name = "executeTime", nullable = true, length = 128)
    public String getExecuteTime() {
        return executeTime;
    }

    public void setExecuteTime(String executeTime) {
        this.executeTime = executeTime;
    }

    @Basic
    @Column(name = "tag", nullable = true, length = 255)
    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RenWorkitemEntity that = (RenWorkitemEntity) o;
        return Objects.equals(wid, that.wid) &&
                Objects.equals(rtid, that.rtid) &&
                Objects.equals(processId, that.processId) &&
                Objects.equals(boId, that.boId) &&
                Objects.equals(taskName, that.taskName) &&
                Objects.equals(taskId, that.taskId) &&
                Objects.equals(documentation, that.documentation) &&
                Objects.equals(attributes, that.attributes) &&
                Objects.equals(firingTime, that.firingTime) &&
                Objects.equals(enablementTime, that.enablementTime) &&
                Objects.equals(startTime, that.startTime) &&
                Objects.equals(completionTime, that.completionTime) &&
                Objects.equals(status, that.status) &&
                Objects.equals(resourceStatus, that.resourceStatus) &&
                Objects.equals(startedBy, that.startedBy) &&
                Objects.equals(completedBy, that.completedBy) &&
                Objects.equals(timertrigger, that.timertrigger) &&
                Objects.equals(timerexpiry, that.timerexpiry) &&
                Objects.equals(latestStartTime, that.latestStartTime) &&
                Objects.equals(executeTime, that.executeTime) &&
                Objects.equals(tag, that.tag);
    }

    @Override
    public int hashCode() {

        return Objects.hash(wid, rtid, processId, boId, taskName, taskId, documentation, attributes, firingTime, enablementTime, startTime, completionTime, status, resourceStatus, startedBy, completedBy, timertrigger, timerexpiry, latestStartTime, executeTime, tag);
    }
}
