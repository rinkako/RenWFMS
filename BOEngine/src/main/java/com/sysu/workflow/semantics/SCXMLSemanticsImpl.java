
package com.sysu.workflow.semantics;

import com.sysu.workflow.*;
import com.sysu.workflow.entity.ProcessInstanceEntity;
import com.sysu.workflow.invoke.Invoker;
import com.sysu.workflow.invoke.InvokerException;
import com.sysu.workflow.model.*;
import com.sysu.workflow.service.processservice.RuntimeService;
import com.sysu.workflow.system.EventVariable;

import java.util.*;

/**
 * 封装和实现了 SCXML解释器的算法部分
 * This class encapsulate and implements the
 * <a href="http://www.w3.org/TR/2014/CR-scxml-20140313/#AlgorithmforSCXMLInterpretation">
 * W3C SCXML Algorithm for SCXML Interpretation</a>
 * <p/>
 * 自定义语义可以通过继承这个类来实现
 * <p>Custom semantics can be created by sub-classing this implementation.</p>
 * <p/>
 * 这个实现是无状态的，所有的方法都是公共可访问的
 * <p>This implementation is full stateless and all methods are public accessible to make
 * <p/>
 * 很容易扩展，重用，测试他的行为
 * it easier to extend, reuse and test its behavior.</p>
 */
public class SCXMLSemanticsImpl implements SCXMLSemantics {

    /**
     * Suffix for error event that are triggered in reaction to invalid data
     * model locations.
     */
    public static final String ERR_ILLEGAL_ALLOC = ".error.illegalalloc";

    /**
     * Optional post processing immediately following SCXMLReader. May be used
     * for removing pseudo-states etc.
     *
     * @param input  SCXML state machine
     * @param errRep ErrorReporter callback
     * @return normalized SCXML state machine, pseudo states are removed, etc.
     */
    public SCXML normalizeStateMachine(final SCXML input, final ErrorReporter errRep) {
        //it is a no-op for now
        return input;
    }

    /**
     * First step in the execution of an SCXML state machine.
     * <p>
     * This will first (re)initialize the state machine instance, destroying any existing state!
     * </p>
     * <p>
     * The first step is corresponding to the Algorithm for SCXML processing from the interpret() procedure to the
     * mainLoop() procedure up to the blocking wait for an external event.
     * </p>
     * <p>
     * This step will thus complete the SCXML initial execution and a subsequent macroStep to stabilize the state
     * machine again before returning.
     * </p>
     * <p>
     * If the state machine no longer is running after all this, first the {@link #finalStep(SCXMLExecutionContext)}
     * will be called for cleanup before returning.
     * </p>
     *
     * @param exctx The execution context for this step
     * @throws ModelException if the state machine instance failed to initialize or a SCXML model error occurred during
     *                        the execution.
     */
    public void firstStep(final SCXMLExecutionContext exctx) throws ModelException {
        // (re)initialize the execution context and state machine instance
        //清除所有的执行上下文和状态机实例
        exctx.initialize();

        //执行全局 Script
        // execute global script if defined
        executeGlobalScript(exctx);

        //执行初始化第一步
        // enter initial states
        HashSet<TransitionalState> statesToInvoke = new HashSet<TransitionalState>();
        Step step = new Step(null);
        step.getTransitList().add(exctx.getStateMachine().getInitialTransition());
        microStep(exctx, step, statesToInvoke);

        // Execute Immediate Transitions，执行即时转移

        //在执行完第一次的microstep 微步之后，在看有没有在运行中，如果有，就执行即时转移
        if (exctx.isRunning()) {
            macroStep(exctx, statesToInvoke);
        }
        //最后判断是否停止运行了，如果停止了，就执行finalStep
        if (!exctx.isRunning()) {
            finalStep(exctx);
        }
    }

    /**
     * Next step in the execution of an SCXML state machine.
     * <p>
     * The next step is corresponding to the Algorithm for SCXML processing mainEventLoop() procedure after receiving an
     * external event, up to the blocking wait for another external event.
     * </p>
     * <p>
     * If the state machine isn't {@link SCXMLExecutionContext#isRunning()} (any more), invoking this method will simply
     * do nothing.
     * </p>
     * <p>
     * If the provided event is a {@link TriggerEvent#CANCEL_EVENT}, the state machine will stop running.
     * </p>
     * <p>
     * Otherwise, the event is set in the {@link SCXMLSystemContext} and processing of the event then is started, and
     * if the event leads to any transitions a microStep for this event will be performed, followed up by a macroStep
     * to stabilize the state machine again before returning.
     * </p>
     * <p>
     * If the state machine no longer is running after all this, first the {@link #finalStep(SCXMLExecutionContext)}
     * will be called for cleanup before returning.
     * </p>
     *
     * @param exctx The execution context for this step
     * @param event The event to process
     * @throws ModelException if a SCXML model error occurred during the execution.
     */
    public void nextStep(final SCXMLExecutionContext exctx, final TriggerEvent event) throws ModelException {
        if (!exctx.isRunning()) {
            return;
        }
        if (isCancelEvent(event)) {
            exctx.stopRunning();
        } else {
            setSystemEventVariable(exctx.getScInstance(), event, false);


            processInvokes(exctx, event);
            Step step = new Step(event);
            selectTransitions(exctx, step);
            if (!step.getTransitList().isEmpty()) {
                HashSet<TransitionalState> statesToInvoke = new HashSet<TransitionalState>();
                microStep(exctx, step, statesToInvoke);
                if (exctx.isRunning()) {
                    macroStep(exctx, statesToInvoke);
                }
            }
        }
        if (!exctx.isRunning()) {
            finalStep(exctx);
        }
    }

