/*
 * Project Ren @ 2018
 * Rinkako, Ariana, Gordan. SYSU SDCS.
 */
package org.sysu.renResourcing;

import org.sysu.renResourcing.context.ResourcingContext;
import org.sysu.renResourcing.utility.LogUtil;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.PriorityBlockingQueue;

/**
 * Author: Rinkako
 * Date  : 2018/2/5
 * Usage : This class is a global singleton class, which is responsible for engine
 *         service request handle scheduling. All type of resourcing services will
 *         all be encapsulated as a {@code ResourcingContext}, and a {@code RTracker}
 *         will be created for controlling its lifecycle.
 */
public final class RScheduler implements Observer {
    /**
     * Global static instance.
     */
    private static RScheduler syncObject = new RScheduler();

    /**
     * Container list of active trackers.
     */
    private final ArrayList<RTracker> trackerVector = new ArrayList<>();

    /**
     * Pending resourcing request context queue.
     * This field is thread safe.
     */
    private PriorityBlockingQueue<ResourcingContext> pendingQueue = new PriorityBlockingQueue<>();

    /**
     * Schedule a resourcing context.
     */
    public void Schedule(ResourcingContext context) {
        if (context == null) {
            LogUtil.Log("Schedule null context, ignored.",
                    RScheduler.class.getName(), LogUtil.LogLevelType.WARNING, "");
            return;
        }
        try {
            this.pendingQueue.add(context);

            this.HandlePendingQueue();
        }
        catch (Exception ex) {
            // todo rtid is not unique for a resourcing request
            LogUtil.Log("Schedule resourcing context exception occurred, " + ex,
                    RScheduler.class.getName(), LogUtil.LogLevelType.ERROR, "");
        }
    }

    /**
     * Process the pending queue, until active tracker exceed concurrent control threshold.
     */
    private void HandlePendingQueue() {

    }

    /**
     * Get the global unique instance of Scheduler.
     * @return RScheduler object.
     */
    public static RScheduler GetInstance() {
        return RScheduler.syncObject;
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
     * Create a new scheduler.
     * Private accessibility for preventing construct outside.
     */
    private RScheduler() { }
}
