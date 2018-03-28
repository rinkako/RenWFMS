
package org.sysu.workflow;

import java.io.Serializable;
import java.util.*;

/**
 * The BOXMLSystemContext is used as a read only Context wrapper
 * <p/>
 * and provides the SCXML (read only) system variables which are injected via the unwrapped {@link #getContext()}.
 *
 * @see <a href="http://www.w3.org/TR/scxml/#SystemVariables">http://www.w3.org/TR/scxml/#SystemVariables</a>
 */
public class BOXMLSystemContext implements Context, Serializable {

    /**
     * Serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The protected system variables names as defined in the SCXML specification
     *
     * @see <a href="http://www.w3.org/TR/scxml/#SystemVariables">http://www.w3.org/TR/scxml/#SystemVariables</a>
     */
    public static final String EVENT_KEY = "_event";
    public static final String SESSIONID_KEY = "_sessionid";
    public static final String SCXML_NAME_KEY = "_name";
    public static final String IOPROCESSORS_KEY = "_ioprocessors";
    public static final String X_KEY = "_x";

    /**
     * Extend: instance index key
     */
    public static final String INSTANCEINDEX_KEY = "_instanceIndex";
    public static final String NOTIFIABLE_ID_KEY = "_id";
    public static final String GLOBAL_ID_KEY = "_gid";


    /**
     * The Commons SCXML internal {@link #getPlatformVariables() platform variable key} holding the current SCXML
     * status instance
     **/
    public static final String STATUS_KEY = "status";

    /**
     * The set of protected system variables names
     */
    private static final Set<String> PROTECTED_NAMES = new HashSet<String>(Arrays.asList(
            EVENT_KEY, SESSIONID_KEY, SCXML_NAME_KEY, IOPROCESSORS_KEY, X_KEY, INSTANCEINDEX_KEY,
            NOTIFIABLE_ID_KEY, GLOBAL_ID_KEY));

    /**
     * The wrapped system context
     */

    private Context systemContext;

    /**
     * The auto-generated next sessionId prefixed ID
     *
     * @see #generateSessionId()
     */
    private long nextSessionSequenceId;

    /**
     * Initialize or replace systemContext
     *
     * @param systemContext the system context to set
     * @throws NullPointerException if systemContext == null
     */
    void setSystemContext(Context systemContext) {
        if (this.systemContext != null) {
            // replace systemContext
            systemContext.getVars().putAll(this.systemContext.getVars());
        } else {
            // create Platform variables map
            systemContext.setLocal(X_KEY, new HashMap<String, Object>());
        }
        this.systemContext = systemContext;
        this.protectedVars = Collections.unmodifiableMap(systemContext.getVars());
    }

    /**
     * The unmodifiable wrapped variables map from the wrapped system context
     */
    private Map<String, Object> protectedVars;

    public BOXMLSystemContext(Context systemContext) {
        setSystemContext(systemContext);
    }

    public String generateSessionId() {
        return getContext().get(SESSIONID_KEY) + "-" + nextSessionSequenceId++;
    }


    public void set(final String name, final Object value) {
        if (PROTECTED_NAMES.contains(name)) {
            throw new UnsupportedOperationException();
        }
        // non-protected variables are set on the parent of the system context (e.g. root context)
        systemContext.getParent().set(name, value);
    }


    public void setLocal(final String name, final Object value) {
        throw new UnsupportedOperationException();
    }


    public Object get(final String name) {
        return systemContext.get(name);
    }


    public boolean has(final String name) {
        return systemContext.has(name);
    }


    public boolean hasLocal(final String name) {
        return systemContext.hasLocal(name);
    }


    public Map<String, Object> getVars() {
        return protectedVars;
    }


    public void reset() {
        throw new UnsupportedOperationException();
    }


    public Context getParent() {
        return systemContext.getParent();
    }


    public BOXMLSystemContext getSystemContext() {
        return this;
    }

    /**
     * @return The Platform specific system variables map stored under the {@link #X_KEY _x} root system variable
     */
    @SuppressWarnings("unchecked")
    public Map<String, Object> getPlatformVariables() {
        return (Map<String, Object>) get(X_KEY);
    }

    /**
     * @return Returns the wrapped (modifiable) system context
     */
    Context getContext() {
        return systemContext;
    }
}
