package org.sysu.workflow.model.extend;

import org.sysu.workflow.ActionExecutionContext;
import org.sysu.workflow.SCXMLExpressionException;
import org.sysu.workflow.model.Action;
import org.sysu.workflow.model.ModelException;

import java.io.Serializable;

/**
 * Created by Rinkako on 2017/6/15.
 */
public class Role extends Action implements Serializable {
    /**
     * Serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The resource role name
     */
    private String name;

    /**
     * The id for this role
     */
    private String id;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public void execute(ActionExecutionContext exctx) {

    }
}
