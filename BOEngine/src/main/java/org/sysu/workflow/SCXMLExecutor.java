
package org.sysu.workflow;

import org.sysu.workflow.bridge.YAWLAdapter;
import org.sysu.workflow.engine.*;
import org.sysu.workflow.engine.TimeInstanceTree;
import org.sysu.workflow.invoke.Invoker;
import org.sysu.workflow.model.*;
import org.sysu.workflow.model.extend.Resources;
import org.sysu.workflow.model.extend.Role;
import org.sysu.workflow.semantics.SCXMLSemanticsImpl;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;
import org.sysu.workflow.engine.InstanceManager;
import org.sysu.workflow.engine.TimeTreeNode;
import org.sysu.workflow.model.*;


import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * 执行SCXML文档的 SCXML 引擎
 * <p/>
 * 执行的语义封装在 SCXMLSementics implementation里面，默认实现是SCXMLSemanticsImpl类
 * <p/>
 * 引擎使用SCXMLExecutionContext来管理状态，来为SCXMLSemanticesImpl类提供所有的服务
 *
 * @see SCXMLSemantics
 */
public class SCXMLExecutor implements SCXMLIOProcessor {

    /**
     * The Logger for the SCXMLExecutor.
     * SCXMLExecutor的日志记录器
     */
    private Log log = LogFactory.getLog(SCXMLExecutor.class);

    /**
     * Parent SCXMLExecutor
     * <p/>
     * 父SCXMLExecutor
     */
    private SCXMLExecutor parentSCXMLExecutor;

    /**
     * Interpretation semantics.
     * SCXML文档所使用的语义
     */
    private SCXMLSemantics semantics;

    /**
     * The state machine execution context
     * 状态机执行上下文
     */
    private SCXMLExecutionContext exctx;

    /**
     * The index of this executor in application
     * 该状态机执行器在应用程序的索引
     */
    private String executorIndex;

    public String Tid = UUID.randomUUID().toString();

    public String RootTid = "";

    /**
     * The external event queue
     * 外部事件队列
     */
    private final Queue<TriggerEvent> externalEventQueue = new ConcurrentLinkedQueue<TriggerEvent>();

    /**
     * Convenience constructor.
     * 简便的构造器
     */
    public SCXMLExecutor() {
        this(null, null, null, null);
    }

    /**
     * 构造器
     *
     * @param expEvaluator The expression evaluator
     * @param evtDisp      The event dispatcher
     * @param errRep       The error reporter
     */
    public SCXMLExecutor(final Evaluator expEvaluator,
                         final EventDispatcher evtDisp, final ErrorReporter errRep) {
        this(expEvaluator, evtDisp, errRep, null);
    }

    /**
     * Constructor.
     *
     * @param expEvaluator The expression evaluator
     * @param evtDisp      The event dispatcher
     * @param errRep       The error reporter
     * @param semantics    The SCXML semantics
     */
    public SCXMLExecutor(final Evaluator expEvaluator,
                         final EventDispatcher evtDisp, final ErrorReporter errRep,
                         final SCXMLSemantics semantics) {
        this.semantics = semantics != null ? semantics : new SCXMLSemanticsImpl();
        this.exctx = new SCXMLExecutionContext(this, expEvaluator, evtDisp, errRep);
        this.exctx.Tid = this.Tid;
        this.exctx.RootTid = this.RootTid;
    }


    /**
     * Constructor.
     *
     * @param expEvaluator The expression evaluator
     * @param evtDisp      The event dispatcher
     * @param errRep       The error reporter
     * @param semantics    The SCXML semantics
     * @param rootTid      根状态机的Tid
     */
    public SCXMLExecutor(final Evaluator expEvaluator,
                         final EventDispatcher evtDisp, final ErrorReporter errRep,
                         final SCXMLSemantics semantics,
                         final String rootTid) {
        this.semantics = semantics != null ? semantics : new SCXMLSemanticsImpl();
        this.exctx = new SCXMLExecutionContext(this, expEvaluator, evtDisp, errRep);
        this.RootTid = rootTid;
        this.exctx.Tid = this.Tid;
        this.exctx.RootTid = this.RootTid;
    }

    /**
     * Constructor using a parent SCXMLExecutor
     *
     * @param parentSCXMLExecutor the parent SCXMLExecutor
     */
    public SCXMLExecutor(final SCXMLExecutor parentSCXMLExecutor) {
        this.parentSCXMLExecutor = parentSCXMLExecutor;
        this.semantics = parentSCXMLExecutor.semantics;
        this.exctx = new SCXMLExecutionContext(this, parentSCXMLExecutor.getEvaluator(),
                parentSCXMLExecutor.getEventdispatcher(), parentSCXMLExecutor.getErrorReporter());
        this.exctx.Tid = this.Tid;
        this.exctx.RootTid = this.RootTid;
    }

