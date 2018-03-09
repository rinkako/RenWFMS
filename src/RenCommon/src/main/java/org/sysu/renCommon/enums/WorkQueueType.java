/*
 * Project Ren @ 2018
 * Rinkako, Ariana, Gordan. SYSU SDCS.
 */
package org.sysu.renCommon.enums;

import java.io.Serializable;

/**
 * Author: Rinkako
 * Date  : 2018/2/3
 * Usage : Enum of work queue type.
 */
public enum WorkQueueType implements Serializable {
    UNDEFINED,
    OFFERED,
    ALLOCATED,
    STARTED,
    SUSPENDED,
    UNOFFERED,
    WORKLISTED
}
