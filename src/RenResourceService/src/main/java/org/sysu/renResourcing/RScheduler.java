/*
 * Project Ren @ 2018
 * Rinkako, Ariana, Gordan. SYSU SDCS.
 */
package org.sysu.renResourcing;

import org.sysu.renCommon.context.ObservableMessage;
import org.sysu.renCommon.enums.LogLevelType;
import org.sysu.renResourcing.consistency.ContextCachePool;
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
public class RScheduler implements Observer {
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
     *
     * @param context resourcing request context to be scheduled
     */
    public void Schedule(ResourcingContext context) {
        if (context == null) {
            LogUtil.Log("Schedule null context, ignored.",
                    RScheduler.class.getName(), LogLevelType.WARNING, "");
            return;
        }
        try {
            this.pendingQueue.add(context);
            LogUtil.Log(String.format("Schedule resourcing context(%s) to pending queue, current queue length is %s",
                    context.getRstid(), this.pendingQueue.size()), RScheduler.class.getName(),
                    LogLevelType.INFO, context.getRtid());
            this.HandlePendingQueue();
        } catch (Exception ex) {
            LogUtil.Log(String.format("Schedule resourcing context(%s) exception occurred, %s", context.getRstid(), ex),
                    RScheduler.class.getName(), LogLevelType.ERROR, context.getRtid());
            throw ex;  // re thrown to controller
        }
    }

    /**
     * Schedule a resourcing context synchronously and wait for return value.
     *
     * @param context resourcing request context to be scheduled
     * @return response package in json encoded string
     */
    public String ScheduleSync(ResourcingContext context) {
        if (context == null) {
            LogUtil.Log("Schedule null context, ignored.",
                    RScheduler.class.getName(), LogLevelType.WARNING, "");
            return null;
        }
        try {
            context.SetScheduled();
            RTracker tracker = new RTracker(context);
            tracker.addObserver(this);
            tracker.run();
            return context.getExecutionResult();
        } catch (Exception ex) {
            LogUtil.Log("Exception occurred when schedule context synchronously, " + ex,
                    RScheduler.class.getName(), LogLevelType.ERROR, context.getRtid());
            throw ex;
        }
    }

    /**
     * Get pending resourcing requests count.
     *
     * @return length of pending queue
     */
    public int GetPendingCount() {
        this.handlePendingLock.lock();
        try {
            return this.pendingQueue.size();
        } finally {
            this.handlePendingLock.unlock();
        }
    }

    /**
     * Process the pending queue, until active tracker exceed concurrent control threshold.
     */
    private void HandlePendingQueue() {
        this.handlePendingLock.lock();
        try {
            while (this.trackerVector.size() < GlobalContext.CONCURRENT_MAX_TRACKER) {
                ResourcingContext pCtx = this.pendingQueue.poll();
                if (pCtx != null) {
                    LogUtil.Log(String.format("Resourcing context(%s) is scheduled to launch.", pCtx.getRstid()),
                            RScheduler.class.getName(), LogLevelType.INFO, pCtx.getRtid());
                    pCtx.SetScheduled();
                    ResourcingContext.SaveToSteady(pCtx);
                    this.LaunchTracker(pCtx);
                } else {
                    break;
                }
            }
        } finally {
            this.handlePendingLock.unlock();
        }
    }

    /**
     * Actually handle a workitem launching.
     *
     * @param context resourcing request context to be fired
     */
    private void LaunchTracker(ResourcingContext context) {
        RTracker tracker = new RTracker(context);
        this.trackerVectorLock.lock();
        try {
            this.trackerVector.add(tracker);
        } finally {
            this.trackerVectorLock.unlock();
        }
        tracker.addObserver(this);
        new Thread(tracker).start();
    }

    /**
     * Get the global unique instance of Scheduler.
     *
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
     *
     * @param o   the observable object.
     * @param arg an argument passed to the <code>notifyObservers</code>
     */
    @Override
    public void update(Observable o, Object arg) {
        RTracker tracker = (RTracker) o;
        ObservableMessage obMsg = (ObservableMessage) arg;
        this.trackerVectorLock.lock();
        try {
            this.trackerVector.remove(tracker);
        } finally {
            this.trackerVectorLock.unlock();
        }
        ResourcingContext rCtx = tracker.getContext();
        try {
            rCtx.SetFinish();
            switch (obMsg.getCode()) {
                case GlobalContext.OBSERVABLE_NOTIFY_SUCCESS:
                    rCtx.setIsSucceed(1);
                    break;
                case GlobalContext.OBSERVABLE_NOTIFY_EXCEPTION:
                    rCtx.setIsSucceed(-1);
                    break;
                default:
                    rCtx.setIsSucceed(0);
                    break;
            }
            ResourcingContext.SaveToSteady(rCtx);
            ContextCachePool.Remove(ResourcingContext.class, rCtx.getRstid());
        } catch (Exception ex) {
            try {
                LogUtil.Log(String.format("Achieving resourcing context(%s: %s) but exception occurred, %S",
                        rCtx.getService().name(), rCtx.getRstid(), ex), RScheduler.class.getName(),
                        LogLevelType.ERROR, rCtx.getRtid());
            } catch (Exception ex2) {
                LogUtil.Log(String.format("Achieving resourcing context but exception occurred: %s, %s", ex, ex2),
                        RScheduler.class.getName(), LogLevelType.ERROR, "");
            }
        } finally {
            this.HandlePendingQueue();
        }
    }

    /**
     * Create a new scheduler.
     * Private accessibility for preventing construct outside.
     */
    private RScheduler() { }
}