/*
 * Project Ren @ 2018
 * Rinkako, Ariana, Gordan. SYSU SDCS.
 */
package org.sysu.renNameService;
import org.sysu.renNameService.entity.RenNsTransactionEntity;
import org.sysu.renNameService.pattern.NObservable;
import org.sysu.renNameService.pattern.NObserver;
import org.sysu.renNameService.transaction.NameServiceTransaction;
import org.sysu.renNameService.transaction.TransactionType;
import org.sysu.renNameService.utility.LogUtil;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import java.util.HashSet;
import java.util.concurrent.PriorityBlockingQueue;

/**
 * Author: Rinkako
 * Date  : 2018/1/24
 * Usage : Name service scheduler for concurrent control.
 */
public class NSScheduler extends NObserver {
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
    public void Schedule(NameServiceTransaction nst) {
        switch (GlobalConfigContext.CONCURRENT_CONTROL_TYPE) {
            case None:
                this.LaunchTransactionDirectly(nst);
                break;
            case StandaloneControl:
                this.TransactionQueue.add(nst);
                LogUtil.Log(String.format("Schedule NSTransaction to queue: %s", nst.getTransactionContext().getNsid()),
                        NSScheduler.class.getName(), nst.getTransactionContext().getRtid());
                this.HandlePendingTransaction();
                break;
            case GlobalControl:
                throw new NotImplementedException();
        }
    }

    /**
     * This method will be called when this observer is notified.
     * by its subjecting observable object.
     * @param notifier notifier reference
     * @param tag notify message package
     */
    @Override
    public void Notified(NObservable notifier, Object tag) {

    }

    /**
     * Process the pending queue, until active tracker exceed limitation.
     */
    private synchronized void HandlePendingTransaction() {
        while (this.ExecutingTransactionSet.size() < GlobalConfigContext.SCHEDULER_MAX_EXECUTING_TRANSACTION) {
            if (this.TransactionQueue.isEmpty()) {
                return;
            }
            NameServiceTransaction nst = this.TransactionQueue.poll();
            this.LaunchTransaction(nst);
        }
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
     */
    private void LaunchTransactionDirectly(NameServiceTransaction nst) {
        RenNsTransactionEntity context = nst.getTransactionContext();
        try {
            TransactionType tType = TransactionType.values()[context.getType()];
            switch (tType) {
                case BusinessRoleMapping:
                    break;
                case Namespacing:
                    throw new NotImplementedException();
                default:
                    LogUtil.Log("Launch transaction directly failed, wrong type code", NSScheduler.class.getName(),
                            LogUtil.LogLevelType.WARNNING, context.getRtid());
            }
        }
        catch (Exception ex) {
            LogUtil.Log("Launch transaction directly failed, " + ex, NSScheduler.class.getName(),
                    LogUtil.LogLevelType.ERROR, context.getRtid());
        }
    }

    /**
     * Private constructor for singleton scheduler.
     */
    private NSScheduler() {
        super(NSScheduler.class.getName());
    }

    /**
     * Pending transaction queue.
     */
    private PriorityBlockingQueue<NameServiceTransaction> TransactionQueue = new PriorityBlockingQueue<>();

    /**
     * Executing transaction set.
     */
    private HashSet<NameServiceTransaction> ExecutingTransactionSet = new HashSet<>();

    /**
     * Scheduler unique instance.
     */
    private static final NSScheduler syncObject = new NSScheduler();
}
