
package com.sysu.workflow;

import com.sysu.workflow.env.SimpleDispatcher;
import com.sysu.workflow.env.SimpleErrorReporter;
import com.sysu.workflow.invoke.Invoker;
import com.sysu.workflow.invoke.InvokerException;
import com.sysu.workflow.invoke.SimpleSCXMLInvoker;
import com.sysu.workflow.model.Invoke;
import com.sysu.workflow.model.ModelException;
import com.sysu.workflow.model.SCXML;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.*;

/**
 * SCXML执行上下文，提供了在解释一个SCXML的过程中使用到的所有服务和内部数据，
 * 状态机通过micro和macro 步骤执行
 * SCXMLExecutionContext provides all the services and internal data used during the interpretation of an SCXML
 * statemachine across micro and macro steps
 * <p/>
 * update by zhengshouzi on 2015/12/29.
 */
public class SCXMLExecutionContext implements SCXMLIOProcessor {

    /**
     * 默认的和需要支持的SCXML处理器调用服务URI
     * Default and required supported SCXML Processor Invoker service URI
     */
    public static final String SCXML_INVOKER_TYPE_URI = "http://www.w3.org/TR/scxml/";
    /**
     * SCXML_INVOKER_TYPE的别名
     * Alias for {@link #SCXML_INVOKER_TYPE_URI}
     */
    public static final String SCXML_INVOKER_TYPE = "scxml";
    /**
     * action执行上下文实例，提供了对执行上下文严格的访问
     * The action execution context instance, providing restricted access to this execution context
     */
    private final ActionExecutionContext actionExecutionContext;
    /**
     * SCXML执行上下文的执行器
     * The SCXMLExecutor of this SCXMLExecutionContext
     */
    private final SCXMLExecutor scxmlExecutor;
    /**
     * 内部事件队列
     * The internal event queue
     */
    private final Queue<TriggerEvent> internalEventQueue = new LinkedList<TriggerEvent>();
    /**
     * 调用的类的Map,通过调用的目标类型指定key
     * <p/>
     * The Invoker classes map, keyed by invoke target types (specified using "type" attribute).
     */
    private final Map<String, Class<? extends Invoker>> invokerClasses = new HashMap<String, Class<? extends Invoker>>();
    /**
     * 存储了唯一的invokeId对于一个调用
     * The map storing the unique invokeId for an Invoke with an active Invoker
     */
    private final Map<Invoke, String> invokeIds = new HashMap<Invoke, String>();
    /**
     * 活跃的调用者，
     * The Map of active Invoker, keyed by their unique invokeId.
     */
    private final Map<String, Invoker> invokers = new HashMap<String, Invoker>();
    /**
     * 当前I/O处理器的集合
     * The Map of the current ioProcessors
     */
    private final Map<String, SCXMLIOProcessor> ioProcessors = new HashMap<String, SCXMLIOProcessor>();
    /**
     * SCXML执行日志
     * SCXML Execution Logger for the application.
     */
    private Log appLog = LogFactory.getLog(SCXMLExecutionContext.class);
    /**
     * SCInstance实例
     * The SCInstance.
     */
    private SCInstance scInstance;
    /**
     * 表达式求值器
     * The evaluator for expressions.
     */
    private Evaluator evaluator;
    /**
     * 外部事件处理器
     * The external IOProcessor for Invokers to communicate back on
     */
    private SCXMLIOProcessor externalIOProcessor;
    /**
     * 事件分发器，
     * The event dispatcher to interface with external documents etc.
     */
    private EventDispatcher eventdispatcher;
    /**
     * 环境描述错误报告
     * The environment specific error reporter.
     */
    private ErrorReporter errorReporter = null;
    /**
     * 通知注册
     * The notification registry.
     */
    private NotificationRegistry notificationRegistry;
    /**
     * 表明SCXML的配置应该在执行之前被检查
     * Flag indicating if the SCXML configuration should be checked before execution (default = true)
     */
    private boolean checkLegalConfiguration = true;

