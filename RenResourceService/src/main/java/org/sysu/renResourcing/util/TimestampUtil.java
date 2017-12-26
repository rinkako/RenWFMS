package org.sysu.renResourcing.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Author: gd
 * Date  : 2017/12/14
 * Usage : This utility used for getting the timestamp.
 */
public class TimestampUtil {

    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static String GetTimeStamp() {
        return sdf.format(new Date());
    }
}
