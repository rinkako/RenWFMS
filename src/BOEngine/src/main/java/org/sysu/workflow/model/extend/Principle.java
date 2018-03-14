/*
 * Project Ren @ 2018
 * Rinkako, Ariana, Gordan. SYSU SDCS.
 */
package org.sysu.workflow.model.extend;

import org.sysu.workflow.ActionExecutionContext;
import org.sysu.workflow.Context;
import org.sysu.workflow.Evaluator;
import org.sysu.workflow.SCXMLExpressionException;
import org.sysu.workflow.model.ModelException;
import org.sysu.workflow.model.Param;
import org.sysu.workflow.model.ParamsContainer;
import org.sysu.workflow.utility.SerializationUtil;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Author: Rinkako
 * Date  : 2018/3/3
 * Usage : Label context of Principle.
 */
public class Principle extends ParamsContainer implements Serializable {

    /**
     * Serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Distribute method, `Offer` or `Allocate`.
     */
    private String method;

    /**
     * Distributor name.
     */
    private String distributor;

    /**
     * Constraints of this principle.
     */
    private ArrayList<Constraint> constraints = new ArrayList<>();

    /**
     * Add a new constraint to this principle.
     * @param constraint constraint instance
     */
    public void AddConstraint(Constraint constraint) {
        this.constraints.add(constraint);
    }

    /**
     * Generate descriptor for this principle.
     * @return string descriptor
     */
    public String GenerateDescriptor() throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        for (Param p : this.getParams()) {
            sb.append(p.GenerateDescriptor(null, null)).append(",");
        }
        String jsonifyParam = sb.toString();
        if (jsonifyParam.length() > 1) {
            jsonifyParam = jsonifyParam.substring(0, jsonifyParam.length() - 1);
        }
        jsonifyParam += "}";
        sb = new StringBuilder();
        sb.append("{");
        for (Constraint c : this.constraints) {
            sb.append(c.GenerateDescriptor(null, null)).append(",");
        }
        String jsonifyConstraints = sb.toString();
        if (jsonifyConstraints.length() > 1) {
            jsonifyConstraints = jsonifyConstraints.substring(0, jsonifyConstraints.length() - 1);
        }
        jsonifyConstraints += "}";
        return String.format("%s@%s@%s@%s", this.method, this.distributor, jsonifyParam, jsonifyConstraints);
    }

    /**
     * Execute this action instance.
     *
     * @param exctx The ActionExecutionContext for this execution instance
     * @throws ModelException           If the execution causes the model to enter
     *                                  a non-deterministic state.
     * @throws SCXMLExpressionException If the execution involves trying
     *                                  to evaluate an expression which is malformed.
     */
    @Override
    public void execute(ActionExecutionContext exctx) throws ModelException, SCXMLExpressionException {
        // nothing
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getDistributor() {
        return distributor;
    }

    public void setDistributor(String distributor) {
        this.distributor = distributor;
    }

    public ArrayList<Constraint> getConstraints() {
        return constraints;
    }
}
