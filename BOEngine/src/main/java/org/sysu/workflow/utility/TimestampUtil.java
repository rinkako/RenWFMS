package org.sysu.workflow.utility;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Author: Ariana
 * Date  : 2018/1/21
 * Usage : Static methods for timestamp generation.
 */
public class TimestampUtil {
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static String GetTimeStamp() {
        return sdf.format(new Date());
    }
}
