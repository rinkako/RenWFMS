/*
 * Project Ren @ 2018
 * Rinkako, Ariana, Gordan. SYSU SDCS.
 */
package org.sysu.renResourcing.utility;

import org.hibernate.Session;
import org.sysu.renCommon.enums.LogLevelType;
import org.sysu.renCommon.utility.TimestampUtil;
import org.sysu.renResourcing.GlobalContext;
import org.sysu.renResourcing.context.steady.RenLogEntity;

import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Author: Rinkako
 * Date  : 2018/2/21
 * Usage : Static methods for resource service internal logging.
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
        String printStr = String.format("[%s]%s-%s: %s", level.name(), TimestampUtil.GetTimestampString(), label, msg);
        System.out.println(printStr);
    }

    /**
     * Log a structure information message to steady.
     * @param msg message text
     * @param label message label
     */
    public static void Log(String msg, String label, String rtid) {
        LogUtil.Log(msg, label, LogLevelType.INFO, rtid);
    }

    /**
     * Log a structure message to steady.
     * @param msg message text
     * @param label message label
     * @param level message level
     * @param rtid process rtid
     */
    public static void Log(String msg, String label, LogLevelType level, String rtid) {
        LogUtil.ActualLog(msg, label, level, rtid, 0);
    }

    /**
     * Write log to steady.
     * @param msg message text
     * @param label message label
     * @param level message level
     * @param depth exception depth
     */
    private static void ActualLog(String msg, String label, LogLevelType level, String rtid, int depth) {
        LogUtil.Echo(msg, label, level);
        if (depth > 1) {
            return;
        }
        try {
            // use read lock to prevent flush
            LogUtil.readWriteLock.readLock().lock();
            LogMessagePackage lmp = new LogMessagePackage(rtid, msg, label, level,
                    TimestampUtil.GetCurrentTimestamp());
            LogUtil.logBuffer.add(lmp);
        }
        catch (Exception ex) {
            LogUtil.ActualLog("When logging, exception occurred" + ex, LogUtil.class.getName(),
                    LogLevelType.ERROR, rtid, depth + 1);
        }
        finally {
            boolean flushFlag = LogUtil.logBuffer.size() >= GlobalContext.LOG_BUFFER_SIZE;
            LogUtil.readWriteLock.readLock().unlock();
            if (flushFlag) {
                LogUtil.FlushLog();
            }
        }
    }

    /**
     * Flush buffered log to steady memory.
     * THIS IS NOT A TRANSACTION.
     */
    public static synchronized void FlushLog() {
        LogUtil.readWriteLock.writeLock().lock();
        if (LogUtil.logBuffer.size() == 0) {
            LogUtil.readWriteLock.writeLock().unlock();
            return;
        }
        Session session = HibernateUtil.GetLocalSession();
        try {
            LogMessagePackage lmp;
            while ((lmp = LogUtil.logBuffer.poll()) != null) {
                RenLogEntity rnle = new RenLogEntity();
                rnle.setLogid(String.format("RsLog_%s", UUID.randomUUID()));
                rnle.setLabel(lmp.Label);
                rnle.setLevel(lmp.Level.name());
                rnle.setMessage(lmp.Message);
                rnle.setTimestamp(lmp.Timestamp);
                rnle.setRtid(lmp.Rtid);
                session.save(rnle);
            }
            session.flush();
        }
        catch (Exception ex) {
            LogUtil.Echo("Flush log exception, " + ex, LogUtil.class.getName(), LogLevelType.ERROR);
        }
        finally {
            LogUtil.readWriteLock.writeLock().unlock();
            HibernateUtil.CloseLocalSession();
        }
    }

    /**
     * Log buffer.
     */
    private static ConcurrentLinkedQueue<LogMessagePackage> logBuffer = new ConcurrentLinkedQueue<>();

    /**
     * Buffer read write lock.
     */
    private static ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();
}
