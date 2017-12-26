
package com.sysu.workflow.invoke;

/**
 * Exception thrown when a process specified by an &lt;invoke&gt;
 * cannot be initiated.
 */
public class InvokerException extends Exception {

    /**
     * Serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * @see Exception#Exception()
     */
    public InvokerException() {
        super();
    }

    /**
     * @param message The error message
     * @see Exception#Exception(String)
     */
    public InvokerException(final String message) {
        super(message);
    }

    /**
     * @param cause The cause
     * @see Exception#Exception(Throwable)
     */
    public InvokerException(final Throwable cause) {
        super(cause);
    }

    /**
     * @param message The error message
     * @param cause   The cause
     * @see Exception#Exception(String, Throwable)
     */
    public InvokerException(final String message,
                            final Throwable cause) {
        super(message, cause);
    }

}

