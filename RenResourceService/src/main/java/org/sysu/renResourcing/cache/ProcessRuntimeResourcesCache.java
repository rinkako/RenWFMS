/*
 * Project Ren @ 2018
 * Rinkako, Ariana, Gordan. SYSU SDCS.
 */
package org.sysu.renResourcing.cache;

import org.sysu.renResourcing.context.ParticipantContext;

import java.util.ArrayList;

/**
 * Author: Rinkako
 * Date  : 2018/2/6
 * Usage : This class is a cache pool for all running processes resources
 *         context. Notice that all cache will lost after RS service shut
 *         down, therefore all data stored in the cache should be flushed
 *         into steady memory, or discard as garbage.
 *         For the consistency, different RS micro-service maintains its
 *         own cache and a running process only request resource service
 *         from ONE specific RS micro-service instance. In a special case
 *         caches may have inconsistency situation: a process run on a RS
 *         before complete but that RS is becoming overloaded, BO Engine
 *         will send next resourcing request to another RS instance which
 *         may have faults when it try to get any involved context. Hence
 *         we appoint that: 1.resourcing requests of one specific time of
 *         running process is always sent to a specific RS if RS is not
 *         overloaded; 2.when a RS overloaded and new RS micro-service is
 *         launched, resourcing requests from some running process will
 *         be sent to new RS instance which NEVER handle any request for
 *         this running process. This appointment can make sure that one
 *         running process always have only one valid cache.
 */
public class ProcessRuntimeResourcesCache {
    /**
     * Register cache for a running process which will request for resourcing after soon.
     * @param rtid process rtid
     * @param involved involved worker resources global id list
     */
    public static void GenerateCacheForProcessRuntime(String rtid, ArrayList<String> involved) {


    }
}