    /**
     * SCInstance的sessionId本地缓存
     * Local cache of the SCInstance sessionId, to be able to check against clear/reinitialization
     */
    private String sessionId;

    public String Tid;

    public String RootTid;


    /**
     * 构造器
     *
     * @param scxmlExecutor   The SCXMLExecutor of this SCXMLExecutionContext
     * @param evaluator       The evaluator
     * @param eventDispatcher The event dispatcher, if null a SimpleDispatcher instance will be used
     * @param errorReporter   The error reporter, if null a SimpleErrorReporter instance will be used
     */
    protected SCXMLExecutionContext(SCXMLExecutor scxmlExecutor, Evaluator evaluator,
                                    EventDispatcher eventDispatcher, ErrorReporter errorReporter) {
        this.scxmlExecutor = scxmlExecutor;
        this.externalIOProcessor = scxmlExecutor;
        this.evaluator = evaluator;
        this.eventdispatcher = eventDispatcher != null ? eventDispatcher : new SimpleDispatcher();
        this.errorReporter = errorReporter != null ? errorReporter : new SimpleErrorReporter();
        this.notificationRegistry = new NotificationRegistry();


        this.scInstance = new SCInstance(this, this.evaluator, this.errorReporter);
        this.actionExecutionContext = new ActionExecutionContext(this);

        ioProcessors.put(SCXMLIOProcessor.DEFAULT_EVENT_PROCESSOR, getExternalIOProcessor());
        ioProcessors.put(SCXMLIOProcessor.SCXML_EVENT_PROCESSOR, getExternalIOProcessor());
        ioProcessors.put(SCXMLIOProcessor.INTERNAL_EVENT_PROCESSOR, getInternalIOProcessor());
        if (scxmlExecutor.getParentSCXMLExecutor() != null) {
            ioProcessors.put(SCXMLIOProcessor.PARENT_EVENT_PROCESSOR, scxmlExecutor.getParentSCXMLExecutor());
        }
        initializeIOProcessors();
        registerInvokerClass(SCXML_INVOKER_TYPE_URI, SimpleSCXMLInvoker.class);
        registerInvokerClass(SCXML_INVOKER_TYPE, SimpleSCXMLInvoker.class);
    }

    public SCXMLExecutor getSCXMLExecutor() {
        return scxmlExecutor;
    }

    public SCXMLIOProcessor getExternalIOProcessor() {
        return externalIOProcessor;
    }

    public SCXMLIOProcessor getInternalIOProcessor() {
        return this;
    }

    /**
     * @return Returns the restricted execution context for actions
     */
    public ActionExecutionContext getActionExecutionContext() {
        return actionExecutionContext;
    }

    /**
     * @return Returns true if this state machine is running
     */
    public boolean isRunning() {
        return scInstance.isRunning();
    }

    /**
     * Stop a running state machine
     * 停止状态机的运行
     */
    public void stopRunning() {
        scInstance.setRunning(false);
    }

    /**
     * @return if the SCXML configuration will be checked before execution
     * 是否是合法的配置
     */
    public boolean isCheckLegalConfiguration() {
        return checkLegalConfiguration;
    }

    /**
     * Set if the SCXML configuration should be checked before execution (default = true)
     * 设置是否是合法的配置
     *
     * @param checkLegalConfiguration flag to set
     */
    public void setCheckLegalConfiguration(boolean checkLegalConfiguration) {
        this.checkLegalConfiguration = checkLegalConfiguration;
    }

