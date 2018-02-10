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
 * Date  : 2018/2/10
 * Usage :
 */
@Entity
@Table(name = "ren_rseventlog", schema = "renboengine", catalog = "")
public class RenRseventlogEntity {
    private String rsevid;
    private String wid;
    private String taskid;
    private String processid;
    private String workerid;
    private String event;
    private Timestamp timestamp;

    @Id
    @Column(name = "rsevid", nullable = false, length = 64)
    public String getRsevid() {
        return rsevid;
    }

    public void setRsevid(String rsevid) {
        this.rsevid = rsevid;
    }

    @Basic
    @Column(name = "wid", nullable = false, length = 64)
    public String getWid() {
        return wid;
    }

    public void setWid(String wid) {
        this.wid = wid;
    }

    @Basic
    @Column(name = "taskid", nullable = false, length = 64)
    public String getTaskid() {
        return taskid;
    }

    public void setTaskid(String taskid) {
        this.taskid = taskid;
    }

    @Basic
    @Column(name = "processid", nullable = false, length = 64)
    public String getProcessid() {
        return processid;
    }

    public void setProcessid(String processid) {
        this.processid = processid;
    }

    @Basic
    @Column(name = "workerid", nullable = false, length = 64)
    public String getWorkerid() {
        return workerid;
    }

    public void setWorkerid(String workerid) {
        this.workerid = workerid;
    }

    @Basic
    @Column(name = "event", nullable = false, length = 32)
    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    @Basic
    @Column(name = "timestamp", nullable = false)
    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RenRseventlogEntity that = (RenRseventlogEntity) o;
        return Objects.equals(rsevid, that.rsevid) &&
                Objects.equals(wid, that.wid) &&
                Objects.equals(taskid, that.taskid) &&
                Objects.equals(processid, that.processid) &&
                Objects.equals(workerid, that.workerid) &&
                Objects.equals(event, that.event) &&
                Objects.equals(timestamp, that.timestamp);
    }

    @Override
    public int hashCode() {

        return Objects.hash(rsevid, wid, taskid, processid, workerid, event, timestamp);
    }
}
