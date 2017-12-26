
package com.sysu.workflow;

/**
 * An interface for reporting SCXML errors to the host environment,
 * containing the definition of commonly occuring errors while executing
 * SCXML documents.
 * <p/>
 * 一个用来给host 环境报告SCXML错误的接口，
 * 错误包括，定义的执行过程的所有错误
 */
public interface ErrorReporter {

    /**
     * Handler for reporting an error.
     * <p/>
     * 处理错误报告
     *
     * @param errCode   one of the ErrorReporter's constants
     * @param errDetail human readable description
     * @param errCtx    typically an SCXML element which caused an error,
     *                  may be accompanied by additional information
     */
    void onError(String errCode, String errDetail, Object errCtx);
}
