/*
 * Project Ren @ 2018
 * Rinkako, Ariana, Gordan. SYSU SDCS.
 */
package org.sysu.workflow.model.extend;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Author: Rinkako
 * Date  : 2017/3/7
 * Usage : Label context of Tasks.
 */
public class Tasks implements Serializable {
    /**
     * Serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The set of &lt;task&gt; elements, parsed as Elements, that are
     * children of this &lt;tasks&gt; element.
     */
    private ArrayList<Task> tasklist;

    /**
     * The set of &lt;subProcess&gt; elements, parsed as Elements, that are
     * children of this &lt;tasks&gt; element.
     */
    private List<SubProcess> processList;

    /**
     * Constructor.
     */
    public Tasks() {

        this.tasklist = new ArrayList<Task>();
        this.processList = new ArrayList<SubProcess>();
    }

    /**
     * Get all the task children of this tasks container.
     *
     * @return Returns the task.
     */
    public final ArrayList<Task> getTaskList() {
        return this.tasklist;
    }

    /**
     * Add a Task.
     *
     * @param taskObj The task child to be added.
     */
    public final void addTask(final Task taskObj) {
        if (taskObj != null) {
            this.tasklist.add(taskObj);
        }
    }

    /**
     * Get all the process children of this tasks container.
     * @return return the process list
     */
    public final List<SubProcess> getProcessList(){
        return this.processList;
    }

    public final void addProcess(final SubProcess process){
        if(process != null)
            this.processList.add(process);
    }
}
