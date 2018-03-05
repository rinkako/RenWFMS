/*
 * Project Ren @ 2018
 * Rinkako, Ariana, Gordan. SYSU SDCS.
 */
package org.sysu.renCommon.interactionRouter;

import java.util.Map;

/**
 * Author: Rinkako
 * Date  : 2018/2/27
 * Usage : An interaction router using Kafka stream service for modules interaction.
 */
public class KafkaRouter implements RInteractionRouter {

    /**
     * Send a message encoded by Key-value pair map to the destination module.
     *
     * @param destination an identifiable destination message receiving point, such as URL or Topic
     * @param args        a map of encoded message content
     * @param rtid        process rtid
     * @return response string if exist
     */
    @Override
    public String Send(String destination, Map<String, String> args, String rtid) throws Exception {
        return null;
    }
}
