package org.sysu.workflow.entity;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "ren_process", schema = "renboengine", catalog = "")
public class RenProcessEntity {
    private String pid;
    private String processName;
    private String mainBo;
    private String creatorRenid;
    private Timestamp createTimestamp;
    private int launchCount;
    private int successCount;
    private Timestamp lastLaunchTimestamp;
    private long averageCost;
    private int state;
    private int authtype;
    private String selfsignature;

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
    public int getLaunchCount() {
        return launchCount;
    }

    public void setLaunchCount(int launchCount) {
        this.launchCount = launchCount;
    }

    @Basic
    @Column(name = "success_count", nullable = false)
    public int getSuccessCount() {
        return successCount;
    }

    public void setSuccessCount(int successCount) {
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
    public long getAverageCost() {
        return averageCost;
    }

    public void setAverageCost(long averageCost) {
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

    @Basic
    @Column(name = "authtype", nullable = false)
    public int getAuthtype() {
        return authtype;
    }

    public void setAuthtype(int authtype) {
        this.authtype = authtype;
    }

    @Basic
    @Column(name = "selfsignature", nullable = true, length = -1)
    public String getSelfsignature() {
        return selfsignature;
    }

    public void setSelfsignature(String selfsignature) {
        this.selfsignature = selfsignature;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RenProcessEntity that = (RenProcessEntity) o;

        if (launchCount != that.launchCount) return false;
        if (successCount != that.successCount) return false;
        if (averageCost != that.averageCost) return false;
        if (state != that.state) return false;
        if (authtype != that.authtype) return false;
        if (pid != null ? !pid.equals(that.pid) : that.pid != null) return false;
        if (processName != null ? !processName.equals(that.processName) : that.processName != null) return false;
        if (mainBo != null ? !mainBo.equals(that.mainBo) : that.mainBo != null) return false;
        if (creatorRenid != null ? !creatorRenid.equals(that.creatorRenid) : that.creatorRenid != null) return false;
        if (createTimestamp != null ? !createTimestamp.equals(that.createTimestamp) : that.createTimestamp != null)
            return false;
        if (lastLaunchTimestamp != null ? !lastLaunchTimestamp.equals(that.lastLaunchTimestamp) : that.lastLaunchTimestamp != null)
            return false;
        if (selfsignature != null ? !selfsignature.equals(that.selfsignature) : that.selfsignature != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = pid != null ? pid.hashCode() : 0;
        result = 31 * result + (processName != null ? processName.hashCode() : 0);
        result = 31 * result + (mainBo != null ? mainBo.hashCode() : 0);
        result = 31 * result + (creatorRenid != null ? creatorRenid.hashCode() : 0);
        result = 31 * result + (createTimestamp != null ? createTimestamp.hashCode() : 0);
        result = 31 * result + launchCount;
        result = 31 * result + successCount;
        result = 31 * result + (lastLaunchTimestamp != null ? lastLaunchTimestamp.hashCode() : 0);
        result = 31 * result + (int) (averageCost ^ (averageCost >>> 32));
        result = 31 * result + state;
        result = 31 * result + authtype;
        result = 31 * result + (selfsignature != null ? selfsignature.hashCode() : 0);
        return result;
    }
}
