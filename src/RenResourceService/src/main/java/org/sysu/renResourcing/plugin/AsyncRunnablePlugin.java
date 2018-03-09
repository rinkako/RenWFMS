/*
 * Project Ren @ 2018
 * Rinkako, Ariana, Gordan. SYSU SDCS.
 */
package org.sysu.renResourcing.plugin;

/**
 * Author: Rinkako
 * Date  : 2018/2/3
 * Usage : Basic class for running asynchronously plugins.
 */
public abstract class AsyncRunnablePlugin extends RTrackablePlugin implements Runnable {

    /**
     * Run plugin asynchronously.
     * @see Thread#run()
     */
    @Override
    public abstract void run();
}
