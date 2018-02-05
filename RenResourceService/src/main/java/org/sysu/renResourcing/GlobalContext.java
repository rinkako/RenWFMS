package org.sysu.renResourcing;

import org.sysu.renResourcing.log.RLogger;

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

    public static RLogger EngineLogger;
}
