/*
 * Project Ren @ 2018
 * Rinkako, Ariana, Gordan. SYSU SDCS.
 */
package org.sysu.renCommon.enums;

import java.io.Serializable;

/**
 * Author: Rinkako
 * Date  : 2018/2/4
 * Usage : Enum of initialization by.
 */
public enum InitializationByType implements Serializable {
    // Allocate by user
    USER_INITIATED,
    // Allocate by rs engine
    SYSTEM_INITIATED
}
