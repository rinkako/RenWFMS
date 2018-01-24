/*
 * Project Ren @ 2018
 * Rinkako, Ariana, Gordan. SYSU SDCS.
 */
package org.sysu.renNameService.pattern;

/**
 * Author: Rinkako
 * Date  : 2018/1/24
 * Usage : This interface is used to describe the subject instance in
 *         the pattern of Publish-Subject, which will be notified when
 *         any publisher it subjected is updated.
 */
public abstract class NObserver {
    /**
     * Create a observer.
     * @param observerId observer unique id
     */
    public NObserver(String observerId) {
        this.observerId = observerId;
    }

    /**
     * This method will be called when this observer is notified
     * by its subjecting observable object.
     * @param notifier notifier reference
     * @param tag notify message package
     */
    public abstract void Notified(NObservable notifier, Object tag);

    /**
     * Get id of observer.
     * @return id string
     */
    public String getObserverId() {
        return observerId;
    }

    /**
     * Observer unique id.
     */
    private String observerId;
}
