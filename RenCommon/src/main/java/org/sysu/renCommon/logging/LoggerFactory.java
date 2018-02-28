/*
 * Project Ren @ 2018
 * Rinkako, Ariana, Gordan. SYSU SDCS.
 */
package org.sysu.renCommon.logging;

/**
 * Author: Rinkako
 * Date  : 2018/1/18
 * Usage : A factory to retrieve thread local logger.
 */
public class LoggerFactory {

    /**
     * session object in thread local, thread safe.
     */
    private static ThreadLocal session = new ThreadLocal();

    /**
     * Private constructor.
     */
    private LoggerFactory() { }

    /**
     * Get logger for this thread.
     * @return logger instance
     */
    @SuppressWarnings("unchecked")
    public static RInternalLogger GetLocalSession() {
        RInternalLogger s = (RInternalLogger) session.get();
        if (s == null) {
            s = new DBLogger();
            LoggerFactory.session.set(s);
        }
        return s;
    }

    /**
     * Close active logger in this thread.
     */
    @SuppressWarnings("unchecked")
    public static void CloseLocalSession() {
        LoggerFactory.session.set(null);
    }
}