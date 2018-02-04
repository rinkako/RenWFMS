/*
 * Project Ren @ 2018
 * Rinkako, Ariana, Gordan. SYSU SDCS.
 */
package org.sysu.renResourcing.utility;

/**
 * Author: Rinkako
 * Date  : 2018/2/4
 * Usage : Some useful helper methods.
 */
public class CommonUtil {
    /**
     * Check if a string is null or empty.
     * @param str string to be checked
     * @return boolean of result
     */
    public static boolean IsNullOrEmpty(String str) {
        return str != null && !str.equals("");
    }
}
