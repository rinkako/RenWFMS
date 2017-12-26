
package com.sysu.workflow.model;

/**
 * Defines the allowable Transition type attribute values,
 * <p>
 * The Transition type determines whether the source state is exited in transitions
 * whose target state is a descendant of the source state.
 * </p>
 *
 * @see <a href="http://www.w3.org/TR/2014/CR-scxml-20140313/#transition">
 * http://www.w3.org/TR/2014/CR-scxml-20140313/#transition</a>
 */
public enum TransitionType {
    internal,
    external
}
