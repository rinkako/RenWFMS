package org.sysu.renResourcing.context.steady;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "ren_rsparticipant", schema = "renboengine", catalog = "")
public class RenRsparticipantEntity {
    private String workerid;
    private String displayname;
    private int type;
    private int reentrantType;
    private int referenceCounter;
    private String agentLocation;
    private String note;

    @Id
    @Column(name = "workerid")
    public String getWorkerid() {
        return workerid;
    }

    public void setWorkerid(String workerid) {
        this.workerid = workerid;
    }

    @Basic
    @Column(name = "displayname")
    public String getDisplayname() {
        return displayname;
    }

    public void setDisplayname(String displayname) {
        this.displayname = displayname;
    }

    @Basic
    @Column(name = "type")
    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Basic
    @Column(name = "reentrantType")
    public int getReentrantType() {
        return reentrantType;
    }

    public void setReentrantType(int reentrantType) {
        this.reentrantType = reentrantType;
    }

    @Basic
    @Column(name = "referenceCounter")
    public int getReferenceCounter() {
        return referenceCounter;
    }

    public void setReferenceCounter(int referenceCounter) {
        this.referenceCounter = referenceCounter;
    }

    @Basic
    @Column(name = "agentLocation")
    public String getAgentLocation() {
        return agentLocation;
    }

    public void setAgentLocation(String agentLocation) {
        this.agentLocation = agentLocation;
    }

    @Basic
    @Column(name = "note")
    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RenRsparticipantEntity that = (RenRsparticipantEntity) o;
        return type == that.type &&
                reentrantType == that.reentrantType &&
                referenceCounter == that.referenceCounter &&
                Objects.equals(workerid, that.workerid) &&
                Objects.equals(displayname, that.displayname) &&
                Objects.equals(agentLocation, that.agentLocation) &&
                Objects.equals(note, that.note);
    }

    @Override
    public int hashCode() {

        return Objects.hash(workerid, displayname, type, reentrantType, referenceCounter, agentLocation, note);
    }
}
