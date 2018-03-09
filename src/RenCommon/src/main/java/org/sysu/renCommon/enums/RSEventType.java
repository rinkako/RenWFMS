/*
 * Project Ren @ 2018
 * Rinkako, Ariana, Gordan. SYSU SDCS.
 */
package org.sysu.renCommon.enums;

/**
 * Author: Rinkako
 * Date  : 2018/2/10
 * Usage : Enum of Resource event type
 */
public enum  RSEventType {
    offer,
    allocate,
    start,
    suspend,
    deallocate,
    delegate,
    reallocate,
    skip,
    cancel,
    complete,
    unoffer,
    resume,
    exception_principle,
    exception_lifecycle
}
