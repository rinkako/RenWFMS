package org.sysu.workflow.entity;

import javax.persistence.*;

@Entity
@Table(name = "ren_rstask", schema = "renboengine", catalog = "")
public class RenRstaskEntity {
    private String taskid;
    private String boid;
    private String polymorphismName;
    private String polymorphismId;
    private String brole;
    private String principle;
    private String eventdescriptor;
    private String hookdescriptor;
    private String documentation;
    private String parameters;

    @Id
    @Column(name = "taskid")
    public String getTaskid() {
        return taskid;
    }

    public void setTaskid(String taskid) {
        this.taskid = taskid;
    }

    @Basic
    @Column(name = "boid")
    public String getBoid() {
        return boid;
    }

    public void setBoid(String boid) {
        this.boid = boid;
    }

    @Basic
    @Column(name = "polymorphism_name")
    public String getPolymorphismName() {
        return polymorphismName;
    }

    public void setPolymorphismName(String polymorphismName) {
        this.polymorphismName = polymorphismName;
    }

    @Basic
    @Column(name = "polymorphism_id")
    public String getPolymorphismId() {
        return polymorphismId;
    }

    public void setPolymorphismId(String polymorphismId) {
        this.polymorphismId = polymorphismId;
    }

    @Basic
    @Column(name = "brole")
    public String getBrole() {
        return brole;
    }

    public void setBrole(String brole) {
        this.brole = brole;
    }

    @Basic
    @Column(name = "principle")
    public String getPrinciple() {
        return principle;
    }

    public void setPrinciple(String principle) {
        this.principle = principle;
    }

    @Basic
    @Column(name = "eventdescriptor")
    public String getEventdescriptor() {
        return eventdescriptor;
    }

    public void setEventdescriptor(String eventdescriptor) {
        this.eventdescriptor = eventdescriptor;
    }

    @Basic
    @Column(name = "hookdescriptor")
    public String getHookdescriptor() {
        return hookdescriptor;
    }

    public void setHookdescriptor(String hookdescriptor) {
        this.hookdescriptor = hookdescriptor;
    }

    @Basic
    @Column(name = "documentation")
    public String getDocumentation() {
        return documentation;
    }

    public void setDocumentation(String documentation) {
        this.documentation = documentation;
    }

    @Basic
    @Column(name = "parameters")
    public String getParameters() {
        return parameters;
    }

    public void setParameters(String parameters) {
        this.parameters = parameters;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RenRstaskEntity that = (RenRstaskEntity) o;

        if (taskid != null ? !taskid.equals(that.taskid) : that.taskid != null) return false;
        if (boid != null ? !boid.equals(that.boid) : that.boid != null) return false;
        if (polymorphismName != null ? !polymorphismName.equals(that.polymorphismName) : that.polymorphismName != null)
            return false;
        if (polymorphismId != null ? !polymorphismId.equals(that.polymorphismId) : that.polymorphismId != null)
            return false;
        if (brole != null ? !brole.equals(that.brole) : that.brole != null) return false;
        if (principle != null ? !principle.equals(that.principle) : that.principle != null) return false;
        if (eventdescriptor != null ? !eventdescriptor.equals(that.eventdescriptor) : that.eventdescriptor != null)
            return false;
        if (hookdescriptor != null ? !hookdescriptor.equals(that.hookdescriptor) : that.hookdescriptor != null)
            return false;
        if (documentation != null ? !documentation.equals(that.documentation) : that.documentation != null)
            return false;
        if (parameters != null ? !parameters.equals(that.parameters) : that.parameters != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = taskid != null ? taskid.hashCode() : 0;
        result = 31 * result + (boid != null ? boid.hashCode() : 0);
        result = 31 * result + (polymorphismName != null ? polymorphismName.hashCode() : 0);
        result = 31 * result + (polymorphismId != null ? polymorphismId.hashCode() : 0);
        result = 31 * result + (brole != null ? brole.hashCode() : 0);
        result = 31 * result + (principle != null ? principle.hashCode() : 0);
        result = 31 * result + (eventdescriptor != null ? eventdescriptor.hashCode() : 0);
        result = 31 * result + (hookdescriptor != null ? hookdescriptor.hashCode() : 0);
        result = 31 * result + (documentation != null ? documentation.hashCode() : 0);
        result = 31 * result + (parameters != null ? parameters.hashCode() : 0);
        return result;
    }
}
