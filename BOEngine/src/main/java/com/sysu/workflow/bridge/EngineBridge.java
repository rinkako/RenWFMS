package com.sysu.workflow.bridge;

import com.sysu.workflow.SCXMLExecutor;
import com.sysu.workflow.TriggerEvent;
import com.sysu.workflow.entity.BOMessage;
import com.sysu.workflow.model.ModelException;

import java.io.File;
import java.util.*;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * 桥类：为状态机工作流引擎和YAWL引擎建立通讯接口
 * Created by Rinkako on 2017/3/7 0007.
 */
public class EngineBridge {
    /**
     * 工厂方法，获取类的唯一实例
     * @return 桥接器的引用
     */
    public static EngineBridge GetInstance() {
        return EngineBridge.syncObj == null ?
                EngineBridge.syncObj = new EngineBridge() : EngineBridge.syncObj;
    }

    /**
     * <p>初始化桥接器</p>
     * <p>该方法在应用程序初始化时被调用</p>
     * @param handler 应用程序消息处理器
     */
    public void Init(EngineBridgeAppHandler handler) {
        this.SetAppHandler(handler);
    }

    /**
     * 为桥设置一个状态机处理器
     * @param executor 要绑定的状态机处理器
     */
    public void SetExecutorReference(String executorId, SCXMLExecutor executor) {
        executor.setExecutorIndex(executorId);
        this.executorMap.put(executorId, executor);
    }

    /**
     * 获取一个状态机处理器的引用
     * @param executorId 状态机的编号
     * @return 状态机处理器的引用
     */
    public SCXMLExecutor GetExecutorReference(int executorId) {
        Integer intPackage = Integer.valueOf(executorId);
        return this.executorMap.get(intPackage);
    }

    /**
     * 为桥绑定应用程序的消息处理器
     * @param handler 要绑定的消息处理器
     */
    public void SetAppHandler(EngineBridgeAppHandler handler) {
        this.appHandler = handler;
    }

    /**
     * 向引擎发送一个外部事件并触发它
     * @param eventName 要触发的外部事件名
     * @param payload 附加在事件上的包装
     * @throws ModelException
     */
    public void SendEventAndTrigger(String executorId, String eventName, Object payload) throws ModelException {
        TriggerEvent tevt = new TriggerEvent(eventName, TriggerEvent.SIGNAL_EVENT, payload);
        try {
            if (this.executorMap.containsKey(executorId)) {
                this.executorMap.get(executorId).triggerEvent(tevt);
            }
            else {
                System.out.println("Executor not found in bridge: " + executorId);
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
            throw new IllegalArgumentException(ex.toString());
        }
    }

    /**
     * 获取一个待处理的消息，若没有则返回null
     * @return
     */
    public BOMessage GetPendingMessage() {
        return this.stateMachieMessageQueue.poll();
    }

    /**
     * 获取消息队列中剩余的消息数量
     * @return
     */
    public int Count() {
        return this.stateMachieMessageQueue.size();
    }

    /**
     * 将一个要发送给应用程序的消息放入队列
     * @param msg 要发送的消息
     */
    public void EnqueueBOMessage(BOMessage msg) {
        if (msg != null) {
            this.stateMachieMessageQueue.add(msg);
            if (this.appHandler != null) {
                this.appHandler.WasNotified();
            }
        }
        else {
            throw new IllegalArgumentException();
        }
    }

    /**
     * 将一个任务作为消息放入发送到应用程序的队列中
     * @param execIdx 触发消息的执行器id
     * @param taskName 任务名称
     * @param params 任务的输入参数
     * @param roleName 角色名称
     * @param callbackEv 处理完成的事件名
     */
    public static void QuickEnqueueBOMessage(String execIdx, String taskName, Map<String, Object> params, String roleName, String callbackEv) {
        BOMessage boMsg = new BOMessage();
        boMsg.AddMessageItem(execIdx, taskName, params, roleName, callbackEv);
        EngineBridge.GetInstance().EnqueueBOMessage(boMsg);
    }

    /**
     *将一个子流程作为消息放入发送到YAWL引擎的队列中
     * @param execIdx 触发这个消息的执行器在应用程序里的索引号（业务对象实例ID）
     * @param processName 子流程名称
     * @param processSrc 流程定义文件的绝对路径
     * @param params 传入子流程的输入参数
     * @param callbackEventList 子流程处理完可能返回的事件列表
     */
    public static void QuickEnqueueBOMessage(String execIdx, String processName, String processSrc, Map<String, Object> params, List<String> callbackEventList){
        File file = new File(processSrc);
        BOMessage boMessage = new BOMessage();
        boMessage.addProcessMessageItem(execIdx, processName, processSrc,file, params, callbackEventList);
        EngineBridge.GetInstance().EnqueueBOMessage(boMessage);
    }

    /**
     * 状态机要传递给应用程序的消息队列
     */
    private Queue<BOMessage> stateMachieMessageQueue;

    /**
     * 私有的构造器
     */
    private EngineBridge() {
        this.stateMachieMessageQueue = new LinkedBlockingDeque<BOMessage>();
        this.executorMap = new Hashtable<String, SCXMLExecutor>();
    }

    /**
     * 引擎处理器的引用，她是整个状态机树上的根
     */
    private Map<String, SCXMLExecutor> executorMap;

    /**
     * 应用程序消息处理器的引用
     */
    private EngineBridgeAppHandler appHandler;

    /**
     * 桥的唯一实例
     */
    private static EngineBridge syncObj = null;
}
