/*
 * Project Ren @ 2018
 * Rinkako, Ariana, Gordan. SYSU SDCS.
 */
package org.sysu.renResourcing.trigger;

/**
 * Author: Rinkako
 * Date  : 2018/2/5
 * Usage : Interface for class which can be triggered by RTrigger.
 */
public interface RTriggerable {
    /**
     * This method will be called when the Triggerable object
     * was triggered by its binding trigger.
     * @param trigger RTrigger instance.
     * @param payload Triggering payload package
     */
    void Triggered(RTrigger trigger, Object payload);
}
