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

    /**
     * Salt for authority encryption.
     */
    public static final String AUTHORITY_SALT = "bab53e0679c74ren8148b75ea2a7db4e";

    /**
     * Valid duration of authorization token.
     */
    public static final Long AUTHORITY_TOKEN_VALID_SECOND = 2 * 60 * 60L;

    /**
     * Service URL for BO Engine Serialization BO.
     */
    public static final String URL_BOENGINE_SERIALIZEBO = "http://localhost:10232/boengine/serializeBO";
}
