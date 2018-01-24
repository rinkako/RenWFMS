/*
 * Project Ren @ 2018
 * Rinkako, Ariana, Gordan. SYSU SDCS.
 */
package org.sysu.renNameService.pattern;

import java.util.ArrayList;

/**
 * Author: Rinkako
 * Date  : 2018/1/24
 * Usage : This class is used to describe the publisher in the pattern of
 *         Publish-Subject, which will notify its subject instance when
 *         its state changed.
 */
public abstract class NObservable {
    /**
     * Attach a new observer to this observable object.
     * @param ob observer instance object
     */
    public void Attach(NObserver ob) {
        this.observerVector.add(ob);
    }

    /**
     * Detach an observer from this observable object.
     * @param ob observer instance object
     */
    public void Detach(NObserver ob) {
        this.observerVector.remove(ob);
    }

    /**
     * Remove all observers.
     */
    public void ClearObserver() {
        this.observerVector.clear();
    }

    /**
     * Get the count number of observers.
     * @return observer number
     */
    public int CountObserver() {
        return this.observerVector.size();
    }

    /**
     * Notify all observer.
     * @param tag message package
     */
    public void NotifyAll(Object tag) {
        for (NObserver ob : this.observerVector) {
            ob.Notified(this, tag);
        }
    }

    /**
     * Notify a specific observer.
     * @param observer id of observer to be notified
     * @param tag message package
     * @return if this observer exist
     */
    public boolean Notify(String observer, Object tag) {
        for (NObserver ob : this.observerVector) {
            if (ob.getObserverId().equals(observer)) {
                ob.Notified(this, tag);
                return true;
            }
        }
        return false;
    }

    /**
     * Vector for maintaining observer.
     */
    private ArrayList<NObserver> observerVector = new ArrayList<>();
}
