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
@Table(name = "ren_workqueue", schema = "renboengine", catalog = "")
public class RenWorkqueueEntity {
    private String queueId;
    private String ownerId;
    private int type;

    @Id
    @Column(name = "queueId", nullable = false, length = 64)
    public String getQueueId() {
        return queueId;
    }

    public void setQueueId(String queueId) {
        this.queueId = queueId;
    }

    @Basic
    @Column(name = "ownerId", nullable = true, length = 64)
    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    @Basic
    @Column(name = "type", nullable = false)
    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RenWorkqueueEntity that = (RenWorkqueueEntity) o;
        return type == that.type &&
                Objects.equals(queueId, that.queueId) &&
                Objects.equals(ownerId, that.ownerId);
    }

    @Override
    public int hashCode() {

        return Objects.hash(queueId, ownerId, type);
    }
}