    /**
     * The final step in the execution of an SCXML state machine.
     * <p>
     * This final step is corresponding to the Algorithm for SCXML processing exitInterpreter() procedure, after the
     * state machine stopped running.
     * </p>
     * <p>
     * If the state machine still is {@link SCXMLExecutionContext#isRunning()} invoking this method will simply
     * do nothing.
     * </p>
     * <p>
     * This final step will exit all remaining active states and cancel any active invokers.
     * </p>
     * <p>
     * <em>TODO: the current implementation does not yet provide final donedata handling.</em>
     * </p>
     *
     * @param exctx The execution context for this step
     * @throws ModelException if a SCXML model error occurred during the execution.
     */
    public void finalStep(SCXMLExecutionContext exctx) throws ModelException {
        if (exctx.isRunning()) {
            return;
        }
        ArrayList<EnterableState> configuration = new ArrayList<EnterableState>(exctx.getScInstance().getStateConfiguration().getActiveStates());
        Collections.sort(configuration, DocumentOrder.reverseDocumentOrderComparator);
        for (EnterableState es : configuration) {
            for (OnExit onexit : es.getOnExits()) {
                executeContent(exctx, onexit);
            }
            if (es instanceof TransitionalState) {
                // check if invokers are active in this state
                for (Invoke inv : ((TransitionalState) es).getInvokes()) {
                    exctx.cancelInvoker(inv);
                }
            }
            exctx.getNotificationRegistry().fireOnExit(es, es);
            exctx.getNotificationRegistry().fireOnExit(exctx.getStateMachine(), es);
            if (!(es instanceof Final && es.getParent() == null)) {
                exctx.getScInstance().getStateConfiguration().exitState(es);
            }
            // else: keep final Final
            // TODO: returnDoneEvent(s.donedata)?
        }
    }

    /**
     * Perform a micro step in the execution of a state machine.
     * 在执行的状态机中，执行一个微步（micro step）
     *
     * <p/>
     * 微步相应的算法是microstep()程序
     * This micro step is corresponding to the Algorithm for SCXML processing microstep() procedure.
     *
     * <p/>
     *
     * @param exctx          The execution context for this step
     * @param step           The current micro step
     * @param statesToInvoke the set of activated states which invokes need to be invoked at the end of the current
     *                       macro step
     * @throws ModelException if a SCXML model error occurred during the execution.
     */
    public void microStep(final SCXMLExecutionContext exctx, final Step step,
                          final Set<TransitionalState> statesToInvoke)
            throws ModelException {
        //针对当前配置构建退出集合和进入集合，给出step的转移列表
        buildStep(exctx, step);
        //退出退出状态集合
        exitStates(exctx, step, statesToInvoke);
        //执行转移
        executeTransitionContent(exctx, step);
        //进入进入状态集合
        enterStates(exctx, step, statesToInvoke);
        if (step.getEntrySet().size() != 0) {
            System.out.print("Now Enter:");
            for (EnterableState s : step.getEntrySet()) {
                System.out.print(" " + s.getId());
            }
            System.out.println();
        }
        //清除这一步的即时转移
        step.clearIntermediateState();
    }

    /**
     * buildStep builds the exitSet and entrySet for the current configuration given the transitionList on the step.
     * 针对当前配置构建退出集合和进入集合，给出step的转移列表，并没有真正的执行退出，和进入操作
     *
     * @param exctx The SCXML execution context
     * @param step  The step containing the list of transitions to be taken
     * @throws ModelException if the result of taking the transitions would lead to an illegal configuration
     */
    public void buildStep(final SCXMLExecutionContext exctx, final Step step) throws ModelException {
        step.clearIntermediateState();

        // compute exitSet, if there is something to exit and record their History configurations if applicable
        if (!exctx.getScInstance().getStateConfiguration().getActiveStates().isEmpty()) { //如果当前状态机配置的活跃状态不为空（状态机刚启动的时候肯定是空的），，计算退出状态集合
            computeExitSet(step, exctx.getScInstance().getStateConfiguration());
        }
        // compute entrySet  ，计算进入状态集合
        computeEntrySet(exctx, step);

        // default result states to entrySet   ， 默认的进入集合
        Set<EnterableState> states = step.getEntrySet();

        if (!step.getExitSet().isEmpty()) { //如果当前步的退出集合不为空，状态机刚启动的时候肯定是空的。
            // calculate result states by taking current states, subtracting exitSet and adding entrySet
            states = new HashSet<EnterableState>(exctx.getScInstance().getStateConfiguration().getStates());   //将当前的所有原子状态机集合放入states
            states.removeAll(step.getExitSet());   //移除所有退出状态集合
            states.addAll(step.getEntrySet()); //再加上所有的进入状态集合
        }
        // validate the result states represent a legal configuration， 验证当前的状态配置是否是一个合法的配置
        if (exctx.isCheckLegalConfiguration() && !isLegalConfiguration(states, exctx.getErrorReporter())) {
            throw new ModelException("Illegal state machine configuration!");
        }
    }

