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
 * Date  : 2018/2/5
 * Usage :
 */
@Entity
@Table(name = "ren_rsrecord", schema = "renboengine", catalog = "")
public class RenRsrecordEntity {
    private String rstid;
    private String rtid;
    private String resourcingId;
    private Timestamp receiveTimestamp;
    private Timestamp scheduledTimestamp;
    private Timestamp finishTimestamp;
    private int priority;
    private String service;
    private String args;

    @Id
    @Column(name = "rstid", nullable = false, length = 64)
    public String getRstid() {
        return rstid;
    }

    public void setRstid(String rstid) {
        this.rstid = rstid;
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
    @Column(name = "receive_timestamp", nullable = true)
    public Timestamp getReceiveTimestamp() {
        return receiveTimestamp;
    }

    public void setReceiveTimestamp(Timestamp receiveTimestamp) {
        this.receiveTimestamp = receiveTimestamp;
    }

    @Basic
    @Column(name = "scheduled_timestamp", nullable = true)
    public Timestamp getScheduledTimestamp() {
        return scheduledTimestamp;
    }

    public void setScheduledTimestamp(Timestamp scheduledTimestamp) {
        this.scheduledTimestamp = scheduledTimestamp;
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
    @Column(name = "priority", nullable = false)
    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    @Basic
    @Column(name = "service", nullable = true, length = -1)
    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    @Basic
    @Column(name = "args", nullable = true, length = -1)
    public String getArgs() {
        return args;
    }

    public void setArgs(String args) {
        this.args = args;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RenRsrecordEntity that = (RenRsrecordEntity) o;
        return priority == that.priority &&
                Objects.equals(rstid, that.rstid) &&
                Objects.equals(rtid, that.rtid) &&
                Objects.equals(resourcingId, that.resourcingId) &&
                Objects.equals(receiveTimestamp, that.receiveTimestamp) &&
                Objects.equals(scheduledTimestamp, that.scheduledTimestamp) &&
                Objects.equals(finishTimestamp, that.finishTimestamp) &&
                Objects.equals(service, that.service) &&
                Objects.equals(args, that.args);
    }

    @Override
    public int hashCode() {

        return Objects.hash(rstid, rtid, resourcingId, receiveTimestamp, scheduledTimestamp, finishTimestamp, priority, service, args);
    }
}
