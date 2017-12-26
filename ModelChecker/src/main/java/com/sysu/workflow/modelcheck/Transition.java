package com.sysu.workflow.modelcheck;

/**
 * User：ThinerZQ
 * Email：thinerzq@gmail.com
 * Date：2017/1/5 16:39
 * Project：WorkflowModelCheck
 * Package：com.sysu.workflow.modelcheck
 */
public class Transition {
    int number;
    String name;
    StateNode source;
    String event;
    String cond;
    String action;
    StateNode target;

    public StateNode getSource() {
        return source;
    }

    public void setSource(StateNode source) {
        this.source = source;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getCond() {
        return cond;
    }

    public void setCond(String cond) {
        this.cond = cond;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public StateNode getTarget() {
        return target;
    }

    public void setTarget(StateNode target) {
        this.target = target;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Transition{" +
                "number=" + number +
                ", name='" + name + '\'' +
                ", sourceNumber=" + source.getNumber() +
                ", event='" + event + '\'' +
                ", cond='" + cond + '\'' +
                ", action='" + action + '\'' +
                ", targetNumber=" + target.getNumber() +
                '}';
    }
}
