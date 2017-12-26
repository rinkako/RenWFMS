
package com.sysu.workflow.env.minimal;

import com.sysu.workflow.Context;
import com.sysu.workflow.env.SimpleContext;

import java.util.Collections;
import java.util.Map;

/**
 * MinimalContext implementation for the SCXML Null Data Model.
 * <p>
 * This context disables any access to the parent context (other than through getParent()) and
 * ignores setting any local data.
 * </p>
 * <p>
 * The MinimalContext requires a none MinimalContext based parent Context for creation.
 * If the parent context is of type MinimalContext, <em>its</em> parent will be used as the parent of the
 * new MinimalContext instance
 * </p>
 */
public class MinimalContext extends SimpleContext {

    /**
     * Serial version UID.
     */
    private static final long serialVersionUID = 1L;

    private static Context getMinimalContextParent(final Context parent) {
        if (parent != null) {
            if (parent instanceof MinimalContext) {
                return getMinimalContextParent(parent.getParent());
            }
            return parent;
        }
        throw new IllegalStateException("A MinimalContext instance requires a non MinimalContext based parent.");
    }

    public MinimalContext(final Context parent) {
        super(getMinimalContextParent(parent));
    }

    @Override
    public void set(final String name, final Object value) {
    }

    @Override
    public Object get(final String name) {
        return null;
    }

    @Override
    public boolean has(final String name) {
        return false;
    }

    @Override
    public boolean hasLocal(final String name) {
        return false;
    }

    @Override
    public void reset() {
    }

    @Override
    public void setLocal(final String name, final Object value) {
    }

    @Override
    protected void setVars(final Map<String, Object> vars) {
    }

    @Override
    public Map<String, Object> getVars() {
        return Collections.emptyMap();
    }
}
