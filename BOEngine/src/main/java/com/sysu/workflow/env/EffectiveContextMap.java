
package com.sysu.workflow.env;

import com.sysu.workflow.Context;
import com.sysu.workflow.Evaluator;

import java.io.Serializable;
import java.util.AbstractMap;
import java.util.HashSet;
import java.util.Set;

/**
 * A map that will back the effective {@link Context} for an {@link Evaluator} execution.
 * The effective context enables the chaining of contexts all the way from the current state node to the root.
 */
public final class EffectiveContextMap extends AbstractMap<String, Object> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * The {@link Context} for the current state.
     */
    private final Context leaf;

    /**
     * Constructor.
     *
     * @param ctx context of the current leave state node
     */
    public EffectiveContextMap(final Context ctx) {
        super();
        this.leaf = ctx;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<Entry<String, Object>> entrySet() {
        Set<Entry<String, Object>> entrySet = new HashSet<Entry<String, Object>>();
        Context current = leaf;
        while (current != null) {
            entrySet.addAll(current.getVars().entrySet());
            current = current.getParent();
        }
        return entrySet;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object put(final String key, final Object value) {
        Object old = leaf.get(key);
        if (leaf.has(key)) {
            leaf.set(key, value);
        } else {
            leaf.setLocal(key, value);
        }
        return old;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object get(final Object key) {
        if (key != null) {
            Context current = leaf;
            while (current != null) {
                if (current.getVars().containsKey(key.toString())) {
                    return current.getVars().get(key);
                }
                current = current.getParent();
            }
        }
        return null;
    }
}
