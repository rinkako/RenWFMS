/*
 * Project Ren @ 2018
 * Rinkako, Ariana, Gordan. SYSU SDCS.
 */
package org.sysu.renNameService.roleMapping;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.sysu.renNameService.GlobalContext;
import org.sysu.renNameService.entity.RenAuthEntity;
import org.sysu.renNameService.entity.RenRolemapArchivedEntity;
import org.sysu.renNameService.entity.RenRolemapEntity;
import org.sysu.renNameService.utility.HibernateUtil;
import org.sysu.renNameService.utility.HttpClientUtil;
import org.sysu.renNameService.utility.LogUtil;
import org.sysu.renNameService.utility.RSASignatureUtil;

import java.util.*;

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
     * @param organGid organization global id
     * @param dataVersion organization data version
     * @param descriptor register parameter descriptor string
     */
    public static void RegisterRoleMapService(String rtid, String organGid, String dataVersion, String descriptor) {
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
     * Get all resource from a Ren auth user COrgan gateway.
     * @param renid ren auth id
     * @param rtid process rtid
     * @param nsid transaction id for signature
     * @return a list of [human, agent, group, position, capability] json string
     */
    public static String GetAllResourceFromCOrgan(String renid, String rtid, String nsid) {
        Session session = HibernateUtil.GetLocalThreadSession();
        Transaction transaction = session.beginTransaction();
        boolean cmtFlag = false;
        try {
            RenAuthEntity rae = session.get(RenAuthEntity.class, renid);
            cmtFlag = true;
            transaction.commit();
            assert rae != null;
            String corganUrl = rae.getCorganGateway();
            if (corganUrl == null || corganUrl.equals("")) {
                LogUtil.Log("Get resource by COrgan, but auth user does not bind any COrgan gateway",
                        RoleMappingService.class.getName(), LogUtil.LogLevelType.WARNING, rtid);
                return "";
            }
            HashMap<String, String> args = new HashMap<>();
            String nsSign = RSASignatureUtil.Signature(nsid + "," + renid, GlobalContext.PRIVATE_KEY);
            assert nsSign != null;
            args.put("renid", renid);
            args.put("nsid", nsid);
            args.put("token", RSASignatureUtil.SafeUrlBase64Encode(nsSign));
            return HttpClientUtil.SendPost(corganUrl + "ns/getresources", args, "");
        }
        catch (Exception ex) {
            LogUtil.Log("When GetAllResourceFromCOrgan role map service, exception occurred, " + ex.toString(),
                    RoleMappingService.class.getName(), LogUtil.LogLevelType.ERROR, rtid);
            if (!cmtFlag) {
                transaction.rollback();
            }
            return "";
        }
    }

    /**
     * Get all relation connections from a Ren auth user COrgan gateway.
     * @param renid ren auth id
     * @param rtid process rtid
     * @param nsid transaction id for signature
     * @return a list of connection json string
     */
    public static String GetAllConnectionFromCOrgan(String renid, String rtid, String nsid) {
        Session session = HibernateUtil.GetLocalThreadSession();
        Transaction transaction = session.beginTransaction();
        boolean cmtFlag = false;
        try {
            RenAuthEntity rae = session.get(RenAuthEntity.class, renid);
            cmtFlag = true;
            transaction.commit();
            assert rae != null;
            String corganUrl = rae.getCorganGateway();
            if (corganUrl == null || corganUrl.equals("")) {
                LogUtil.Log("Get resource by COrgan, but auth user does not bind any COrgan gateway",
                        RoleMappingService.class.getName(), LogUtil.LogLevelType.WARNING, rtid);
                return "";
            }
            HashMap<String, String> args = new HashMap<>();
            String nsSign = RSASignatureUtil.Signature(nsid + "," + renid, GlobalContext.PRIVATE_KEY);
            assert nsSign != null;
            args.put("renid", renid);
            args.put("nsid", nsid);
            args.put("token", RSASignatureUtil.SafeUrlBase64Encode(nsSign));
            return HttpClientUtil.SendPost(corganUrl + "ns/getconnections", args, "");
        }
        catch (Exception ex) {
            LogUtil.Log("When GetAllConnectionFromCOrgan role map service, exception occurred, " + ex.toString(),
                    RoleMappingService.class.getName(), LogUtil.LogLevelType.ERROR, rtid);
            if (!cmtFlag) {
                transaction.rollback();
            }
            return "";
        }
    }

    /**
     * Get data version from a Ren auth user COrgan gateway.
     * @param renid ren auth id
     * @param nsid transaction id for signature
     * @return a list of connection json string
     */
    public static String GetDataVersionAndGidFromCOrgan(String renid, String nsid) {
        Session session = HibernateUtil.GetLocalThreadSession();
        Transaction transaction = session.beginTransaction();
        boolean cmtFlag = false;
        try {
            RenAuthEntity rae = session.get(RenAuthEntity.class, renid);
            cmtFlag = true;
            transaction.commit();
            assert rae != null;
            String corganUrl = rae.getCorganGateway();
            if (corganUrl == null || corganUrl.equals("")) {
                LogUtil.Log("Get data version by COrgan, but auth user does not bind any COrgan gateway",
                        RoleMappingService.class.getName(), LogUtil.LogLevelType.WARNING, "");
                return "";
            }
            HashMap<String, String> args = new HashMap<>();
            String nsSign = RSASignatureUtil.Signature(nsid + "," + renid, GlobalContext.PRIVATE_KEY);
            assert nsSign != null;
            args.put("renid", renid);
            args.put("nsid", nsid);
            args.put("token", RSASignatureUtil.SafeUrlBase64Encode(nsSign));
            return HttpClientUtil.SendPost(corganUrl + "ns/getdataversiongid", args, "");
        }
        catch (Exception ex) {
            LogUtil.Log("When GetDataVersionFromCOrgan role map service, exception occurred, " + ex.toString(),
                    RoleMappingService.class.getName(), LogUtil.LogLevelType.ERROR, "");
            if (!cmtFlag) {
                transaction.rollback();
            }
            return "";
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
        return rrae;
    }
}
