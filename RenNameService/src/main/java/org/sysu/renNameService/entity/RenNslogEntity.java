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
 * Date  : 2018/2/1
 * Usage :
 */
@Entity
@Table(name = "ren_nslog", schema = "renboengine", catalog = "")
public class RenNslogEntity {
    private String logid;
    private String label;
    private String level;
    private String message;
    private Timestamp timestamp;
    private String rtid;

    @Id
    @Column(name = "logid", nullable = false, length = 64)
    public String getLogid() {
        return logid;
    }

    public void setLogid(String logid) {
        this.logid = logid;
    }

    @Basic
    @Column(name = "label", nullable = true, length = 64)
    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    @Basic
    @Column(name = "level", nullable = true, length = 16)
    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    @Basic
    @Column(name = "message", nullable = true, length = -1)
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Basic
    @Column(name = "timestamp", nullable = true)
    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    @Basic
    @Column(name = "rtid", nullable = true, length = 64)
    public String getRtid() {
        return rtid;
    }

    public void setRtid(String rtid) {
        this.rtid = rtid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RenNslogEntity that = (RenNslogEntity) o;
        return Objects.equals(logid, that.logid) &&
                Objects.equals(label, that.label) &&
                Objects.equals(level, that.level) &&
                Objects.equals(message, that.message) &&
                Objects.equals(timestamp, that.timestamp) &&
                Objects.equals(rtid, that.rtid);
    }

    @Override
    public int hashCode() {

        return Objects.hash(logid, label, level, message, timestamp, rtid);
    }
}
