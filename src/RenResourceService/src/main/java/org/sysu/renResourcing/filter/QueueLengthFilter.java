/*
 * Project Ren @ 2018
 * Rinkako, Ariana, Gordan. SYSU SDCS.
 */
package org.sysu.renResourcing.filter;

import org.apache.commons.jexl2.Expression;
import org.apache.commons.jexl2.JexlContext;
import org.sysu.renCommon.enums.WorkQueueType;
import org.sysu.renResourcing.context.ParticipantContext;
import org.sysu.renResourcing.context.WorkQueueContainer;
import org.sysu.renResourcing.context.WorkQueueContext;
import org.sysu.renResourcing.context.WorkitemContext;
import org.sysu.renResourcing.plugin.evaluator.RJexlEvaluator;
import org.sysu.renResourcing.plugin.evaluator.RJexlMapContext;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.UUID;

/**
 * Author: Rinkako
 * Date  : 2018/2/9
 * Usage : Filters a distribution set based on length of Work Queue.
 */
public class QueueLengthFilter extends RFilter {

    /**
     * Filter description.
     */
    public static final String Descriptor = "The QueueLength filter do " +
            "will apply a condition expression to check if participant " +
            " offered queue length satisfied the condition.";

    /**
     * Parameter name of queue length in condition expression.
     */
    private static final String ParameterLength = "length";

    /**
     * Condition evaluable instance.
     */
    private Expression conditionExprObj;

    /**
     * Constructor for reflect.
     */
    public QueueLengthFilter() {
        this("Filter_" + UUID.randomUUID().toString(), QueueLengthFilter.class.getName(), null);
    }

    /**
     * Apply principle to configure selector.
     */
    @Override
    protected void ApplyPrinciple() {
        Map distributorArgs = this.principle.getDistributorArgsMap();
        this.SetCondition(String.format("%s %s", QueueLengthFilter.ParameterLength, distributorArgs.get(QueueLengthFilter.ParameterLength)));
    }

    /**
     * Create a new filter.
     *
     * @param id          unique id for selector fetching
     * @param type        type name string
     * @param args        parameter dictionary in HashMap
     */
    public QueueLengthFilter(String id, String type, HashMap<String, String> args) {
        super(id, type, QueueLengthFilter.Descriptor, args);
    }

    /**
     * Set the filter condition.
     * Expression should contain variable `length` for getting queue length.
     * A valid expression example: "length lt 10" means queue length less than 10
     * @param conditionExpr condition expression in JLex
     */
    private void SetCondition(String conditionExpr) {
        this.conditionExprObj = RJexlEvaluator.CommonEngine.createExpression(conditionExpr);
    }

    /**
     * Perform filter on the candidate set.
     *
     * @param candidateSet candidate participant set
     * @param context      workitem context
     * @return filtered participant set
     */
    @Override
    public HashSet<ParticipantContext> PerformFilter(HashSet<ParticipantContext> candidateSet, WorkitemContext context) {
        HashSet<ParticipantContext> retSet = new HashSet<>();
        RJexlMapContext tmpCtx = new RJexlMapContext();
        for (ParticipantContext p : candidateSet) {
            String workerId = p.getWorkerId();
            WorkQueueContext offered = WorkQueueContainer.GetContext(workerId).DirectlyGetQueue(WorkQueueType.OFFERED);
            int queueLen = offered == null ? 0 : offered.Count();
            tmpCtx.Add(QueueLengthFilter.ParameterLength, queueLen);
            Object condResult = this.conditionExprObj.evaluate((JexlContext) tmpCtx.GetInternalContext());
            if (condResult == null ? Boolean.FALSE : (Boolean) condResult) {
                retSet.add(p);
            }
        }
        return retSet;
    }
}
