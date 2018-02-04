/*
 * Project Ren @ 2018
 * Rinkako, Ariana, Gordan. SYSU SDCS.
 */
package org.sysu.renResourcing;

/**
 * Author: Rinkako
 * Date  : 2018/2/4
 * Usage : This class performs the engine controller role. All service requests are
 *         passed to engine here from RESTful API by Spring router controllers, all
 *         service requests here are in a same view, means there no any concept of
 *         HTTP request, etc. LEngine is responsible for solving the request and
 *         give a response result to return immediately.
 *         This is a Singleton, use {@code GetInstance} instead of create new one.
 */
public class ResourcingEngine {
    /**
     * RS engine global instance.
     */
    private static ResourcingEngine syncObject = new ResourcingEngine();

    /**
     * Get the singleton instance of resourcing engine.
     * @return {@code ResourcingEngine} global unique instance.
     */
    public static ResourcingEngine GetInstance() {
        return ResourcingEngine.syncObject;
    }

    /**
     * Private constructor for preventing instance create outside.
     */
    private ResourcingEngine() { }

}
