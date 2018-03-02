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
 * Date  : 2018/3/2
 * Usage :
 */
@Entity
@Table(name = "ren_exitem", schema = "renboengine", catalog = "")
public class RenExitemEntity {
    private String workitemId;
    private String rtid;
    private int status;
    private int visibility;
    private String handlerAuthName;
    private Timestamp timestamp;
    private String reason;

    @Id
    @Column(name = "workitemId")
    public String getWorkitemId() {
        return workitemId;
    }

    public void setWorkitemId(String workitemId) {
        this.workitemId = workitemId;
    }

    @Basic
    @Column(name = "rtid")
    public String getRtid() {
        return rtid;
    }

    public void setRtid(String rtid) {
        this.rtid = rtid;
    }

    @Basic
    @Column(name = "status")
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Basic
    @Column(name = "visibility")
    public int getVisibility() {
        return visibility;
    }

    public void setVisibility(int visibility) {
        this.visibility = visibility;
    }

    @Basic
    @Column(name = "handlerAuthName")
    public String getHandlerAuthName() {
        return handlerAuthName;
    }

    public void setHandlerAuthName(String handlerAuthName) {
        this.handlerAuthName = handlerAuthName;
    }

    @Basic
    @Column(name = "timestamp")
    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    @Basic
    @Column(name = "reason")
    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RenExitemEntity that = (RenExitemEntity) o;
        return status == that.status &&
                visibility == that.visibility &&
                Objects.equals(workitemId, that.workitemId) &&
                Objects.equals(rtid, that.rtid) &&
                Objects.equals(handlerAuthName, that.handlerAuthName) &&
                Objects.equals(timestamp, that.timestamp) &&
                Objects.equals(reason, that.reason);
    }

    @Override
    public int hashCode() {

        return Objects.hash(workitemId, rtid, status, visibility, handlerAuthName, timestamp, reason);
    }
}
