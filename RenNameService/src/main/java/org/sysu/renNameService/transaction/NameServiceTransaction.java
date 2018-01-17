package org.sysu.renNameService.transaction;

import java.util.Date;

/**
 * Transaction data package
 * All requests sent to Name service engine will be packaged
 * by this class instance before being processed.
 */
public class NameServiceTransaction {
    /**
     * Transaction unique id.
     */
    private String transactionId;

    /**
     * Transaction type.
     */
    private TransactionType type;

    /**
     * Transaction create timestamp.
     */
    private Date acceptTimestamp;

    /**
     * Transaction begin to process timestamp.
     */
    private Date scheduledTimestamp;

    /**
     * Service context of this transaction.
     */
    private Object entityContext;

    // Getters and Setters

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public TransactionType getType() {
        return type;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }

    public Date getAcceptTimestamp() {
        return acceptTimestamp;
    }

    public void setAcceptTimestamp(Date acceptTimestamp) {
        this.acceptTimestamp = acceptTimestamp;
    }

    public Date getScheduledTimestamp() {
        return scheduledTimestamp;
    }

    public void setScheduledTimestamp(Date scheduledTimestamp) {
        this.scheduledTimestamp = scheduledTimestamp;
    }

    public Object getEntityContext() {
        return entityContext;
    }

    public void setEntityContext(Object entityContext) {
        this.entityContext = entityContext;
    }
}
