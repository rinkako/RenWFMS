
package com.sysu.workflow;

/**
 * Exception thrown when a malformed expression is encountered during
 * evaluation of an expression in a SCXML document.
 */
public class SCXMLExpressionException extends Exception {

    /**
     * Serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * @see Exception#Exception()
     */
    public SCXMLExpressionException() {
        super();
    }

    /**
     * @param message The error message
     * @see Exception#Exception(String)
     */
    public SCXMLExpressionException(final String message) {
        super(message);
    }

    /**
     * @param cause The cause
     * @see Exception#Exception(Throwable)
     */
    public SCXMLExpressionException(final Throwable cause) {
        super(cause);
    }

    /**
     * @param message The error message
     * @param cause   The cause
     * @see Exception#Exception(String, Throwable)
     */
    public SCXMLExpressionException(final String message,
                                    final Throwable cause) {
        super(message, cause);
    }

}

