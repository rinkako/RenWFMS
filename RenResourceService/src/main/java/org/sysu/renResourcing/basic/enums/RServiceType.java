package org.sysu.renResourcing.basic.enums;

import java.io.Serializable;

/**
 * Author: Rinkako
 * Date  : 2017/11/14
 * Usage : Enum of RenResourcing services.
 */
public enum RServiceType implements Serializable {
    SubmitResourcingTask,
    AcceptWorkitem,
    StartWorkitem,
    AcceptAndStartWorkitem,
    CompleteWorkitem,
    SuspendWorkitem,
    UnsuspendWorkitem,
    SkipWorkitem,
    DeallocateWorkitem,
    ReallocateWorkitem
}
