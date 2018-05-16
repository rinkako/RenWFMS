package org.sysu.workflow.entity;

import javax.persistence.*;
import java.util.Arrays;
import java.util.Objects;

/**
 * Author: Rinkako
 * Date  : 2018/5/16
 * Usage :
 */
@Entity
@Table(name = "ren_binstep", schema = "renboengine", catalog = "")
public class RenBinstepEntity {
    private String nodeId;
    private String rtid;
    private String supervisorId;
    private String notifiableId;
    private byte[] binlog;

    @Id
    @Column(name = "nodeId", nullable = false, length = 64)
    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
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
    @Column(name = "supervisorId", nullable = false, length = 64)
    public String getSupervisorId() {
        return supervisorId;
    }

    public void setSupervisorId(String supervisorId) {
        this.supervisorId = supervisorId;
    }

    @Basic
    @Column(name = "notifiableId", nullable = false, length = -1)
    public String getNotifiableId() {
        return notifiableId;
    }

    public void setNotifiableId(String notifiableId) {
        this.notifiableId = notifiableId;
    }

    @Basic
    @Column(name = "binlog", nullable = true)
    public byte[] getBinlog() {
        return binlog;
    }

    public void setBinlog(byte[] binlog) {
        this.binlog = binlog;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RenBinstepEntity that = (RenBinstepEntity) o;
        return Objects.equals(nodeId, that.nodeId) &&
                Objects.equals(rtid, that.rtid) &&
                Objects.equals(supervisorId, that.supervisorId) &&
                Objects.equals(notifiableId, that.notifiableId) &&
                Arrays.equals(binlog, that.binlog);
    }

    @Override
    public int hashCode() {

        int result = Objects.hash(nodeId, rtid, supervisorId, notifiableId);
        result = 31 * result + Arrays.hashCode(binlog);
        return result;
    }
}
