
package com.sysu.workflow.model;

import java.io.Serializable;

/**
 * SCXML中的content元素
 * The class in this SCXML object model that corresponds to the
 * &lt;content&gt; SCXML element.
 */
public class Content implements Serializable {

    /**
     * Serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * 表达式
     * The param expression, may be null.
     */
    private String expr;

    /**
     * content内容
     * The body of this content, may be null.
     */
    private Object body;

    /**
     * Get the expression for this content.
     *
     * @return String The expression for this content.
     */
    public final String getExpr() {
        return expr;
    }

    /**
     * Set the expression for this content.
     *
     * @param expr The expression for this content.
     */
    public final void setExpr(final String expr) {
        this.expr = expr;
    }

    /**
     * Returns the content body as DocumentFragment
     *
     * @return the content body as DocumentFragment
     */
    public Object getBody() {
        return body;
    }

    public void setBody(final Object body) {
        this.body = body;
    }
}
