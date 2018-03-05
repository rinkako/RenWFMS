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
@Table(name = "ren_runtimerecord", schema = "renboengine", catalog = "")
public class RenRuntimerecordEntity {
    private String rtid;
    private String processId;
    private String processName;
    private String sessionId;
    private String launchAuthorityId;
    private Timestamp launchTimestamp;
    private String launchFrom;
    private Integer launchType;
    private String tag;
    private String interpreterId;
    private String resourcingId;
    private String resourceBinding;
    private Integer resourceBindingType;
    private Integer failureType;
    private String participantCache;
    private Timestamp finishTimestamp;
    private int isSucceed;

    @Id
    @Column(name = "rtid", nullable = false, length = 64)
    public String getRtid() {
        return rtid;
    }

    public void setRtid(String rtid) {
        this.rtid = rtid;
    }

    @Basic
    @Column(name = "process_id", nullable = true, length = 64)
    public String getProcessId() {
        return processId;
    }

    public void setProcessId(String processId) {
        this.processId = processId;
    }

    @Basic
    @Column(name = "process_name", nullable = true, length = -1)
    public String getProcessName() {
        return processName;
    }

    public void setProcessName(String processName) {
        this.processName = processName;
    }

    @Basic
    @Column(name = "session_id", nullable = true, length = 64)
    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    @Basic
    @Column(name = "launch_authority_id", nullable = true, length = 64)
    public String getLaunchAuthorityId() {
        return launchAuthorityId;
    }

    public void setLaunchAuthorityId(String launchAuthorityId) {
        this.launchAuthorityId = launchAuthorityId;
    }

    @Basic
    @Column(name = "launch_timestamp", nullable = true)
    public Timestamp getLaunchTimestamp() {
        return launchTimestamp;
    }

    public void setLaunchTimestamp(Timestamp launchTimestamp) {
        this.launchTimestamp = launchTimestamp;
    }

    @Basic
    @Column(name = "launch_from", nullable = true, length = -1)
    public String getLaunchFrom() {
        return launchFrom;
    }

    public void setLaunchFrom(String launchFrom) {
        this.launchFrom = launchFrom;
    }

    @Basic
    @Column(name = "launch_type", nullable = true)
    public Integer getLaunchType() {
        return launchType;
    }

    public void setLaunchType(Integer launchType) {
        this.launchType = launchType;
    }

    @Basic
    @Column(name = "tag", nullable = true, length = -1)
    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    @Basic
    @Column(name = "interpreter_id", nullable = true, length = 64)
    public String getInterpreterId() {
        return interpreterId;
    }

    public void setInterpreterId(String interpreterId) {
        this.interpreterId = interpreterId;
    }

    @Basic
    @Column(name = "resourcing_id", nullable = true, length = 64)
    public String getResourcingId() {
        return resourcingId;
    }

    public void setResourcingId(String resourcingId) {
        this.resourcingId = resourcingId;
    }

    @Basic
    @Column(name = "resource_binding", nullable = true, length = -1)
    public String getResourceBinding() {
        return resourceBinding;
    }

    public void setResourceBinding(String resourceBinding) {
        this.resourceBinding = resourceBinding;
    }

    @Basic
    @Column(name = "resource_binding_type", nullable = true)
    public Integer getResourceBindingType() {
        return resourceBindingType;
    }

    public void setResourceBindingType(Integer resourceBindingType) {
        this.resourceBindingType = resourceBindingType;
    }

    @Basic
    @Column(name = "failure_type", nullable = true)
    public Integer getFailureType() {
        return failureType;
    }

    public void setFailureType(Integer failureType) {
        this.failureType = failureType;
    }

    @Basic
    @Column(name = "participant_cache", nullable = true, length = -1)
    public String getParticipantCache() {
        return participantCache;
    }

    public void setParticipantCache(String participantCache) {
        this.participantCache = participantCache;
    }

    @Basic
    @Column(name = "finish_timestamp", nullable = true)
    public Timestamp getFinishTimestamp() {
        return finishTimestamp;
    }

    public void setFinishTimestamp(Timestamp finishTimestamp) {
        this.finishTimestamp = finishTimestamp;
    }

    @Basic
    @Column(name = "is_succeed", nullable = false)
    public int getIsSucceed() {
        return isSucceed;
    }

    public void setIsSucceed(int isSucceed) {
        this.isSucceed = isSucceed;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RenRuntimerecordEntity that = (RenRuntimerecordEntity) o;
        return isSucceed == that.isSucceed &&
                Objects.equals(rtid, that.rtid) &&
                Objects.equals(processId, that.processId) &&
                Objects.equals(processName, that.processName) &&
                Objects.equals(sessionId, that.sessionId) &&
                Objects.equals(launchAuthorityId, that.launchAuthorityId) &&
                Objects.equals(launchTimestamp, that.launchTimestamp) &&
                Objects.equals(launchFrom, that.launchFrom) &&
                Objects.equals(launchType, that.launchType) &&
                Objects.equals(tag, that.tag) &&
                Objects.equals(interpreterId, that.interpreterId) &&
                Objects.equals(resourcingId, that.resourcingId) &&
                Objects.equals(resourceBinding, that.resourceBinding) &&
                Objects.equals(resourceBindingType, that.resourceBindingType) &&
                Objects.equals(failureType, that.failureType) &&
                Objects.equals(participantCache, that.participantCache) &&
                Objects.equals(finishTimestamp, that.finishTimestamp);
    }

    @Override
    public int hashCode() {

        return Objects.hash(rtid, processId, processName, sessionId, launchAuthorityId, launchTimestamp, launchFrom, launchType, tag, interpreterId, resourcingId, resourceBinding, resourceBindingType, failureType, participantCache, finishTimestamp, isSucceed);
    }
}