    /**
     * Initialize method which will cancel all current active Invokers, clear the internal event queue and mark the
     * state machine process as running (again).
     * 初始化方法
     * 取消所有当前活跃的Invokers，清理内部事件队列，启动状态机过程
     *
     * @throws ModelException if the state machine instance failed to initialize.
     */
    public void initialize() throws ModelException {
        if (!invokeIds.isEmpty()) {
            for (Invoke invoke : new ArrayList<Invoke>(invokeIds.keySet())) {
                cancelInvoker(invoke);
            }
        }
        internalEventQueue.clear();
        scInstance.initialize();
        initializeIOProcessors();
        scInstance.setRunning(true);
        //saveProcessInstance();
    }


    /**
     * @return Returns the SCXML Execution Logger for the application
     * 得到SCXML Execution日志
     */
    public Log getAppLog() {
        return appLog;
    }

    /**
     * @return Returns the state machine
     * 返回状态机
     */
    public SCXML getStateMachine() {
        return scInstance.getStateMachine();
    }

    /**
     * Set or replace the state machine to be executed
     * <p>
     * If the state machine instance has been initialized before, it will be initialized again, destroying all existing
     * state!
     * </p>
     * <p/>
     * 设置或者替换被执行的状态机
     * 如果状态机已经初始化过了，这个方法将会再次初始化，清理掉之前所有存在的状态
     *
     * @param stateMachine The state machine to set
     * @throws ModelException if attempting to set a null value or the state machine instance failed to re-initialize
     */
    protected void setStateMachine(SCXML stateMachine) throws ModelException {
        scInstance.setStateMachine(stateMachine);
        // synchronize possible derived evaluator
        this.evaluator = scInstance.getEvaluator();
        initializeIOProcessors();
    }

    /**
     * The SCXML specification section "C.1.1 _ioprocessors Value" states that the SCXMLEventProcessor <em>must</em>
     * maintain a 'location' field inside its entry in the _ioprocessors environment variable.
     * <p/>
     * 返回SCXMLEventProcessor的位置
     *
     * @return the 'location' of the SCXMLEventProcessor
     */
    public String getLocation() {
        return null;
    }

    /**
     * 返回SCInstance实例
     *
     * @return Returns the SCInstance
     */
    public SCInstance getScInstance() {
        return scInstance;
    }

    /**
     * 返回求值器
     *
     * @return Returns The evaluator.
     */
    public Evaluator getEvaluator() {
        return evaluator;
    }

    /**
     * Set or replace the evaluator
     * <p>
     * If the state machine instance has been initialized before, it will be initialized again, destroying all existing
     * state!
     * </p>
     * 设置或者替换求值器，
     * 如果状态机实例之前被初始化过了，将会再次被初始化，清理掉所有之前的状态
     *
     * @param evaluator The evaluator to set
     * @throws ModelException if attempting to set a null value or the state machine instance failed to re-initialize
     */
    protected void setEvaluator(Evaluator evaluator) throws ModelException {
        scInstance.setEvaluator(evaluator, false);
        // synchronize possible derived evaluator
        this.evaluator = scInstance.getEvaluator();
        initializeIOProcessors();
    }

    /**
     * @return Returns the error reporter
     * 返回错误报告
     */
    public ErrorReporter getErrorReporter() {
        return errorReporter;
    }

    /**
     * Set or replace the error reporter
     * 设置或者替换错误报告
     *
     * @param errorReporter The error reporter to set, if null a SimpleErrorReporter instance will be used instead
     */
    protected void setErrorReporter(ErrorReporter errorReporter) {
        this.errorReporter = errorReporter != null ? errorReporter : new SimpleErrorReporter();
        try {
            scInstance.setErrorReporter(errorReporter);
        } catch (ModelException me) {
            // won't happen
        }
    }

    /**
     * @return Returns the event dispatcher
     * 返回之间分发器
     */
    public EventDispatcher getEventDispatcher() {
        return eventdispatcher;
    }

    /**
     * Set or replace the event dispatch
     * 设置或者替换事件分发器
     *
     * @param eventdispatcher The event dispatcher to set, if null a SimpleDispatcher instance will be used instead
     */
    protected void setEventdispatcher(EventDispatcher eventdispatcher) {
            this.eventdispatcher = eventdispatcher != null ? eventdispatcher : new SimpleDispatcher();
    }