    /**
     * @return 返回父亲的SCXMLExecutor
     */
    protected SCXMLExecutor getParentSCXMLExecutor() {
        return parentSCXMLExecutor;
    }

    /**
     * 得到当前状态机实例的当前状态
     *
     * @return The current Status
     */
    public synchronized Status getStatus() {
        return exctx.getScInstance().getCurrentStatus();
    }

    /**
     * Initializes the state machine with a specific active configuration
     * <p>
     * This will first (re)initialize the current state machine: clearing all variable contexts, histories and current
     * status, and clones the SCXML root datamodel into the root context.
     * </p>
     * 使用指定的active 的configuration 初始化状态机
     * 这将会初始化或者再次初始化当前的状态，清除所有的变量和上下文，历史和上下文，并且赋值原来的数据模型到新的根上下文
     *
     * @param atomicStateIds The set of atomic state ids for the state machine
     * @throws ModelException when the state machine hasn't been properly configured yet, when an unknown or illegal
     *                        stateId is specified, or when the specified active configuration does not represent a legal configuration.
     * @see SCInstance#initialize()
     * @see SCXMLSemantics#isLegalConfiguration(Set, ErrorReporter)
     */
    public synchronized void setConfiguration(Set<String> atomicStateIds) throws ModelException {
        exctx.initialize();
        Set<EnterableState> states = new HashSet<EnterableState>();
        for (String stateId : atomicStateIds) {
            TransitionTarget tt = getStateMachine().getTargets().get(stateId);
            if (tt instanceof EnterableState && ((EnterableState) tt).isAtomicState()) {
                EnterableState es = (EnterableState) tt;
                while (es != null && !states.add(es)) {
                    es = es.getParent();
                }
            } else {
                throw new ModelException("Illegal atomic stateId " + stateId + ": state unknown or not an atomic state");
            }
        }
        if (semantics.isLegalConfiguration(states, getErrorReporter())) {
            for (EnterableState es : states) {
                exctx.getScInstance().getStateConfiguration().enterState(es);
            }
            logState();
        } else {
            throw new ModelException("Illegal state machine configuration!");
        }
    }

    /**
     * Set or replace the expression evaluator
     * <p>
     * If the state machine instance has been initialized before, it will be initialized again, destroying all existing
     * state!
     * </p>
     * <p>
     * Also the external event queue will be cleared.
     * </p>
     * <p/>
     * 设置或者替换表达式求值器
     * 如果状态机实例已经初始化过了，它应该再次初始化，清理所有存在的状态
     * 外部事件队列也应该被清理
     *
     * @param evaluator The evaluator to set
     * @throws ModelException if attempting to set a null value or the state machine instance failed to re-initialize
     */
    public void setEvaluator(final Evaluator evaluator) throws ModelException {
        exctx.setEvaluator(evaluator);
    }

    /**
     * 得到当前的表达式求值器
     *
     * @return Evaluator The evaluator in use.
     */
    public Evaluator getEvaluator() {
        return exctx.getEvaluator();
    }

    /**
     * Get the root context for the state machine execution.
     * 得到当前状态机执行的根上下文
     * <p>
     * 根上下文能够被用来提供外部数据给状态机使用
     * The root context can be used for providing external data to the state machine
     * </p>
     *
     * @return Context The root context.
     */
    public Context getRootContext() {
        return exctx.getScInstance().getRootContext();
    }

    /**
     * Get the global context for the state machine execution.
     * 得到状态机执行的全局上下文
     * 全局上下文是最顶层的上下文，，在状态机内部应该被当做  read-only状态
     * <p>
     * The global context is the top level context within the state machine itself and should be regarded and treated
     * "read-only" from external usage.
     * </p>
     *
     * @return Context The global context.
     */
    public Context getGlobalContext() {
        return exctx.getScInstance().getGlobalContext();
    }

    /**
     * 设置状态机执行的根上下文
     * <p/>
     * 应该被调用在引擎启动之前
     * <p/>
     * Set the root context for the state machine execution.
     * <b>NOTE:</b> Should only be used before the executor is set in motion.
     *
     * @param rootContext The Context that ties to the host environment.
     */
    public void setRootContext(final Context rootContext) {
        exctx.getScInstance().setRootContext(rootContext);
    }

