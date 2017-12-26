
package com.sysu.workflow.invoke;

import com.sysu.workflow.Context;
import com.sysu.workflow.SCXMLExecutor;
import com.sysu.workflow.SCXMLIOProcessor;
import com.sysu.workflow.TriggerEvent;
import com.sysu.workflow.env.SimpleSCXMLListener;
import com.sysu.workflow.io.SCXMLReader;
import com.sysu.workflow.model.ModelException;
import com.sysu.workflow.model.SCXML;

import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.util.Map;

/**
 * A simple {@link Invoker} for SCXML documents. Invoked SCXML document
 * may not contain external namespace elements, further invokes etc.
 *
 * 一个简单的SCXML会话调用器，调用SCXML文档，may not包含外部命名空间元素
 */
public class SimpleSCXMLInvoker implements Invoker, Serializable {

    /**
     * Serial version UID.
     */
    private static final long serialVersionUID = 1L;
    /**
     * Parent state ID.
     * 调用发生的 状态
     */
    private String parentStateId;
    /**
     * Invoking parent SCXMLExecutor
     * 调用者的 引擎
     */
    private SCXMLExecutor parentSCXMLExecutor;
    /**
     * The invoked state machine executor.
     * 被调用的状态机（本状态机）的引擎
     */
    private SCXMLExecutor executor;
    /**
     * Cancellation status.
     * 是否取消了
     */
    private boolean cancelled;


    /**
     * {@inheritDoc}.
     */

    public String getInvokeId() {
        return parentStateId;
    }

    /**
     * {@inheritDoc}.
     */

    public void setInvokeId(final String invokeId) {
        this.parentStateId = invokeId;
        this.cancelled = false;
    }

    /**
     * {@inheritDoc}.
     */

    public void setParentSCXMLExecutor(SCXMLExecutor parentSCXMLExecutor) {
        this.parentSCXMLExecutor = parentSCXMLExecutor;
    }

    /**
     * {@inheritDoc}.
     */

    public SCXMLIOProcessor getChildIOProcessor() {
        // not used
        return executor;
    }

    /**
     * 开始调用了，
     * 有很多东西（ executor = new SCXMLExecutor(parentSCXMLExecutor);）
     * 语义，求值器，事件分发器，错误报告都是直接利用父状态机的内容
     *
     * {@inheritDoc}.
     */

    public void invoke(final String source, final Map<String, Object> params)
            throws InvokerException {

        //构造被调用的状态机
        SCXML scxml;
        try {
            scxml = SCXMLReader.read(new URL(source));
        } catch (ModelException me) {
            throw new InvokerException(me.getMessage(), me.getCause());
        } catch (IOException ioe) {
            throw new InvokerException(ioe.getMessage(), ioe.getCause());
        } catch (XMLStreamException xse) {
            throw new InvokerException(xse.getMessage(), xse.getCause());
        }
        executor = new SCXMLExecutor(parentSCXMLExecutor);
        try {
            executor.setStateMachine(scxml);
        } catch (ModelException me) {
            throw new InvokerException(me);
        }

        //将调用里面的参数都放入到被调用的状态机的上下文里面
        Context rootCtx = executor.getRootContext();
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            rootCtx.setLocal(entry.getKey(), entry.getValue());
        }

        //给当前被调用的引擎添加上一个scxml的全局监听器
        executor.addListener(scxml, new SimpleSCXMLListener());
        try {
            executor.go();
        } catch (ModelException me) {
            throw new InvokerException(me.getMessage(), me.getCause());
        }
        if (executor.getStatus().isFinal()) {
            TriggerEvent te = new TriggerEvent("done.invoke." + parentStateId, TriggerEvent.SIGNAL_EVENT);
            new AsyncTrigger(parentSCXMLExecutor, te).start();
        }
    }

    /**
     * {@inheritDoc}.
     */

    public void parentEvent(final TriggerEvent evt)
            throws InvokerException {
        if (cancelled) {
            return; // no further processing should take place
        }
        boolean doneBefore = executor.getStatus().isFinal();
        executor.addEvent(evt);
        if (!doneBefore && executor.getStatus().isFinal()) {
            TriggerEvent te = new TriggerEvent("done.invoke." + parentStateId, TriggerEvent.SIGNAL_EVENT);
            new AsyncTrigger(parentSCXMLExecutor, te).start();
        }
    }

    /**
     * {@inheritDoc}.
     */

    public void cancel()
            throws InvokerException {
        cancelled = true;
        executor.addEvent(new TriggerEvent("cancel.invoke." + parentStateId, TriggerEvent.CANCEL_EVENT));
    }
}

