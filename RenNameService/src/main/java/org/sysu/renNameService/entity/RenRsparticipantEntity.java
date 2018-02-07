/*
 * Project Ren @ 2018
 * Rinkako, Ariana, Gordan. SYSU SDCS.
 */
package org.sysu.renNameService.entity;

import javax.persistence.*;
import java.util.Objects;

/**
 * Author: Rinkako
 * Date  : 2018/2/7
 * Usage :
 */
@Entity
@Table(name = "ren_rsparticipant", schema = "renboengine", catalog = "")
public class RenRsparticipantEntity {
    private String workerid;
    private String displayname;
    private int type;
    private int reentrantType;
    private int referenceCounter;

    @Id
    @Column(name = "workerid", nullable = false, length = 64)
    public String getWorkerid() {
        return workerid;
    }

    public void setWorkerid(String workerid) {
        this.workerid = workerid;
    }

    @Basic
    @Column(name = "displayname", nullable = true, length = -1)
    public String getDisplayname() {
        return displayname;
    }

    public void setDisplayname(String displayname) {
        this.displayname = displayname;
    }

    @Basic
    @Column(name = "type", nullable = false)
    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Basic
    @Column(name = "reentrantType", nullable = false)
    public int getReentrantType() {
        return reentrantType;
    }

    public void setReentrantType(int reentrantType) {
        this.reentrantType = reentrantType;
    }

    @Basic
    @Column(name = "referenceCounter", nullable = false)
    public int getReferenceCounter() {
        return referenceCounter;
    }

    public void setReferenceCounter(int referenceCounter) {
        this.referenceCounter = referenceCounter;
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
                Objects.equals(displayname, that.displayname);
    }

    @Override
    public int hashCode() {

        return Objects.hash(workerid, displayname, type, reentrantType, referenceCounter);
    }
}