    /**
     * @return Returns the notification registry
     * 返回通知注册
     */
    public NotificationRegistry getNotificationRegistry() {
        return notificationRegistry;
    }

    /**
     * Initialize the _ioprocessors environment variable, which only can be done when the evaluator is available
     * 初始化 _ioprocessor环境变量，，只有在求值器不为空的情况下可用
     */
    protected void initializeIOProcessors() {
        if (scInstance.getEvaluator() != null) {
            // lazy register/reset #_scxml_sessionId event target
            String currentSessionId = (String) getScInstance().getSystemContext().get(SCXMLSystemContext.SESSIONID_KEY);
            if (sessionId != null && !sessionId.equals(currentSessionId)) {
                // remove possible old/stale #_scxml_sessionId target
                ioProcessors.remove(SCXMLIOProcessor.SCXML_SESSION_EVENT_PROCESSOR_PREFIX + sessionId);
            }
            sessionId = currentSessionId;
            if (!ioProcessors.containsKey(SCXMLIOProcessor.SCXML_SESSION_EVENT_PROCESSOR_PREFIX + sessionId)) {
                ioProcessors.put(SCXMLIOProcessor.SCXML_SESSION_EVENT_PROCESSOR_PREFIX + sessionId, getExternalIOProcessor());
            }
            getScInstance().getSystemContext().setLocal(SCXMLSystemContext.IOPROCESSORS_KEY, Collections.unmodifiableMap(ioProcessors));
        }
    }

    /**
     * Detach the current SCInstance to allow external serialization.
     * 分离当前的SCInstance
     * <p>
     * {@link #attachInstance(SCInstance)} can be used to re-attach a previously detached instance
     * </p>
     *
     * @return the detached instance
     */
    protected SCInstance detachInstance() {
        SCInstance instance = scInstance;
        scInstance.detach();
        Map<String, Object> systemVars = scInstance.getSystemContext().getVars();
        systemVars.remove(SCXMLSystemContext.IOPROCESSORS_KEY);
        systemVars.remove(SCXMLSystemContext.EVENT_KEY);
        scInstance = null;
        return instance;
    }

    /**
     * Re-attach a previously detached SCInstance.
     * 再连接SCInstance
     * <p>
     * Note: an already attached instance will get overwritten (and thus lost).
     * </p>
     *
     * @param instance An previously detached SCInstance
     */
    protected void attachInstance(SCInstance instance) {
        if (scInstance != null) {
            scInstance.detach();
        }
        scInstance = instance;
        if (scInstance != null) {
            scInstance.detach();
            try {
                scInstance.setInternalIOProcessor(this);
                scInstance.setEvaluator(evaluator, true);
                scInstance.setErrorReporter(errorReporter);
                initializeIOProcessors();
            } catch (ModelException me) {
                // should not happen
            }
        }
    }

    /**
     * Register an Invoker for this target type.
     * 注册目标类型的调用者
     *
     * @param type         The target type (specified by "type" attribute of the invoke element).
     * @param invokerClass The Invoker class.
     */
    protected void registerInvokerClass(final String type, final Class<? extends Invoker> invokerClass) {
        invokerClasses.put(type, invokerClass);
    }

    /**
     * Remove the Invoker registered for this target type (if there is one registered).
     * 移除调用者
     *
     * @param type The target type (specified by "type" attribute of the invoke element).
     */
    protected void unregisterInvokerClass(final String type) {
        invokerClasses.remove(type);
    }

