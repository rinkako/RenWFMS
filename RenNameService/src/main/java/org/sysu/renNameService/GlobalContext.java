/*
 * Project Ren @ 2018
 * Rinkako, Ariana, Gordan. SYSU SDCS.
 */
package org.sysu.renNameService;
import org.sysu.renNameService.transaction.ConcurrentControlType;
import java.util.UUID;

/**
 * Usage : This class is used to store runtime context.
 */
public final class GlobalContext {
    /**
     * Name service micro-service global id.
     */
    public static String NAME_SERVICE_GLOBAL_ID = UUID.randomUUID().toString();

    /**
     * Transaction action key in argument dict.
     */
    public static final String TRANSACTION_ACTION_KEY = "?action";

    /**
     * Transaction executor success flag string.
     */
    public static final String TRANSACTION_EXECUTOR_SUCCESS = "success";

    /**
     * Transaction executor fail flag string.
     */
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
