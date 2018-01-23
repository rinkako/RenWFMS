/*
 * Project Ren @ 2018
 * Rinkako, Ariana, Gordan. SYSU SDCS.
 */
package org.sysu.renNameService.utility;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.sysu.renNameService.entity.RenNslogEntity;
import java.sql.Timestamp;

/**
 * Author: Rinkako
 * Date  : 2018/1/18
 * Usage : Static methods for name service logging.
 */
public final class LogUtil {
    /**
     * Show a structure information message.
     * @param msg message text
     * @param label message label
     */
    public static void Echo(String msg, String label) {
        LogUtil.Echo(msg, label, LogLevelType.INFO);
    }

    /**
     * Show a structure message.
     * @param msg message text
     * @param label message label
     * @param level message level
     */
    public static void Echo(String msg, String label, LogLevelType level) {
        String printStr = String.format("[%s]%s-%s: %s", level.name(), TimestampUtil.GetTimeStamp(), label, msg);
        System.out.println(printStr);
    }

    /**
     * Log a structure information message to steady.
     * @param msg message text
     * @param label message label
     */
    public static void Log(String msg, String label) {
        LogUtil.Log(msg, label, LogLevelType.INFO);
    }

    /**
     * Log a structure message to steady.
     * @param msg message text
     * @param label message label
     * @param level message level
     */
    public static void Log(String msg, String label, LogLevelType level) {
        LogUtil.ActualLog(msg, label, level, 0);
    }

    /**
     * Write log to steady.
     * @param msg message text
     * @param label message label
     * @param level message level
     * @param depth exception depth
     */
    private static void ActualLog(String msg, String label, LogLevelType level, int depth) {
        LogUtil.Echo(msg, label, level);
        if (depth > 1) {
            return;
        }
        Session session = HibernateUtil.GetLocalThreadSession();
        Transaction transaction = session.beginTransaction();
        try {
            transaction.begin();
            RenNslogEntity rnle = new RenNslogEntity();
            rnle.setLabel(label);
            rnle.setLevel(level.name());
            rnle.setMessage(msg);
            rnle.setTimestamp(new Timestamp(System.currentTimeMillis()));
            session.save(rnle);
            transaction.commit();
        }
        catch (Exception ex) {
            transaction.rollback();
            LogUtil.ActualLog("When logging, exception occurred" + ex, LogUtil.class.getName(),
                    LogLevelType.ERROR, depth + 1);
        }
    }

    /**
     * Enum: Log level type.
     */
    public enum LogLevelType {
        INFO, WARNNING, ERROR, UNAUTHORIZED
    }
}
