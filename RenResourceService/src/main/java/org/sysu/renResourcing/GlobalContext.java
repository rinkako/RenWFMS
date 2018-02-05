package org.sysu.renResourcing;

import java.util.UUID;

/**
 * Author: Rinkako
 * Date  : 2017/11/20 0020
 * Usage :
 */
public class GlobalContext {
    /**
     * Resource service micro-service global id.
     */
    public static String RESOURCE_SERVICE_GLOBAL_ID = UUID.randomUUID().toString();

    /**
     * Response message of successfully queued request.
     */
    public static final String RESPONSE_SUCCESS = "OK";

    /**
     * Success notification string of Observable object.
     */
    public static final String OBSERVABLE_NOTIFY_SUCCESS = "success";

    /**
     * Success notification string of Observable object.
     */
    public static final String OBSERVABLE_NOTIFY_EXCEPTION = "exception";

    /**
     * Concurrent control: max active tracker.
     */
    public static int CONCURRENT_MAX_TRACKER = 100;
}