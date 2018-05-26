/*
 * Project Ren @ 2018
 * Rinkako, Ariana, Gordan. SYSU SDCS.
 */
package org.sysu.renResourcing.statistics;

import java.io.Serializable;
import java.util.List;

/**
 * Author: Rinkako
 * Date  : 2018/2/3
 * Usage : Storing some statistic data for accelerating resourcing.
 */
public class StatisticPrototype implements Serializable {

    /**
     * Serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Weight of this prototype when statistic.
     */
    public double Weight = 1.0f;

    /**
     * Bias of this prototype when statistic.
     */
    public double Bias = 0.0f;

    /**
     * Average value of data set.
     */
    private double average = 0.0f;

    /**
     * Average value for how many samples.
     */
    private int sampleCount = 0;

    /**
     * Synchronization mutex.
     */
    private final Object syncMutex = new Object();

    /**
     * Get average value of this statistics prototype.
     *
     * @return average value in Double
     */
    public double GetAverage() {
        synchronized (this.syncMutex) {
            return this.average;
        }
    }

    /**
     * Get the sample count of average value generated from.
     *
     * @return count of sample in Int
     */
    public int GetSampleCount() {
        synchronized (this.syncMutex) {
            return this.sampleCount;
        }
    }

    /**
     * Add a single sample to this prototype.
     *
     * @param sampleItem sample to add
     */
    public void AddSample(Double sampleItem) {
        synchronized (this.syncMutex) {
            this.average += (sampleItem - this.average) / (double) (this.sampleCount + 1);
            this.sampleCount++;
        }
    }

    /**
     * Add samples to this prototype.
     *
     * @param sampleItems List of sample to add
     */
    public void AddSamples(List<Double> sampleItems) {
        synchronized (this.syncMutex) {
            for (int i = 0; i < sampleItems.size(); i++) {
                this.average += (sampleItems.get(i) - this.average) / (double) (this.sampleCount + i + 1);
            }
            this.sampleCount += sampleItems.size();
        }
    }

    /**
     * Get a prototype context from steady.
     * NOTICE that prototype must not cache, it need strong consistency not eventually.
     *
     * @return prototype instance.
     */
    public static StatisticPrototype GetPrototype() {
        return null;
    }

    /**
     * Private constructor prevent from constructing outside.
     */
    private StatisticPrototype() {

    }
}
