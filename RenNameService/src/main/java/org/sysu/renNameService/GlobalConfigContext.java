/*
 * Project Ren @ 2018
 * Rinkako, Ariana, Gordan. SYSU SDCS.
 */
package org.sysu.renNameService;
import org.sysu.renNameService.transaction.ConcurrentControlType;

/**
 * Usage : This class is used to store runtime config context, usually readonly.
 */
public final class GlobalConfigContext {
    /**
     * Size for log buffer.
     */
    public static final int LOG_BUFFER_SIZE = 10;

    /**
     * Size of scheduler concurrent executing transaction.
     */
    public static int SCHEDULER_MAX_EXECUTING_TRANSACTION = 100;

    /**
     * Is concurrent controll is enabled.
     */
    public static ConcurrentControlType CONCURRENT_CONTROL_TYPE = ConcurrentControlType.None;
}
