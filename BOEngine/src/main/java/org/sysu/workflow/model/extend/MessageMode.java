/*
 * Project Ren @ 2018
 * Rinkako, Ariana, Gordan. SYSU SDCS.
 */
package org.sysu.workflow.model.extend;

/**
 * Author: Rinkako
 * Date  : 2017/6/15
 * Usage : Enum of send message destination type.
 */
public enum MessageMode {
    BROADCAST,
    TO_OFFSPRING,
    TO_CHILD,
    TO_SIBLING,
    TO_ANCESTOR,
    MULTICAST,
    TO_PARENT,
    UNICAST,
    TO_NOTIFIABLE_ID
}

