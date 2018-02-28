/*
 * Project Ren @ 2018
 * Rinkako, Ariana, Gordan. SYSU SDCS.
 */
package org.sysu.renCommon.logging;
import org.sysu.renCommon.enums.LogLevelType;

/**
 * Author: Rinkako
 * Date  : 2018/2/28
 * Usage : Base class for all internal logger. Internal means these loggers
 *         ONLY log for WFMS internal event like information or exception,
 *         external event like RS workitem log is not logged by them.
 */
public abstract class RInternalLogger {
    /**
     * Show a structure information message.
     * @param msg message text
     * @param label message label
     */
    public abstract void Echo(String msg, String label);

    /**
     * Show a structure message.
     * @param msg message text
     * @param label message label
     * @param level message level
     */
    public abstract void Echo(String msg, String label, LogLevelType level);

    /**
     * Log a structure information message to steady.
     * @param msg message text
     * @param label message label
     */
    public abstract void Log(String msg, String label, String rtid);

    /**
     * Log a structure message to steady.
     * @param msg message text
     * @param label message label
     * @param level message level
     * @param rtid process rtid
     */
    public abstract void Log(String msg, String label, LogLevelType level, String rtid);
}
