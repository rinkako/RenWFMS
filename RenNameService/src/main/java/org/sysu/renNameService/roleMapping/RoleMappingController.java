/*
 * Project Ren @ 2018
 * Rinkako, Arianna, Gordan. SYSU SDCS.
 */
package org.sysu.renNameService.roleMapping;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.sysu.renNameService.entity.RenRolemapEntity;
import org.sysu.renNameService.utility.HibernateUtil;
import org.sysu.renNameService.utility.LogUtil;

import java.util.ArrayList;

/**
 * Author: Rinkako
 * Date  : 2018/1/16
 * Usage : All business role map service will be handled in this controller.
 */
public final class RoleMappingController {
    // For Resource Service

    /**
     * Get resource description JSON involved in a process.
     * @param rtid process rtid
     * @return String of COrgan response JSON
     */
    public static String GetInvolvedResource(String rtid) {
        return null;
    }

    /**
     * Get global id of organization workers by business role name.
     * @param rtid process rtid
     * @param bRoleName business role id name
     * @return ArrayList of Worker global id string
     */
    public static ArrayList<String> GetWorkerByBusinessRole(String rtid, String bRoleName) {
        return null;
    }

    /**
     * Get business role name by a organization entity global id.
     * @param rtid process rtid
     * @param globalId organization entity global id
     * @return ArrayList of business role name string
     */
    public static ArrayList<String> GetBusinessRoleByGlobalId(String rtid, String globalId) {
        return null;
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
        Session session = HibernateUtil.OpenSession();
        Transaction transaction = session.beginTransaction();
        try {
            // todo
            transaction.commit();
        }
        catch (Exception ex) {
            LogUtil.Log("When finish role map service, exception occured, " + ex.toString() + ", service rollback",
                    RoleMappingController.class.getName(), LogUtil.LogLevelType.ERROR);
            transaction.rollback();
        }
        finally {
            HibernateUtil.CloseSession(session);
        }
    }

    // For Master Control Panel

    /**
     * Register role map service for a specific process runtime.
     * @param rtid process rtid
     * @param registerJSON register parameter json string
     */
    public static void RegisterRoleMapService(String rtid, String registerJSON) {

    }
}
