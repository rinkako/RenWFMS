package com.sysu.workflow.engine;

import com.sysu.workflow.SCXMLExecutionContext;

import java.util.ArrayList;

/**
 * 实例树节点类
 * Created by Rinkako on 2017/3/15.
 */
public class TimeTreeNode {
    /**
     * 构造器
     * @param filename 该节点的业务对象描述文件的名字（不带XML后缀名）
     * @param timeId 节点的唯一编号
     * @param exect 节点绑定的执行器上下文
     * @param parent 节点的母亲节点
     */
    public TimeTreeNode(String filename, String timeId, SCXMLExecutionContext exect, TimeTreeNode parent) {
        this.filename = filename;
        this.Parent = parent;
        this.timeId = timeId;
        this.exect = exect;
        this.Children = new ArrayList<TimeTreeNode>();
    }

    /**
     * 为节点增加一个孩子
     * @param ttn 要增加的孩子节点
     */
    public void AddChild(TimeTreeNode ttn) {
        ttn.Parent = this;
        this.Children.add(ttn);
    }

    /**
     * 获取节点唯一编号
     * @return 节点的唯一编号
     */
    public String getTimeId() {
        return timeId;
    }

    /**
     * 获取节点绑定的执行器上下文
     * @return 节点绑定的执行器上下文
     */
    public SCXMLExecutionContext getExect() {
        return exect;
    }

    /**
     * 获取节点的业务对象描述文件的名字
     * @return 不带XML后缀名的业务对象描述文件名字
     */
    public String getFilename() {
        return filename;
    }

    /**
     * 节点的孩子
     */
    public ArrayList<TimeTreeNode> Children;

    /**
     * 节点的母亲
     */
    public TimeTreeNode Parent;

    /**
     * 节点唯一编号
     */
    private String timeId;

    /**
     * 创建该执行器的不带XML后缀名的业务对象描述文件名字
     */
    private String filename;

    /**
     * 绑定的执行器上下文
     */
    private SCXMLExecutionContext exect;
}