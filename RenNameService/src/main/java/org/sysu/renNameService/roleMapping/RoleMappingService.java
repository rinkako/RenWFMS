/*
 * Project Ren @ 2018
 * Rinkako, Ariana, Gordan. SYSU SDCS.
 */
package org.sysu.renNameService.roleMapping;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.sysu.renNameService.entity.RenRolemapArchivedEntity;
import org.sysu.renNameService.entity.RenRolemapEntity;
import org.sysu.renNameService.utility.HibernateUtil;
import org.sysu.renNameService.utility.LogUtil;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Author: Rinkako
 * Date  : 2018/1/16
 * Usage : All business role map service will be handled in this service module.
 */
public final class RoleMappingService {
    // For Resource Service

    /**
     * Get resource description JSON involved in a process.
     * @param rtid process rtid
     * @return String of COrgan response JSON
     */
    @SuppressWarnings("unchecked")
    public static ArrayList<RenRolemapEntity> GetInvolvedResource(String rtid) {
        CachedRoleMap crm = RoleMapCachePool.Retrieve(rtid);
        // from steady
        if (crm == null) {
            Session session = HibernateUtil.GetLocalThreadSession();
            Transaction transaction = session.beginTransaction();
            try {
                List<RenRolemapEntity> qRet = session.createQuery(String.format("FROM RenRolemapEntity WHERE rtid = '%s'", rtid)).list();
                transaction.commit();
                return (ArrayList<RenRolemapEntity>) qRet;
            }
            catch (Exception ex) {
                LogUtil.Log("When get worker by business role, exception occurred, " + ex.toString() + ", service rollback",
                        RoleMappingService.class.getName(), LogUtil.LogLevelType.ERROR, rtid);
                transaction.rollback();
                throw ex;
            }
        }
        // from cache
        else {
            return crm.getCacheList();
        }
    }

    /**
     * Get global id of organization workers by business role name.
     * @param rtid process rtid
     * @param bRoleName business role id name
     * @return ArrayList of Worker global id string
     */
    public static ArrayList<String> GetWorkerByBusinessRole(String rtid, String bRoleName) {
        ArrayList<String> retList = new ArrayList<>();
        CachedRoleMap crm = RoleMapCachePool.Retrieve(rtid);
        // from steady
        if (crm == null) {
            crm = new CachedRoleMap();
            Session session = HibernateUtil.GetLocalThreadSession();
            Transaction transaction = session.beginTransaction();
            try {
                List qRet = session.createQuery(String.format("FROM RenRolemapEntity WHERE rtid = '%s' AND broleName = '%s'", rtid, bRoleName)).list();
                for (Object rre : qRet) {
                    RenRolemapEntity rreObj = (RenRolemapEntity) rre;
                    retList.add(rreObj.getMappedGid());
                    crm.addCacheItem(rreObj);
                }
                transaction.commit();
                if (qRet.size() > 0) {
                    crm.setOrganDataVersion(((RenRolemapEntity) qRet.get(0)).getDataVersion());
                }
                RoleMapCachePool.Add(rtid, crm);
            }
            catch (Exception ex) {
                LogUtil.Log("When get worker by business role, exception occurred, " + ex.toString() + ", service rollback",
                        RoleMappingService.class.getName(), LogUtil.LogLevelType.ERROR, rtid);
                transaction.rollback();
                throw ex;
            }
        }
        // from cache
        else {
            ArrayList<RenRolemapEntity> cachedList = crm.getCacheListByBRole(bRoleName);
            for (RenRolemapEntity rre : cachedList) {
                retList.add(rre.getMappedGid());
            }
        }
        return retList;
    }

