/*
 * Project Ren @ 2018
 * Rinkako, Arianna, Gordan. SYSU SDCS.
 */
package org.sysu.renNameService.utility;

/**
 * Author: Rinkako
 * Date  : 2018/1/18
 * Usage : Static methods for name service logging.
 */
public final class LogUtil {
    /**
     * @param msg
     * @param label
     */
    public static void Echo(String msg, String label) {
        LogUtil.Echo(msg, label, LogLevelType.INFO);
    }

    /**
     * @param msg
     * @param label
     * @param level
     */
    public static void Echo(String msg, String label, LogLevelType level) {
        String printStr = String.format("[%s]%s-%s: %s", level.name(), TimestampUtil.GetTimeStamp(), label, msg);
        System.out.println(printStr);
    }

    /**
     * @param msg
     * @param label
     */
    public static void Log(String msg, String label) {
        LogUtil.Log(msg, label, LogLevelType.INFO);
    }

    /**
     * @param msg
     * @param label
     * @param level
     */
    public static void Log(String msg, String label, LogLevelType level) {
        LogUtil.Echo(msg, label, level);
    }

    /**
     *
     */
    public enum LogLevelType {
        INFO, WARNNING, ERROR, UNAUTHORIZED
    }
}
