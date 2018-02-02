/*
 * Project Ren @ 2018
 * Rinkako, Ariana, Gordan. SYSU SDCS.
 */
package org.sysu.renResourcing.statistics;

import java.util.ArrayList;

/**
 * Author: Rinkako
 * Date  : 2018/2/2
 * Usage : Helper methods for resourcing data statistics.
 */
public class StatisticHelper {
    /**
     * Get the average value of a list of Long integer.
     * @param longList ArrayList of Long
     * @return average value
     */
    public static long GetAverage(ArrayList<Long> longList) {
        if (longList.size() == 0) {
            return 0;
        }
        long longSum = 0;
        for (long lt : longList) {
            longSum += lt;
        }
        return longSum / longList.size();
    }
}
