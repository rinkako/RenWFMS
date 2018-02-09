/*
 * Project Ren @ 2018
 * Rinkako, Ariana, Gordan. SYSU SDCS.
 */
package org.sysu.renResourcing.consistency;

import org.sysu.renResourcing.context.RCacheablesContext;

import javax.validation.constraints.NotNull;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Author: Rinkako
 * Date  : 2018/2/8
 * Usage : This class is a cache pool for all running processes resources
 *         context. Notice that all cache will lost after RS service shut
 *         down, therefore all data stored in the cache should be flushed
 *         into steady memory, or discard as garbage.
 *         For the consistency, different RS micro-service maintains its
 *         own cache and a running process only request resource service
 *         from ONE specific RS micro-service instance. In a special case
 *         caches may have inconsistency situation: a process run on a RS
 *         before complete but that RS is becoming overloaded, BO Engine
 *         will send next resourcing request to another RS instance which
 *         may have faults when it try to get any involved context. Hence
 *         we appoint that: 1.resourcing requests of one specific time of
 *         running process is always sent to a specific RS if RS is not
 *         overloaded; 2.when a RS overloaded and new RS micro-service is
 *         launched, resourcing requests from some running process will
 *         be sent to new RS instance which NEVER handle any request for
 *         this running process. This appointment can make sure that one
 *         running process always have ONLY one valid cache.
 */
public class ContextCachePool {

    /**
     * Cache pool for cacheables.
     */
    private static ConcurrentHashMap<String, RCacheablesContext> cachePool = new ConcurrentHashMap<>();

    /**
     * Add or update a context cache.
     * @param idKey cache fetch key, unique for a type of context object
     * @param cacheObj cache context
     */
    public static void AddOrUpdate(@NotNull String idKey, @NotNull RCacheablesContext cacheObj) {
        String cacheKey = ContextCachePool.GenerateCacheKey(cacheObj.getClass(), idKey);
        ContextCachePool.cachePool.put(cacheKey, cacheObj);
    }

    /**
     * Remove a cache.
     * @param clazz context class
     * @param idKey cache fetch key, unique for a type of context object
     */
    public static void Remove(@NotNull Class<?> clazz, @NotNull String idKey) {
        String cacheKey = ContextCachePool.GenerateCacheKey(clazz, idKey);
        ContextCachePool.cachePool.remove(cacheKey);
    }

    /**
     * Retrieve a cached container.
     * @param clazz context class
     * @param idKey cache fetch key, unique for a type of context object
     * @param <Ty> type of context
     * @return result cached context
     */
    @SuppressWarnings("unchecked")
    public static <Ty> Ty Retrieve(@NotNull Class<Ty> clazz, @NotNull String idKey) {
        String cacheKey = ContextCachePool.GenerateCacheKey(clazz, idKey);
        return (Ty) ContextCachePool.cachePool.get(cacheKey);
    }

    /**
     * Clear the whole cache pool.
     */
    public static void Clear() {
        ContextCachePool.cachePool.clear();
    }

    /**
     * Clear a specific type of contexts in cache.
     * @param clazz context class
     */
    public static synchronized void Clear(@NotNull Class<?> clazz) {
        ConcurrentHashMap<String, RCacheablesContext> cloned = new ConcurrentHashMap<>(ContextCachePool.cachePool);
        for (Map.Entry<String, RCacheablesContext> kvp : cloned.entrySet()) {
            if (clazz.isInstance(kvp.getValue())) {
                ContextCachePool.cachePool.remove(kvp.getKey());
            }
        }
    }

    /**
     * Get a key string for a context id.
     * @param originalId context id
     * @return cache id
     */
    private static String GenerateCacheKey(Class<?> clazz, String originalId) {
        return String.format("%s@@%s", clazz.getSimpleName(), originalId);
    }

}
