
package org.sysu.workflow;

import org.sysu.workflow.env.SimpleContext;
import org.sysu.workflow.model.*;
import org.sysu.workflow.semantics.ErrorConstants;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.Serializable;
import java.util.*;

/**
 * <p/>
 * The <code>SCInstance</code> performs book-keeping functions for
 * a particular execution of a state chart represented by a
 * <code>SCXML</code> object.
 * <p/>
 */
public class SCInstance implements Serializable {

    /**
     * Serial version UID.
     */
    private static final long serialVersionUID = 2L;

    /**
     * SCInstance 不能被初始化，没有一个StateMachine实例
     */
    private static final String ERR_NO_STATE_MACHINE = "SCInstance: State machine not set";

    /**
     * SCInstance 不能被初始化没有一个错误报告
     */
    private static final String ERR_NO_ERROR_REPORTER = "SCInstance: ErrorReporter not set";

    /**
     * 表明状态机实例已经初始化
     */
    private boolean initialized;

    /**
     * 被执行的状态机
     */
    private SCXML stateMachine;

    /**
     * 当前被执行状态机的状态配置
     */
    private final StateConfiguration stateConfiguration;

    /**
     * 当前状态
     */
    private final Status currentStatus;

    /**
     * 状态机是否在运行中
     */
    private boolean running;

    /**
     * SCXML 的内部事件处理器
     */
    private transient SCXMLIOProcessor internalIOProcessor;

    /**
     * 当前状态机实例的求值器
     */
    private transient Evaluator evaluator;

    /**
     * 错误报告
     */
    private transient ErrorReporter errorReporter = null;

    /**
     * 每一个 EnterableState 的 Context
     */
    private final Map<EnterableState, Context> contexts = new HashMap<EnterableState, Context>();

    /**
     * The map of last known configurations per History.
     */
    private final Map<History, Set<EnterableState>> histories = new HashMap<History, Set<EnterableState>>();

    /**
     * root 上下文
     */
    private Context rootContext;

    /**
     * The wrapped system context.
     */
    private SCXMLSystemContext systemContext;

    /**
     * The global context
     */
    private Context globalContext;

    /**
     * Flag indicating if the globalContext is shared between all states (a single flat context, default false)
     */
    private boolean singleContext;


    /**
     * 构造函数
     *
     * @param internalIOProcessor The I/O Processor for the internal event queue
     * @param evaluator           The evaluator
     * @param errorReporter       The error reporter
     */
    protected SCInstance(final SCXMLIOProcessor internalIOProcessor, final Evaluator evaluator,
                         final ErrorReporter errorReporter) {
        this.internalIOProcessor = internalIOProcessor;
        this.evaluator = evaluator;
        this.errorReporter = errorReporter;
        this.stateConfiguration = new StateConfiguration();
        this.currentStatus = new Status(stateConfiguration);
    }


    /**
     * 初始化，或者再初始化状态机实例，清除所有的变量上下文，历史，当前状态信息，复制SCXML的数据模型到root Context
     * (re)Initializes the state machine instance, clearing all variable contexts, histories and current status,
     * and clones the SCXML root datamodel into the root context.
     *
     * @throws ModelException if the state machine hasn't been setup for this instance
     */
    protected void initialize() throws ModelException {
        running = false;
        if (stateMachine == null) {
            throw new ModelException(ERR_NO_STATE_MACHINE);
        }
        if (evaluator == null) {
            evaluator = EvaluatorFactory.getEvaluator(stateMachine);
        }
        if (stateMachine.getDatamodelName() != null && !stateMachine.getDatamodelName().equals(evaluator.getSupportedDatamodel())) {
            throw new ModelException("Incompatible SCXML document datamodel \"" + stateMachine.getDatamodelName() + "\""
                    + " for evaluator " + evaluator.getClass().getName() + " supported datamodel \"" + evaluator.getSupportedDatamodel() + "\"");
        }
        if (errorReporter == null) {
            throw new ModelException(ERR_NO_ERROR_REPORTER);
        }
        systemContext = null;
        globalContext = null;
        contexts.clear();
        histories.clear();
        stateConfiguration.clear();

        // 克隆数据模型
        Datamodel rootdm = stateMachine.getDatamodel();
        cloneDatamodel(rootdm, getGlobalContext(), evaluator, errorReporter);
        initialized = true;
    }