    /**
     * 设置是否要将状态上下文和全局上下文共享
     *
     * @param singleContext
     * @throws ModelException
     */
    public void setSingleContext(boolean singleContext) throws ModelException {
        getSCInstance().setSingleContext(singleContext);
    }

    /**
     * 返回是否共享了上下文
     *
     * @return
     */
    public boolean isSingleContext() {
        return getSCInstance().isSingleContext();
    }

    /**
     * Get the state machine that is being executed.
     * <b>NOTE:</b> This is the state machine definition or model used by this
     * executor instance. It may be shared across multiple executor instances
     * and should not be altered once in use. Also note that
     * manipulation of instance data for the executor should happen through
     * its root context or state contexts only, never through the direct
     * manipulation of any {@link Datamodel}s associated with this state
     * machine definition.
     * <p/>
     * 得到当前的状态机
     *
     * @return Returns the stateMachine.
     */
    public SCXML getStateMachine() {
        return exctx.getStateMachine();
    }

    /**
     * Set or replace the state machine to be executed
     * 设置或者替换被执行的状态机
     * <p>
     * 如果状态机实例已经被初始化了，会再次初始化，清理所有的状态
     * If the state machine instance has been initialized before, it will be initialized again, destroying all existing
     * state!
     * </p>
     * <p>
     * Also the external event queue will be cleared.
     * 外部事件队列也应该被清理
     * </p>
     *
     * @param stateMachine The state machine to set
     * @throws ModelException if attempting to set a null value or the state machine instance failed to re-initialize
     */
    public void setStateMachine(final SCXML stateMachine) throws ModelException {
        exctx.setStateMachine(semantics.normalizeStateMachine(stateMachine, exctx.getErrorReporter()));
        externalEventQueue.clear();
    }

    /**
     * Get the environment specific error reporter.
     * 得到指定的错误报告器
     *
     * @return Returns the errorReporter.
     */
    public ErrorReporter getErrorReporter() {
        return exctx.getErrorReporter();
    }

    /**
     * Set or replace the error reporter
     * 设置或者替换错误报告器
     *
     * @param errorReporter The error reporter to set, if null a SimpleErrorReporter instance will be used instead
     */
    public void setErrorReporter(final ErrorReporter errorReporter) {
        exctx.setErrorReporter(errorReporter);
    }

    /**
     * Get the event dispatcher.
     * 得到事件分发器
     *
     * @return Returns the eventdispatcher.
     */
    public EventDispatcher getEventdispatcher() {
        return exctx.getEventDispatcher();
    }

    /**
     * Set or replace the event dispatch
     * 设置或者替换事件分发器
     *
     * @param eventdispatcher The event dispatcher to set, if null a SimpleDispatcher instance will be used instead
     */
    public void setEventdispatcher(final EventDispatcher eventdispatcher) {
        exctx.setEventdispatcher(eventdispatcher);
    }

    /**
     * Set if the SCXML configuration should be checked before execution (default = true)
     * 设置是否状态机的配置应该被检查
     *
     * @param checkLegalConfiguration flag to set
     */
    public void setCheckLegalConfiguration(boolean checkLegalConfiguration) {
        this.exctx.setCheckLegalConfiguration(checkLegalConfiguration);
    }

    /**
     * @return if the SCXML configuration will be checked before execution
     */
    public boolean isCheckLegalConfiguration() {
        return exctx.isCheckLegalConfiguration();
    }

    /**
     * Get the notification registry.
     * 得到通知注册
     *
     * @return The notification registry.
     */
    public NotificationRegistry getNotificationRegistry() {
        return exctx.getNotificationRegistry();
    }

    /**
     * Add a listener to the {@link org.sysu.workflow.model.Observable}.
     * <p/>
     * 添加一个监听器
     *
     * @param observable The {@link org.sysu.workflow.model.Observable} to attach the listener to.
     * @param listener   The SCXMLListener.
     */
    public void addListener(final org.sysu.workflow.model.Observable observable, final SCXMLListener listener) {
        exctx.getNotificationRegistry().addListener(observable, listener);
    }

    /**
     * Remove this listener from the {@link org.sysu.workflow.model.Observable}.
     * 移除监听器
     *
     * @param observable The {@link org.sysu.workflow.model.Observable}.
     * @param listener   The SCXMLListener to be removed.
     */
    public void removeListener(final org.sysu.workflow.model.Observable observable,
                               final SCXMLListener listener) {
        exctx.getNotificationRegistry().removeListener(observable, listener);
    }

