package org.sysu.workflow.restful.entity;

import javax.persistence.*;

@Entity
@Table(name = "ren_processbo", schema = "renboengine")
public class RenProcessboEntity {
    private String pbid;
    private String processId;
    private String boId;

    @Id
    @Column(name = "pbid", nullable = false, length = 64)
    public String getPbid() {
        return pbid;
    }

    public void setPbid(String pbid) {
        this.pbid = pbid;
    }

    @Basic
    @Column(name = "pid", nullable = false, length = 64)
    public String getProcessId() {
        return processId;
    }

    public void setProcessId(String processId) {
        this.processId = processId;
    }

    @Basic
    @Column(name = "boid", nullable = true, length = 64)
    public String getBoId() {
        return boId;
    }

    public void setBoId(String boId) {
        this.boId = boId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RenProcessboEntity that = (RenProcessboEntity) o;

        if (pbid != null ? !pbid.equals(that.pbid) : that.pbid != null) return false;
        if (processId != null ? !processId.equals(that.processId) : that.processId != null) return false;
        if (boId != null ? !boId.equals(that.boId) : that.boId != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = pbid != null ? pbid.hashCode() : 0;
        result = 31 * result + (processId != null ? processId.hashCode() : 0);
        result = 31 * result + (boId != null ? boId.hashCode() : 0);
        return result;
    }
}
