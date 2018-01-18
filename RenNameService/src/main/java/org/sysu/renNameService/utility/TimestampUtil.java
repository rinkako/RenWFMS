/*
 * Project Ren @ 2018
 * Rinkako, Arianna, Gordan. SYSU SDCS.
 */
package org.sysu.renNameService.utility;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Author: Rinkako
 * Date  : 2018/1/18
 * Usage : Static methods for timestamp generation.
 */
public class TimestampUtil {

    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static String GetTimeStamp() {
        return sdf.format(new Date());
    }
}
