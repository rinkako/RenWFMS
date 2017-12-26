
package com.sysu.workflow;

import com.sysu.workflow.model.EnterableState;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * 一个状态机的状态
 * <p/>
 * The current active states of a state machine
 */
public class StateConfiguration implements Serializable {
    /**
     * Serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The states that are currently active.
     * 当前活跃的状态
     */
    private final Set<EnterableState> activeStates = new HashSet<EnterableState>();
    private final Set<EnterableState> activeStatesSet = Collections.unmodifiableSet(activeStates);

    /**
     * The atomic states that are currently active.
     * 当前活跃状态中的原子状态
     */
    private final Set<EnterableState> atomicStates = new HashSet<EnterableState>();
    private final Set<EnterableState> atomicStatesSet = Collections.unmodifiableSet(atomicStates);

    /**
     * Get the active states
     * 得到活跃中的状态
     *
     * @return active states including simple states and their
     * complex ancestors up to the root.
     */
    public Set<EnterableState> getActiveStates() {
        return activeStatesSet;
    }

    /**
     * Get the current atomic states (leaf only).
     * 得到当前所有的原子状态集合，这个集合是不可更改的
     *
     * @return Returns the atomic states - simple (leaf) states only.
     */
    public Set<EnterableState> getStates() {
        return atomicStatesSet;
    }

    /**
     * 进入一个活跃状态，如果状态是原子状态，添加到当前的states
     * Enter an active state
     * If the state is atomic also record it add it to the current states
     *
     * @param state state to enter
     */
    public void enterState(final EnterableState state) {
        if (!activeStates.add(state)) {
            throw new IllegalStateException("State " + state.getId() + " already added.");
        }
        if (state.isAtomicState()) {
            if (!atomicStates.add(state)) {
                throw new IllegalStateException("Atomic state " + state.getId() + " already added.");
            }
        }
    }

    /**
     * Exit an active state
     * If the state is atomic also remove it from current states
     * <p/>
     * 退出一个活跃的状态，
     *
     * @param state state to exit
     */
    public void exitState(final EnterableState state) {
        if (!activeStates.remove(state)) {
            throw new IllegalStateException("State " + state.getId() + " not active.");
        }
        atomicStates.remove(state);
    }

    /**
     * 清除状态配置
     * Clear the state configuration
     */
    public void clear() {
        activeStates.clear();
        atomicStates.clear();
    }
}
