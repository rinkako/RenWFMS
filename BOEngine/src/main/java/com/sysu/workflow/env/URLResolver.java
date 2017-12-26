
package com.sysu.workflow.env;

import com.sysu.workflow.PathResolver;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * A PathResolver implementation that resolves against a base URL.
 *
 * @see PathResolver
 */
public class URLResolver implements PathResolver, Serializable {

    /**
     * Serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Implementation independent log category.
     */
    private Log log = LogFactory.getLog(PathResolver.class);

    /**
     * The base URL to resolve against.
     */
    private URL baseURL = null;

    /**
     * Constructor.
     *
     * @param baseURL The base URL to resolve against
     */
    public URLResolver(final URL baseURL) {
        this.baseURL = baseURL;
    }

    /**
     * Uses URL(URL, String) constructor to combine URL's.
     *
     * @see PathResolver#resolvePath(String)
     */
    public String resolvePath(final String ctxPath) {
        URL combined;
        try {
            combined = new URL(baseURL, ctxPath);
            return combined.toString();
        } catch (MalformedURLException e) {
            log.error("Malformed URL", e);
        }
        return null;
    }

    /**
     * @see PathResolver#getResolver(String)
     */
    public PathResolver getResolver(final String ctxPath) {
        URL combined;
        try {
            combined = new URL(baseURL, ctxPath);
            return new URLResolver(combined);
        } catch (MalformedURLException e) {
            log.error("Malformed URL", e);
        }
        return null;
    }

}

