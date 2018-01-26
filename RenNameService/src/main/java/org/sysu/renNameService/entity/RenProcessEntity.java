/*
 * Project Ren @ 2018
 * Rinkako, Ariana, Gordan. SYSU SDCS.
 */
package org.sysu.renNameService.entity;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

/**
 * Author: Rinkako
 * Date  : 2018/1/26
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
    @Column(name = "launch_count", nullable = true)
    public Integer getLaunchCount() {
        return launchCount;
    }

    public void setLaunchCount(Integer launchCount) {
        this.launchCount = launchCount;
    }

    @Basic
    @Column(name = "success_count", nullable = true)
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
    @Column(name = "average_cost", nullable = true)
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
        return Objects.equals(pid, that.pid) &&
                Objects.equals(processName, that.processName) &&
                Objects.equals(mainBo, that.mainBo) &&
                Objects.equals(creatorRenid, that.creatorRenid) &&
                Objects.equals(createTimestamp, that.createTimestamp) &&
                Objects.equals(launchCount, that.launchCount) &&
                Objects.equals(successCount, that.successCount) &&
                Objects.equals(lastLaunchTimestamp, that.lastLaunchTimestamp) &&
                Objects.equals(averageCost, that.averageCost);
    }

    @Override
    public int hashCode() {

        return Objects.hash(pid, processName, mainBo, creatorRenid, createTimestamp, launchCount, successCount, lastLaunchTimestamp, averageCost);
    }
}