    /**
     * Create a new {@link Invoker}
     * <p/>
     * 创建一个新的调用者
     *
     * @param type The type of the target being invoked.
     * @return An {@link Invoker} for the specified type, if an
     * invoker class is registered against that type,
     * <p/>
     * <code>null</code> otherwise.
     * @throws InvokerException When a suitable {@link Invoker} cannot be instantiated.
     */
    public Invoker newInvoker(final String type) throws InvokerException {
        Class<? extends Invoker> invokerClass = invokerClasses.get(type);
        if (invokerClass == null) {
            throw new InvokerException("No Invoker registered for type \"" + type + "\"");
        }
        try {
            return invokerClass.newInstance();
        } catch (InstantiationException ie) {
            throw new InvokerException(ie.getMessage(), ie.getCause());
        } catch (IllegalAccessException iae) {
            throw new InvokerException(iae.getMessage(), iae.getCause());
        }
    }

    /**
     * Get the {@link Invoker} for this {@link Invoke}.
     * May return <code>null</code>. A non-null {@link Invoker} will be
     * returned if and only if the {@link Invoke} parent TransitionalState is
     * currently active and contains the &lt;invoke&gt; child.
     * <p/>
     * 得到调用者
     *
     * @param invoke The <code>Invoke</code>.
     * @return The Invoker.
     */
    public Invoker getInvoker(final Invoke invoke) {
        return invokers.get(invokeIds.get(invoke));
    }

    /**
     * Register the active {@link Invoker} for a {@link Invoke}
     * <p/>
     * 注册调用者
     *
     * @param invoke  The Invoke.
     * @param invoker The Invoker.
     * @throws InvokerException when the Invoker doesn't have an invokerId
     */
    public void registerInvoker(final Invoke invoke, final Invoker invoker) throws InvokerException {
        String invokeId = invoker.getInvokeId();
        if (invokeId == null) {
            throw new InvokerException("Registering an Invoker without invokerId");
        }
        invokeIds.put(invoke, invokeId);
        invokers.put(invokeId, invoker);
        ioProcessors.put(SCXMLIOProcessor.EVENT_PROCESSOR_ALIAS_PREFIX + invoke.getId(), invoker.getChildIOProcessor());
        initializeIOProcessors();
    }

    /**
     * Remove a previously active Invoker, which must already have been canceled
     *
     * @param invoke The Invoke for the Invoker to remove
     */
    public void removeInvoker(final Invoke invoke) {
        invokers.remove(invokeIds.remove(invoke));
        ioProcessors.remove(SCXMLIOProcessor.EVENT_PROCESSOR_ALIAS_PREFIX + invoke.getId());
        initializeIOProcessors();
    }

    /**
     * @return Returns the map of current active Invokes and their invokeId
     */
    public Map<Invoke, String> getInvokeIds() {
        return invokeIds;
    }


    /**
     * Cancel and remove an active Invoker
     * 取消调用者
     *
     * @param invoke The Invoke for the Invoker to cancel
     */
    public void cancelInvoker(Invoke invoke) {
        String invokeId = invokeIds.get(invoke);
        if (invokeId != null) {
            try {
                invokers.get(invokeId).cancel();
            } catch (InvokerException ie) {
                TriggerEvent te = new TriggerEvent("failed.invoke.cancel." + invokeId, TriggerEvent.ERROR_EVENT);
                addEvent(te);
            }
            removeInvoker(invoke);
        }
    }

    /**
     * Add an event to the internal event queue
     * 添加一个事件到内部事件队列
     *
     * @param event The event
     */

    public void addEvent(TriggerEvent event) {
        internalEventQueue.add(event);
    }

    /**
     * @return Returns the next event from the internal event queue, if available
     * 返回内部事件队列中的下一个事件
     */
    public TriggerEvent nextInternalEvent() {
        return internalEventQueue.poll();
    }

    /**
     * @return Returns true if the internal event queue isn't empty
     * 如果内部事件队列不是空，返回true
     */
    public boolean hasPendingInternalEvent() {
        return !internalEventQueue.isEmpty();
    }


    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) { this.sessionId = sessionId; }

}
