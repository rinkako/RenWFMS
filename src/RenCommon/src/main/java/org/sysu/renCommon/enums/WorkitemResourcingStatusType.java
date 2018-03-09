/*
 * Project Ren @ 2018
 * Rinkako, Ariana, Gordan. SYSU SDCS.
 */
package org.sysu.renCommon.enums;

import java.io.Serializable;

/**
 * Author: Rinkako
 * Date  : 2018/2/6
 * Usage : Enum of workitem resourcing status.
 */
public enum  WorkitemResourcingStatusType implements Serializable {
    Offered,
    Allocated,
    Started,
    Suspended,
    Unoffered,
    Unresourced,
    Completed,
    Skipped
}
