package com.sysu.workflow.entity;

import org.apache.commons.collections.map.HashedMap;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 引擎消息类：作为引擎向应用程序发送消息的包装中间件
 * Created by Rinkako on 2017/3/7 0007.
 */
public class BOMessage {
    /**
     * 构造器
     */
    public BOMessage() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        this.triggerExecutorIndex = "0";
        this.messageCreateTimestampString = sdf.format(new Date());
        this.agentList = new ArrayList<String>();
        this.tasksList = new ArrayList<String>();
        this.params = new LinkedHashMap<String, Object>();
        this.callbackEventList = new ArrayList<String>();
    }

    /**
     * 将一个要发送给YAWL引擎的任务项和它的处理者添加到消息中
     * @param taskName 任务名称
     * @param params 任务参数
     * @param dealRole 任务处理者
     * @param callbackEv 任务完成后要触发的事件名
     */
    public void AddMessageItem(String triggerExecIdx, String taskName, Map<String, Object> params, String dealRole, String callbackEv) {
        if (taskName != null && dealRole != null) {
            this.triggerExecutorIndex = triggerExecIdx;
            this.tasksList.add(taskName);
            this.agentList.add(dealRole);
            this.params = params;
            this.callbackEventList.add(callbackEv);
        }
        else {
            throw new IllegalArgumentException();
        }
    }

    public void addProcessMessageItem(String triggerExecIdx, String processName, String processSrc, File processFile, Map<String, Object> params, List<String> callbackEventList){
        if(processName != null && processSrc != null){
            triggerExecutorIndex = triggerExecIdx;
            this.processName = processName;
            this.processSrc = processSrc;
            this.processFile = processFile;
            this.params = params;
            this.callbackEventList = callbackEventList;
        }
        else {
            throw new IllegalArgumentException();
        }
    }
    /**
     * 获取发出这条信息的状态机执行器的id
     * @return 状态机执行器id
     */
    public String GetExecutorIndex() {
        return this.triggerExecutorIndex;
    }

    /**
     * 获取应用程序要处理的任务向量
     * @return 任务向量的引用
     */
    public List<String> GetTaskList() {
        return this.tasksList;
    }

    /**
     * 获取应用程序要处理的任务向量所一一对应的角色向量
     * @return Agent向量的引用
     */
    public List<String> GetAgentList() {
        return this.agentList;
    }

    /**
     * 获取应用程序要处理的任务向量所一一对应的参数字符串向量
     * @return Params向量的引用
     */
    public Map<String,Object> GetParams() { return this.params; }


    /**
     * 获取应用程序要处理的任务向量所一一对应的参数字符串向量
     * @return Params向量的引用
     */
    public List<String> GetCallbackList() { return this.callbackEventList; }
    //============传递原子任务或yawl流程文件都需要的实例变量=====================//
    /**
     * 触发这个消息的执行器在应用程序里的索引号
     */
    private String triggerExecutorIndex;
    /**
     * 这个消息要传递给应用程序的任务所对应的参数字符串向量
     */
    private Map<String, Object> params;
    /**
     * 这个消息要传递给应用程序的任务所对应的完成反馈事件向量
     */
    private List<String> callbackEventList;

    //===================传递原子任务需要的实例变量=============================//
    /**
     * 这个消息要传递给应用程序的任务向量
     */
    private List<String> tasksList;

    /**
     * 这个消息要传递给应用程序的任务所对应的处理角色向量
     */
    private List<String> agentList;

    /**
     * 消息产生的时间戳
     */
    private String messageCreateTimestampString;

    //=====================传递yawl流程文件需要的实例变量========================//
    /**
     * 这个消息要传递给YAWL引擎的yawl流程文件的绝对路径
     */
    private String processSrc;
    /**
     * 这个消息要传递给YAWL引擎的yawl流程文件
     */
    private File processFile;
    /**
     * 这个消息要传递给YAWL引擎的yawl流程文件的名字
     */
    private String processName;
}
