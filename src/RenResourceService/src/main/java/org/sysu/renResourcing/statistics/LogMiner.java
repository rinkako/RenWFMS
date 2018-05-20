package org.sysu.renResourcing.statistics;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.sysu.renResourcing.context.steady.RenRseventlogEntity;
import org.sysu.renResourcing.utility.HibernateUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: Rinkako
 * Date  : 2018/5/17
 * Usage : Methods for mining event log.
 */
public class LogMiner {

    private static final String baseQuery = "FROM RenRseventlogEntity AS re";

    @SuppressWarnings("unchecked")
    public static List<Long> GetTaskDurationsForParticipant(String taskId, String participantId) {
        Session session = HibernateUtil.GetLocalSession();
        Transaction transaction = session.beginTransaction();
        List<Long> retList = new ArrayList<>();
        boolean cmtFlag = false;
        try {
            StringBuilder sb = new StringBuilder(LogMiner.baseQuery);
            sb.append(" WHERE re.taskid = '%s' AND");
            sb.append(" re.completedBy = '%s'");
            List<RenRseventlogEntity> evtList = session.createQuery(String.format(sb.toString(), taskId, participantId)).list();
            transaction.commit();
            cmtFlag = true;
            for (RenRseventlogEntity evt : evtList) {

            }
        }
        catch (Exception ex) {
            if (cmtFlag) {
                transaction.rollback();
            }
        }
        finally {
            HibernateUtil.CloseLocalSession();
        }
        return retList;
    }




}
