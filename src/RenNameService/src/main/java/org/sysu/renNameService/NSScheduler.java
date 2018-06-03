/*
 * Project Ren @ 2018
 * Rinkako, Ariana, Gordan. SYSU SDCS.
 */
package org.sysu.renNameService;
import org.sysu.renCommon.utility.TimestampUtil;
import org.sysu.renNameService.transaction.NameServiceTransaction;
import org.sysu.renNameService.utility.LogUtil;

import java.util.HashSet;
import java.util.Hashtable;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Author: Rinkako
 * Date  : 2018/1/24
 * Usage : Name service scheduler for concurrent control.
 */
public class NSScheduler implements Observer {
    /**
     * Get the name service scheduler.
     * @return {@code NSScheduler} global unique instance
     */
    public static NSScheduler GetInstance() {
        return NSScheduler.syncObject;
    }

    /**
     * Schedule a name service transaction and process pending queue.
     * @param nst NameServiceTransaction instance
     */
    public Object Schedule(NameServiceTransaction nst) {
        switch (GlobalContext.CONCURRENT_CONTROL_TYPE) {
            case None:
                return this.ScheduleSync(nst);
            case StandaloneControl:
                this.TransactionQueue.add(nst);
                LogUtil.Log(String.format("Schedule NSTransaction to queue: %s", nst.getTransactionContext().getNsid()),
                        NSScheduler.class.getName(), nst.getTransactionContext().getRtid());
                this.HandlePendingTransaction();
                break;
            case GlobalControl:
                // todo
                break;
        }
        return null;
    }

    /**
     * Process the pending queue, until active tracker exceed limitation.
     */
    private synchronized void HandlePendingTransaction() {
        this.executingSetLock.lock();
        while (this.ExecutingTransactionSet.size() < GlobalContext.CONCURRENT_MAX_EXECUTING_TRANSACTION) {
            if (this.TransactionQueue.isEmpty()) {
                return;
            }
            NameServiceTransaction nst = this.TransactionQueue.poll();
            this.LaunchTransaction(nst);
        }
        this.executingSetLock.unlock();
    }

    /**
     * Actually handle a name service transaction launching process by transaction tracker.
     * @param nst NameServiceTransaction instance
     */
    private void LaunchTransaction(NameServiceTransaction nst) {

    }

    /**
     * Synchronously handle a name service transaction launching process.
     * @param nst NameServiceTransaction instance
     * @return execution result
     */
    public Object ScheduleSync(NameServiceTransaction nst) {
//        LogUtil.Log(String.format("NSTransaction is scheduled to execute: %s", nst.getTransactionContext().getNsid()),
//                NSScheduler.class.getName(), nst.getTransactionContext().getRtid());
        nst.getTransactionContext().setScheduledTimestamp(TimestampUtil.GetCurrentTimestamp());
        NSExecutor executor = new NSExecutor(this);
        this.executingSetLock.lock();
        this.ExecutingTransactionSet.add(nst);
        this.executingSetLock.unlock();
        return executor.ExecuteSync(nst);
    }

    /**
     * This method is called whenever the observed object is changed. An
     * application calls an <tt>Observable</tt> object's
     * <code>notifyObservers</code> method to have all the object's
     * observers notified of the change.
     * @param o the observable object.
     * @param arg an argument passed to the <code>notifyObservers</code>
     */
    @Override
    @SuppressWarnings("unchecked")
    public void update(Observable o, Object arg) {
        if (arg != null) {
            Hashtable<String, Object> result = (Hashtable<String, Object>) arg;
            String execType = (String) result.get("execType");
            NameServiceTransaction transaction = (NameServiceTransaction) result.get("context");
            switch (execType) {
                case "BusinessRoleMapping":
                    if (result.get("execCode").equals(GlobalContext.TRANSACTION_EXECUTOR_SUCCESS)) {
                        LogUtil.Log(String.format("RoleMap NSTransaction is finished: %s (%s)", result.get("nsid"), result.get("action")),
                                NSScheduler.class.getName(), (String) result.get("rtid"));
                    }
                    else {
                        LogUtil.Log(String.format("RoleMap NSTransaction is failed: %s (%s)", result.get("nsid"), result.get("action")),
                                NSScheduler.class.getName(), (String) result.get("rtid"));
                    }
                    break;
                case "Namespacing":
                    String ns_nsid = (String) result.get("nsid");
                    if (result.get("execCode").equals(GlobalContext.TRANSACTION_EXECUTOR_SUCCESS)) {
                        LogUtil.Log(String.format("NameSpacing NSTransaction is finished: %s (%s)", ns_nsid == null ? "" : ns_nsid, result.get("action")),
                                NSScheduler.class.getName(), (String) result.get("rtid"));
                    }
                    else {
                        LogUtil.Log(String.format("NameSpacing NSTransaction is failed: %s (%s)", ns_nsid == null ? "" : ns_nsid, result.get("action")),
                                NSScheduler.class.getName(), (String) result.get("rtid"));
                    }
                    break;
            }
            this.executingSetLock.lock();
            this.ExecutingTransactionSet.remove(transaction);
            this.executingSetLock.unlock();
        }
    }

    /**
     * Private constructor for singleton scheduler.
     */
    private NSScheduler() { }

    /**
     * Pending transaction queue.
     */
    private PriorityBlockingQueue<NameServiceTransaction> TransactionQueue = new PriorityBlockingQueue<>();

    /**
     * Executing transaction set.
     */
    private HashSet<NameServiceTransaction> ExecutingTransactionSet = new HashSet<>();

    /**
     * Lock for executing set.
     */
    private ReentrantLock executingSetLock = new ReentrantLock();

    /**
     * Scheduler global unique instance.
     */
    private static final NSScheduler syncObject = new NSScheduler();
}
