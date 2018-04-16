
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
 * The <code>BOInstance</code> performs book-keeping functions for
 * a particular execution of a state chart represented by a
 * <code>SCXML</code> object.
 * <p/>
 */
public class BOInstance implements Serializable {

    /**
     * Serial version UID.
     */
    private static final long serialVersionUID = 2L;

    /**
     * BOInstance no state machine error.
     */
    private static final String ERR_NO_STATE_MACHINE = "BOInstance: State machine not set";

    /**
     * BOInstance not set error.
     */
    private static final String ERR_NO_ERROR_REPORTER = "BOInstance: ErrorReporter not set";

    /**
     * Binding rtid.
     */
    public String Rtid;

    /**
     * Initialized flag.
     */
    private boolean initialized;

    /**
     * SCXML reference.
     */
    private SCXML stateMachine;

    /**
     * State configuration package.
     */
    private final StateConfiguration stateConfiguration;

    /**
     * Current status
     */
    private final Status currentStatus;

    /**
     * SCXML running flag.
     */
    private boolean running;

    /**
     * SCXML execution context.
     */
    private transient BOXMLIOProcessor internalIOProcessor;

    /**
     * Evaluator reference.
     */
    private transient Evaluator evaluator;

    /**
     * Error reporter reference.
     */
    private transient ErrorReporter errorReporter = null;

    /**
     * Map of EnterableState Context
     */
    private final Map<EnterableState, Context> contexts = new HashMap<EnterableState, Context>();

    /**
     * The map of last known configurations per History.
     */
    private final Map<History, Set<EnterableState>> histories = new HashMap<History, Set<EnterableState>>();

    /**
     * root context
     */
    private Context rootContext;

    /**
     * The wrapped system context.
     */
    private BOXMLSystemContext systemContext;

    /**
     * The global context
     */
    private Context globalContext;

    /**
     * Flag indicating if the globalContext is shared between all states (a single flat context, default false)
     */
    private boolean singleContext;


    /**
     * Constructor.
     *
     * @param internalIOProcessor The I/O Processor for the internal event queue
     * @param evaluator           The evaluator
     * @param errorReporter       The error reporter
     */
    protected BOInstance(final BOXMLIOProcessor internalIOProcessor, final Evaluator evaluator,
                         final ErrorReporter errorReporter) {
        this.internalIOProcessor = internalIOProcessor;
        this.evaluator = evaluator;
        this.errorReporter = errorReporter;
        this.stateConfiguration = new StateConfiguration();
        this.currentStatus = new Status(stateConfiguration);
    }


    /**
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

        Datamodel rootdm = stateMachine.getDatamodel();
        cloneDatamodel(rootdm, getGlobalContext(), evaluator, errorReporter);
        initialized = true;
    }

    /**
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
     *
     * @param internalIOProcessor the I/O Processor
     */
    protected void setInternalIOProcessor(BOXMLIOProcessor internalIOProcessor) {
        this.internalIOProcessor = internalIOProcessor;
    }

    /**
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
                throw new ModelException("BOInstance: re-attached without Evaluator");
            }
        }
    }

    /**
     * @return Return the current evaluator
     */
    protected Evaluator getEvaluator() {
        return evaluator;
    }

    /**
     * Set or re-attach the error reporter
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
     */
    public SCXML getStateMachine() {
        return stateMachine;
    }

    /**
     * <p>
     * If this state machine instance has been initialized before, it will be initialized again, destroying all existing
     * state!
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
            throw new ModelException("BOInstance: already initialized");
        }
        this.singleContext = singleContext;
    }

    /**
     * @return Check if context is single without sharing.
     */
    public boolean isSingleContext() {
        return singleContext;
    }

    /**
     * Clone data model.
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
     */
    public StateConfiguration getStateConfiguration() {
        return stateConfiguration;
    }

    /**
     * @return Returns the current status for this instance
     */
    public Status getCurrentStatus() {
        return currentStatus;
    }


    public String getExecContextSessionId() {
        return ((BOXMLExecutionContext)this.internalIOProcessor).getSessionId();
    }

    /**
     * @return Returns if the state machine is running
     */
    public boolean isRunning() {
        return running;
    }

    /**
     * Sets the running status of the state machine
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
                systemContext = new BOXMLSystemContext(internalContext);
                systemContext.getContext().set(BOXMLSystemContext.SESSIONID_KEY, UUID.randomUUID().toString());
                String _name = stateMachine != null && stateMachine.getName() != null ? stateMachine.getName() : "";
                systemContext.getContext().set(BOXMLSystemContext.SCXML_NAME_KEY, _name);
                BOXMLExecutionContext executionContext = ((BOXMLExecutionContext) this.internalIOProcessor);
                systemContext.getContext().set(BOXMLSystemContext.NOTIFIABLE_ID_KEY, executionContext.NotifiableId);
                systemContext.getContext().set(BOXMLSystemContext.GLOBAL_ID_KEY, executionContext.NodeId);
                systemContext.getPlatformVariables().put(BOXMLSystemContext.STATUS_KEY, currentStatus);
            }
        }
        return systemContext != null ? systemContext.getContext() : null;
    }

    /**
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
     * <p/>
     * <p>Note: used for testing purposes only</p>
     *
     * @param state The EnterableState
     * @return The context or null if not created yet.
     */
    Context lookupContext(final EnterableState state) {
        return contexts.get(state);
    }

    /**
     * Set the context for an EnterableState
     * <p/>
     * <p>Note: used for testing purposes only</p>
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

