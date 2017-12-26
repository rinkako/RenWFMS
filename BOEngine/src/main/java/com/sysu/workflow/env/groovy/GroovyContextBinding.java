
package com.sysu.workflow.env.groovy;

import groovy.lang.Binding;
import groovy.lang.MissingPropertyException;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Delegating Groovy Binding class which delegates all variables access to its GroovyContext
 */
public class GroovyContextBinding extends Binding implements Serializable {

    private static final long serialVersionUID = 1L;

    private GroovyContext context;

    public GroovyContextBinding(GroovyContext context) {
        if (context == null) {
            throw new IllegalArgumentException("Parameter context may not be null");
        }
        this.context = context;
    }

    GroovyContext getContext() {
        return context;
    }

    @Override
    public Object getVariable(String name) {
        Object result = context.get(name);
        if (result == null && !context.has(name)) {
            throw new MissingPropertyException(name, this.getClass());
        }
        return result;
    }

    @Override
    public void setVariable(String name, Object value) {
        if (context.has(name)) {
            context.set(name, value);
        } else {
            context.setLocal(name, value);
        }
    }

    @Override
    public boolean hasVariable(String name) {
        return context.has(name);
    }

    @Override
    public Map<String, Object> getVariables() {
        return new LinkedHashMap<String, Object>(context.getVars());
    }

    @Override
    public Object getProperty(String property) {
        return getVariable(property);
    }

    @Override
    public void setProperty(String property, Object newValue) {
        setVariable(property, newValue);
    }
}
