
package com.sysu.workflow.model;

/**
 * Exception that is thrown when the SCXML model supplied to the
 * executor has a fatal flaw that prevents the executor from
 * further interpreting the the model.
 */
public class ModelException extends Exception {

    /**
     * Serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * @see Exception#Exception()
     */
    public ModelException() {
        super();
    }

    /**
     * @param message the detail message
     * @see Exception#Exception(String)
     */
    public ModelException(final String message) {
        super(message);
    }

    /**
     * @param cause the cause
     * @see Exception#Exception(Throwable)
     */
    public ModelException(final Throwable cause) {
        super(cause);
    }

    /**
     * @param message the detail message
     * @param cause   the cause
     * @see Exception#Exception(String, Throwable)
     */
    public ModelException(final String message, final Throwable cause) {
        super(message, cause);
    }

}

