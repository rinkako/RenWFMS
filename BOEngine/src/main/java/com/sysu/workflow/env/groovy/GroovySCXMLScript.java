
package com.sysu.workflow.env.groovy;

import com.sysu.workflow.Builtin;
import com.sysu.workflow.SCXMLExpressionException;
import com.sysu.workflow.XPathBuiltin;
import groovy.lang.Binding;
import groovy.lang.MissingPropertyException;
import groovy.lang.Script;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Map;

/**
 * Groovy {@link Script} base class for SCXML, providing the standard 'builtin' functions {@link #In(String)},
 * {@link #Data(String)} and {@link #Location(String)}, as well as JEXL like convenience functions
 * {@link #empty(Object)} and {@link #var(String)}.
 */
public abstract class GroovySCXMLScript extends Script {

    GroovyContext context;
    GroovyContextBinding binding;

    protected GroovySCXMLScript() {
        super(null);
    }

    @Override
    public void setBinding(final Binding binding) {
        super.setBinding(binding);
        this.binding = (GroovyContextBinding) binding;
        this.context = this.binding.getContext();
    }

    /**
     * Implements the In() predicate for SCXML documents ( see Builtin#isMember )
     *
     * @param state The State ID to compare with
     * @return Whether this State belongs to this Set
     */
    public boolean In(final String state) {
        return Builtin.isMember(context, state);
    }

    /**
     * Implements the Data() predicate for SCXML documents.
     *
     * @param expression the XPath expression
     * @return the data matching the expression
     * @throws SCXMLExpressionException A malformed expression exception
     */
    public Object Data(final String expression) throws SCXMLExpressionException {
        return XPathBuiltin.eval(context, expression);
    }

    /**
     * Implements the Location() predicate for SCXML documents.
     *
     * @param location the XPath expression
     * @return the location list for the location expression
     * @throws SCXMLExpressionException A malformed expression exception
     */
    public Object Location(final String location) throws SCXMLExpressionException {
        return XPathBuiltin.evalLocation(context, location);
    }

    /**
     * The var function can be used to check if a variable is defined,
     * <p>
     * In the Groovy language (implementation) you cannot check for an undefined variable directly:
     * Groovy will raise a MissingPropertyException before you get the chance.
     * </p>
     * <p>
     * The var function works around this by indirectly looking up the variable, which you therefore have to specify as a String.
     * </p>
     * <p>
     * So, use <code>var('name')</code>, not <code>var(name)</code>
     * </p>
     * <p>
     * Note: this function doesn't support object navigation, like <code>var('name.property')</code>.<br>
     * Instead, once you established a variable 'name' exists, you <em>thereafter</em> can use the standard Groovy
     * Safe Navigation operator (?.), like so: <code>name?.property</code>.<br>
     * See for more information: <a href="http://docs.codehaus.org/display/GROOVY/Operators#Operators-SafeNavigationOperator(?.)">Groovy SafeNavigationOperator</a>
     * </p>
     *
     * @param property the name of variable to check if it exists
     * @return true if the variable exists, false otherwise
     */
    public boolean var(String property) {
        if (!context.has(property)) {
            try {
                getMetaClass().getProperty(this, property);
            } catch (MissingPropertyException e) {
                return false;
            }
        }
        return true;
    }

    /**
     * The empty function mimics the behavior of the JEXL empty function, in that it returns true if the parameter is:
     * <ul>
     * <li>null, or</li>
     * <li>an empty String, or</li>
     * <li>an zero length Array, or</li>
     * <li>an empty Collection, or</li>
     * <li>an empty Map</li>
     * </ul>
     * <p>
     * Note: one difference with the JEXL language is that Groovy doesn't allow checking for undefined variables.<br>
     * Before being able to check, Groovy will already have raised an MissingPropertyException if the variable cannot be found.<br>
     * To work around this, the custom {@link #var(String)} function is available.
     * </p>
     *
     * @param obj the object to check if it is empty
     * @return true if the object is empty, false otherwise
     */
    public boolean empty(Object obj) {
        return obj == null ||
                (obj instanceof String && ((String) obj).isEmpty()) ||
                ((obj.getClass().isArray() && Array.getLength(obj) == 0)) ||
                (obj instanceof Collection && ((Collection) obj).size() == 0) ||
                (obj instanceof Map && ((Map) obj).isEmpty());
    }
}
