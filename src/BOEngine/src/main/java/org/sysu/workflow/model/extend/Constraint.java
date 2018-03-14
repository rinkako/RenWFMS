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

/**
 * Author: Rinkako
 * Date  : 2018/3/3
 * Usage : Label context of Constraint.
 */
public class Constraint extends ParamsContainer implements Serializable {

    /**
     * Serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Constraint name.
     */
    private String name;

    /**
     * Generate descriptor for this constraint.
     * @return string descriptor
     */
    public String GenerateDescriptor(Evaluator evaluator, Context ctx) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        for (Param p : this.getParams()) {
            sb.append(p.GenerateDescriptor(evaluator, ctx)).append(",");
        }
        String jsonifyParam = sb.toString();
        if (jsonifyParam.length() > 1) {
            jsonifyParam = jsonifyParam.substring(0, jsonifyParam.length() - 1);
        }
        jsonifyParam += "}";
        return String.format("\"%s\":%s", this.name, jsonifyParam);
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
