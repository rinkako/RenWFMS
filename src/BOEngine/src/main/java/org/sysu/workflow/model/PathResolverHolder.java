
package org.sysu.workflow.model;

import org.sysu.workflow.PathResolver;

/**
 * A <code>PathResolverHolder</code> is an context that holds a
 * {@link PathResolver}.
 */
public interface PathResolverHolder {

    /**
     * Set the {@link PathResolver} to use.
     *
     * @param pathResolver The path resolver to use.
     */
    void setPathResolver(PathResolver pathResolver);

    /**
     * Get the {@link PathResolver}.
     *
     * @return The path resolver in use.
     */
    PathResolver getPathResolver();

}

