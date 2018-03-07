
package org.sysu.workflow;

/**
 * An interface for reporting SCXML errors to the host environment,
 * containing the definition of commonly occuring errors while executing
 * SCXML documents.
 * <p/>
 */
public interface ErrorReporter {

    /**
     * Handler for reporting an error.
     *
     * @param errCode   one of the ErrorReporter's constants
     * @param errDetail human readable description
     * @param errCtx    typically an SCXML element which caused an error,
     *                  may be accompanied by additional information
     */
    void onError(String errCode, String errDetail, Object errCtx);
}
