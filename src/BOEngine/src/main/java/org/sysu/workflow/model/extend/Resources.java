/*
 * Project Ren @ 2018
 * Rinkako, Ariana, Gordan. SYSU SDCS.
 */
package org.sysu.workflow.model.extend;

import org.sysu.workflow.ActionExecutionContext;
import org.sysu.workflow.SCXMLExpressionException;
import org.sysu.workflow.model.ModelException;
import org.sysu.workflow.model.ParamsContainer;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 * Author: Rinkako
 * Date  : 2017/6/15
 * Usage : Label context of Resources.
 */
public class Resources extends ParamsContainer implements Serializable {

    /**
     * Serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Role vector
     */
    private List<Role> roleVector = new LinkedList<Role>();

    /**
     * Add a resource item to the resources catalogue vector
     * @param r the role pending to add
     */
    public void AddRole(Role r) {
        this.roleVector.add(r);
    }

    /**
     * Resource vector
     */
    private List<Resource> resourcesVector = new LinkedList<Resource>();

    /**
     * Add a resource item to the resources catalogue vector
     * @param r the resource to add
     */
    public void AddResource(Resource r) {
        this.resourcesVector.add(r);
    }

    /**
     * Get the vector of resource
     * @return the reference of resource vector
     */
    public List<Resource> GetResourceList() {
        return this.resourcesVector;
    }

    /**
     * Get the vector of role
     * @return the reference of role vector
     */
    public List<Role> GetRoleList() {
        return this.roleVector;
    }

    /**
     * Register resource to the ResourceService
     * @param exctx The ActionExecutionContext for this execution instance
     * @throws ModelException
     * @throws SCXMLExpressionException
     */
    @Override
    public void execute(ActionExecutionContext exctx) {

    }
}
