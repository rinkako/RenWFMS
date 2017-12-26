
package com.sysu.workflow.env.groovy;

import com.sysu.workflow.Context;
import com.sysu.workflow.env.SimpleContext;
import groovy.lang.Closure;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.*;
import java.util.Iterator;
import java.util.Map;

/**
 * Groovy Context implementation for Commons SCXML.
 */
public class GroovyContext extends SimpleContext {

    private static final long serialVersionUID = 1L;

    private static final Log log = LogFactory.getLog(GroovyContext.class);

    private String scriptBaseClass;
    private GroovyEvaluator evaluator;
    private GroovyContextBinding binding;
    private Map<String, Object> vars;

    GroovyContextBinding getBinding() {
        if (binding == null) {
            binding = new GroovyContextBinding(this);
        }
        return binding;
    }

    /**
     * Constructor.
     */
    public GroovyContext() {
        super();
    }

    /**
     * Constructor with initial vars.
     *
     * @param parent      The parent context.
     * @param initialVars The initial set of variables.
     * @param evaluator   The groovy evaluator
     */
    public GroovyContext(final Context parent, final Map<String, Object> initialVars, GroovyEvaluator evaluator) {
        super(parent, initialVars);
        this.evaluator = evaluator;
    }

    /**
     * Constructor with parent context.
     *
     * @param parent    The parent context.
     * @param evaluator The groovy evaluator
     */
    public GroovyContext(final Context parent, GroovyEvaluator evaluator) {
        super(parent);
        this.evaluator = evaluator;
    }

    protected GroovyEvaluator getGroovyEvaluator() {
        return evaluator;
    }

    protected void setGroovyEvaluator(GroovyEvaluator evaluator) {
        this.evaluator = evaluator;
    }

    @Override
    public Map<String, Object> getVars() {
        return vars;
    }

    @Override
    protected void setVars(final Map<String, Object> vars) {
        this.vars = vars;
    }

    protected void setScriptBaseClass(String scriptBaseClass) {
        this.scriptBaseClass = scriptBaseClass;
    }

    protected String getScriptBaseClass() {
        if (scriptBaseClass != null) {
            return scriptBaseClass;
        }
        if (getParent() instanceof GroovyContext) {
            return ((GroovyContext) getParent()).getScriptBaseClass();
        }
        return null;
    }

    private void writeObject(ObjectOutputStream out) throws IOException {
        boolean closureErased = false;
        if (vars != null) {
            Iterator<Map.Entry<String, Object>> iterator = getVars().entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, Object> entry = iterator.next();
                if (entry.getValue() != null && entry.getValue() instanceof Closure) {
                    iterator.remove();
                    closureErased = true;
                }
            }
            if (closureErased) {
                log.warn("Encountered and removed Groovy Closure(s) in the GroovyContext during serialization: these are not supported for (de)serialization");
            }
        }
        out.writeObject(this.scriptBaseClass);
        out.writeObject(this.evaluator);
        out.writeObject(this.binding);
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        new ObjectOutputStream(bout).writeObject(this.vars);
        out.writeObject(bout.toByteArray());
    }

    @SuppressWarnings("unchecked")
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        this.scriptBaseClass = (String) in.readObject();
        this.evaluator = (GroovyEvaluator) in.readObject();
        this.binding = (GroovyContextBinding) in.readObject();
        byte[] bytes = (byte[]) in.readObject();
        if (evaluator != null) {
            this.vars = (Map<String, Object>)
                    new ObjectInputStream(new ByteArrayInputStream(bytes)) {
                        protected Class resolveClass(ObjectStreamClass osc) throws IOException, ClassNotFoundException {
                            return Class.forName(osc.getName(), true, evaluator.getGroovyClassLoader());
                        }
                    }.readObject();
        } else {
            this.vars = (Map<String, Object>) new ObjectInputStream(new ByteArrayInputStream(bytes)).readObject();
        }
    }
}
