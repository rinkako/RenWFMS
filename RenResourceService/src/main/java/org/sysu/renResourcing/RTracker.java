/*
 * Project Ren @ 2018
 * Rinkako, Ariana, Gordan. SYSU SDCS.
 */
package org.sysu.renResourcing;

import org.sysu.renResourcing.basic.enums.TrackerPhase;
import org.sysu.renResourcing.context.ResourcingContext;

import java.util.Observable;
import java.util.Observer;

/**
 * Author: Rinkako
 * Date  : 2018/2/2
 * Usage : A tracker is used to trace its binding resourcing service executing situation
 *         and control the service life-cycle.
 */
public class RTracker implements Observer {

    /**
     * Create a new tracker.
     */
    public RTracker(ResourcingContext context) {
        this.context = context;
        this.phase = TrackerPhase.Ready;
    }

    /**
     * Get binding tracing context.
     * @return {@code ResourcingContext} instance
     */
    public ResourcingContext getContext() {
        return this.context;
    }

    /**
     * Get current phase of this tracker.
     * @return {@code TrackerPhase} enum
     */
    public TrackerPhase getPhase() {
        return this.phase;
    }

    /**
     * This method is called whenever the observed object is changed. An
     * application calls an <tt>Observable</tt> object's
     * <code>notifyObservers</code> method to have all the object's
     * observers notified of the change.
     * @param o   the observable object.
     * @param arg an argument passed to the <code>notifyObservers</code>
     */
    @Override
    public void update(Observable o, Object arg) {

    }

    /**
     * Tracker binding context.
     */
    private ResourcingContext context;

    /**
     * Current tracker phase.
     */
    private TrackerPhase phase;
}
