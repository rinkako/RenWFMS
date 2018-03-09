/*
 * Project Ren @ 2018
 * Rinkako, Ariana, Gordan. SYSU SDCS.
 */
package org.sysu.renResourcing.principle;

import org.sysu.renCommon.enums.WorkitemDistributionType;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

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
     * Name of distributor.
     */
    private String distributorName;

    /**
     * Arguments map for distributor.
     */
    private HashMap distributorArgsMap;

    /**
     * Constraints list.
     */
    private ArrayList<String> constraints;

    /**
     * Constraints argument map list.
     */
    private ArrayList<HashMap> constraintsArgs;

    /**
     * Get distribution type of task.
     * @return distribution type enum
     */
    public WorkitemDistributionType getDistributionType() {
        return this.distributionType;
    }

    /**
     * Get distributor name of task.
     * @return name of distributor to be reflection created
     */
    public String getDistributorName() {
        return this.distributorName;
    }

    /**
     * Get filter condition of task.
     * @return filter condition map
     */
    public HashMap getDistributorArgsMap() {
        return this.distributorArgsMap;
    }

    /**
     * Add a constraint to principle.
     * @param constraintName constraint name
     * @param constraintArgs argument dictionary
     */
    public void AddConstraint(String constraintName, HashMap constraintArgs) {
        this.constraints.add(constraintName);
        this.constraintsArgs.add(constraintArgs);
    }

    /**
     * Set parsed principle data.
     * @param type distribution type enum
     * @param distributorName distributor name for reflect instance generated
     * @param distributorArgs filter arguments map
     */
    public void SetParsed(WorkitemDistributionType type, String distributorName, HashMap distributorArgs) {
        this.distributionType = type == null ? WorkitemDistributionType.Allocate : type;
        this.distributorName = distributorName;
        this.distributorArgsMap = distributorArgs;
        this.constraints = new ArrayList<>();
        this.constraintsArgs = new ArrayList<>();
    }
}
