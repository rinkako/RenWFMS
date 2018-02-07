/*
 * Project Ren @ 2018
 * Rinkako, Ariana, Gordan. SYSU SDCS.
 */
package org.sysu.renResourcing.context.steady;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Objects;

/**
 * Author: Rinkako
 * Date  : 2018/2/8
 * Usage :
 */
public class RenQueueitemsEntityPK implements Serializable {
    private String workqueueId;
    private String workitemId;

    @Column(name = "workqueueId", nullable = false, length = 64)
    @Id
    public String getWorkqueueId() {
        return workqueueId;
    }

    public void setWorkqueueId(String workqueueId) {
        this.workqueueId = workqueueId;
    }

    @Column(name = "workitemId", nullable = false, length = 64)
    @Id
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
        RenQueueitemsEntityPK that = (RenQueueitemsEntityPK) o;
        return Objects.equals(workqueueId, that.workqueueId) &&
                Objects.equals(workitemId, that.workitemId);
    }

    @Override
    public int hashCode() {

        return Objects.hash(workqueueId, workitemId);
    }
}
