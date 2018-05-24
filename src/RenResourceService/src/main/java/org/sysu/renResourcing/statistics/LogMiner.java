package org.sysu.renResourcing.statistics;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.sysu.renCommon.enums.LogLevelType;
import org.sysu.renCommon.utility.CommonUtil;
import org.sysu.renResourcing.context.steady.RenRseventlogEntity;
import org.sysu.renResourcing.utility.HibernateUtil;
import org.sysu.renResourcing.utility.LogUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Author: Rinkako
 * Date  : 2018/5/17
 * Usage : Methods for mining event log.
 */
public class LogMiner {

    /**
     * Base query string prefix.
     */
    private static final String baseQuery = "FROM RenRseventlogEntity AS re";

    /**
     * Get a list of durations(start -> complete) of a task processed by a participant.
     *
     * @param taskId        task global id
     * @param participantId participant global id
     * @param rtid          process rtid
     * @return a list of Long
     */
    public static List<Long> GetTaskExecutionDurationsForParticipant(String taskId, String participantId, String rtid) {
        return LogMiner.GetDurations(taskId, "start", "complete", participantId, rtid);
    }

    /**
     * Get a list of durations(allocate -> start) of a task processed by a participant.
     *
     * @param taskId        task global id
     * @param participantId participant global id
     * @param rtid          process rtid
     * @return a list of Long
     */
    public static List<Long> GetTaskLaunchDurationsForParticipant(String taskId, String participantId, String rtid) {
        return LogMiner.GetDurations(taskId, "allocate", "start", participantId, rtid);
    }

    /**
     * Get a list of durations(offer -> allocate) of a task processed by a participant.
     *
     * @param taskId        task global id
     * @param participantId participant global id
     * @param rtid          process rtid
     * @return a list of Long
     */
    public static List<Long> GetTaskAcceptDurationsForParticipant(String taskId, String participantId, String rtid) {
        return LogMiner.GetDurations(taskId, "offer", "allocate", participantId, rtid);
    }



    /**
     * Get a list of durations(completed - started) of a task.
     *
     * @param taskId        task global id
     * @param beginStatus   begin event name
     * @param endStatus     end event name
     * @param rtid          process rtid
     * @return a list of Long
     */
    public static List<Long> GetDurations(String taskId, String beginStatus, String endStatus, String rtid) {
        return LogMiner.GetDurations(taskId, beginStatus, endStatus, "", rtid);
    }

    /**
     * Get a list of durations(completed - started) of a task processed by a participant.
     *
     * @param taskId        task global id
     * @param beginStatus   begin event name
     * @param endStatus     end event name
     * @param participantId participant global id
     * @param rtid          process rtid
     * @return a list of Long
     */
    @SuppressWarnings("unchecked")
    public static List<Long> GetDurations(String taskId, String beginStatus, String endStatus, String participantId, String rtid) {
        Session session = HibernateUtil.GetLocalSession();
        Transaction transaction = session.beginTransaction();
        List<Long> retList = new ArrayList<>();
        boolean cmtFlag = false;
        try {
            StringBuilder sb = new StringBuilder(LogMiner.baseQuery);
            sb.append(" WHERE re.taskid = '%s' AND");
            sb.append(" event IN ('%s', '%s')");
            String parsedSQL;
            if (CommonUtil.IsNullOrEmpty(participantId)) {
                parsedSQL = String.format(sb.toString(), taskId, beginStatus, endStatus);
            } else {
                sb.append(" AND re.workerid = '%s'");
                parsedSQL = String.format(sb.toString(), taskId, beginStatus, endStatus, participantId);
            }
            List<RenRseventlogEntity> evtList = session.createQuery(parsedSQL).list();
            transaction.commit();
            cmtFlag = true;
            HashMap<String, RenRseventlogEntity> pendingMap = new HashMap<>();
            for (RenRseventlogEntity anEvtList : evtList) {
                String currentWid = anEvtList.getWid();
                if (pendingMap.containsKey(currentWid)) {
                    RenRseventlogEntity beforeLog = pendingMap.get(currentWid);
                    if (beforeLog.getEvent().equals(beginStatus)) {
                        retList.add(anEvtList.getTimestamp().getTime() - beforeLog.getTimestamp().getTime());
                    } else {
                        retList.add(beforeLog.getTimestamp().getTime() - anEvtList.getTimestamp().getTime());
                    }
                } else {
                    pendingMap.put(currentWid, anEvtList);
                }
            }
        } catch (Exception ex) {
            if (!cmtFlag) {
                transaction.rollback();
            }
            LogUtil.Log(String.format("Exception when duration retrieving(%s -> %s), service rollback. %s", beginStatus, endStatus, ex),
                    LogMiner.class.getName(), LogLevelType.ERROR, rtid);
        } finally {
            HibernateUtil.CloseLocalSession();
        }
        return retList;
    }
}