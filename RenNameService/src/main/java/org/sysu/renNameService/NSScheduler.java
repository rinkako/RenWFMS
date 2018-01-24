/*
 * Project Ren @ 2018
 * Rinkako, Ariana, Gordan. SYSU SDCS.
 */
package org.sysu.renNameService;
import org.sysu.renNameService.transaction.NameServiceTransaction;
import org.sysu.renNameService.transaction.TransactionType;
import org.sysu.renNameService.utility.LogUtil;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.sql.Timestamp;
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
        switch (GlobalConfigContext.CONCURRENT_CONTROL_TYPE) {
            case None:
                return this.LaunchTransactionDirectly(nst);
            case StandaloneControl:
                this.TransactionQueue.add(nst);
                LogUtil.Log(String.format("Schedule NSTransaction to queue: %s", nst.getTransactionContext().getNsid()),
                        NSScheduler.class.getName(), nst.getTransactionContext().getRtid());
                this.HandlePendingTransaction();
                break;
            case GlobalControl:
                throw new NotImplementedException();
        }
        return null;
    }

    /**
     * Process the pending queue, until active tracker exceed limitation.
     */
    private synchronized void HandlePendingTransaction() {
        this.executingSetLock.lock();
        while (this.ExecutingTransactionSet.size() < GlobalConfigContext.CONCURRENT_MAX_EXECUTING_TRANSACTION) {
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
     * Actually handle a name service transaction launching process directly.
     * @param nst NameServiceTransaction instance
     * @return execution result
     */
    private Object LaunchTransactionDirectly(NameServiceTransaction nst) {
        LogUtil.Log(String.format("NSTransaction is scheduled to execute: %s", nst.getTransactionContext().getNsid()),
                NSScheduler.class.getName(), nst.getTransactionContext().getRtid());
        nst.getTransactionContext().setScheduledTimestamp(new Timestamp(System.currentTimeMillis()));
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
     * @param o   the observable object.
     * @param arg an argument passed to the <code>notifyObservers</code>
     */
    @Override
    @SuppressWarnings("unchecked")
    public void update(Observable o, Object arg) {
        Hashtable<String, Object> result = null;
        if (arg != null) {
            result = (Hashtable<String, Object>) arg;
            if (result.get("execType").equals(TransactionType.BusinessRoleMapping.name())) {
                NameServiceTransaction nst = (NameServiceTransaction) result.get("context");
                if (result.get("execCode").equals(GlobalConfigContext.TRANSACTION_EXECUTOR_SUCCESS)) {
                    LogUtil.Log(String.format("NSTransaction is finished: %s (%s)", result.get("nsid"), result.get("action")),
                            NSScheduler.class.getName(), (String) result.get("rtid"));
                }
                else {
                    LogUtil.Log(String.format("NSTransaction is failed: %s (%s)", result.get("nsid"), result.get("action")),
                            NSScheduler.class.getName(), (String) result.get("rtid"));
                }
                this.executingSetLock.lock();
                this.ExecutingTransactionSet.remove(nst);
                this.executingSetLock.unlock();
            }
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
     * Scheduler unique instance.
     */
    private static final NSScheduler syncObject = new NSScheduler();
}
