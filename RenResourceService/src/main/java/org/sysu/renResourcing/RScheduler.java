/*
 * Project Ren @ 2018
 * Rinkako, Ariana, Gordan. SYSU SDCS.
 */
package org.sysu.renResourcing;

import org.sysu.renResourcing.cache.RuntimeContextCachePool;
import org.sysu.renResourcing.context.ResourcingContext;
import org.sysu.renResourcing.utility.LogUtil;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.locks.ReentrantLock;

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
     * Fair reentrant lock for pending queue.
     */
    private final ReentrantLock handlePendingLock = new ReentrantLock(true);

    /**
     * Fair reentrant lock for tracker vector.
     */
    private final ReentrantLock trackerVectorLock = new ReentrantLock(true);

    /**
     * Pending resourcing request context queue.
     * This field is thread safe.
     */
    private PriorityBlockingQueue<ResourcingContext> pendingQueue = new PriorityBlockingQueue<>();

    /**
     * Schedule a resourcing context.
     * @param context resourcing request context to be scheduled
     */
    public void Schedule(ResourcingContext context) {
        if (context == null) {
            LogUtil.Log("Schedule null context, ignored.",
                    RScheduler.class.getName(), LogUtil.LogLevelType.WARNING, "");
            return;
        }
        try {
            this.pendingQueue.add(context);
            LogUtil.Log(String.format("Schedule resourcing context(%s) to pending queue, current queue length is %s",
                    context.getRstid(), this.pendingQueue.size()), RScheduler.class.getName(),
                    LogUtil.LogLevelType.INFO, context.getRtid());
            this.HandlePendingQueue();
        }
        catch (Exception ex) {
            LogUtil.Log(String.format("Schedule resourcing context(%s) exception occurred, %s", context.getRstid(), ex),
                    RScheduler.class.getName(), LogUtil.LogLevelType.ERROR, context.getRtid());
            throw ex;  // re throw to ResourcingEngine
        }
    }

    /**
     * Process the pending queue, until active tracker exceed concurrent control threshold.
     */
    private void HandlePendingQueue() {
        this.handlePendingLock.lock();
        try {
            while (this.pendingQueue.size() < GlobalContext.CONCURRENT_MAX_TRACKER) {
                ResourcingContext pCtx = this.pendingQueue.poll();
                if (pCtx != null) {
                    LogUtil.Log(String.format("Resourcing context(%s) is scheduled to launch.", pCtx.getRstid()),
                            RScheduler.class.getName(), LogUtil.LogLevelType.INFO, pCtx.getRtid());
                    pCtx.SetScheduled();
                    this.LaunchTracker(pCtx);
                } else {
                    break;
                }
            }
        }
        finally {
            this.handlePendingLock.unlock();
        }
    }

    /**
     * Actually handle a workitem launching.
     * @param context resourcing request context to be fired
     */
    private void LaunchTracker(ResourcingContext context) {
        RTracker tracker = new RTracker(context);
        this.trackerVectorLock.lock();
        try {
            this.trackerVector.add(tracker);
        }
        finally {
            this.trackerVectorLock.unlock();
        }
        new Thread(tracker).start();
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
        assert o instanceof RTracker;
        RTracker tracker = (RTracker) o;
        this.trackerVectorLock.lock();
        try {
            this.trackerVector.remove(tracker);
        }
        finally {
            this.trackerVectorLock.unlock();
        }
        ResourcingContext rCtx = tracker.getContext();
        try {
            rCtx.SetFinish();
            ResourcingContext.SaveToSteady(rCtx);
            RuntimeContextCachePool.Remove(ResourcingContext.class, rCtx.getRstid());
        }
        catch (Exception ex) {
            try {
                LogUtil.Log(String.format("Achieving resourcing context(%s: %s) but exception occurred, %S",
                        rCtx.getService().name(), rCtx.getRstid(), ex), RScheduler.class.getName(),
                        LogUtil.LogLevelType.ERROR, rCtx.getRtid());
            }
            catch (Exception ex2) {
                LogUtil.Log(String.format("Achieving resourcing context but exception occurred: %s, %s", ex, ex2),
                        RScheduler.class.getName(), LogUtil.LogLevelType.ERROR, "");
            }
        }
    }

    /**
     * Create a new scheduler.
     * Private accessibility for preventing construct outside.
     */
    private RScheduler() { }
}