    /**
     * Get business role name by a organization entity global id.
     * @param rtid process rtid
     * @param globalId organization entity global id
     * @return ArrayList of business role name string
     */
    public static ArrayList<String> GetBusinessRoleByGlobalId(String rtid, String globalId) {
        ArrayList<String> retList = new ArrayList<>();
        CachedRoleMap crm = RoleMapCachePool.Retrieve(rtid);
        // from steady
        if (crm == null) {
            crm = new CachedRoleMap();
            Session session = HibernateUtil.GetLocalThreadSession();
            Transaction transaction = session.beginTransaction();
            try {
                List qRet = session.createQuery(String.format("FROM RenRolemapEntity WHERE rtid = '%s' AND mappedGid = '%s'", rtid, globalId)).list();
                for (Object rre : qRet) {
                    RenRolemapEntity rreObj = (RenRolemapEntity) rre;
                    retList.add(rreObj.getBroleName());
                    crm.addCacheItem(rreObj);
                }
                transaction.commit();
                if (qRet.size() > 0) {
                    crm.setOrganDataVersion(((RenRolemapEntity) qRet.get(0)).getDataVersion());
                }
                RoleMapCachePool.Add(rtid, crm);
            }
            catch (Exception ex) {
                LogUtil.Log("When get business role by gid, exception occurred, " + ex.toString() + ", service rollback",
                        RoleMappingService.class.getName(), LogUtil.LogLevelType.ERROR, rtid);
                transaction.rollback();
                throw ex;
            }
        }
        // from cache
        else {
            ArrayList<RenRolemapEntity> cachedList = crm.getCacheListByGid(globalId);
            for (RenRolemapEntity rre : cachedList) {
                retList.add(rre.getBroleName());
            }
        }
        return retList;
    }

    // For BO Engine

    /**
     * Finish role mapping service and dispose cache.
     * @param rtid process rtid
     */
    public static void FinishRoleMapService(String rtid) {
        // remove cache
        RoleMapCachePool.Remove(rtid);
        // remove relations in steady memory
        Session session = HibernateUtil.GetLocalThreadSession();
        Transaction transaction = session.beginTransaction();
        try {
            List qRet = session.createQuery(String.format("FROM RenRolemapEntity WHERE rtid = '%s'", rtid)).list();
            for (Object rre : qRet) {
                RenRolemapArchivedEntity rrae = RoleMappingService.AchieveRoleMap((RenRolemapEntity) rre);
                session.save(rrae);
                session.delete(rre);
            }
            transaction.commit();
        }
        catch (Exception ex) {
            LogUtil.Log("When finish role map service, exception occurred, " + ex.toString() + ", service rollback",
                    RoleMappingService.class.getName(), LogUtil.LogLevelType.ERROR, rtid);
            transaction.rollback();
            throw ex;
        }
    }

    // For Master Control Panel

    /**
     * Register role map service for a specific process runtime.
     * @param rtid process rtid
     * @param descriptor register parameter descriptor string
     */
    public static void RegisterRoleMapService(String rtid, String organGid, String dataVersion, int isolationType, String descriptor) {
        ArrayList<AbstractMap.SimpleEntry<String, String>> parsedList = RoleMapParser.Parse(descriptor);
        // create cache map
        CachedRoleMap crm = new CachedRoleMap();
        crm.setOrganDataVersion(dataVersion);
        for (AbstractMap.SimpleEntry<String, String> kvp : parsedList) {
            RenRolemapEntity rre = new RenRolemapEntity();
            String generateUUID = String.format("RoleMap_%s", UUID.randomUUID());
            rre.setMapId(generateUUID);
            rre.setRtid(rtid);
            rre.setBroleName(kvp.getKey());
            rre.setCorganGid(organGid);
            rre.setMappedGid(kvp.getValue());
            rre.setDataVersion(dataVersion);
            rre.setTransactionIsolation(isolationType);
            crm.addCacheItem(rre);
        }
        // save to steady
        List<RenRolemapEntity> rreList = crm.getCacheList();
        Session session = HibernateUtil.GetLocalThreadSession();
        Transaction transaction = session.beginTransaction();
        try {
            for (RenRolemapEntity t : rreList) {
                session.save(t);
            }
            transaction.commit();
        }
        catch (Exception ex) {
            LogUtil.Log("When register role map service, exception occurred, " + ex.toString() + ", service rollback",
                    RoleMappingService.class.getName(), LogUtil.LogLevelType.ERROR, rtid);
            transaction.rollback();
            throw ex;
        }
    }

    /**
     * Generate an achieve role map entity.
     * @param rre role map entity
     * @return role map achieved entity
     */
    private static RenRolemapArchivedEntity AchieveRoleMap(RenRolemapEntity rre) {
        RenRolemapArchivedEntity rrae = new RenRolemapArchivedEntity();
        rrae.setMapId(rre.getMapId());
        rrae.setRtid(rre.getRtid());
        rrae.setBroleName(rre.getBroleName());
        rrae.setCorganGid(rre.getCorganGid());
        rrae.setMappedGid(rre.getMappedGid());
        rrae.setDataVersion(rre.getDataVersion());
        rrae.setTransactionIsolation(rre.getTransactionIsolation());
        return rrae;
    }
}
