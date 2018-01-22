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
    public ArrayList<RenRolemapEntity> getCacheList() {
        return this.cacheList;
    }

    /**
     * Get the cached specific business role maps.
     * @param bRole business role name
     * @return ArrayList of cached RenRolemapEntity instances with a specific business role
     */
    public ArrayList<RenRolemapEntity> getCacheListByBRole(String bRole) {
        ArrayList<RenRolemapEntity> retList = new ArrayList<>();
        for (RenRolemapEntity rre : this.cacheList) {
            if (rre.getBroleName().equals(bRole)) {
                retList.add(rre);
            }
        }
        return retList;
    }

    /**
     * Get the cached specific mapped gid role maps.
     * @param gid global id
     * @return ArrayList of cached RenRolemapEntity instances with a specific global id
     */
    public ArrayList<RenRolemapEntity> getCacheListByGid(String gid) {
        ArrayList<RenRolemapEntity> retList = new ArrayList<>();
        for (RenRolemapEntity rre : this.cacheList) {
            if (rre.getMappedGid().equals(gid)) {
                retList.add(rre);
            }
        }
        return retList;
    }

    // Getters and Setters

    public String getOrganDataVersion() {
        return organDataVersion;
    }

    public void setOrganDataVersion(String organDataVersion) {
        this.organDataVersion = organDataVersion;
    }
}
