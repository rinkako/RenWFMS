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
     * Transaction action key in argument dict.
     */
    public static final String TRANSACTION_ACTION_KEY = "?action";

    public static final String TRANSACTION_EXECUTOR_SUCCESS = "success";

    public static final String TRANSACTION_EXECUTOR_FAILED = "fail";

    /**
     * Size for log buffer.
     */
    public static final int LOG_BUFFER_SIZE = 10;

    /**
     * Size of scheduler concurrent executing transaction.
     */
    public static int CONCURRENT_MAX_EXECUTING_TRANSACTION = 100;

    /**
     * Is concurrent control is enabled.
     */
    public static ConcurrentControlType CONCURRENT_CONTROL_TYPE = ConcurrentControlType.None;
}
