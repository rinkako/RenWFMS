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
 * Date  : 2018/3/5
 * Usage :
 */
@Entity
@Table(name = "ren_workitem", schema = "renboengine", catalog = "")
public class RenWorkitemEntity {
    private String wid;
    private String rtid;
    private String resourcingId;
    private String processId;
    private String boId;
    private String taskid;
    private String taskPolymorphismId;
    private String arguments;
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
    private long executeTime;
    private String callbackNodeId;

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
    @Column(name = "resourcing_id", nullable = false, length = 64)
    public String getResourcingId() {
        return resourcingId;
    }

    public void setResourcingId(String resourcingId) {
        this.resourcingId = resourcingId;
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
    @Column(name = "taskid", nullable = false, length = -1)
    public String getTaskid() {
        return taskid;
    }

    public void setTaskid(String taskid) {
        this.taskid = taskid;
    }

    @Basic
    @Column(name = "taskPolymorphismId", nullable = false, length = -1)
    public String getTaskPolymorphismId() {
        return taskPolymorphismId;
    }

    public void setTaskPolymorphismId(String taskPolymorphismId) {
        this.taskPolymorphismId = taskPolymorphismId;
    }

    @Basic
    @Column(name = "arguments", nullable = true, length = -1)
    public String getArguments() {
        return arguments;
    }

    public void setArguments(String arguments) {
        this.arguments = arguments;
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
    @Column(name = "executeTime", nullable = false)
    public long getExecuteTime() {
        return executeTime;
    }

    public void setExecuteTime(long executeTime) {
        this.executeTime = executeTime;
    }

    @Basic
    @Column(name = "callbackNodeId", nullable = false, length = 64)
    public String getCallbackNodeId() {
        return callbackNodeId;
    }

    public void setCallbackNodeId(String callbackNodeId) {
        this.callbackNodeId = callbackNodeId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RenWorkitemEntity that = (RenWorkitemEntity) o;
        return executeTime == that.executeTime &&
                Objects.equals(wid, that.wid) &&
                Objects.equals(rtid, that.rtid) &&
                Objects.equals(resourcingId, that.resourcingId) &&
                Objects.equals(processId, that.processId) &&
                Objects.equals(boId, that.boId) &&
                Objects.equals(taskid, that.taskid) &&
                Objects.equals(taskPolymorphismId, that.taskPolymorphismId) &&
                Objects.equals(arguments, that.arguments) &&
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
                Objects.equals(callbackNodeId, that.callbackNodeId);
    }

    @Override
    public int hashCode() {

        return Objects.hash(wid, rtid, resourcingId, processId, boId, taskid, taskPolymorphismId, arguments, firingTime, enablementTime, startTime, completionTime, status, resourceStatus, startedBy, completedBy, timertrigger, timerexpiry, latestStartTime, executeTime, callbackNodeId);
    }
}
