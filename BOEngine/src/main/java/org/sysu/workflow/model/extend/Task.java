package org.sysu.workflow.model.extend;

import org.sysu.workflow.ActionExecutionContext;
import org.sysu.workflow.SCXMLExpressionException;
import org.sysu.workflow.model.Action;
import org.sysu.workflow.model.ModelException;

import java.io.Serializable;

/**
 * 任务类：为业务对象提供业务方法的定义
 * Created by Rinkako on 2017/3/7.
 */
public class Task extends Action implements Serializable {
    /**
     * Serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * 任务的唯一ID
     */
    private String id;

    /**
     * 任务名
     */
    private String name;

    /**
     * 把任务指定给某类业务角色
     */
    private String brole;

    /**
     * 任务实例个数，默认情况下为1
     * 实际已经没这个属性了
     */
    private String instanceExpr = "1";

    /**
     * 不需要用户定义的属性，调用任务时自动保存到数据库中
     */
    private String startTime;

    /**
     * 不需要用户定义的属性，任务结束时保存当前时间到数据库中
     */
    private String endTime;

    /**
     * 任务完成后发送到接收该事件的业务对象实例的外部队列的事件名，可包含数据
     */
    private String event;

    /**
     * 任务分派策略
     */
    private String principle;

    /**
     * 获取id的值
     */
    public String getId() {
        return id;
    }

    /**
     * 设置id的值
     * @param id 任务的id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * 获取name的值
     */
    public String getName() {
        return name;
    }

    /**
     * 设置name的值
     * @param name 人物名
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取agent的值
     */
    public String getBrole() {
        return this.brole;
    }

    /**
     * 设置agent的值
     * @param brole 任务的业务角色
     */
    public void setBrole(String brole) {
        this.brole = brole;
    }

    /**
     * 获取instanceExpr的值
     */
    public String getInstanceExpr() {
        return instanceExpr;
    }

    /**
     * 设置instanceExpr的值
     * @param instanceExpr 任务实例个数
     */
    public void setInstanceExpr(String instanceExpr) {
        this.instanceExpr = instanceExpr;
    }

    /**
     * 获取starttime的值
     */
    public String getStartTime() {
        return startTime;
    }

    /**
     * 设置starttime的值
     * @param startTime 开始时刻
     */
    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    /**
     * 获取endtime的值
     */
    public String getEndTime() {
        return endTime;
    }

    /**
     * 设置endtime的值
     * @param endTime 结束时刻
     */
    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    /**
     * 获取event的值
     */
    public String getEvent() {
        return event;
    }

    /**
     * 设置event的值
     * @param event 完成后发送到接受该事件的业务对象实例的外部队列的外部事件名
     */
    public void setEvent(String event) {
        this.event = event;
    }

    /**
     * 获取principle的值
     */
    public String getPrinciple() {
        return principle;
    }

    /**
     * 设置principle的值
     * @param principle 当前任务的分配策略
     */
    public void setPrinciple(String principle) {
        this.principle = principle;
    }

    /**
     * 重写父类的执行方法
     * @param exctx 动作的上下文
     */
    @Override
    public void execute(ActionExecutionContext exctx) throws ModelException, SCXMLExpressionException {

    }
}
