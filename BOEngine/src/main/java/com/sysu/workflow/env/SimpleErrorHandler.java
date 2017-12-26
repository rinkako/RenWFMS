
package com.sysu.workflow.env;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXParseException;

import java.io.Serializable;

/**
 * Custom error handler that logs the parsing errors in the
 * SCXML document.
 */
public class SimpleErrorHandler implements ErrorHandler, Serializable {

    /**
     * Serial version UID.
     */
    private static final long serialVersionUID = 1L;
    /**
     * Message prefix.
     */
    private static final String MSG_PREFIX = "SCXML SAX Parsing: ";
    /**
     * Message postfix.
     */
    private static final String MSG_POSTFIX = " Correct the SCXML document.";

    /**
     * Log.
     */
    private Log log = LogFactory.getLog(getClass());

    /**
     * Constructor.
     */
    public SimpleErrorHandler() {
        super();
    }

    /**
     * @see ErrorHandler#error(SAXParseException)
     */
    public void error(final SAXParseException exception) {
        if (log.isErrorEnabled()) {
            log.error(MSG_PREFIX + exception.getMessage() + MSG_POSTFIX,
                    exception);
        }
    }

    /**
     * @see ErrorHandler#fatalError(SAXParseException)
     */
    public void fatalError(final SAXParseException exception) {
        if (log.isFatalEnabled()) {
            log.fatal(MSG_PREFIX + exception.getMessage() + MSG_POSTFIX,
                    exception);
        }
    }

    /**
     * @see ErrorHandler#warning(SAXParseException)
     */
    public void warning(final SAXParseException exception) {
        if (log.isWarnEnabled()) {
            log.warn(MSG_PREFIX + exception.getMessage() + MSG_POSTFIX,
                    exception);
        }
    }
}

