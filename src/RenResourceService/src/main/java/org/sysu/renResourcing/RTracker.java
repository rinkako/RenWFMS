/*
 * Project Ren @ 2018
 * Rinkako, Ariana, Gordan. SYSU SDCS.
 */
package org.sysu.renResourcing;

import org.sysu.renCommon.context.ObservableMessage;
import org.sysu.renCommon.enums.LogLevelType;
import org.sysu.renCommon.enums.TrackerPhase;
import org.sysu.renResourcing.context.ResourcingContext;
import org.sysu.renResourcing.interfaceService.InterfaceB;
import org.sysu.renResourcing.interfaceService.InterfaceW;
import org.sysu.renResourcing.utility.LogUtil;
import org.sysu.renCommon.utility.SerializationUtil;

import java.util.Observable;
import java.util.Observer;

/**
 * Author: Rinkako
 * Date  : 2018/2/2
 * Usage : A tracker is used to trace its binding resourcing service
 *         executing situation and control the service life-cycle.
 *         Tracker is running asynchronously, and when it is needed,
 *         it will notify the main scheduler about the changes.
 */
public class RTracker extends Observable implements Observer, Runnable {

    /**
     * Create a new tracker.
     */
    RTracker(ResourcingContext context) {
        this.context = context;
        this.phase = TrackerPhase.Ready;
    }

    /**
     * Get binding tracing context.
     *
     * @return {@code ResourcingContext} instance
     */
    public ResourcingContext getContext() {
        return this.context;
    }

    /**
     * Get current phase of this tracker.
     *
     * @return {@code TrackerPhase} enum
     */
    public TrackerPhase getPhase() {
        return this.phase;
    }

    /**
     * When an object implementing interface <code>Runnable</code> is used
     * to create a thread, starting the thread causes the object's
     * <code>run</code> method to be called in that separately executing
     * thread.
     * <p>
     * The general contract of the method <code>run</code> is that it may
     * take any action whatsoever.
     *
     * @see Thread#run()
     */
    @Override
    public void run() {
        // Execute the actual resourcing task algorithm
        ObservableMessage obm = new ObservableMessage(GlobalContext.OBSERVABLE_NOTIFY_SUCCESS);
        try {
            this.ActualRun();
            this.phase = TrackerPhase.Finished;
        }
        // All exception inside tracker will be handle here, no need to throw outside since it asynchronous executed.
        catch (Exception ex) {
            LogUtil.Log(String.format("Tracker(%s) exception occurred, %s", this.context.getRstid(), ex),
                    RTracker.class.getName(), LogLevelType.ERROR, this.context.getRtid());
            obm = new ObservableMessage(GlobalContext.OBSERVABLE_NOTIFY_EXCEPTION);
            obm.AddPayload("message", ex.toString());
            this.phase = TrackerPhase.Failed;
        }
        // bubble notification to Scheduler
        finally {
            this.setChanged();
            this.notifyObservers(obm);
        }
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

    }

    /**
     * Actually handle resourcing request.
     */
    private void ActualRun() throws Exception {
        Object execResult = "";
        switch (this.context.getService()) {
            case SubmitResourcingTask:
                InterfaceB.PerformEngineSubmitTask(this.context);
                break;
            case FinishProcess:
                InterfaceB.PerformEngineFinishProcess(this.context);
                break;
            case AcceptWorkitem:
                execResult = InterfaceW.AcceptOffer(this.context);
                break;
            case StartWorkitem:
                execResult = InterfaceW.Start(this.context);
                break;
            case AcceptAndStartWorkitem:
                execResult = InterfaceW.AcceptAndStart(this.context);
                break;
            case CompleteWorkitem:
                execResult = InterfaceW.Complete(this.context);
                break;
            case SuspendWorkitem:
                execResult = InterfaceW.Suspend(this.context);
                break;
            case UnsuspendWorkitem:
                execResult = InterfaceW.Unsuspend(this.context);
                break;
            case SkipWorkitem:
                execResult = InterfaceW.Skip(this.context);
                break;
            case ReallocateWorkitem:
                execResult = InterfaceW.Reallocate(this.context);
                break;
            case DeallocateWorkitem:
                execResult = InterfaceW.Deallocate(this.context);
                break;
            case GetQueue:
                execResult = InterfaceW.GetWorkQueue(this.context);
                break;
            case GetQueueList:
                execResult = InterfaceW.GetWorkQueueList(this.context);
                break;
            case GetAllWorkitemsByRTID:
                execResult = InterfaceW.GetAllActiveWorkitemsInUserFriendly(this.context);
                break;
            case GetAllWorkitemsByDomain:
                execResult = InterfaceW.GetAllWorkitemsInUserFriendlyForDomain(this.context);
                break;
            case GetAllWorkitemsByParticipant:
                execResult = InterfaceW.GetAllWorkitemsInUserFriendlyForParticipant(this.context);
                break;
            case GetByWid:
                execResult = InterfaceW.GetWorkitemInFriendly(this.context);
                break;
        }
        this.context.setExecutionResult(SerializationUtil.JsonSerialization(execResult));
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
