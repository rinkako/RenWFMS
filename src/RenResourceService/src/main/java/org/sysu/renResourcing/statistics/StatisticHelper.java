/*
 * Project Ren @ 2018
 * Rinkako, Ariana, Gordan. SYSU SDCS.
 */
package org.sysu.renResourcing.statistics;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Author: Rinkako
 * Date  : 2018/2/2
 * Usage : Helper methods for resourcing data statistics.
 */
public class StatisticHelper {

    /**
     * Get the average value of a list of Long integer.
     * @param longList List of Long
     * @return average value
     */
    public static long GetAverage(List<Long> longList) {
        if (longList.size() == 0) {
            return 0;
        }
        long longSum = 0;
        for (long lt : longList) {
            longSum += lt;
        }
        return longSum / longList.size();
    }

    /**
     * Get the max long value in the collection.
     * @param collection collection to be checked
     * @return max long value
     */
    public static long GetMax(Collection<Long> collection) {
        return Collections.max(collection);
    }

    /**
     * Get the min long value in the collection.
     * @param collection collection to be checked
     * @return min long value
     */
    public static long GetMin(Collection<Long> collection) {
        return Collections.min(collection);
    }

}
