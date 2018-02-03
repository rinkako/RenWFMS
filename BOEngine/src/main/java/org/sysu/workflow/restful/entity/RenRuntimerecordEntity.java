package org.sysu.workflow.restful.entity;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "ren_runtimerecord", schema = "renboengine")
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RenRuntimerecordEntity that = (RenRuntimerecordEntity) o;

        if (rtid != null ? !rtid.equals(that.rtid) : that.rtid != null) return false;
        if (processId != null ? !processId.equals(that.processId) : that.processId != null) return false;
        if (processName != null ? !processName.equals(that.processName) : that.processName != null) return false;
        if (sessionId != null ? !sessionId.equals(that.sessionId) : that.sessionId != null) return false;
        if (launchAuthorityId != null ? !launchAuthorityId.equals(that.launchAuthorityId) : that.launchAuthorityId != null)
            return false;
        if (launchTimestamp != null ? !launchTimestamp.equals(that.launchTimestamp) : that.launchTimestamp != null)
            return false;
        if (launchFrom != null ? !launchFrom.equals(that.launchFrom) : that.launchFrom != null) return false;
        if (launchType != null ? !launchType.equals(that.launchType) : that.launchType != null) return false;
        if (tag != null ? !tag.equals(that.tag) : that.tag != null) return false;
        if (interpreterId != null ? !interpreterId.equals(that.interpreterId) : that.interpreterId != null)
            return false;
        if (resourcingId != null ? !resourcingId.equals(that.resourcingId) : that.resourcingId != null) return false;
        if (resourceBinding != null ? !resourceBinding.equals(that.resourceBinding) : that.resourceBinding != null)
            return false;
        if (resourceBindingType != null ? !resourceBindingType.equals(that.resourceBindingType) : that.resourceBindingType != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = rtid != null ? rtid.hashCode() : 0;
        result = 31 * result + (processId != null ? processId.hashCode() : 0);
        result = 31 * result + (processName != null ? processName.hashCode() : 0);
        result = 31 * result + (sessionId != null ? sessionId.hashCode() : 0);
        result = 31 * result + (launchAuthorityId != null ? launchAuthorityId.hashCode() : 0);
        result = 31 * result + (launchTimestamp != null ? launchTimestamp.hashCode() : 0);
        result = 31 * result + (launchFrom != null ? launchFrom.hashCode() : 0);
        result = 31 * result + (launchType != null ? launchType.hashCode() : 0);
        result = 31 * result + (tag != null ? tag.hashCode() : 0);
        result = 31 * result + (interpreterId != null ? interpreterId.hashCode() : 0);
        result = 31 * result + (resourcingId != null ? resourcingId.hashCode() : 0);
        result = 31 * result + (resourceBinding != null ? resourceBinding.hashCode() : 0);
        result = 31 * result + (resourceBindingType != null ? resourceBindingType.hashCode() : 0);
        return result;
    }
}
