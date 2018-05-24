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
     * This method is overflow safe.
     *
     * @param longList List of Long
     * @return average value in Double
     */
    public static double GetAverage(List<Long> longList) {
        double curAvg = 0;
        for (int i = 0; i < longList.size(); i++) {
            curAvg += (longList.get(i) - curAvg) / (double)i;
        }
        return curAvg;
    }

    /**
     * Get the max long value in the collection.
     *
     * @param collection collection to be checked
     * @return max long value
     */
    public static long GetMax(Collection<Long> collection) {
        return Collections.max(collection);
    }

    /**
     * Get the min long value in the collection.
     *
     * @param collection collection to be checked
     * @return min long value
     */
    public static long GetMin(Collection<Long> collection) {
        return Collections.min(collection);
    }

}
