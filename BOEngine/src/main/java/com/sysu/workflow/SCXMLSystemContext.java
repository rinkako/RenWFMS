
package com.sysu.workflow;

import java.io.Serializable;
import java.util.*;

/**
 * The SCXMLSystemContext is used as a read only Context wrapper
 * SCXML系统上下文，被用来作为一个只读的上下文包装器
 * 提供SCXML只读的系统变量，这些系统变量被注入通过  unwrapped 方法 getContext()
 * <p/>
 * and provides the SCXML (read only) system variables which are injected via the unwrapped {@link #getContext()}.
 *
 * @see <a href="http://www.w3.org/TR/scxml/#SystemVariables">http://www.w3.org/TR/scxml/#SystemVariables</a>
 */
public class SCXMLSystemContext implements Context, Serializable {

    /**
     * Serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * 受保护的系统变量，5个
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
     * 这个是我的扩展的，启动这个流程的人/。。。放入到X_KEY下面去？？？？？？？
     */
    public static final String INITIATORID_KEY = "_initiator";

    /**
     * The Commons SCXML internal {@link #getPlatformVariables() platform variable key} holding the current SCXML
     * status instance
     **/
    public static final String STATUS_KEY = "status";

    /**
     * 受保护的变量的名字，私有的
     * The set of protected system variables names
     */
    private static final Set<String> PROTECTED_NAMES = new HashSet<String>(Arrays.asList(
            new String[]{EVENT_KEY, SESSIONID_KEY, SCXML_NAME_KEY, IOPROCESSORS_KEY, X_KEY, INITIATORID_KEY}
    ));

    /**
     * The wrapped system context
     * 被包裹的系统上下文，也就是私有的
     */

    private Context systemContext;

    /**
     * The auto-generated next sessionId prefixed ID下一个回话ID，的前缀
     *
     * @see #generateSessionId()
     */
    private long nextSessionSequenceId;

    /**
     * Initialize or replace systemContext
     * 初始化或者替换系统上下文
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

    public SCXMLSystemContext(Context systemContext) {
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


    public SCXMLSystemContext getSystemContext() {
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
