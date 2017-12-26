package com.sysu.workflow.bridge;

/**
 * 为应用程序获取状态机的状态变化提供接口
 * 应用程序中应该实现这个接口来订阅来自EngineBridge的消息通知
 * Created by Rinkako on 2017/3/7 0007.
 */
public interface EngineBridgeAppHandler {
    /**
     * 通知应用程序处理消息队列
     */
    void WasNotified();
}
