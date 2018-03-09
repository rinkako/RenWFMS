
package org.sysu.workflow;

import org.sysu.workflow.model.SCXML;

/**
 * An EvaluatorProvider provides an {@link Evaluator} instance for a specific SCXML datamodel type.
 */
public interface EvaluatorProvider {

    /**
     * @return The SCXML datamodel type this provider supports
     */
    String getSupportedDatamodel();

    /**
     * @return a default or generic {@link Evaluator} supporting the {@link #getSupportedDatamodel()}
     */
    Evaluator getEvaluator();

    /**
     * Factory method to return a dedicated and optimized {@link Evaluator} instance for a specific SCXML document.
     * <p>
     * As the returned Evaluator <em>might</em> be optimized and dedicated for the SCXML document instance, the
     * Evaluator may not be shareable and reusable for other SCXML documents.
     * </p>
     *
     * @param document the SCXML document
     * @return a new and not sharable Evaluator instance
     */
    Evaluator getEvaluator(SCXML document);
}
