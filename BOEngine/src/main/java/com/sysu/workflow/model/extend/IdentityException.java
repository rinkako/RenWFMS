package com.sysu.workflow.model.extend;

/**
 *
 * 解析用户过程中出现的异常
 *
 *
 * Created by zhengshouzi on 2015/12/26.
 */




public class IdentityException extends Exception{
    /**
     * Serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * @see Exception#Exception()
     */
    public IdentityException() {
        super();
    }

    /**
     * @param message the detail message
     * @see Exception#Exception(String)
     */
    public IdentityException(final String message) {
        super(message);
    }

    /**
     * @param cause the cause
     * @see Exception#Exception(Throwable)
     */
    public IdentityException(final Throwable cause) {
        super(cause);
    }

    /**
     * @param message the detail message
     * @param cause   the cause
     * @see Exception#Exception(String, Throwable)
     */
    public IdentityException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
