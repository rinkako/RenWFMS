package org.sysu.renCommon.enums;/*
 * Project Ren @ 2018
 * Rinkako, Ariana, Gordan. SYSU SDCS.
 */

import java.io.Serializable;

/**
 * Author: Rinkako
 * Date  : 2018/2/21
 * Usage : Enum of Resource binding type.
 */
public enum ResourceBindingType implements Serializable {
    RepeatableRead,
    DirtyRead,
    FastFail
}
