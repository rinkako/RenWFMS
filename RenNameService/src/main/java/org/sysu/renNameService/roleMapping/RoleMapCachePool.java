/*
 * Project Ren @ 2018
 * Rinkako, Ariana, Gordan. SYSU SDCS.
 */
package org.sysu.renNameService.roleMapping;
import java.util.Hashtable;

/**
 * Author: Rinkako
 * Date  : 2018/1/16
 * Usage : This class is a static class for caching the role maps of running processes.
 *         Only mapping transaction using `RepeatableRead` isolation type will be stored.
 */
final class RoleMapCachePool {
    /**
     * Mapping cache pool.
     */
    private static Hashtable<String, CachedRoleMap> cachePool = new Hashtable<String, CachedRoleMap>();

    /**
     * Add a role map package to the cache.
     * @param rtid process rtid
     * @param roleMap role map package
     * @return Whether replace exist item
     */
    public synchronized static Boolean Add(String rtid, CachedRoleMap roleMap) {
        return RoleMapCachePool.cachePool.put(rtid, roleMap) != null;
    }

    /**
     * Retrieve a cached role map.
     * @param rtid process rtid
     * @return Cached role map package instance, null if not exist
     */
    public synchronized static CachedRoleMap Retrieve(String rtid) {
        return RoleMapCachePool.cachePool.get(rtid);
    }

    /**
     * Remove a cached role map.
     * @param rtid process rtid
     * @return Whether key exist before being removed
     */
    public synchronized static Boolean Remove(String rtid) {
        return RoleMapCachePool.cachePool.remove(rtid) != null;
    }

    /**
     * Remove all cached role maps.
     */
    public synchronized static void Clear() {
        RoleMapCachePool.cachePool.clear();
    }
}
