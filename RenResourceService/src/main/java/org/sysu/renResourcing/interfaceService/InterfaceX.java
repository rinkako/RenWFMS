/*
 * Project Ren @ 2018
 * Rinkako, Ariana, Gordan. SYSU SDCS.
 */
package org.sysu.renResourcing.interfaceService;

import org.sysu.renResourcing.context.WorkitemContext;

/**
 * Author: Rinkako
 * Date  : 2018/2/9
 * Usage : Implementation of Interface X of Resource Service.
 *         Interface X is responsible for process exception handling.
 */
public class InterfaceX {

    /**
     * Signal a workitem is failed, and redirect it to its admin launcher exception workitem pool.
     * @param workitem failed workitem
     */
    public static void FailedRedirectToLauncherAuthPool(WorkitemContext workitem) {

    }

    /**
     * Signal a workitem is failed, and redirect it to BO WFMS admin exception workitem pool.
     * @param workitem failed workitem
     */
    public static void FailedRedirecToAdminPool(WorkitemContext workitem) {

    }

    /**
     * Notify the auth user about exception happened by notify its binding hook URL.
     * @param workitem failed workitem
     */
    public static void NotifyException(WorkitemContext workitem) {

    }

}
