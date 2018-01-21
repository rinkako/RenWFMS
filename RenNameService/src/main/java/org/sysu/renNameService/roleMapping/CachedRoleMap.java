/*
 * Project Ren @ 2018
 * Rinkako, Ariana, Gordan. SYSU SDCS.
 */
package org.sysu.renNameService.roleMapping;
import org.sysu.renNameService.entity.RenRolemapEntity;
import java.util.ArrayList;

/**
 * Author: Rinkako
 * Date  : 2018/1/16
 * Usage : This class is used to store business role maps of one process in a specific runtime.
 */
final class CachedRoleMap {
    /**
     * Cache for organization system data version.
     */
    private String organDataVersion;

    /**
     * Cache role map item storing list.
     */
    private ArrayList<RenRolemapEntity> cacheList = new ArrayList<>();

    // Cache operation methods

    /**
     * Add cache item to the cache map.
     * @param entity RenRolemapEntity instance to be cached
     */
    public void addCacheItem(RenRolemapEntity entity) {
        this.cacheList.add(entity);
    }

    /**
     * Get the cached role maps.
     * @return ArrayList of cached RenRolemapEntity instances
     */
    public ArrayList<RenRolemapEntity> getList() {
        return this.cacheList;
    }

    // Getters and Setters

    public String getOrganDataVersion() {
        return organDataVersion;
    }

    public void setOrganDataVersion(String organDataVersion) {
        this.organDataVersion = organDataVersion;
    }
}
