package org.sysu.workflow;

import org.sysu.renCommon.interactionRouter.RInteractionRouter;
import org.sysu.renCommon.interactionRouter.RestfulRouter;

import java.util.UUID;

/**
 * Global static data container.
 */
public class GlobalContext {

    //false:read from database
    //true:read local BO, no any interaction
    public static boolean IsLocalDebug = false;

    /**
     * Name service micro-service global id.
     */
    public static final String ENGINE_GLOBAL_ID = String.format("WFMSComponent_Engine_%s", UUID.randomUUID().toString());

    /**
     * Size for log buffer.
     */
    public static final int LOG_BUFFER_SIZE = 0;

    /**
     * Main internal interaction router.
     */
    public static final RInteractionRouter Interaction = new RestfulRouter();
}
