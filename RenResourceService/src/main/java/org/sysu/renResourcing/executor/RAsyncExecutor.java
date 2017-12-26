package org.sysu.renResourcing.executor;

/**
 * Author: Rinkako
 * Date  : 2017/11/17
 * Usage : This class is an abstract class and inherited by all service executor.
           It provides the common operators for service invoking.
 */
public abstract class RAsyncExecutor implements Runnable {

    public void run() {
        this.ExecuteSync();
    }

    public abstract void ExecuteSync();

    public abstract void Kill();

    public abstract Object GetContext();

}