    /**
     * Perform a macro step in the execution of a state machine.
     *
     * 执行一大步，直到当前会话等待一个外部事件
     *
     * <p/>
     * This macro step is corresponding to the Algorithm for SCXML processing mainEventLoop() procedure macro step
     *
     * sub-flow, which are the first <em>3</em> steps of the described <em>4</em>, so everything up to the blocking
     * wait for an external event.
     * <p/>
     *
     * @param exctx          The execution context for this step
     * @param statesToInvoke the set of activated states which invokes need to be invoked at the end of the current
     *                       macro step
     * @throws ModelException if a SCXML model error occurred during the execution.
     */
    public void macroStep(final SCXMLExecutionContext exctx, final Set<TransitionalState> statesToInvoke)
            throws ModelException {
        //循环一直等待
        do {
            //这一大步是否结束了
            boolean macroStepDone = false;
            //
            do {
                //构造一大步的每一次Step
                Step step = new Step(null);
                selectTransitions(exctx, step);
                if (step.getTransitList().isEmpty()) {   //如果转移列表是空的
                    TriggerEvent event = exctx.nextInternalEvent();  //得到内部事件队列中的下一个内部事件
                    if (event != null) {  //如果内部事件不是空的
                        if (isCancelEvent(event)) {      //先判断这个内部事件是不是 《取消事件》
                            exctx.stopRunning();
                        } else {      //不是取消事件，设置系统变量，
                            setSystemEventVariable(exctx.getScInstance(), event, true);
                            step = new Step(event);
                            selectTransitions(exctx, step);
                        }
                    }
                }
                if (step.getTransitList().isEmpty()) {
                    macroStepDone = true;
                } else {
                    microStep(exctx, step, statesToInvoke);
                }

            } while (exctx.isRunning() && !macroStepDone);

            if (exctx.isRunning() && !statesToInvoke.isEmpty()) {
                initiateInvokes(exctx, statesToInvoke);
                statesToInvoke.clear();
            }
        } while (exctx.isRunning() && exctx.hasPendingInternalEvent());
    }

    /**
     * Compute and store the set of states to exit for the current list of transitions in the provided step.
     * <p/>
     * This method corresponds to the Algorithm for SCXML processing computeExitSet() procedure.
     * <p/>
     *
     * @param step               The step containing the list of transitions to be taken
     * @param stateConfiguration The current configuration of the state machine ({@link SCInstance#getStateConfiguration()}).
     */
    public void computeExitSet(final Step step, final StateConfiguration stateConfiguration) {
        if (!stateConfiguration.getActiveStates().isEmpty()) {
            for (SimpleTransition st : step.getTransitList()) {
                computeExitSet(st, step.getExitSet(), stateConfiguration.getActiveStates());
            }
            recordHistory(step, stateConfiguration.getStates(), stateConfiguration.getActiveStates());
        }
    }

    /**
     * Compute and store the set of states to exit for one specific transition in the provided step.
     * <p/>
     * This method corresponds to the Algorithm for SCXML processing computeExitSet() procedure.
     * <p/>
     *
     * @param transition   The transition to compute the states to exit from
     * @param exitSet      The set for adding the states to exit to
     * @param activeStates The current active states of the state machine ({@link StateConfiguration#getActiveStates()}).
     */
    public void computeExitSet(SimpleTransition transition, Set<EnterableState> exitSet, Set<EnterableState> activeStates) {
        if (!transition.getTargets().isEmpty()) {
            TransitionalState transitionDomain = transition.getTransitionDomain();
            if (transitionDomain == null) {
                // root transition: every active state will be exited
                exitSet.addAll(activeStates);
            } else {
                for (EnterableState state : activeStates) {
                    if (state.isDescendantOf(transitionDomain)) {
                        exitSet.add(state);
                    }
                }
            }
        }
    }

    /**
     * Record the history configurations for states to exit if applicable and temporarily store this in the step.
     * <p>
     * These history configurations must be pre-recorded as they might impact (re)entrance calculation during
     * {@link #computeEntrySet(SCXMLExecutionContext, Step)}.
     * </p>
     * <p>
     * Only after the new configuration has been validated (see: {@link #isLegalConfiguration(Set, ErrorReporter)}), the
     * history configurations will be persisted during the actual {@link #exitStates(SCXMLExecutionContext, Step, Set)}
     * processing.
     * </p>
     *
     * @param step         The step containing the list of states to exit, and the map to record the new history configurations
     * @param atomicStates The current set of active atomic states in the state machine
     * @param activeStates The current set of all active states in the state machine
     */
    public void recordHistory(final Step step, final Set<EnterableState> atomicStates, final Set<EnterableState> activeStates) {
        for (EnterableState es : step.getExitSet()) {
            if (es instanceof TransitionalState && ((TransitionalState) es).hasHistory()) {
                TransitionalState ts = (TransitionalState) es;
                Set<EnterableState> shallow = null;
                Set<EnterableState> deep = null;
                for (History h : ts.getHistory()) {
                    if (h.isDeep()) {
                        if (deep == null) {
                            //calculate deep history for a given state once
                            deep = new HashSet<EnterableState>();
                            for (EnterableState ott : atomicStates) {
                                if (ott.isDescendantOf(es)) {
                                    deep.add(ott);
                                }
                            }
                        }
                        step.getNewHistoryConfigurations().put(h, deep);
                    } else {
                        if (shallow == null) {
                            //calculate shallow history for a given state once
                            shallow = new HashSet<EnterableState>(ts.getChildren());
                            shallow.retainAll(activeStates);
                        }
                        step.getNewHistoryConfigurations().put(h, shallow);
                    }
                }
            }
        }
    }