    /**
     * 分离，派遣，拆开组合的物体
     * Detach this state machine instance to allow external serialization.
     * <p>
     * This clears the internal I/O processor, evaluator and errorReporter members.
     * </p>
     */
    protected void detach() {
        this.internalIOProcessor = null;
        this.evaluator = null;
        this.errorReporter = null;
    }

    /**
     * Sets the I/O Processor for the internal event queue
     * 设置 内部的I/O处理器
     *
     * @param internalIOProcessor the I/O Processor
     */
    protected void setInternalIOProcessor(SCXMLIOProcessor internalIOProcessor) {
        this.internalIOProcessor = internalIOProcessor;
    }

    /**
     * 设置，或者重新连上
     * Set or re-attach the evaluator
     * <p>
     * If not re-attaching and this state machine instance has been initialized before,
     * it will be initialized again, destroying all existing state!
     * </p>
     *
     * @param evaluator The evaluator for this state machine instance
     * @param reAttach  Flag whether or not re-attaching it
     * @throws ModelException if {@code evaluator} is null
     */
    protected void setEvaluator(Evaluator evaluator, boolean reAttach) throws ModelException {
        this.evaluator = evaluator;
        if (initialized) {
            if (!reAttach) {
                // change of evaluator after initialization: re-initialize
                initialize();
            } else if (evaluator == null) {
                throw new ModelException("SCInstance: re-attached without Evaluator");
            }
        }
    }

    /**
     * 得到当前求值器
     *
     * @return Return the current evaluator
     */
    protected Evaluator getEvaluator() {
        return evaluator;
    }

    /**
     * Set or re-attach the error reporter
     * 设置再连接的错误报告器
     *
     * @param errorReporter The error reporter for this state machine instance.
     * @throws ModelException if an attempt is made to set a null value for the error reporter
     */
    protected void setErrorReporter(ErrorReporter errorReporter) throws ModelException {
        if (errorReporter == null) {
            throw new ModelException(ERR_NO_ERROR_REPORTER);
        }
        this.errorReporter = errorReporter;
    }

    /**
     * @return Return the state machine for this instance
     * 返回状态机实例
     */
    public SCXML getStateMachine() {
        return stateMachine;
    }

    /**
     * 设置状态机实例
     * <p>
     * If this state machine instance has been initialized before, it will be initialized again, destroying all existing
     * state!
     * 如果SCInstance之前初始化过，执行这个函数将会再次初始化，并清除所有原来的状态
     * </p>
     *
     * @param stateMachine The state machine for this instance
     * @throws ModelException if an attempt is made to set a null value for the state machine
     */
    protected void setStateMachine(SCXML stateMachine) throws ModelException {
        if (stateMachine == null) {
            throw new ModelException(ERR_NO_STATE_MACHINE);
        }
        this.stateMachine = stateMachine;
        initialize();
    }

    public void setSingleContext(boolean singleContext) throws ModelException {
        if (initialized) {
            throw new ModelException("SCInstance: already initialized");
        }
        this.singleContext = singleContext;
    }

    /**
     * 返回全局上下文是不是在所有状态之间共享的
     *
     * @return
     */
    public boolean isSingleContext() {
        return singleContext;
    }

