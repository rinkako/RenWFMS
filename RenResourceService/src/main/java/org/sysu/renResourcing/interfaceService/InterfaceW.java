/*
 * Project Ren @ 2018
 * Rinkako, Ariana, Gordan. SYSU SDCS.
 */
package org.sysu.renResourcing.interfaceService;

import org.sysu.renResourcing.context.ParticipantContext;
import org.sysu.renResourcing.context.WorkitemContext;

/**
 * Author: Rinkako
 * Date  : 2018/2/9
 * Usage : Implementation of Interface W of Resource Service.
 *         Interface W is responsible for providing services for outside clients.
 *         User sub-systems use this interface for manage work queues.
 *         Usually methods in the interface will return result immediately.
 */
public class InterfaceW {

    /**
     * Accept offer a workitem.
     * @param workitemId workitem global id
     * @param workerId worker global id
     * @return execution result
     */
    public static String AcceptOffer(String workitemId, String workerId) {
        return "";
    }

    /**
     * Deallocate a workitem.
     * @param workitemId workitem global id
     * @param workerId worker global id
     * @return execution result
     */
    public static String Deallocate(String workitemId, String workerId) {
        return "";
    }

    /**
     * Start a workitem.
     * @param workitemId workitem global id
     * @param workerId worker global id
     * @return execution result
     */
    public static String Start(String workitemId, String workerId) {
        return "";
    }

    /**
     * Reallocate a workitem.
     * @param workitemId workitem global id
     * @param workerId worker global id
     * @return execution result
     */
    public static String Reallocate(String workitemId, String workerId) {
        return "";
    }

    /**
     * Accept and start a workitem.
     * @param workitemId workitem global id
     * @param workerId worker global id
     * @return execution result
     */
    public static String AcceptAndStart(String workitemId, String workerId) {
        return "";
    }

    /**
     * Skip a workitem.
     * @param workitemId workitem global id
     * @param workerId worker global id
     * @return execution result
     */
    public static String Skip(String workitemId, String workerId) {
        return "";
    }

    /**
     * Suspend a workitem.
     * @param workitemId workitem global id
     * @param workerId worker global id
     * @return execution result
     */
    public static String Suspend(String workitemId, String workerId) {
        return "";
    }

    /**
     * Unsuspend a workitem.
     * @param workitemId workitem global id
     * @param workerId worker global id
     * @return execution result
     */
    public static String Unsuspend(String workitemId, String workerId) {
        return "";
    }

    /**
     * Get all workitems in all types of queue of a worker.
     * @param workerId worker global id
     * @param domain domain name
     * @return a dictionary of (WorkQueueType, ListOfWorkitemDescriptors)
     */
    public static String GetWorkQueues(String workerId, String domain) {
        return "";
    }

    /**
     * Get all workitems in a specific type of queue of a worker.
     * @param workerId worker global id
     * @param domain auth domain name
     * @param queueTypeName queue type name
     * @return workitem descriptors string in list
     */
    public static String GetWorkQueue(String workerId, String domain, String queueTypeName) {
        return "";
    }

    /**
     * Get all workers with any non-empty work queue in a domain.
     * @param domain auth domain name
     * @return worker gid in a list
     */
    public static String GetNotEmptyQueueWorkers(String domain) {
        return "";
    }

    /**
     * Get all workers with a non-empty offered work queue in a domain.
     * @param domain auth domain name
     * @return worker gid in a list
     */
    public static String GetNotEmptyOfferedQueueWorkers(String domain) {
        return "";
    }

    /**
     * Get all workers with a non-empty allocated work queue in a domain.
     * @param domain auth domain name
     * @return worker gid in a list
     */
    public static String GetNotEmptyAllocatedQueueWorkers(String domain) {
        return "";
    }

    /**
     * Get all workers with a non-empty allocated or allocated work queue in a domain.
     * @param domain auth domain name
     * @return worker gid in a list
     */
    public static String GetNotEmptyOfferedAllocatedQueueWorkers(String domain) {
        return "";
    }
}