    /**
     * Register an Invoker for this target type.
     * 注册调用者，
     *
     * @param type         The target type (specified by "type" attribute of the invoke element).
     * @param invokerClass The Invoker class.
     */
    public void registerInvokerClass(final String type, final Class<? extends Invoker> invokerClass) {
        exctx.registerInvokerClass(type, invokerClass);
    }

    /**
     * Remove the Invoker registered for this target type (if there is one registered).
     * <p/>
     * 移除调用者注册器，
     *
     * @param type The target type (specified by "type" attribute of the invoke element).
     */
    public void unregisterInvokerClass(final String type) {
        exctx.unregisterInvokerClass(type);
    }

    /**
     * Detach the current SCInstance to allow external serialization.
     * <p>
     * {@link #attachInstance(SCInstance)} can be used to re-attach a previously detached instance
     * </p>
     * <p>
     * Note: until an instance is re-attached, no operations are allowed (and probably throw exceptions) except
     * for {@link #addEvent(TriggerEvent)} which might still be used (concurrently) by running Invokers, or
     * {@link #hasPendingEvents()} to check for possible pending events.
     * </p>
     *
     * @return the detached instance
     */
    public SCInstance detachInstance() {
        return exctx.detachInstance();
    }

    /**
     * Re-attach a previously detached SCInstance.
     * <p>
     * Note: an already attached instance will get overwritten (and thus lost).
     * </p>
     *
     * @param instance An previously detached SCInstance
     */
    public void attachInstance(SCInstance instance) {
        exctx.attachInstance(instance);
    }

    /**
     * @return Returns true if the state machine is running
     */
    public boolean isRunning() {
        return exctx.isRunning();
    }

    /**
     * 初始化状态机的执行，启动状态机引擎
     *
     * @throws ModelException in case there is a fatal SCXML object
     *                        model problem.
     */
    public void go() throws ModelException {
        // register a new instance tree if this state-machine is the root one
        try {
            if (this.RootTid.equals("") || this.RootTid.equals(this.Tid)) {
                TimeInstanceTree myTree = new TimeInstanceTree();
                TimeTreeNode nRoot = new TimeTreeNode(this.exctx.getStateMachine().getName(), this.Tid, this.exctx, null);
                myTree.SetRoot(nRoot);
                InstanceManager.RegisterInstanceTree(myTree);
                this.RootTid = this.Tid;
                this.exctx.RootTid = this.RootTid;
            }
        }
        catch (Exception e) {
            System.out.println("Executor error at go when create instance tree");
            e.printStackTrace();
        }
        // connect to the resource server if never connect
        boolean reconnectFlag = YAWLAdapter.SessionHandle == null;
        if (!reconnectFlag) {
            StringBuilder checkRes = new StringBuilder();
            YAWLAdapter.GetInstance().CheckConnectToRouter(YAWLAdapter.SessionHandle, checkRes);
            if (checkRes.toString().equals("true")) {
                reconnectFlag = false;
            }
        }
        if (reconnectFlag) {
            try {
                StringBuilder res = new StringBuilder();
                YAWLAdapter.GetInstance().ConnectToRouter("scxml", "scxml", res);
                YAWLAdapter.SessionHandle = res.toString();
            } catch (Exception e) {
                System.out.println("Cannot connect to Resource Service when executor go");
                e.printStackTrace();
            }
        }
        // register resources
        try {
            SCXML scxml = this.exctx.getScInstance().getStateMachine();
            Resources rsCata = scxml.getResources();
            if (rsCata != null) {
                YAWLAdapter adapter = YAWLAdapter.GetInstance();
                // Register roles
                List<Role> roleList = rsCata.GetRoleList();
                for (Role r : roleList) {
                    StringBuilder roleAddRes = new StringBuilder();
                    if (adapter.addRole(YAWLAdapter.SessionHandle, r.getName(), "", "", "", roleAddRes)) {
                        r.setId(roleAddRes.toString());
                    }
                    else {
                        System.out.println("Add role failed: " + r.getName() + " " + roleAddRes.toString());
                    }
                }
                // Register roles for human resources
            }
        }
        catch (Exception e) {
            System.out.println("Executor error at go when register resource");
            e.printStackTrace();
        }
        // same as reset
        this.reset();
    }