    /**
     * Compute and store the set of states to enter for the current list of transitions in the provided step.
     * <p/>
     * This method corresponds to the Algorithm for SCXML processing computeEntrySet() procedure.
     * <p/>
     *
     * @param exctx The execution context for this step
     * @param step  The step containing the list of transitions to be taken
     */
    public void computeEntrySet(final SCXMLExecutionContext exctx, final Step step) {
        Set<History> historyTargets = new HashSet<History>();
        Set<EnterableState> entrySet = new HashSet<EnterableState>();
        for (SimpleTransition st : step.getTransitList()) {
            for (TransitionTarget tt : st.getTargets()) {
                if (tt instanceof EnterableState) {
                    entrySet.add((EnterableState) tt);
                } else {
                    // History
                    historyTargets.add((History) tt);
                }
            }
        }
        for (EnterableState es : entrySet) {
            addDescendantStatesToEnter(exctx, step, es);
        }
        for (History h : historyTargets) {
            addDescendantStatesToEnter(exctx, step, h);
        }
        for (SimpleTransition st : step.getTransitList()) {
            TransitionalState ancestor = st.getTransitionDomain();
            for (TransitionTarget tt : st.getTargets()) {
                addAncestorStatesToEnter(exctx, step, tt, ancestor);
            }
        }
    }

    /**
     * This method corresponds to the Algorithm for SCXML processing addDescendantStatesToEnter() procedure.
     *
     * @param exctx The execution context for this step
     * @param step  The step
     * @param tt    The TransitionTarget
     */
    public void addDescendantStatesToEnter(final SCXMLExecutionContext exctx, final Step step,
                                           final TransitionTarget tt) {
        if (tt instanceof History) {
            History h = (History) tt;
            Set<EnterableState> lastConfiguration = step.getNewHistoryConfigurations().get(h);
            if (lastConfiguration == null) {
                lastConfiguration = exctx.getScInstance().getLastConfiguration(h);
            }
            if (lastConfiguration.isEmpty()) {
                step.getDefaultHistoryTransitions().put(h.getParent(), h.getTransition());
                for (TransitionTarget dtt : h.getTransition().getTargets()) {
                    addDescendantStatesToEnter(exctx, step, dtt);
                }
                for (TransitionTarget dtt : h.getTransition().getTargets()) {
                    addAncestorStatesToEnter(exctx, step, dtt, tt.getParent());
                }
            } else {
                for (TransitionTarget dtt : lastConfiguration) {
                    addDescendantStatesToEnter(exctx, step, dtt);
                }
                for (TransitionTarget dtt : lastConfiguration) {
                    addAncestorStatesToEnter(exctx, step, dtt, tt.getParent());
                }
            }
        } else { // tt instanceof EnterableState
            EnterableState es = (EnterableState) tt;
            step.getEntrySet().add(es);
            if (es instanceof Parallel) {
                for (EnterableState child : ((Parallel) es).getChildren()) {
                    if (!containsDescendant(step.getEntrySet(), child)) {
                        addDescendantStatesToEnter(exctx, step, child);
                    }
                }
            } else if (es instanceof State && ((State) es).isComposite()) {
                step.getDefaultEntrySet().add(es);
                for (TransitionTarget dtt : ((State) es).getInitial().getTransition().getTargets()) {
                    addDescendantStatesToEnter(exctx, step, dtt);
                }
                for (TransitionTarget dtt : ((State) es).getInitial().getTransition().getTargets()) {
                    addAncestorStatesToEnter(exctx, step, dtt, tt);
                }
            }
        }
    }

    /**
     * This method corresponds to the Algorithm for SCXML processing addAncestorStatesToEnter() procedure.
     *
     * @param exctx    The execution context for this step
     * @param step     The step
     * @param tt       The TransitionTarget
     * @param ancestor The ancestor TransitionTarget
     */
    public void addAncestorStatesToEnter(final SCXMLExecutionContext exctx, final Step step,
                                         final TransitionTarget tt, TransitionTarget ancestor) {
        // for for anc in getProperAncestors(tt,ancestor)
        for (int i = tt.getNumberOfAncestors() - 1; i > -1; i--) {
            EnterableState anc = tt.getAncestor(i);
            if (anc == ancestor) {
                break;
            }
            step.getEntrySet().add(anc);
            if (anc instanceof Parallel) {
                for (EnterableState child : ((Parallel) anc).getChildren()) {
                    if (!containsDescendant(step.getEntrySet(), child)) {
                        addDescendantStatesToEnter(exctx, step, child);
                    }
                }

            }
        }
    }

    /**
     * @param states the set of states to check for descendants
     * @param state  the state to check with
     * @return Returns true if a member of the provided states set is a descendant of the provided state.
     */
    public boolean containsDescendant(Set<EnterableState> states, EnterableState state) {
        for (EnterableState es : states) {
            if (es.isDescendantOf(state)) {
                return true;
            }
        }
        return false;
    }

    /**
     * This method corresponds to the Algorithm for SCXML processing selectTransitions() as well as the
     * 选择转移，
     * selectEventlessTransitions() procedure, depending on the event (or null) in the provided step
     * <p/>
     *
     * @param exctx The execution context for this step
     * @param step  The step
     * @throws ModelException if there is a fatal SCXML state error
     */
    public void selectTransitions(final SCXMLExecutionContext exctx, final Step step) throws ModelException {

        //step里面的转移清理了
        step.getTransitList().clear();
        ArrayList<Transition> enabledTransitions = new ArrayList<Transition>();

        ArrayList<EnterableState> configuration = new ArrayList<EnterableState>(exctx.getScInstance().getStateConfiguration().getActiveStates());
        Collections.sort(configuration, DocumentOrder.documentOrderComparator);

        HashSet<EnterableState> visited = new HashSet<EnterableState>();

        String eventName = step.getEvent() != null ? step.getEvent().getName() : null;
        for (EnterableState es : configuration) {
            if (es.isAtomicState()) {
                if (es instanceof Final) {
                    // Final states don't have transitions, skip to parent
                    if (es.getParent() == null) {
                        // should not happen: a top level active Final state should have stopped the state machine
                        throw new ModelException("Illegal state machine configuration: encountered top level <final> "
                                + "state while processing an event");
                    } else {
                        es = es.getParent();
                    }
                }
                TransitionalState state = (TransitionalState) es;
                TransitionalState current = state;
                int ancestorIndex = state.getNumberOfAncestors() - 1;
                boolean transitionMatched = false;
                do {
                    for (Transition transition : current.getTransitionsList()) {
                        if (transitionMatched = matchTransition(exctx, transition, eventName)) {
                            enabledTransitions.add(transition);
                            break;
                        }
                    }
                    current = (!transitionMatched && ancestorIndex > -1) ? state.getAncestor(ancestorIndex--) : null;
                } while (!transitionMatched && current != null && visited.add(current));
            }
        }
        removeConflictingTransitions(exctx, step, enabledTransitions);
    }

