
package com.sysu.workflow.model;

import java.util.Comparator;

/**
 * DocumentOrder is implemented by {@link EnterableState} and {@link Transition} elements in the SCXML document
 * representing their document order,
 * <p>
 * They are ordered with ancestor states before their descendant states,
 * and the transitions within a state in document order before any descendant states.
 * </p>
 * <p>Note: it is assumed there will be no more than Integer.MAX_VALUE of such elements in a single SCXML document</p>
 */
public interface DocumentOrder {

    Comparator<DocumentOrder> documentOrderComparator = new Comparator<DocumentOrder>() {

        public int compare(final DocumentOrder o1, final DocumentOrder o2) {
            return o1.getOrder() - o2.getOrder();
        }
    };

    Comparator<DocumentOrder> reverseDocumentOrderComparator = new Comparator<DocumentOrder>() {

        public int compare(final DocumentOrder o1, final DocumentOrder o2) {
            return o2.getOrder() - o1.getOrder();
        }
    };

    /**
     * @return the relative document order within the SCXML document of this element
     */
    int getOrder();
}
