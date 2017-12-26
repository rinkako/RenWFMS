
package com.sysu.workflow.model;

/**
 * A <code>BodyContainer</code> is an entity that retains the element body
 * text from the document.
 */
public interface BodyContainer {

    /**
     * Set the body content as a String.
     *
     * @param body The text content in the element body.
     */
    void setBody(String body);

    /**
     * Set the body content as a String.
     *
     * @return The text content in the element body.
     */
    String getBody();

}
