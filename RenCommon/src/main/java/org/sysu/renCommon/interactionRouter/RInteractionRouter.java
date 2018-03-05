/*
 * Project Ren @ 2018
 * Rinkako, Ariana, Gordan. SYSU SDCS.
 */
package org.sysu.renCommon.interactionRouter;

import java.util.Map;

/**
 * Author: Rinkako
 * Date  : 2018/2/27
 * Usage : Base class of Interaction router, which is responsible for making communication
 *         among modules in RenWFMS.
 */
public interface RInteractionRouter {

    /**
     * Send a message encoded by Key-value pair map to the destination module.
     * @param destination an identifiable destination message receiving point, such as URL or Topic
     * @param args a map of encoded message content
     * @param rtid process rtid
     * @return response string if exist
     */
    String Send(String destination, Map<String, String> args, String rtid) throws Exception;
}
