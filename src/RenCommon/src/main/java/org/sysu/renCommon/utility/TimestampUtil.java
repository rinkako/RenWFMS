package org.sysu.renCommon.utility;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Author: Rinkako
 * Date  : 2017/12/14
 * Usage : This utility used for getting the timestamp.
 */
public class TimestampUtil {

    /**
     * Get timestamp in pattern of `yyyy-MM-dd HH:mm:ss`
     * @return timestamp in string
     */
    public static String GetTimestampString() {
        return sdf.format(new Date());
    }

    /**
     * Get current timestamp.
     * @return Timestamp instance of current moment
     */
    public static Timestamp GetCurrentTimestamp() {
        return new Timestamp(System.currentTimeMillis());
    }

    /**
     * Timestamp string formatter.
     */
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
}
