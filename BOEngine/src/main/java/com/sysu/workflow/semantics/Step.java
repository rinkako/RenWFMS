
package com.sysu.workflow.semantics;

import com.sysu.workflow.TriggerEvent;
import com.sysu.workflow.model.EnterableState;
import com.sysu.workflow.model.History;
import com.sysu.workflow.model.SimpleTransition;
import com.sysu.workflow.model.TransitionalState;

import java.util.*;

/**
 * SCXML执行一步的逻辑单元
 * A logical unit of progression in the execution of a SCXML model.
 */
public class Step {

    /**
     * 这一步中的事件
     * The event in this step.
     */
    private TriggerEvent event;

    /**
     * 这一步的退出集合
     * The set of states that were exited during this step.
     */
    private Set<EnterableState> exitSet;

    /**
     * 这一步的进入集合
     * The set of states that were entered during this step.
     */
    private Set<EnterableState> entrySet;

    /**
     * 这一步的默认进入结合
     * The set of states that were entered during this step by default
     */
    private Set<EnterableState> defaultEntrySet;

    /**
     * 默认历史转移，进入某一个状态的时候得到的值
     * The map of default History transitions to be executed as result of entering states in this step.
     */
    private Map<TransitionalState, SimpleTransition> defaultHistoryTransitions;

    /**
     * 新的历史配置，退出某一个状态的时候
     * The map of new History configurations created as result of exiting states in this step
     */
    private Map<History, Set<EnterableState>> newHistoryConfigurations;

    /**
     * 一系列简单的转移
     * The list of Transitions taken during this step.
     */
    private List<SimpleTransition> transitList;

    /**
     * 构造函数
     * @param event The event received in this unit of progression
     */
    public Step(TriggerEvent event) {
        this.event = event;
        this.exitSet = new HashSet<EnterableState>();
        this.entrySet = new HashSet<EnterableState>();
        this.defaultEntrySet = new HashSet<EnterableState>();
        this.defaultHistoryTransitions = new HashMap<TransitionalState, SimpleTransition>();
        this.newHistoryConfigurations = new HashMap<History, Set<EnterableState>>();
        this.transitList = new ArrayList<SimpleTransition>();
    }

    /**
     * 确保即时转移状态被清理了，在开始处理and/or事件转移之前
     * Ensure the intermediate state of this step is cleared before start processing the event and/or transitions
     */
    public void clearIntermediateState() {
        exitSet.clear();
        entrySet.clear();
        defaultEntrySet.clear();
        defaultHistoryTransitions.clear();
        newHistoryConfigurations.clear();
    }

    /**
     * 返回进入集合
     * @return Returns the entrySet.
     */
    public Set<EnterableState> getEntrySet() {
        return entrySet;
    }

    /**
     * 默认进入集合
     * @return Returns the defaultEntrySet.
     */
    public Set<EnterableState> getDefaultEntrySet() {
        return defaultEntrySet;
    }

    /**
     * 返回默认历史转移集合
     * @return Returns the map of default History transitions to be executed as result of entering states in this step
     */
    public Map<TransitionalState, SimpleTransition> getDefaultHistoryTransitions() {
        return defaultHistoryTransitions;
    }

    /**
     * 返回新的历史配置，退出一个状态的时候
     * @return Returns the map of new History configurations created as result of exiting states in this step
     */
    public Map<History, Set<EnterableState>> getNewHistoryConfigurations() {
        return newHistoryConfigurations;
    }

    /**
     * 返回退出集合
     * @return Returns the exitSet.
     */
    public Set<EnterableState> getExitSet() {
        return exitSet;
    }

    /**
     * 返回当前事件
     * @return Returns the current event.
     */
    public TriggerEvent getEvent() {
        return event;
    }

    /**
     * 返回转移集合
     * @return Returns the transitList.
     */
    public List<SimpleTransition> getTransitList() {
        return transitList;
    }
}

