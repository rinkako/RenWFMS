package org.sysu.workflow;

import org.sysu.renCommon.interactionRouter.RInteractionRouter;
import org.sysu.renCommon.interactionRouter.RestfulRouter;

/**
 * Global static data container.
 */
public class GlobalContext {
    //false:read from database
    //true:read local BO
    public static boolean IsLocalDebug = true;
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
