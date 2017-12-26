
package com.sysu.workflow.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * The class in this SCXML object model that corresponds to the SCXML
 * &lt;datamodel&gt; element.
 */
public class Datamodel implements Serializable {

    /**
     * Serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The set of &lt;data&gt; elements, parsed as Elements, that are
     * children of this &lt;datamodel&gt; element.
     */
    private List<Data> data;

    /**
     * Constructor.
     */
    public Datamodel() {
        this.data = new ArrayList<Data>();
    }

    /**
     * Get all the data children of this datamodel.
     *
     * @return Returns the data.
     */
    public final List<Data> getData() {
        return data;
    }

    /**
     * Add a Data.
     *
     * @param datum The data child to be added.
     */
    public final void addData(final Data datum) {
        if (datum != null) {
            data.add(datum);
        }
    }

}

