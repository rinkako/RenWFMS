package org.sysu.workflow;

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
}
