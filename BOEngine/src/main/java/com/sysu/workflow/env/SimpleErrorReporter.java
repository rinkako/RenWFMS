
package com.sysu.workflow.env;

import com.sysu.workflow.ErrorReporter;
import com.sysu.workflow.model.*;
import com.sysu.workflow.semantics.ErrorConstants;
import org.apache.commons.logging.LogFactory;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;

/**
 * Custom error reporter that log execution errors.
 */
public class SimpleErrorReporter implements ErrorReporter, Serializable {

    /**
     * Serial version UID.
     */
    private static final long serialVersionUID = 1L;
    /**
     * Log.
     */
    private Log log = LogFactory.getLog(getClass());

    /**
     * Constructor.
     */
    public SimpleErrorReporter() {
        super();
    }

    /**
     * @see ErrorReporter#onError(String, String, Object)
     */
    @SuppressWarnings("unchecked")
    public void onError(final String errorCode, final String errDetail,
                        final Object errCtx) {
        //Note: the if-then-else below is based on the actual usage
        // (codebase search), it has to be kept up-to-date as the code changes
        String errCode = errorCode.intern();
        StringBuffer msg = new StringBuffer();
        msg.append(errCode).append(" (");
        msg.append(errDetail).append("): ");
        if (errCode == ErrorConstants.NO_INITIAL) {
            if (errCtx instanceof SCXML) {
                //determineInitialStates
                msg.append("<SCXML>");
            } else if (errCtx instanceof State) {
                //determineInitialStates
                //determineTargetStates
                msg.append("State " + LogUtils.getTTPath((State) errCtx));
            }
        } else if (errCode == ErrorConstants.UNKNOWN_ACTION) {
            //executeActionList
            msg.append("Action: " + errCtx.getClass().getName());
        } else if (errCode == ErrorConstants.ILLEGAL_CONFIG) {
            //isLegalConfig
            if (errCtx instanceof Map.Entry) { //unchecked cast below
                Map.Entry<EnterableState, Set<EnterableState>> badConfigMap =
                        (Map.Entry<EnterableState, Set<EnterableState>>) errCtx;
                EnterableState es = badConfigMap.getKey();
                Set<EnterableState> vals = badConfigMap.getValue();
                msg.append(LogUtils.getTTPath(es) + " : [");
                for (Iterator<EnterableState> i = vals.iterator(); i.hasNext(); ) {
                    EnterableState ex = i.next();
                    msg.append(LogUtils.getTTPath(ex));
                    if (i.hasNext()) { // reason for iterator usage
                        msg.append(", ");
                    }
                }
                msg.append(']');
            } else if (errCtx instanceof Set) { //unchecked cast below
                Set<EnterableState> vals = (Set<EnterableState>) errCtx;
                msg.append("<SCXML> : [");
                for (Iterator<EnterableState> i = vals.iterator(); i.hasNext(); ) {
                    EnterableState ex = i.next();
                    msg.append(LogUtils.getTTPath(ex));
                    if (i.hasNext()) {
                        msg.append(", ");
                    }
                }
                msg.append(']');
            }
        } else if (errCode == ErrorConstants.EXPRESSION_ERROR) {
            if (errCtx instanceof Executable) {
                TransitionTarget parent = ((Executable) errCtx).getParent();
                msg.append("Expression error inside " + LogUtils.getTTPath(parent));
            } else if (errCtx instanceof Data) {
                // Data expression error
                msg.append("Expression error for data element with id " + ((Data) errCtx).getId());
            } else if (errCtx instanceof SCXML) {
                // Global Script
                msg.append("Expression error inside the global script");
            }
        }
        handleErrorMessage(errorCode, errDetail, errCtx, msg);
    }

    /**
     * Final handling of the resulting errorMessage build by {@link #onError(String, String, Object)} onError}.
     * <p>The default implementation write the errorMessage as a warning to the log.</p>
     *
     * @param errorCode    one of the ErrorReporter's constants
     * @param errDetail    human readable description
     * @param errCtx       typically an SCXML element which caused an error,
     *                     may be accompanied by additional information
     * @param errorMessage human readable detail of the error including the state, transition and data
     */
    protected void handleErrorMessage(final String errorCode, final String errDetail,
                                      final Object errCtx, final CharSequence errorMessage) {

        if (log.isWarnEnabled()) {
            log.warn(errorMessage.toString());
        }
    }
}