    /**
     * Clone data model.
     * 克隆数据模型
     *
     * @param ctx           The context to clone to.
     * @param datamodel     The datamodel to clone.
     * @param evaluator     The expression evaluator.
     * @param errorReporter The error reporter
     */
    protected void cloneDatamodel(final Datamodel datamodel, final Context ctx, final Evaluator evaluator,
                                  final ErrorReporter errorReporter) {
        if (datamodel == null || Evaluator.NULL_DATA_MODEL.equals(evaluator.getSupportedDatamodel())) {
            return;
        }
        List<Data> data = datamodel.getData();
        if (data == null) {
            return;
        }
        for (Data datum : data) {
            if (ctx.has(datum.getId())) {
                // earlier or externally defined 'initial' value found: do not overwrite
                continue;
            }
            Node datumNode = datum.getNode();
            Node valueNode = null;
            if (datumNode != null) {
                valueNode = datumNode.cloneNode(true);
            }
            // prefer "src" over "expr" over "inline"
            if (datum.getSrc() != null) {
                ctx.setLocal(datum.getId(), valueNode);
            } else if (datum.getExpr() != null) {
                Object value;
                try {
                    ctx.setLocal(Context.NAMESPACES_KEY, datum.getNamespaces());
                    value = evaluator.eval(ctx, datum.getExpr());
                    ctx.setLocal(Context.NAMESPACES_KEY, null);
                } catch (SCXMLExpressionException see) {
                    if (internalIOProcessor != null) {
                        internalIOProcessor.addEvent(new TriggerEvent(TriggerEvent.ERROR_EXECUTION, TriggerEvent.ERROR_EVENT));
                    }
                    errorReporter.onError(ErrorConstants.EXPRESSION_ERROR, see.getMessage(), datum);
                    continue;
                }
                if (Evaluator.XPATH_DATA_MODEL.equals(evaluator.getSupportedDatamodel())) {
                    try {
                        Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
                        // TODO: should use SCXML namespace here?
                        Element dataNode = document.createElement("data");
                        dataNode.setAttribute("id", datum.getId());
                        ctx.setLocal(datum.getId(), dataNode);
                        evaluator.evalAssign(ctx, "$" + datum.getId(), value, Evaluator.AssignType.REPLACE_CHILDREN, null);
                    } catch (ParserConfigurationException pce) {
                        if (internalIOProcessor != null) {
                            internalIOProcessor.addEvent(new TriggerEvent(TriggerEvent.ERROR_EXECUTION, TriggerEvent.ERROR_EVENT));
                        }
                        errorReporter.onError(ErrorConstants.EXECUTION_ERROR, pce.getMessage(), datum);
                    } catch (SCXMLExpressionException see) {
                        if (internalIOProcessor != null) {
                            internalIOProcessor.addEvent(new TriggerEvent(TriggerEvent.ERROR_EXECUTION, TriggerEvent.ERROR_EVENT));
                        }
                        errorReporter.onError(ErrorConstants.EXPRESSION_ERROR, see.getMessage(), datum);
                    }
                } else {
                    ctx.setLocal(datum.getId(), value);
                }
            } else {
                ctx.setLocal(datum.getId(), valueNode);
            }
        }
    }

    /**
     * @return Returns the state configuration for this instance
     * 返回状态机配置
     */
    public StateConfiguration getStateConfiguration() {
        return stateConfiguration;
    }

    /**
     * 返回当前状态
     *
     * @return Returns the current status for this instance
     */
    public Status getCurrentStatus() {
        return currentStatus;
    }


    public String getExecContextSessionId() {
        return ((SCXMLExecutionContext)this.internalIOProcessor).getSessionId();
    }

    /**
     * 返回是否在运行中
     *
     * @return Returns if the state machine is running
     */
    public boolean isRunning() {
        return running;
    }

    /**
     * Sets the running status of the state machine
     * 设置状态机的运行状态
     *
     * @param running flag indicating the running status of the state machine
     * @throws IllegalStateException Exception thrown if trying to set the state machine running when in a Final state
     */
    protected void setRunning(final boolean running) throws IllegalStateException {
        if (!this.running && running && currentStatus.isFinal()) {
            throw new IllegalStateException("The state machine is in a Final state and cannot be set running again");
        }
        this.running = running;
    }

    /**
     * Get the root context.
     * 得到root 上下文
     *
     * @return The root context.
     */
    public Context getRootContext() {
        if (rootContext == null && evaluator != null) {
            rootContext = Evaluator.NULL_DATA_MODEL.equals(evaluator.getSupportedDatamodel())
                    ? new SimpleContext() : evaluator.newContext(null);
        }
        return rootContext;
    }

    /**
     * Set or replace the root context.
     * 设置或者替换根上下文
     *
     * @param context The new root context.
     */
    protected void setRootContext(final Context context) {
        this.rootContext = context;
        // force initialization of rootContext
        getRootContext();
        if (systemContext != null) {
            // re-parent the system context
            systemContext.setSystemContext(evaluator.newContext(rootContext));
        }
    }