    /**
     * This method corresponds to the Algorithm for SCXML processing removeConflictingTransitions() procedure.
     *
     * @param exctx              The execution context for this step
     * @param step               The step
     * @param enabledTransitions The list of enabled transitions
     */
    public void removeConflictingTransitions(final SCXMLExecutionContext exctx, final Step step,
                                             final List<Transition> enabledTransitions) {
        LinkedHashSet<Transition> filteredTransitions = new LinkedHashSet<Transition>();
        LinkedHashSet<Transition> preemptedTransitions = new LinkedHashSet<Transition>();
        Map<Transition, Set<EnterableState>> exitSets = new HashMap<Transition, Set<EnterableState>>();

        Set<EnterableState> configuration = exctx.getScInstance().getStateConfiguration().getActiveStates();
        Collections.sort(enabledTransitions, DocumentOrder.documentOrderComparator);

        for (Transition t1 : enabledTransitions) {
            boolean t1Preempted = false;
            Set<EnterableState> t1ExitSet = exitSets.get(t1);
            for (Transition t2 : filteredTransitions) {
                if (t1ExitSet == null) {
                    t1ExitSet = new HashSet<EnterableState>();
                    computeExitSet(t1, t1ExitSet, configuration);
                    exitSets.put(t1, t1ExitSet);
                }
                Set<EnterableState> t2ExitSet = exitSets.get(t2);
                if (t2ExitSet == null) {
                    t2ExitSet = new HashSet<EnterableState>();
                    computeExitSet(t2, t2ExitSet, configuration);
                    exitSets.put(t2, t2ExitSet);
                }
                Set<EnterableState> smaller = t1ExitSet.size() < t2ExitSet.size() ? t1ExitSet : t2ExitSet;
                Set<EnterableState> larger = smaller == t1ExitSet ? t2ExitSet : t1ExitSet;
                boolean hasIntersection = false;
                for (EnterableState s1 : smaller) {
                    hasIntersection = larger.contains(s1);
                    if (hasIntersection) {
                        break;
                    }
                }
                if (hasIntersection) {
                    if (t1.getParent().isDescendantOf(t2.getParent())) {
                        preemptedTransitions.add(t2);
                    } else {
                        t1Preempted = true;
                        break;
                    }
                }
            }
            if (t1Preempted) {
                exitSets.remove(t1);
            } else {
                for (Transition preempted : preemptedTransitions) {
                    filteredTransitions.remove(preempted);
                    exitSets.remove(preempted);
                }
                filteredTransitions.add(t1);
            }
        }
        step.getTransitList().addAll(filteredTransitions);
    }

    /**
     * @param exctx      The execution context for this step
     * @param transition The transition
     * @param eventName  The (optional) event name to match against
     * @return Returns true if the transition matches against the provided eventName, or is event-less when no eventName
     * is provided, <em>AND</em> its (optional) condition guard evaluates to true.
     */
    public boolean matchTransition(final SCXMLExecutionContext exctx, final Transition transition, final String eventName) {
        if (eventName != null) {
            if (!(transition.isNoEventsTransition() || transition.isAllEventsTransition())) {
                boolean eventMatch = false;
                for (String event : transition.getEvents()) {
                    if (eventName.startsWith(event)) {
                        if (eventName.length() == event.length() || eventName.charAt(event.length()) == '.')
                            eventMatch = true;
                        break;
                    }
                }
                if (!eventMatch) {
                    return false;
                }
            } else if (!transition.isAllEventsTransition()) {
                return false;
            }
        } else if (!transition.isNoEventsTransition()) {
            return false;
        }
        if (transition.getCond() != null) {
            Boolean result = Boolean.FALSE;
            Context context = exctx.getScInstance().getContext(transition.getParent());
            context.setLocal(Context.NAMESPACES_KEY, transition.getNamespaces());
            try {
                if ((result = exctx.getEvaluator().evalCond(context, transition.getCond())) == null) {
                    result = Boolean.FALSE;
                    if (exctx.getAppLog().isDebugEnabled()) {
                        exctx.getAppLog().debug("Treating as false because the cond expression was evaluated as null: '"
                                + transition.getCond() + "'");
                    }
                }
            } catch (SCXMLExpressionException e) {
                exctx.getInternalIOProcessor().addEvent(new TriggerEvent(TriggerEvent.ERROR_EXECUTION, TriggerEvent.ERROR_EVENT));
                exctx.getErrorReporter().onError(ErrorConstants.EXPRESSION_ERROR, "Treating as false due to error: "
                        + e.getMessage(), transition);
            } finally {
                context.setLocal(Context.NAMESPACES_KEY, null);
            }
            return result;
        }
        return true;
    }

