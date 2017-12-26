
package com.sysu.workflow;

import com.sysu.workflow.invoke.Invoker;

/**
 * SCXML I/O处理器提供了一个接口，对于内部事件或者外部系统或者一个被调用的孩子
 * <p/>
 * The SCXML I/O Processor provides the interface for either an internal process or an external system or invoked child
 * SCXML process ({@link Invoker}) to send events into the SCXML processor queue.
 */
public interface SCXMLIOProcessor {

    /**
     * The name of the default SCXML I/O Event Processor
     * 默认的SCXML I/O 事件处理器的名字
     */
    String DEFAULT_EVENT_PROCESSOR = "http://www.w3.org/TR/scxml/#SCXMLEventProcessor";

    /**
     * Prefix for SCXML I/O Event Processor aliases
     * SCXML I/O事件处理的别名的前缀
     */
    String EVENT_PROCESSOR_ALIAS_PREFIX = "#_";

    /**
     * Default SCXML I/O Event Processor alias
     * 默认的SCXML I/O 事件处理器别名
     */
    String SCXML_EVENT_PROCESSOR = "scxml";

    /**
     * Prefix for SCXML I/O (own) Session external Event processor
     * 前缀对于SCXML I/O 会话内部事件队列
     */
    String SCXML_SESSION_EVENT_PROCESSOR_PREFIX = EVENT_PROCESSOR_ALIAS_PREFIX + "scxml_";

    /**
     * The name of the internal Event Processor
     * 内部事件处理的名字
     */
    String INTERNAL_EVENT_PROCESSOR = EVENT_PROCESSOR_ALIAS_PREFIX + "internal";

    /**
     * The name of the parent Event Processor
     * 父亲事件处理器的名字
     */
    String PARENT_EVENT_PROCESSOR = EVENT_PROCESSOR_ALIAS_PREFIX + "parent";

    /**
     * 发送一个事件到SCXML 处理器队列
     *
     * @param event the event to send
     */
    void addEvent(TriggerEvent event);
}
