/*
 * Project Ren @ 2018
 * Rinkako, Ariana, Gordan. SYSU SDCS.
 */
package org.sysu.renResourcing.context.steady;

import javax.persistence.*;
import java.util.Objects;

/**
 * Author: Rinkako
 * Date  : 2018/2/4
 * Usage :
 */
@Entity
@Table(name = "ren_queueitems", schema = "renboengine", catalog = "")
public class RenQueueitemsEntity {
    private String qwid;
    private String workqueueId;
    private String workitemId;

    @Id
    @Column(name = "qwid", nullable = false, length = 64)
    public String getQwid() {
        return qwid;
    }

    public void setQwid(String qwid) {
        this.qwid = qwid;
    }

    @Basic
    @Column(name = "workqueueId", nullable = false, length = 64)
    public String getWorkqueueId() {
        return workqueueId;
    }

    public void setWorkqueueId(String workqueueId) {
        this.workqueueId = workqueueId;
    }

    @Basic
    @Column(name = "workitemId", nullable = false, length = 64)
    public String getWorkitemId() {
        return workitemId;
    }

    public void setWorkitemId(String workitemId) {
        this.workitemId = workitemId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RenQueueitemsEntity that = (RenQueueitemsEntity) o;
        return Objects.equals(qwid, that.qwid) &&
                Objects.equals(workqueueId, that.workqueueId) &&
                Objects.equals(workitemId, that.workitemId);
    }

    @Override
    public int hashCode() {

        return Objects.hash(qwid, workqueueId, workitemId);
    }
}
