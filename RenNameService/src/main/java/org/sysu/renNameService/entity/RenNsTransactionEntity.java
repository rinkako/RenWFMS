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
 * Date  : 2018/1/24
 * Usage :
 */
@Entity
@Table(name = "ren_ns_transaction", schema = "rennameservice", catalog = "")
public class RenNsTransactionEntity {
    private String nsid;
    private Integer type;
    private String requestInvoker;
    private Timestamp acceptTimestamp;
    private Timestamp scheduledTimestamp;
    private String context;
    private Timestamp finishTimestamp;

    @Id
    @Column(name = "nsid", nullable = false, length = 64)
    public String getNsid() {
        return nsid;
    }

    public void setNsid(String nsid) {
        this.nsid = nsid;
    }

    @Basic
    @Column(name = "type", nullable = true)
    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    @Basic
    @Column(name = "requestInvoker", nullable = true, length = 64)
    public String getRequestInvoker() {
        return requestInvoker;
    }

    public void setRequestInvoker(String requestInvoker) {
        this.requestInvoker = requestInvoker;
    }

    @Basic
    @Column(name = "acceptTimestamp", nullable = true)
    public Timestamp getAcceptTimestamp() {
        return acceptTimestamp;
    }

    public void setAcceptTimestamp(Timestamp acceptTimestamp) {
        this.acceptTimestamp = acceptTimestamp;
    }

    @Basic
    @Column(name = "scheduledTimestamp", nullable = true)
    public Timestamp getScheduledTimestamp() {
        return scheduledTimestamp;
    }

    public void setScheduledTimestamp(Timestamp scheduledTimestamp) {
        this.scheduledTimestamp = scheduledTimestamp;
    }

    @Basic
    @Column(name = "context", nullable = true, length = -1)
    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    @Basic
    @Column(name = "finishTimestamp", nullable = true)
    public Timestamp getFinishTimestamp() {
        return finishTimestamp;
    }

    public void setFinishTimestamp(Timestamp finishTimestamp) {
        this.finishTimestamp = finishTimestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RenNsTransactionEntity that = (RenNsTransactionEntity) o;
        return Objects.equals(nsid, that.nsid) &&
                Objects.equals(type, that.type) &&
                Objects.equals(requestInvoker, that.requestInvoker) &&
                Objects.equals(acceptTimestamp, that.acceptTimestamp) &&
                Objects.equals(scheduledTimestamp, that.scheduledTimestamp) &&
                Objects.equals(context, that.context);
    }

    @Override
    public int hashCode() {

        return Objects.hash(nsid, type, requestInvoker, acceptTimestamp, scheduledTimestamp, context);
    }
}
