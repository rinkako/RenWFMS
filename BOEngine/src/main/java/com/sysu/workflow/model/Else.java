
package com.sysu.workflow.model;

/**
 * The class in this SCXML object model that corresponds to the
 * &lt;else&gt; SCXML element.
 */
public class Else extends ElseIf {

    /**
     * Serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * &lt;else/&gt; is equivalent to &lt;elseif cond="true" /&gt;.
     */
    public Else() {
        super();
        setCond("true");
    }

}

