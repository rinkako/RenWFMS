/*
 * Project Ren @ 2018
 * Rinkako, Ariana, Gordan. SYSU SDCS.
 */
package org.sysu.renCommon.interactionRouter;

import org.sysu.renCommon.utility.HttpClientUtil;

import java.util.Map;

/**
 * Author: Rinkako
 * Date  : 2018/2/27
 * Usage : An interaction router using HTTP and Restful API for modules interaction.
 */
public class RestfulRouter implements RInteractionRouter {

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
        return HttpClientUtil.SendPost(destination, args, rtid);
    }
}
