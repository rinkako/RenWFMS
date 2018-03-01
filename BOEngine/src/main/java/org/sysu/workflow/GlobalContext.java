package org.sysu.workflow;

import org.sysu.renCommon.interactionRouter.RInteractionRouter;
import org.sysu.renCommon.interactionRouter.RestfulRouter;

import java.util.UUID;

/**
 * Global static data container.
 */
public class GlobalContext {
    //false:read from database
    //true:read local BO
    public static boolean IsLocalDebug = true;

    /**
     * Name service micro-service global id.
     */
    public static final String ENGINE_GLOBAL_ID = String.format("WFMSComponent_Engine_%s", UUID.randomUUID().toString());

    /**
     * Service URL for RS submit task.
     */
    public static final String URL_RS_SUBMITTASK = "http://localhost:10233/internal/submitTask";
    /**
     * Service URL for RS finish life cycle of BO.
     */
    public static final String URL_RS_FINISH = "http://localhost:10233/internal/finRtid";

    /**
     * Size for log buffer.
     */
    public static final int LOG_BUFFER_SIZE = 0;

    /**
     * Main internal interaction router.
     */
    public static final RInteractionRouter Interaction = new RestfulRouter();
}
