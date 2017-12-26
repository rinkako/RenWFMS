
package com.sysu.workflow.model;

import com.sysu.workflow.PathResolver;

/**
 * A <code>PathResolverHolder</code> is an entity that holds a
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

