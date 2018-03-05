/*
 * Project Ren @ 2018
 * Rinkako, Ariana, Gordan. SYSU SDCS.
 */
package org.sysu.renResourcing.interfaceService;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.sysu.renCommon.enums.LogLevelType;
import org.sysu.renCommon.enums.PrivilegeType;
import org.sysu.renCommon.enums.ResourceBindingType;
import org.sysu.renResourcing.context.ParticipantContext;
import org.sysu.renResourcing.context.WorkitemContext;
import org.sysu.renResourcing.context.steady.RenRuntimerecordEntity;
import org.sysu.renCommon.utility.CommonUtil;
import org.sysu.renResourcing.utility.HibernateUtil;
import org.sysu.renResourcing.utility.LogUtil;

import java.util.HashSet;

/**
 * Author: Rinkako
 * Date  : 2018/2/9
 * Usage : Implementation of Interface O of Resource Service.
 *         Interface O is responsible for resources managements. In RenWFMS, we defined
 *         resources in COrgan, and register involved resources to Participant in Name
 *         Service. Therefore we assert when Resource Service need to refer a RESOURCE,
 *         it has already been registered in steady memory as a PARTICIPANT. So, this
 *         interface just for involved resources information retrieving and participants
 *         privileges management.
 */
public class InterfaceO {

    /**
     * Get current valid participant context set.
     * Current valid means that current resources set in Name Service according to process COrgan isolation type.
     * NOTICE that participant load and unload is handled in Name Service.
     * @param rtid process rtid
     * @return a Hash set for current valid participant context
     */
    public static HashSet<ParticipantContext> GetCurrentValidParticipant(String rtid) {
        HashSet<ParticipantContext> retSet = new HashSet<>();
        Session session = HibernateUtil.GetLocalSession();
        Transaction transaction = session.beginTransaction();
        boolean cmtFlag = false;
        try {
            RenRuntimerecordEntity runtimeCtx = session.get(RenRuntimerecordEntity.class, rtid);
            String participants = runtimeCtx.getParticipantCache();
            transaction.commit();
            cmtFlag = true;
            if (CommonUtil.IsNullOrEmpty(participants)) {
                return retSet;
            }
            String[] participantPairItem = participants.split(",");
            for (String workerIdBRolePair : participantPairItem) {
                String[] workerItem = workerIdBRolePair.split(":");
                retSet.add(ParticipantContext.GetContext(rtid, workerItem[0]));
            }
            return retSet;
        }
        catch (Exception ex) {
            if (!cmtFlag) {
                transaction.rollback();
            }
            throw ex;
        }
        finally {
            HibernateUtil.CloseLocalSession();
        }
    }

    /**
     * Get valid participant context set according to business role in specific process runtime.
     * @param rtid process rtid
     * @param brole business role name
     * @return a Hash set for current valid participant context of a business role
     */
    public static HashSet<ParticipantContext> GetParticipantByBRole(String rtid, String brole) {
        HashSet<ParticipantContext> retSet = new HashSet<>();
        Session session = HibernateUtil.GetLocalSession();
        Transaction transaction = session.beginTransaction();
        boolean cmtFlag = false;
        try {
            RenRuntimerecordEntity runtimeCtx = session.get(RenRuntimerecordEntity.class, rtid);
            String participants = runtimeCtx.getParticipantCache();
            transaction.commit();
            cmtFlag = true;
            if (CommonUtil.IsNullOrEmpty(participants)) {
                return retSet;
            }
            String[] participantPairItem = participants.split(",");
            for (String workerIdBRolePair : participantPairItem) {
                String[] workerItem = workerIdBRolePair.split(":");
                if (workerItem[1].equals(brole)) {
                    retSet.add(ParticipantContext.GetContext(rtid, workerItem[0]));
                }
            }
            return retSet;
        }
        catch (Exception ex) {
            if (!cmtFlag) {
                transaction.rollback();
            }
            throw ex;
        }
        finally {
            HibernateUtil.CloseLocalSession();
        }
    }

    /**
     * Check if a participant has a privilege.
     * @param participant participant context
     * @param workitem workitem context
     * @param privilege privilege enum
     * @return true if participant has the privilege
     */
    public static boolean CheckPrivilege(ParticipantContext participant, WorkitemContext workitem, PrivilegeType privilege) {
        // todo
        return true;
    }

    /**
     * This method is called when sensed participant in steady is changed.
     * @param rtid process rtid
     * @return is fail-fast when organization data changed
     */
    public static boolean SenseParticipantDataChanged(String rtid) {
        LogUtil.Log("Sensed binding resources changed.", InterfaceO.class.getName(),
                LogLevelType.INFO, rtid);
        Session session = HibernateUtil.GetLocalSession();
        try {
            RenRuntimerecordEntity rre = session.get(RenRuntimerecordEntity.class, rtid);
            return rre.getResourceBindingType() == ResourceBindingType.FastFail.ordinal();
        }
        finally {
            HibernateUtil.CloseLocalSession();
        }
    }
}
