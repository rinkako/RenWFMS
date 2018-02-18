/*
 * Project Ren @ 2018
 * Rinkako, Ariana, Gordan. SYSU SDCS.
 */
package org.sysu.renResourcing.interfaceService;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.sysu.renResourcing.basic.enums.PrivilegeType;
import org.sysu.renResourcing.context.ParticipantContext;
import org.sysu.renResourcing.context.WorkitemContext;
import org.sysu.renResourcing.context.steady.RenRuntimerecordEntity;
import org.sysu.renResourcing.utility.CommonUtil;
import org.sysu.renResourcing.utility.HibernateUtil;

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
            String[] participantItem = participants.split(",");
            for (String workerId : participantItem) {
                retSet.add(ParticipantContext.GetContext(rtid, workerId));
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
}
