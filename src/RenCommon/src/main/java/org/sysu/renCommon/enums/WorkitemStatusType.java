/*
 * Project Ren @ 2018
 * Rinkako, Ariana, Gordan. SYSU SDCS.
 */
package org.sysu.renCommon.enums;

import java.io.Serializable;

/**
 * Author: Rinkako
 * Date  : 2018/2/6
 * Usage : Enum for workitem execution status.
 */
public enum WorkitemStatusType implements Serializable {
    Enabled,
    Fired,
    Executing,
    Complete,
    ForcedComplete,
    Failed,
    Suspended,
    Discarded
}
