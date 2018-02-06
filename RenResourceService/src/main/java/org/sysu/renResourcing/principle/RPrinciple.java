/*
 * Project Ren @ 2018
 * Rinkako, Ariana, Gordan. SYSU SDCS.
 */
package org.sysu.renResourcing.principle;

import org.sysu.renResourcing.basic.enums.WorkitemDistributionType;

import java.io.Serializable;

/**
 * Author: Rinkako
 * Date  : 2018/2/6
 * Usage : Principle data package for parsed principle descriptor.
 */
public final class RPrinciple implements Serializable {
    /**
     * Serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Distribute type of task.
     */
    private WorkitemDistributionType distributionType;

    /**
     * Name of allocator.
     */
    private String allocatorName;

    /**
     * Condition string for filter.
     */
    private String filterCondition;

    /**
     * Descriptor for constraint rule set.
     */
    private String constraintDescriptor;

    /**
     * Get distribution type of task.
     * @return distribution type enum
     */
    public WorkitemDistributionType getDistributionType() {
        return this.distributionType;
    }

    /**
     * Get allocator name of task.
     * @return name of allocator to be reflection created
     */
    public String getAllocatorName() {
        return this.allocatorName;
    }

    /**
     * Get filter condition of task.
     * @return filter condition to be parsed
     */
    public String getFilterCondition() {
        return this.filterCondition;
    }

    /**
     * Get constraint descriptor of task.
     * @return constraint descriptor string to be parsed
     */
    public String getConstraintDescriptor() {
        return this.constraintDescriptor;
    }

    /**
     * Set parsed principle data.
     * @param type distribution type enum
     * @param allocatorName allocator name for reflect allocator instance generated
     * @param filterCondition filter condition expression to be JLex parsed
     * @param constraintDescriptor constraint descriptor for parsed
     */
    public void SetParsed(WorkitemDistributionType type, String allocatorName, String filterCondition, String constraintDescriptor) {
        this.distributionType = type == null ? WorkitemDistributionType.Allocate : type;
        this.allocatorName = allocatorName;
        this.filterCondition = filterCondition;
        this.constraintDescriptor = constraintDescriptor;
    }
}
