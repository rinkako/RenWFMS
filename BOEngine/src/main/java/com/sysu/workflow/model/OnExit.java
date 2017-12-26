
package com.sysu.workflow.model;

/**
 * SCXML对象模型中对应于 onexit元素，
 * 是一个可选的属性
 * The class in this SCXML object model that corresponds to the
 * &lt;onexit&gt; SCXML element, which is an optional property
 * holding executable content to be run upon exiting the parent
 * State or Parallel.
 */
public class OnExit extends Executable {

    /**
     * Serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * 一个表示器，在执行完这个OnExit之后是否要出了一个exit.state.id内部事件
     * An indicator whether to raise the non-standard "exit.state.id" internal event after executing this OnExit
     */
    private Boolean raiseEvent;

    /**
     * Constructor.
     */
    public OnExit() {
        super();
    }

    /**
     * Set the EnterableState parent.
     *
     * @param parent The parent to set.
     */
    @Override
    public final void setParent(final EnterableState parent) {
        super.setParent(parent);
    }

    /**
     * @return Returns true if the non-standard internal "exit.state.id" event will be raised after executing this OnExit
     */
    public final boolean isRaiseEvent() {
        return raiseEvent != null && raiseEvent;
    }

    /**
     * @return Returns the indicator whether to raise the non-standard "exit.state.id" internal event after executing
     * this OnExit. When null no event will be raised
     */
    public final Boolean getRaiseEvent() {
        return raiseEvent;
    }

    /**
     * Set the indicator whether to raise the non-standard "exit.state.id" internal event after executing this OnExit.
     *
     * @param raiseEvent The indicator, when null no event will be raised
     */
    public final void setRaiseEvent(final Boolean raiseEvent) {
        this.raiseEvent = raiseEvent;
    }
}

