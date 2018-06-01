package org.sysu.renResourcing.context.steady;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

/**
 * Author: Rinkako
 * Date  : 2018/6/1
 * Usage :
 */
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
        return launchCount == that.launchCount &&
                successCount == that.successCount &&
                averageCost == that.averageCost &&
                state == that.state &&
                authtype == that.authtype &&
                Objects.equals(pid, that.pid) &&
                Objects.equals(processName, that.processName) &&
                Objects.equals(mainBo, that.mainBo) &&
                Objects.equals(creatorRenid, that.creatorRenid) &&
                Objects.equals(createTimestamp, that.createTimestamp) &&
                Objects.equals(lastLaunchTimestamp, that.lastLaunchTimestamp) &&
                Objects.equals(selfsignature, that.selfsignature);
    }

    @Override
    public int hashCode() {

        return Objects.hash(pid, processName, mainBo, creatorRenid, createTimestamp, launchCount, successCount, lastLaunchTimestamp, averageCost, state, authtype, selfsignature);
    }
}
