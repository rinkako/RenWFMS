/*
 * Project Ren @ 2018
 * Rinkako, Ariana, Gordan. SYSU SDCS.
 */
package org.sysu.renNameService.transaction;
import org.sysu.renNameService.entity.RenNsTransactionEntity;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

/**
 * Author: Rinkako
 * Date  : 2018/1/17
 * Usage : All requests sent to Name service engine will be packaged
 *         by this class instance before being processed.
 */
public final class NameServiceTransaction implements Comparable {
    /**
     * Add parameter key value to parameter dictionary.
     *
     * @param key   param key
     * @param value param value
     */
    public synchronized void AddParameter(String key, Object value) {
        this.parameterDictionary.put(key, value);
    }

    /**
     * Add parameter key value to parameter dictionary.
     *
     * @param kvps param key value dictionary
     */
    @SuppressWarnings("unchecked")
    public synchronized void AddParameter(Map kvps) {
        this.parameterDictionary.putAll(kvps);
    }

    /**
     * Get transaction parameter dictionary.
     *
     * @return a HashTable for request parameters.
     */
    public synchronized HashMap<String, Object> getParameterDictionary() {
        return parameterDictionary;
    }

    /**
     * Get transaction persist context.
     *
     * @return {@see RenNsTransactionEntity} instance.
     */
    public RenNsTransactionEntity getTransactionContext() {
        return transactionContext;
    }

    @Override
    public int compareTo(Object arg) {
        NameServiceTransaction otherTrans = (NameServiceTransaction) arg;
        int otherPriority = otherTrans.getTransactionContext().getPriority();
        int myPriority = this.transactionContext.getPriority();
        return Integer.compare(myPriority, otherPriority);
    }

    /**
     * Service request parameters.
     */
    private HashMap<String, Object> parameterDictionary = new HashMap<>();

    /**
     * Transaction context for persistence.
     */
    private RenNsTransactionEntity transactionContext = new RenNsTransactionEntity();
}
