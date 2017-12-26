
package com.sysu.workflow.model;

import com.sysu.workflow.ActionExecutionContext;
import com.sysu.workflow.Context;
import com.sysu.workflow.Evaluator;
import com.sysu.workflow.SCXMLExpressionException;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * The class in this SCXML object model that corresponds to the
 * &lt;foreach&gt; SCXML element, which allows an SCXML application to iterate through a collection in the data model
 * and to execute the actions contained within it for each item in the collection.
 */
public class Foreach extends Action implements ActionsContainer {

    /**
     * Serial version UID.
     */
    private static final long serialVersionUID = 1L;

    private String array;
    private String item;
    private String index;

    /**
     * The set of executable elements (those that inheriting from
     * Action) that are contained in this &lt;if&gt; element.
     */
    private List<Action> actions;

    public Foreach() {
        super();
        this.actions = new ArrayList<Action>();
    }


    public final String getContainerElementName() {
        return ELEM_FOREACH;
    }


    public final List<Action> getActions() {
        return actions;
    }


    public final void addAction(final Action action) {
        if (action != null) {
            this.actions.add(action);
        }
    }

    public String getArray() {
        return array;
    }

    public void setArray(final String array) {
        this.array = array;
    }

    public String getItem() {
        return item;
    }

    public void setItem(final String item) {
        this.item = item;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(final String index) {
        this.index = index;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute(ActionExecutionContext exctx) throws ModelException, SCXMLExpressionException {
        Context ctx = exctx.getContext(getParentEnterableState());
        Evaluator eval = exctx.getEvaluator();
        ctx.setLocal(getNamespacesKey(), getNamespaces());
        try {
            Object arrayObject = eval.eval(ctx, array);
            if (arrayObject != null && (arrayObject instanceof Iterable || arrayObject.getClass().isArray())) {
                if (arrayObject.getClass().isArray()) {
                    for (int currentIndex = 0, size = Array.getLength(arrayObject); currentIndex < size; currentIndex++) {
                        ctx.setLocal(item, Array.get(arrayObject, currentIndex));
                        if (index != null) {
                            ctx.setLocal(index, currentIndex);
                        }
                        // The "foreach" statement is a "container"
                        for (Action aa : actions) {
                            aa.execute(exctx);
                        }
                    }
                } else {
                    // Spec requires to iterate over a shallow copy of underlying array in a way that modifications to
                    // the collection during the execution of <foreach> must not affect the iteration behavior.
                    // For array objects (see above) this isn't needed, but for Iterables we don't have that guarantee
                    // so we make a copy first
                    ArrayList<Object> arrayList = new ArrayList<Object>();
                    for (Object value : (Iterable) arrayObject) {
                        arrayList.add(value);
                    }
                    int currentIndex = 0;
                    for (Object value : arrayList) {
                        ctx.setLocal(item, value);
                        if (index != null) {
                            ctx.setLocal(index, currentIndex);
                        }
                        // The "foreach" statement is a "container"
                        for (Action aa : actions) {
                            aa.execute(exctx);
                        }
                        currentIndex++;
                    }
                }
            }
            // else {} TODO: place the error 'error.execution' in the internal event queue. (section "3.12.2 Errors")
        } finally {
            ctx.setLocal(getNamespacesKey(), null);
        }
    }
}
