
package com.sysu.workflow.model;

import org.w3c.dom.Node;

import java.util.List;

/**
 * An <code>ExternalContent</code> implementation represents an
 * element in the SCXML document that may contain &quot;body
 * content&quot;, which means an arbitrary number of child nodes
 * belonging to external namespaces.
 */
public interface ExternalContent {

    /**
     * Return the list of external namespaced children as
     * DOM node instances.
     *
     * @return The list of (external namespaced) child nodes.
     */
    List<Node> getExternalNodes();

}

