
package com.sysu.workflow.env;

import com.sysu.workflow.ErrorReporter;
import com.sysu.workflow.SCXMLListener;
import com.sysu.workflow.model.EnterableState;
import com.sysu.workflow.model.Transition;
import com.sysu.workflow.model.TransitionTarget;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import javax.xml.stream.Location;
import javax.xml.stream.XMLReporter;
import javax.xml.stream.XMLStreamException;
import java.io.Serializable;

/**
 * A simple tracer connected to Apache Commons Logging.
 */
public class Tracer implements ErrorHandler, ErrorReporter,
        SCXMLListener, Serializable, XMLReporter {

    /**
     * Serial version UID.
     */
    private static final long serialVersionUID = 1L;
    /**
     * ErrorHandler delegate.
     */
    private ErrorHandler errHandler;
    /**
     * ErrorReporter delegate.
     */
    private ErrorReporter errReporter;
    /**
     * SCXMLListener delegate.
     */
    private SCXMLListener scxmlListener;
    /**
     * XMLReporter delegate.
     */
    private XMLReporter xmlReporter;

    /**
     * Constructor.
     */
    public Tracer() {
        super();
        errHandler = new SimpleErrorHandler();
        errReporter = new SimpleErrorReporter();
        scxmlListener = new SimpleSCXMLListener();
        xmlReporter = new SimpleXMLReporter();
    }

    /**
     * @see ErrorHandler#warning(SAXParseException)
     */
    public void warning(final SAXParseException exception)
            throws SAXException {
        errHandler.warning(exception);
    }

    /**
     * @see ErrorHandler#error(SAXParseException)
     */
    public void error(final SAXParseException exception)
            throws SAXException {
        errHandler.error(exception);
    }

    /**
     * @see ErrorHandler#fatalError(SAXParseException)
     */
    public void fatalError(final SAXParseException exception)
            throws SAXException {
        errHandler.fatalError(exception);
    }

    /**
     * @see ErrorReporter#onError(String, String, Object)
     */
    public void onError(final String errCode, final String errDetail,
                        final Object errCtx) {
        errReporter.onError(errCode, errDetail, errCtx);
    }

    /**
     * @see SCXMLListener#onEntry(EnterableState)
     */
    public void onEntry(final EnterableState state) {
        scxmlListener.onEntry(state);
    }

    /**
     * @see SCXMLListener#onExit(EnterableState)
     */
    public void onExit(final EnterableState state) {
        scxmlListener.onExit(state);
    }

    /**
     * @see SCXMLListener#onTransition(TransitionTarget, TransitionTarget, Transition, String)
     */
    public void onTransition(final TransitionTarget from,
                             final TransitionTarget to, final Transition transition, final String event) {
        scxmlListener.onTransition(from, to, transition, event);
    }

    /**
     * @see XMLReporter#report(String, String, Object, Location)
     */
    public void report(final String message, final String errorType, final Object relatedInformation,
                       final Location location)
            throws XMLStreamException {
        xmlReporter.report(message, errorType, relatedInformation, location);
    }

}

