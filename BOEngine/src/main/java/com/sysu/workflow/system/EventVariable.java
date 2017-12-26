
package com.sysu.workflow.system;

import java.io.Serializable;

/**
 * Event system variable holding a structure containing the current event's name and any data contained in the event
 * 事件系统变量，保存了当前事件的结构，结构包括，事件的名字，和包含在事件里面的其他数据
 */
public class EventVariable implements Serializable {

    /**
     * Serial version UID.
     */
    private static final long serialVersionUID = 1L;

    public static final String TYPE_PLATFORM = "platform";
    public static final String TYPE_INTERNAL = "internal";
    public static final String TYPE_EXTERNAL = "external";

    /**
     * The name of the event.
     */
    private final String name;

    /**
     * The event type
     */
    private final String type;

    /**
     * The sendid in case the sending entity has specified a value for this.
     */
    private final String sendid;

    /**
     * The URI string of the originating entity in an external event.
     */
    private final String origin;

    /**
     * The type in an external event.
     */
    private final String origintype;

    /**
     * The invoke id of the invocation that triggered the child process.
     */
    private final String invokeid;

    /**
     * Whatever data the sending entity chose to include in the event
     */
    private final Object data;

    public EventVariable(final String name, final String type, final String sendid, final String origin, final String origintype, final String invokeid, final Object data) {
        this.name = name;
        this.type = type;
        this.sendid = sendid;
        this.origin = origin;
        this.origintype = origintype;
        this.invokeid = invokeid;
        this.data = data;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getSendid() {
        return sendid;
    }

    public String getOrigin() {
        return origin;
    }

    public String getOrigintype() {
        return origintype;
    }

    public String getInvokeid() {
        return invokeid;
    }

    public Object getData() {
        return data;
    }
}