    /**
     * 开始执行状态机执行，清楚所有的外部事件
     *
     * @throws ModelException if the state machine instance failed to initialize
     */
    public void reset() throws ModelException {
        // clear any pending external events
        externalEventQueue.clear();

        // go
        semantics.firstStep(exctx);
        logState();
    }

    /**
     * Add a new external event, which may be done concurrently, and even when the current SCInstance is detached.
     * 添加一个外部事件，这个外部事件可能触发多个状态机的转移，设置当当前的SCInstance是被分离的，
     * <p>
     * <p/>
     * No processing of the vent will be done, until the next triggerEvent methods is invoked.
     * </p>
     *
     * @param evt an external event
     */
    public void addEvent(final TriggerEvent evt) {
        if (evt != null) {
            externalEventQueue.add(evt);
        }
    }

    /**
     * @return 如果有外部事件等待处理，返回true
     */
    public boolean hasPendingEvents() {
        return !externalEventQueue.isEmpty();
    }

    /**
     * @return 返回当前外部队列里面的事件数量
     */
    public int getPendingEvents() {
        return externalEventQueue.size();
    }

    /**
     * 当一个事件被触发的时候可以调用这个方法
     *
     * @param evt the external events which triggered during the last
     *            time quantum
     * @throws ModelException in case there is a fatal SCXML object
     *                        model problem.
     */
    public void triggerEvent(final TriggerEvent evt)
            throws ModelException {
        //add a TriggerEvent into the external event queue
        addEvent(evt);
        //trigger all the TriggerEvents in the extrnal event queue
        triggerEvents();
    }

    /**
     * The worker method.
     * Re-evaluates current status whenever any events are triggered.
     * 多个事件触发
     *
     * @param evts an array of external events which triggered during the last
     *             time quantum
     * @throws ModelException in case there is a fatal SCXML object
     *                        model problem.
     */
    public void triggerEvents(final TriggerEvent[] evts)
            throws ModelException {
        if (evts != null) {
            for (TriggerEvent evt : evts) {
                addEvent(evt);
            }
        }
        triggerEvents();
    }

    /**
     * Trigger all pending and incoming events, until there are no more pending events
     * 触发所有等待的和 incoming的事件，直到没有等待的事件
     * @throws ModelException in case there is a fatal SCXML object model problem.
     */
    public void triggerEvents() throws ModelException {
//        ArrayList<TimeTreeNode> childrenList = InstanceManager.GetInstanceTree(this.RootTid).GetNodeById(this.Tid).Children;
//        ArrayList<TriggerEvent> childrenTriggerList = new ArrayList<TriggerEvent>();
//        Object[] eqArr = this.externalEventQueue.toArray();
//        for (Object te : eqArr) {
//            childrenTriggerList.add((TriggerEvent)te);
//        }
//        for (TimeTreeNode cNode : childrenList) {
//            SCXMLExecutor tExecutor = cNode.getExect().getSCXMLExecutor();
//            for (int i = 0; tExecutor.isRunning() && i < childrenTriggerList.size(); i++) {
//                tExecutor.triggerEvent(childrenTriggerList.get(i));
//            }
//        }
        TriggerEvent evt;
        while (exctx.isRunning() && (evt = externalEventQueue.poll()) != null) {
            eventStep(evt);
        }
    }

    /**
     * 事件步中调用语义里面的东西，nextStep
     * @param event
     * @throws ModelException
     */
    protected void eventStep(TriggerEvent event) throws ModelException {
        semantics.nextStep(exctx, event);
        logState();
    }

    /**
     * 得到引擎的SCInstance
     *
     * @return The SCInstance for this executor.
     */
    protected SCInstance getSCInstance() {
        return exctx.getScInstance();
    }

    /**
     * 记录当前状态的活跃状态集合
     */
    protected void logState() {

        if (log.isDebugEnabled()) {
            StringBuilder sb = new StringBuilder("Current States: [ ");
            for (EnterableState es : getStatus().getStates()) {
                sb.append(es.getId()).append(", ");
            }
            int length = sb.length();
            sb.delete(length - 2, length).append(" ]");
            log.debug(sb.toString());
        }
    }

    /**
     * 获取当前执行器在应用程序的索引
     * @return 索引ID
     */
    public String getExecutorIndex() {
        return executorIndex;
    }

    /**
     * 设置当前执行器在应用程序的索引
     * @param executorIndex 索引号
     */
    public void setExecutorIndex(String executorIndex) {
        this.executorIndex = executorIndex;
    }

    /**
     * 获取执行器上下文
     * @return
     */
    public SCXMLExecutionContext getExctx() {
        return exctx;
    }

}