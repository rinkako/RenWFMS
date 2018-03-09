/*
 * Project Ren @ 2018
 * Rinkako, Ariana, Gordan. SYSU SDCS.
 */
package org.sysu.workflow.model.extend;

import org.sysu.workflow.ActionExecutionContext;
import org.sysu.workflow.SCXMLExpressionException;
import org.sysu.workflow.model.Action;
import org.sysu.workflow.model.ModelException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Author: Ariana
 * Date  : 2017/4/15
 * Usage : Label context of SubProcess.
 */
public class SubProcess extends Action implements Serializable {
    /**
     * Serial version UID.
     */
    private static final long serialVersionUID = 1L;
    /**
     * 子过程的ID
     */
    private String id;
    /**
     * 子过程的name
     */
    private String name;
    /**
     * 过程绑定的事件列表
     */
    private List<String> events;
    /**
     * yawl流程定义文件的路径
     */
    private String src;
    /**
     * yawl流程文件的输入参数
     */
    Map<String, Object> payloadDataMap;


    public SubProcess() {

        this.events = new ArrayList<String>();
        payloadDataMap = new LinkedHashMap<String, Object>();
    }

    public List<String> getEvents() {
        return events;
    }

    /**
     *一个顺序流程对应一组可能的反馈事件
     * @param events
     */
    public void setEvent(String events) {
        String[] possibleEvents = events.split(",");
        for(String event : possibleEvents){
            this.events.add(event);
        }
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public Map<String, Object> getPayloadDataMap() {
        return payloadDataMap;
    }

    public void setPayloadDataMap(Map<String, Object> payloadDataMap) {
        this.payloadDataMap = payloadDataMap;
    }

    /**
     *
     * @param exctx The ActionExecutionContext for this execution instance
     * @throws ModelException
     * @throws SCXMLExpressionException
     */
    public void execute(ActionExecutionContext exctx) {

    }
}
