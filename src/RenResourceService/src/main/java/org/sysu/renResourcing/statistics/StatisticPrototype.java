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
 * Usage : Storing some statistic data for accelerating allocation.
 */
public class StatisticPrototype implements Serializable {

    /**
     * Serial version UID.
     */
    private static final long serialVersionUID = 1L;

    public double Weight = 1.0f;

    private double average = 0.0f;

    private int sampleCount = 0;

    public double GetAverage() {
        return this.average;
    }

    public int GetSampleCount() {
        return this.sampleCount;
    }

    public synchronized void AddSample(Double sampleItem) {
        this.average = (this.average * this.sampleCount + sampleItem) / (this.sampleCount + 1);
        this.sampleCount++;
    }

    public synchronized void AddSamples(List<Double> sampleItems) {
        int newSampleCount = sampleItems.size();
        double newSampleSum = 0.0f;
        for (double sample : sampleItems) {
            newSampleSum += sample;
        }
        this.average = (this.average * this.sampleCount + newSampleSum) / (this.sampleCount + newSampleCount);
        this.sampleCount += newSampleCount;
    }
}
