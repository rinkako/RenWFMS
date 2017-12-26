
package com.sysu.workflow;

import com.sysu.workflow.model.*;

/**
 * 监听接口，监听SCXML中所有可观察的实体
 * 包括：SCXML(订阅所有的entry,exit,transition 通知)
 * State （订阅特别的 entry，exit 通知）
 * Transition （订阅特别的transitions 通知）
 * <p/>
 * Listener interface for observable entities in the SCXML model.
 * Observable entities include {@link SCXML}
 * instances (subscribe to all entry, exit and transition notifications),
 * {@link State} instances (subscribe to
 * particular entry and exit notifications) and
 * {@link Transition} instances (subscribe to
 * particular transitions).
 */
public interface SCXMLListener {

    /**
     * Handle the entry into a EnterableState.
     * 处理进入一个状态
     *
     * @param state The EnterableState entered
     */
    void onEntry(EnterableState state);

    /**
     * Handle the exit out of a EnterableState.
     * 处理退出一个状态
     *
     * @param state The EnterableState exited
     */
    void onExit(EnterableState state);

    /**
     * Handle the transition.
     * <p/>
     * 处理一个转移
     *
     * @param from       The source TransitionTarget
     * @param to         The destination TransitionTarget
     * @param transition The Transition taken
     * @param event      The event name triggering the transition
     */
    void onTransition(TransitionTarget from, TransitionTarget to,
                      Transition transition, String event);

}

