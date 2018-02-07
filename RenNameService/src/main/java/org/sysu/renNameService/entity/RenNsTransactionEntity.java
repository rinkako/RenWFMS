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
 * Date  : 2018/2/7
 * Usage :
 */
@Entity
@Table(name = "ren_ns_transaction", schema = "renboengine", catalog = "")
public class RenNsTransactionEntity {
    private String nsid;
    private Integer type;
    private String rtid;
    private int priority;
    private String context;
    private Timestamp acceptTimestamp;
    private Timestamp finishTimestamp;
    private String requestInvoker;
    private Timestamp scheduledTimestamp;

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
    @Column(name = "rtid", nullable = true, length = 64)
    public String getRtid() {
        return rtid;
    }

    public void setRtid(String rtid) {
        this.rtid = rtid;
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
    @Column(name = "context", nullable = true, length = -1)
    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    @Basic
    @Column(name = "accept_timestamp", nullable = true)
    public Timestamp getAcceptTimestamp() {
        return acceptTimestamp;
    }

    public void setAcceptTimestamp(Timestamp acceptTimestamp) {
        this.acceptTimestamp = acceptTimestamp;
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
    @Column(name = "request_invoker", nullable = true, length = 64)
    public String getRequestInvoker() {
        return requestInvoker;
    }

    public void setRequestInvoker(String requestInvoker) {
        this.requestInvoker = requestInvoker;
    }

    @Basic
    @Column(name = "scheduled_timestamp", nullable = true)
    public Timestamp getScheduledTimestamp() {
        return scheduledTimestamp;
    }

    public void setScheduledTimestamp(Timestamp scheduledTimestamp) {
        this.scheduledTimestamp = scheduledTimestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RenNsTransactionEntity that = (RenNsTransactionEntity) o;
        return priority == that.priority &&
                Objects.equals(nsid, that.nsid) &&
                Objects.equals(type, that.type) &&
                Objects.equals(rtid, that.rtid) &&
                Objects.equals(context, that.context) &&
                Objects.equals(acceptTimestamp, that.acceptTimestamp) &&
                Objects.equals(finishTimestamp, that.finishTimestamp) &&
                Objects.equals(requestInvoker, that.requestInvoker) &&
                Objects.equals(scheduledTimestamp, that.scheduledTimestamp);
    }

    @Override
    public int hashCode() {

        return Objects.hash(nsid, type, rtid, priority, context, acceptTimestamp, finishTimestamp, requestInvoker, scheduledTimestamp);
    }
}