    /**
     * This method corresponds to the Algorithm for SCXML processing isFinalState() function.
     *
     * @param es            the enterable state to check
     * @param configuration the current state machine configuration
     * @return Return true if s is a compound state and one of its children is an active final state (i.e. is a member
     * of the current configuration), or if s is a parallel state and isInFinalState is true of all its children.
     */
    public boolean isInFinalState(final EnterableState es, final Set<EnterableState> configuration) {
        if (es instanceof State) {
            for (EnterableState child : ((State) es).getChildren()) {
                if (child instanceof Final && configuration.contains(child)) {
                    return true;
                }
            }
        } else if (es instanceof Parallel) {
            for (EnterableState child : ((Parallel) es).getChildren()) {
                if (!isInFinalState(child, configuration)) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    /**
     * Checks if an external event was send (back) by an specific Invoker
     *
     * @param invokerId the invokerId
     * @param event     received external event
     * @return true if this event was send by the specific Invoker
     */
    public boolean isInvokerEvent(final String invokerId, final TriggerEvent event) {
        return event.getName().equals("done.invoke." + invokerId) ||
                event.getName().startsWith("done.invoke." + invokerId + ".");
    }

    /**
     * Check if an external event indicates the state machine execution must be cancelled.
     *
     * @param event received external event
     * @return true if this event is of type {@link TriggerEvent#CANCEL_EVENT}.
     */
    public boolean isCancelEvent(TriggerEvent event) {
        return (event.getType() == TriggerEvent.CANCEL_EVENT);
    }

    /**
     * Checks whether a given set of states is a legal Harel State Table
     * configuration (with the respect to the definition of the OR and AND
     * states).
     *
     * @param states a set of states
     * @param errRep ErrorReporter to report detailed error info if needed
     * @return true if a given state configuration is legal, false otherwise
     */
    public boolean isLegalConfiguration(final Set<EnterableState> states, final ErrorReporter errRep) {
        /*
         * For every active state we add 1 to the count of its parent. Each
         * Parallel should reach count equal to the number of its children and
         * contribute by 1 to its parent. Each State should reach count exactly
         * 1. SCXML elemnt (top) should reach count exactly 1. We essentially
         * summarize up the hierarchy tree starting with a given set of
         * states = active configuration.
         */
        boolean legalConfig = true; // let's be optimists
        Map<EnterableState, Set<EnterableState>> counts = new HashMap<EnterableState, Set<EnterableState>>();
        Set<EnterableState> scxmlCount = new HashSet<EnterableState>();
        for (EnterableState es : states) {
            EnterableState parent;
            while ((parent = es.getParent()) != null) {
                Set<EnterableState> cnt = counts.get(parent);
                if (cnt == null) {
                    cnt = new HashSet<EnterableState>();
                    counts.put(parent, cnt);
                }
                cnt.add(es);
                es = parent;
            }
            //top-level contribution
            scxmlCount.add(es);
        }
        if (scxmlCount.size() > 1) {
            errRep.onError(ErrorConstants.ILLEGAL_CONFIG, "Multiple top-level OR states active!", scxmlCount);
            legalConfig = false;
        } else {
            //Validate child counts:
            for (Map.Entry<EnterableState, Set<EnterableState>> entry : counts.entrySet()) {
                EnterableState es = entry.getKey();
                Set<EnterableState> count = entry.getValue();
                if (es instanceof Parallel) {
                    Parallel p = (Parallel) es;
                    if (count.size() < p.getChildren().size()) {
                        errRep.onError(ErrorConstants.ILLEGAL_CONFIG, "Not all AND states active for parallel " + p.getId(), entry);
                        legalConfig = false;
                    }
                } else {
                    if (count.size() > 1) {
                        errRep.onError(ErrorConstants.ILLEGAL_CONFIG, "Multiple OR states active for state " + es.getId(), entry);
                        legalConfig = false;
                    }
                }
                count.clear(); //cleanup
            }
        }
        //cleanup
        scxmlCount.clear();
        counts.clear();
        return legalConfig;
    }

    /**
     * Stores the provided event in the system context
     * <p>
     * For the event a EventVariable is instantiated and the provided event its type is mapped to the one of the
     * SCXML specification predefined types.
     * </p>
     *
     * 设置系统事件变量，将外部发送的信息，保存到_event里面
     *
     * @param scInstance the state machine instance holding the system context
     * @param event      The event being stored
     * @param internal   Flag indicating the event was received internally or externally
     */
    public void setSystemEventVariable(final SCInstance scInstance, final TriggerEvent event, boolean internal) {
        Context systemContext = scInstance.getSystemContext();
        EventVariable eventVar = null;
        if (event != null) {
            String eventType = internal ? EventVariable.TYPE_INTERNAL : EventVariable.TYPE_EXTERNAL;

            final int triggerEventType = event.getType();
            if (triggerEventType == TriggerEvent.ERROR_EVENT || triggerEventType == TriggerEvent.CHANGE_EVENT) {
                eventType = EventVariable.TYPE_PLATFORM;
            }

            // TODO: determine sendid, origin, originType and invokeid based on context later.
            eventVar = new EventVariable(event.getName(), eventType, null, null, null, null, event.getPayload());
        }
        systemContext.setLocal(SCXMLSystemContext.EVENT_KEY, eventVar);
    }

    /**
     * Executes the global SCXML script element
     *
     * @param exctx The execution context
     * @throws ModelException if a SCXML model error occurred during the execution.
     */
    public void executeGlobalScript(final SCXMLExecutionContext exctx) throws ModelException {
        Script globalScript = exctx.getStateMachine().getGlobalScript();
        if (globalScript != null) {
            try {
                globalScript.execute(exctx.getActionExecutionContext());
            } catch (SCXMLExpressionException e) {
                exctx.getInternalIOProcessor().addEvent(new TriggerEvent(TriggerEvent.ERROR_EXECUTION, TriggerEvent.ERROR_EVENT));
                exctx.getErrorReporter().onError(ErrorConstants.EXPRESSION_ERROR, e.getMessage(), exctx.getStateMachine());
            }
        }
    }

    /**
     * This method corresponds to the Algorithm for SCXML processing exitStates() procedure, where the states to exit
     * already have been pre-computed in {@link #microStep(SCXMLExecutionContext, Step, Set)}.
     * 这个方法对应于SCXML规范描述的算法 exitStates() 程序，
     *
     *
     * @param exctx          The execution context for this micro step
     * @param step           the step
     * @param statesToInvoke the set of activated states which invokes need to be invoked at the end of the current
     *                       macro step
     * @throws ModelException if a SCXML model error occurred during the execution.
     */
    public void exitStates(final SCXMLExecutionContext exctx, final Step step,
                           final Set<TransitionalState> statesToInvoke)
            throws ModelException {
        if (step.getExitSet().isEmpty()) { //当前不的退出集合是空的，直接返回，例如状态机刚启动的时候退出集合是空的
            return;
        }
        //将要退出的状态放入arraylist， 并按照文档逆序顺序排好序
        ArrayList<EnterableState> exitList = new ArrayList<EnterableState>(step.getExitSet());
        Collections.sort(exitList, DocumentOrder.reverseDocumentOrderComparator);

        //对于每一个需要退出的状态
        for (EnterableState es : exitList) {

            //如果退出的是TransitionalState 状态，并且当前状态（肯定不是原子状态）有你是伪状态
            if (es instanceof TransitionalState && ((TransitionalState) es).hasHistory()) {
                // persist the new history configurations for this state to exit    将新的历史配置存储起来，
                for (History h : ((TransitionalState) es).getHistory()) {
                    exctx.getScInstance().setLastConfiguration(h, step.getNewHistoryConfigurations().get(h));
                }
            }
            //是否有抛出事件的一个标记，默认为false
            boolean onexitEventRaised = false;

            //执行每一个状态状态里面的每一个退出区域
            for (OnExit onexit : es.getOnExits()) {
                //执行可执行内容
                executeContent(exctx, onexit);

                //如果状态不是原子状态退出的时候，需要抛出内部事件
                if (!onexitEventRaised && onexit.isRaiseEvent()) {
                    onexitEventRaised = true;
                    exctx.getInternalIOProcessor().addEvent(new TriggerEvent("exit.state." + es.getId(), TriggerEvent.CHANGE_EVENT));
                }
            }
            //处理对这些状态或者SCXML文档监听的监听器，这里默认是执行日志记录
            exctx.getNotificationRegistry().fireOnExit(es, es);
            exctx.getNotificationRegistry().fireOnExit(exctx.getStateMachine(), es);

            //如果要退出的状态是TransitionalState,并且statesToInvoke里面包含es，
            if (es instanceof TransitionalState && !statesToInvoke.remove(es)) {
                // check if invokers are active in this state
                for (Invoke inv : ((TransitionalState) es).getInvokes()) {
                    exctx.cancelInvoker(inv);
                }
            }
            //最后一步，真正的退出这些状态
            exctx.getScInstance().getStateConfiguration().exitState(es);
        }
    }

    /**
     * Executes the executable content for all transitions in the micro step
     *
     * @param exctx The execution context for this micro step
     * @param step  the step
     * @throws ModelException if a SCXML model error occurred during the execution.
     */
    public void executeTransitionContent(final SCXMLExecutionContext exctx, final Step step) throws ModelException {
        for (SimpleTransition transition : step.getTransitList()) {
            executeContent(exctx, transition);
        }
    }

    /**
     * Executes the executable content for a specific executable in the micro step
     *  执行可执行内容里面的action
     * @param exctx The execution context for this micro step
     * @param exec  the executable providing the execution content
     * @throws ModelException if a SCXML model error occurred during the execution.
     */
    public void executeContent(SCXMLExecutionContext exctx, Executable exec) throws ModelException {
        //对于 onentry, onexit, transition里面的action，调用各自action的execute方法，传递actionexetioncontext
        try {
            for (Action action : exec.getActions()) {
                action.execute(exctx.getActionExecutionContext());
            }
        } catch (SCXMLExpressionException e) {
            exctx.getInternalIOProcessor().addEvent(new TriggerEvent(TriggerEvent.ERROR_EXECUTION, TriggerEvent.ERROR_EVENT));
            exctx.getErrorReporter().onError(ErrorConstants.EXPRESSION_ERROR, e.getMessage(), exec);
        }
        //如果当前可执行内容 是transition的实例
        if (exec instanceof Transition) {
            Transition t = (Transition) exec;
            //如果转移的目标是空的
            if (t.getTargets().isEmpty()) { //
                notifyOnTransition(exctx, t, t.getParent());
            } else {
                for (TransitionTarget tt : t.getTargets()) {
                    notifyOnTransition(exctx, t, tt);
                }
            }
        }
    }

    /**
     * Notifies SCXMLListeners on the transition taken
     * 通知SCXMLListeners 转移发生了
     * @param exctx  The execution context for this micro step
     * @param t      The Transition taken
     * @param target The target of the Transition
     */
    public void notifyOnTransition(final SCXMLExecutionContext exctx, final Transition t,
                                   final TransitionTarget target) {
        EventVariable event = (EventVariable) exctx.getScInstance().getSystemContext().getVars().get(SCXMLSystemContext.EVENT_KEY);
        String eventName = event != null ? event.getName() : null;
        exctx.getNotificationRegistry().fireOnTransition(t, t.getParent(), target, t, eventName);
        exctx.getNotificationRegistry().fireOnTransition(exctx.getStateMachine(), t.getParent(), target, t, eventName);
    }

    /**
     * This method corresponds to the Algorithm for SCXML processing enterStates() procedure, where the states to enter
     * already have been pre-computed in {@link #microStep(SCXMLExecutionContext, Step, Set)}.
     *
     * 这个方法对应于SCXML中的enterStates() ,
     *
     * @param exctx          The execution context for this micro step
     * @param step           the step
     * @param statesToInvoke the set of activated states which invokes need to be invoked at the end of the current
     *                       macro step
     * @throws ModelException if a SCXML model error occurred during the execution.
     */
    public void enterStates(final SCXMLExecutionContext exctx, final Step step,
                            final Set<TransitionalState> statesToInvoke)
            throws ModelException {
        //如果进入集合是空，直接退出。
        if (step.getEntrySet().isEmpty()) {
            return;
        }
        //得到文档顺序的进入集合
        ArrayList<EnterableState> entryList = new ArrayList<EnterableState>(step.getEntrySet());
        Collections.sort(entryList, DocumentOrder.documentOrderComparator);
        //对于每一个进入状态
        for (EnterableState es : entryList) {
            //放入到状态机配置对应的状态里面去
            exctx.getScInstance().getStateConfiguration().enterState(es);
            //es是transitionalState ,并且es 有invoke元素
            if (es instanceof TransitionalState && !((TransitionalState) es).getInvokes().isEmpty()) {
                statesToInvoke.add((TransitionalState) es);
            }

            //抛出进入事件默认为假
            boolean onentryEventRaised = false;
            for (OnEntry onentry : es.getOnEntries()) {
                executeContent(exctx, onentry);
                if (!onentryEventRaised && onentry.isRaiseEvent()) {   //如果onentry里面有raiseEvent元素
                    onentryEventRaised = true;
                    exctx.getInternalIOProcessor().addEvent(new TriggerEvent("entry.state." + es.getId(), TriggerEvent.CHANGE_EVENT));
                }
            }
            //通知
            exctx.getNotificationRegistry().fireOnEntry(es, es);
            exctx.getNotificationRegistry().fireOnEntry(exctx.getStateMachine(), es);

            //如果当前状态是State的实例，并且.....，并且........
            if (es instanceof State && step.getDefaultEntrySet().contains(es) && ((State) es).getInitial() != null) {
                executeContent(exctx, ((State) es).getInitial().getTransition());   //执行初始内容里面的转移
            }
            //如果当前状态是  TransitionState
            if (es instanceof TransitionalState) {
                SimpleTransition hTransition = step.getDefaultHistoryTransitions().get(es);
                if (hTransition != null) {
                    executeContent(exctx, hTransition);
                }
            }
            //如果当前状态是 Final
            if (es instanceof Final) {
                State parent = (State) es.getParent();
                if (parent == null) {
                    exctx.stopRunning();
                } else {
                    exctx.getInternalIOProcessor().addEvent(new TriggerEvent("done.state." + parent.getId(), TriggerEvent.CHANGE_EVENT));
                    if (parent.isRegion()) {
                        if (isInFinalState(parent.getParent(), exctx.getScInstance().getStateConfiguration().getActiveStates())) {
                            exctx.getInternalIOProcessor().addEvent(new TriggerEvent("done.state." + parent.getParent().getId()
                                    , TriggerEvent.CHANGE_EVENT));
                        }
                    }
                }
            }
        }
    }

    /**
     * Initiate any new invoked activities.
     *
     * @param exctx          provides the execution context
     * @param statesToInvoke the set of activated states which invokes need to be invoked
     * @throws ModelException if there is a fatal SCXML state error
     */
    public void initiateInvokes(final SCXMLExecutionContext exctx,
                                final Set<TransitionalState> statesToInvoke) throws ModelException {
        ActionExecutionContext aexctx = exctx.getActionExecutionContext();
        for (TransitionalState ts : statesToInvoke) {
            for (Invoke invoke : ts.getInvokes()) {
                Context ctx = aexctx.getContext(invoke.getParentEnterableState());
                String exctxKey = invoke.getCurrentSCXMLExecutionContextKey();
                ctx.setLocal(exctxKey, exctx);
                invoke.execute(aexctx);
                ctx.setLocal(exctxKey, null);
            }
        }
    }

    /**
     * Forward events to invoked activities, execute finalize handlers.
     *
     * 转发事件到被调用的活动，执行finalize处理器
     *
     * @param exctx provides the execution context
     * @param event The events to be forwarded
     * @throws ModelException in case there is a fatal SCXML object model problem.
     */
    public void processInvokes(final SCXMLExecutionContext exctx, final TriggerEvent event) throws ModelException {
        for (Map.Entry<Invoke, String> entry : exctx.getInvokeIds().entrySet()) {
            if (!isInvokerEvent(entry.getValue(), event)) {
                if (entry.getKey().isAutoForward()) {
                    Invoker inv = exctx.getInvoker(entry.getKey());
                    try {
                        inv.parentEvent(event);
                    } catch (InvokerException ie) {
                        exctx.getAppLog().error(ie.getMessage(), ie);
                        throw new ModelException(ie.getMessage(), ie.getCause());
                    }
                }
            }
            /*
            else {
                // TODO: applyFinalize
            }
            */
        }
    }
}

