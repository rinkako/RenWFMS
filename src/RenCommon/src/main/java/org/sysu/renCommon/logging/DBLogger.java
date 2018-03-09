/*
 * Project Ren @ 2018
 * Rinkako, Ariana, Gordan. SYSU SDCS.
 */
package org.sysu.renCommon.logging;

import org.sysu.renCommon.GlobalConfigContext;
import org.sysu.renCommon.enums.LogLevelType;
import org.sysu.renCommon.utility.TimestampUtil;

import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Author: Rinkako
 * Date  : 2018/2/28
 * Usage : Logger for logging events into steady DB with Hibernate.
 */
public class DBLogger extends RInternalLogger {
    /**
     * Show a structure information message.
     * @param msg message text
     * @param label message label
     */
    public void Echo(String msg, String label) {
        this.Echo(msg, label, LogLevelType.INFO);
    }

    /**
     * Show a structure message.
     * @param msg message text
     * @param label message label
     * @param level message level
     */
    public void Echo(String msg, String label, LogLevelType level) {
        String printStr = String.format("[%s]%s-%s: %s", level.name(), TimestampUtil.GetTimestampString(), label, msg);
        System.out.println(printStr);
    }

    /**
     * Log a structure information message to steady.
     * @param msg message text
     * @param label message label
     */
    public void Log(String msg, String label, String rtid) {
        this.Log(msg, label, LogLevelType.INFO, rtid);
    }

    /**
     * Log a structure message to steady.
     * @param msg message text
     * @param label message label
     * @param level message level
     * @param rtid process rtid
     */
    public void Log(String msg, String label, LogLevelType level, String rtid) {
        this.ActualLog(msg, label, level, rtid, 0);
    }

    /**
     * Write log to steady.
     * @param msg message text
     * @param label message label
     * @param level message level
     * @param depth exception depth
     */
    private void ActualLog(String msg, String label, LogLevelType level, String rtid, int depth) {
        this.Echo(msg, label, level);
        if (depth > 1) {
            return;
        }
        try {
            // use read lock to prevent flush
            this.readWriteLock.readLock().lock();
            LogMessagePackage lmp = new LogMessagePackage(rtid, msg, label, level,
                    TimestampUtil.GetCurrentTimestamp());
            this.logBuffer.add(lmp);
        }
        catch (Exception ex) {
            this.ActualLog("When logging, exception occurred" + ex, DBLogger.class.getName(),
                    LogLevelType.ERROR, rtid, depth + 1);
        }
        finally {
            boolean flushFlag = this.logBuffer.size() >= GlobalConfigContext.LOG_BUFFER_SIZE;
            this.readWriteLock.readLock().unlock();
            if (flushFlag) {
                this.FlushLog();
            }
        }
    }

    /**
     * Flush buffered log to steady memory.
     * THIS IS NOT A TRANSACTION.
     */
    public synchronized void FlushLog() {
//        this.readWriteLock.writeLock().lock();
//        if (this.logBuffer.size() == 0) {
//            this.readWriteLock.writeLock().unlock();
//            return;
//        }
//        Session session = HibernateUtil.GetLocalSession();
//        try {
//            LogMessagePackage lmp;
//            while ((lmp = LogUtil.logBuffer.poll()) != null) {
//                RenLogEntity rnle = new RenLogEntity();
//                rnle.setLogid(String.format("RsLog_%s", UUID.randomUUID()));
//                rnle.setLabel(lmp.Label);
//                rnle.setLevel(lmp.Level.name());
//                rnle.setMessage(lmp.Message);
//                rnle.setTimestamp(lmp.Timestamp);
//                session.save(rnle);
//            }
//            session.flush();
//        }
//        catch (Exception ex) {
//            this.Echo("Flush log exception, " + ex, LogUtil.class.getName(), LogLevelType.ERROR);
//        }
//        finally {
//            this.readWriteLock.writeLock().unlock();
//            HibernateUtil.CloseLocalSession();
//        }
    }

    /**
     * Log buffer.
     */
    private ConcurrentLinkedQueue<LogMessagePackage> logBuffer = new ConcurrentLinkedQueue<>();

    /**
     * Buffer read write lock.
     */
    private ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();
}
