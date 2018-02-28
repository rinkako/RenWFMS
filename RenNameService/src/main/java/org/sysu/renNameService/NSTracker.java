/*
 * Project Ren @ 2018
 * Rinkako, Ariana, Gordan. SYSU SDCS.
 */
package org.sysu.renNameService;
import org.sysu.renCommon.enums.TrackerPhase;
import org.sysu.renNameService.transaction.NameServiceTransaction;
import org.sysu.renNameService.utility.LogUtil;

import java.util.Observable;
import java.util.Observer;

/**
 * Author: Rinkako
 * Date  : 2018/1/27
 * Usage : Tracker is used to manage the queued name service transaction and
 *         supervise its context and executor, and always be supervised by
 *         the main scheduler.
 */
public class NSTracker extends Observable implements Observer {

    /**
     * Create a new asynchronously tracker.
     * @param supervisorScheduler scheduler instance to supervise tracker
     */
    public NSTracker(Observer supervisorScheduler) {
        assert supervisorScheduler instanceof NSScheduler;
        this.addObserver(supervisorScheduler);
    }

    /**
     * Execute a transaction asynchronously.
     * @param nsTransaction transaction context to be executed.
     */
    public void ExecuteAsync(NameServiceTransaction nsTransaction) {
        if (nsTransaction == null) {
            LogUtil.Log("A null NSTransaction is scheduled to launch, tracker ignored.",
                    NSTracker.class.getName(), LogUtil.LogLevelType.WARNING, "");
            return;
        }
        this.context = nsTransaction;
        this.phase = TrackerPhase.Running;
        // todo here create executor to handle service
    }

    /**
     * Get the tracker runtime record id.
     * @return rtid string
     */
    public String GetTrackerRtid() {
        assert this.context != null;
        return this.context.getTransactionContext().getRtid();
    }

    /**
     * Get the tracker context transaction.
     * @return NameServiceTransaction instance.
     */
    public NameServiceTransaction getContext() {
        return this.context;
    }

    /**
     * Get current phase of this tracker.
     * @return tracker phase enum
     */
    public TrackerPhase getPhase() {
        return this.phase;
    }

    /**
     * This method will be called when the binding executor state is changed.
     * @param o the observable object.
     * @param arg an argument passed to the <code>notifyObservers</code>
     */
    @Override
    public void update(Observable o, Object arg) {

    }

    /**
     * Binding transaction context.
     */
    private NameServiceTransaction context;

    /**
     * Tracker current phase.
     */
    private TrackerPhase phase = TrackerPhase.Ready;
}
