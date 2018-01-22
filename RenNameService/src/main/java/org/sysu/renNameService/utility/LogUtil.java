/*
 * Project Ren @ 2018
 * Rinkako, Ariana, Gordan. SYSU SDCS.
 */
package org.sysu.renNameService.utility;

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
        LogUtil.Echo(msg, label, level);
    }

    /**
     * Enum: Log level type.
     */
    public enum LogLevelType {
        INFO, WARNNING, ERROR, UNAUTHORIZED
    }
}
