package org.sysu.renResourcing.context.steady;

import javax.persistence.*;
import java.util.Objects;

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
        return Objects.equals(taskid, that.taskid) &&
                Objects.equals(boid, that.boid) &&
                Objects.equals(polymorphismName, that.polymorphismName) &&
                Objects.equals(polymorphismId, that.polymorphismId) &&
                Objects.equals(brole, that.brole) &&
                Objects.equals(principle, that.principle) &&
                Objects.equals(eventdescriptor, that.eventdescriptor) &&
                Objects.equals(hookdescriptor, that.hookdescriptor) &&
                Objects.equals(documentation, that.documentation) &&
                Objects.equals(parameters, that.parameters);
    }

    @Override
    public int hashCode() {

        return Objects.hash(taskid, boid, polymorphismName, polymorphismId, brole, principle, eventdescriptor, hookdescriptor, documentation, parameters);
    }
}
