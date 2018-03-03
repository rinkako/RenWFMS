/*
 * Project Ren @ 2018
 * Rinkako, Ariana, Gordan. SYSU SDCS.
 */
package org.sysu.workflow.model.extend;

import org.sysu.workflow.ActionExecutionContext;
import org.sysu.workflow.model.ParamsContainer;

import java.io.Serializable;

/**
 * Author: Rinkako
 * Date  : 2018/3/1
 * Usage : Label context of Task.
 */
public class Task extends ParamsContainer implements Serializable {
    /**
     * Serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Task polymorphism id, unique in a process.
     */
    private String id;

    /**
     * Task polymorphism name.
     */
    private String name;

    /**
     * Business role of this task handler.
     */
    private String brole;

    /**
     * Call back event name.
     */
    private String event;

    /**
     * Task resourcing principle.
     */
    private Principle principle;

    /**
     * Get id.
     */
    public String getId() {
        return id;
    }

    /**
     * Set id.
     * @param id Task id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Get name.
     */
    public String getName() {
        return name;
    }

    /**
     * Set name.
     * @param name Task name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get agent.
     */
    public String getBrole() {
        return this.brole;
    }

    /**
     * Set agent.
     * @param brole Task Business Role name
     */
    public void setBrole(String brole) {
        this.brole = brole;
    }

    /**
     * Get event.
     */
    public String getEvent() {
        return event;
    }

    /**
     * Set event.
     * @param event callback event name
     */
    public void setEvent(String event) {
        this.event = event;
    }

    /**
     * Get principle.
     */
    public Principle getPrinciple() {
        return principle;
    }

    /**
     * Set principle.
     * @param principle resourcing principle
     */
    public void setPrinciple(Principle principle) {
        this.principle = principle;
    }

    /**
     * Execute when encounter.
     * @param exctx execution context
     */
    @Override
    public void execute(ActionExecutionContext exctx) {
        // nothing
    }
}
