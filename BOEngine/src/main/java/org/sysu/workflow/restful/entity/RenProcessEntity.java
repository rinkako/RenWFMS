package org.sysu.workflow.restful.entity;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "ren_process", schema = "renboengine")
public class RenProcessEntity {
    private String pid;
    private String processName;
    private String mainBo;
    private String creatorRenid;
    private Timestamp createTimestamp;
    private Integer launchCount;
    private Integer successCount;
    private Timestamp lastLaunchTimestamp;
    private Long averageCost;
    private int state;

    @Id
    @Column(name = "pid", nullable = false, length = 64)
    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    @Basic
    @Column(name = "process_name", nullable = false, length = -1)
    public String getProcessName() {
        return processName;
    }

    public void setProcessName(String processName) {
        this.processName = processName;
    }

    @Basic
    @Column(name = "main_bo", nullable = false, length = 64)
    public String getMainBo() {
        return mainBo;
    }

    public void setMainBo(String mainBo) {
        this.mainBo = mainBo;
    }

    @Basic
    @Column(name = "creator_renid", nullable = false, length = -1)
    public String getCreatorRenid() {
        return creatorRenid;
    }

    public void setCreatorRenid(String creatorRenid) {
        this.creatorRenid = creatorRenid;
    }

    @Basic
    @Column(name = "create_timestamp", nullable = true)
    public Timestamp getCreateTimestamp() {
        return createTimestamp;
    }

    public void setCreateTimestamp(Timestamp createTimestamp) {
        this.createTimestamp = createTimestamp;
    }

    @Basic
    @Column(name = "launch_count", nullable = false)
    public Integer getLaunchCount() {
        return launchCount;
    }

    public void setLaunchCount(Integer launchCount) {
        this.launchCount = launchCount;
    }

    @Basic
    @Column(name = "success_count", nullable = false)
    public Integer getSuccessCount() {
        return successCount;
    }

    public void setSuccessCount(Integer successCount) {
        this.successCount = successCount;
    }

    @Basic
    @Column(name = "last_launch_timestamp", nullable = true)
    public Timestamp getLastLaunchTimestamp() {
        return lastLaunchTimestamp;
    }

    public void setLastLaunchTimestamp(Timestamp lastLaunchTimestamp) {
        this.lastLaunchTimestamp = lastLaunchTimestamp;
    }

    @Basic
    @Column(name = "average_cost", nullable = false)
    public Long getAverageCost() {
        return averageCost;
    }

    public void setAverageCost(Long averageCost) {
        this.averageCost = averageCost;
    }

    @Basic
    @Column(name = "state", nullable = false)
    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RenProcessEntity that = (RenProcessEntity) o;

        if (state != that.state) return false;
        if (pid != null ? !pid.equals(that.pid) : that.pid != null) return false;
        if (processName != null ? !processName.equals(that.processName) : that.processName != null) return false;
        if (mainBo != null ? !mainBo.equals(that.mainBo) : that.mainBo != null) return false;
        if (creatorRenid != null ? !creatorRenid.equals(that.creatorRenid) : that.creatorRenid != null) return false;
        if (createTimestamp != null ? !createTimestamp.equals(that.createTimestamp) : that.createTimestamp != null)
            return false;
        if (launchCount != null ? !launchCount.equals(that.launchCount) : that.launchCount != null) return false;
        if (successCount != null ? !successCount.equals(that.successCount) : that.successCount != null) return false;
        if (lastLaunchTimestamp != null ? !lastLaunchTimestamp.equals(that.lastLaunchTimestamp) : that.lastLaunchTimestamp != null)
            return false;
        if (averageCost != null ? !averageCost.equals(that.averageCost) : that.averageCost != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = pid != null ? pid.hashCode() : 0;
        result = 31 * result + (processName != null ? processName.hashCode() : 0);
        result = 31 * result + (mainBo != null ? mainBo.hashCode() : 0);
        result = 31 * result + (creatorRenid != null ? creatorRenid.hashCode() : 0);
        result = 31 * result + (createTimestamp != null ? createTimestamp.hashCode() : 0);
        result = 31 * result + (launchCount != null ? launchCount.hashCode() : 0);
        result = 31 * result + (successCount != null ? successCount.hashCode() : 0);
        result = 31 * result + (lastLaunchTimestamp != null ? lastLaunchTimestamp.hashCode() : 0);
        result = 31 * result + (averageCost != null ? averageCost.hashCode() : 0);
        result = 31 * result + state;
        return result;
    }
}