    /**
     * Get the unwrapped (modifiable) system context.
     * 得到未修改的系统上下文
     *
     * @return The unwrapped system context.
     */
    public Context getSystemContext() {
        if (systemContext == null) {
            // force initialization of rootContext
            getRootContext();
            if (rootContext != null) {
                Context internalContext = Evaluator.NULL_DATA_MODEL.equals(evaluator.getSupportedDatamodel()) ?
                        new SimpleContext(systemContext) : evaluator.newContext(rootContext);
                systemContext = new SCXMLSystemContext(internalContext);
                systemContext.getContext().set(SCXMLSystemContext.SESSIONID_KEY, UUID.randomUUID().toString());
                String _name = stateMachine != null && stateMachine.getName() != null ? stateMachine.getName() : "";
                systemContext.getContext().set(SCXMLSystemContext.SCXML_NAME_KEY, _name);
                systemContext.getPlatformVariables().put(SCXMLSystemContext.STATUS_KEY, currentStatus);
            }
        }
        return systemContext != null ? systemContext.getContext() : null;
    }

    /**
     * 返回全局上下文，上最顶层的上下文
     *
     * @return Returns the global context, which is the top context <em>within</em> the state machine.
     */
    public Context getGlobalContext() {
        if (globalContext == null) {
            // force initialization of systemContext
            getSystemContext();
            if (systemContext != null) {
                globalContext = evaluator.newContext(systemContext);
            }
        }
        return globalContext;
    }

    /**
     * Get the context for an EnterableState or create one if not created before.
     * 得到一个EnterableState 的上下文，
     *
     * @param state The EnterableState.
     * @return The context.
     */
    public Context getContext(final EnterableState state) {
        Context context = contexts.get(state);
        if (context == null) {
            if (singleContext) {
                context = getGlobalContext();
            } else {
                EnterableState parent = state.getParent();
                if (parent == null) {
                    // docroot
                    context = evaluator.newContext(getGlobalContext());
                } else {
                    context = evaluator.newContext(getContext(parent));
                }
            }
            if (state instanceof TransitionalState) {
                Datamodel datamodel = ((TransitionalState) state).getDatamodel();
                cloneDatamodel(datamodel, context, evaluator, errorReporter);
            }
            contexts.put(state, context);
        }
        return context;
    }

    /**
     * Get the context for an EnterableState if available.
     * 返回一个EnterableState的上下文，如果有的话。
     * <p/>
     * <p>Note: used for testing purposes only</p>
     * 仅仅为了测试支持
     *
     * @param state The EnterableState
     * @return The context or null if not created yet.
     */
    Context lookupContext(final EnterableState state) {
        return contexts.get(state);
    }

    /**
     * Set the context for an EnterableState
     * 设置状态上下文
     * <p/>
     * <p>Note: used for testing purposes only</p>
     * 仅仅为了测试支持
     *
     * @param state   The EnterableState.
     * @param context The context.
     */
    void setContext(final EnterableState state,
                    final Context context) {
        contexts.put(state, context);
    }

    /**
     * Get the last configuration for this history.
     * 得到上一个配置，为了当前的历史
     *
     * @param history The history.
     * @return Returns the lastConfiguration.
     */
    public Set<EnterableState> getLastConfiguration(final History history) {
        Set<EnterableState> lastConfiguration = histories.get(history);
        if (lastConfiguration == null) {
            lastConfiguration = Collections.emptySet();
        }
        return lastConfiguration;
    }

    /**
     * Set the last configuration for this history.
     * 设置当前历史的上一个配置
     *
     * @param history The history.
     * @param lc      The lastConfiguration to set.
     */
    public void setLastConfiguration(final History history,
                                     final Set<EnterableState> lc) {
        histories.put(history, new HashSet<EnterableState>(lc));
    }

    /**
     * Resets the history state.
     * 重置历史状态
     * <p/>
     * <p>Note: used for testing purposes only</p>
     * only test
     *
     * @param history The history.
     */
    public void resetConfiguration(final History history) {
        histories.remove(history);
    }
}

