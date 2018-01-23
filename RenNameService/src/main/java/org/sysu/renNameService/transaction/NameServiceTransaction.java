/*
 * Project Ren @ 2018
 * Rinkako, Ariana, Gordan. SYSU SDCS.
 */
package org.sysu.renNameService.transaction;
import org.sysu.renNameService.entity.RenNsTransactionEntity;
import java.util.Hashtable;

/**
 * Author: Rinkako
 * Date  : 2018/1/17
 * Usage : All requests sent to Name service engine will be packaged
 *         by this class instance before being processed.
 */
public final class NameServiceTransaction {

    /**
     * Add parameter key value to parameter dictionary.
     * @param key param key
     * @param value param value
     */
    public void AddParameter(String key, Object value) {
        this.parameterDictionary.put(key, value);
    }

    /**
     * Get transaction parameter dictionary.
     * @return a HashTable for request parameters.
     */
    public Hashtable<String, Object> getParameterDictionary() {
        return parameterDictionary;
    }

    /**
     * Get transaction persist context.
     * @return {@see RenNsTransactionEntity} instance.
     */
    public RenNsTransactionEntity getTransactionContext() {
        return transactionContext;
    }

    /**
     * Service request parameters.
     */
    private Hashtable<String, Object> parameterDictionary = new Hashtable<>();

    /**
     * Transaction context for persistence.
     */
    private RenNsTransactionEntity transactionContext = new RenNsTransactionEntity();
}
