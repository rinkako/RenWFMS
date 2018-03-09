package org.sysu.renCommon.enums;/*
 * Project Ren @ 2018
 * Rinkako, Ariana, Gordan. SYSU SDCS.
 */

import java.io.Serializable;

/**
 * Author: Rinkako
 * Date  : 2018/2/3
 * Usage : Enum of RTracker phase.
 */
public enum TrackerPhase implements Serializable {
    Ready,
    Running,
    Steadying,
    Finished,
    Killed,
    Failed
}
