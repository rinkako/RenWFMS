/*
 * Project Ren @ 2018
 * Rinkako, Ariana, Gordan. SYSU SDCS.
 */
package org.sysu.renResourcing.cache;

import org.sysu.renResourcing.context.WorkQueueContainer;

import javax.validation.constraints.NotNull;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Author: Rinkako
 * Date  : 2018/2/8
 * Usage : Cache pool for work queue container.
 */
public class WorkQueueContainerPool {
    /**
     * Cache pool map in pattern (workerId, workQueueContainer)
     */
    private static ConcurrentHashMap<String, WorkQueueContainer> cachePool = new ConcurrentHashMap<>();

    /**
     * Add a container to cache.
     * @param workerId worker global id
     * @param container container instance
     */
    public static void AddOrUpdate(@NotNull String workerId, @NotNull WorkQueueContainer container) {
        WorkQueueContainerPool.cachePool.put(workerId, container);
    }

    /**
     * Remove a cache.
     * @param workerId worker global id
     */
    public static void Remove(@NotNull String workerId) {
        WorkQueueContainerPool.cachePool.remove(workerId);
    }

    /**
     * Retrieve a cached container.
     * @param workerId worker global id to get cache
     */
    public static WorkQueueContainer Retrieve(@NotNull String workerId) {
        return WorkQueueContainerPool.cachePool.get(workerId);
    }

    /**
     * Clear the whole cache pool.
     */
    public static void Clear() {
        WorkQueueContainerPool.cachePool.clear();
    }
}
