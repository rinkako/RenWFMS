
package com.sysu.workflow.model.extend;

/**
 * Exception that is thrown when the SCXML model supplied to the
 * executor has a fatal flaw that prevents the executor from
 * further interpreting the the model.
 */
public class UserException extends Exception {

    /**
     * Serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * @see Exception#Exception()
     */
    public UserException() {
        super();
    }

    /**
     * @param message the detail message
     * @see Exception#Exception(String)
     */
    public UserException(final String message) {
        super(message);
    }

    /**
     * @param cause the cause
     * @see Exception#Exception(Throwable)
     */
    public UserException(final Throwable cause) {
        super(cause);
    }

    /**
     * @param message the detail message
     * @param cause   the cause
     * @see Exception#Exception(String, Throwable)
     */
    public UserException(final String message, final Throwable cause) {
        super(message, cause);
    }

}

