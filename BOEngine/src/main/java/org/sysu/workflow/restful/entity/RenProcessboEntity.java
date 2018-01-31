package org.sysu.workflow.restful.entity;

import javax.persistence.*;

@Entity
@Table(name = "ren_processbo", schema = "renboengine")
public class RenProcessboEntity {
    private String pbid;
    private String boid;
    private String pid;

    @Id
    @Column(name = "pbid", nullable = false, length = 64)
    public String getPbid() {
        return pbid;
    }

    public void setPbid(String pbid) {
        this.pbid = pbid;
    }

    @Basic
    @Column(name = "boid", nullable = true, length = 64)
    public String getBoid() {
        return boid;
    }

    public void setBoid(String boid) {
        this.boid = boid;
    }

    @Basic
    @Column(name = "pid", nullable = false, length = 64)
    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RenProcessboEntity that = (RenProcessboEntity) o;

        if (pbid != null ? !pbid.equals(that.pbid) : that.pbid != null) return false;
        if (boid != null ? !boid.equals(that.boid) : that.boid != null) return false;
        if (pid != null ? !pid.equals(that.pid) : that.pid != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = pbid != null ? pbid.hashCode() : 0;
        result = 31 * result + (boid != null ? boid.hashCode() : 0);
        result = 31 * result + (pid != null ? pid.hashCode() : 0);
        return result;
    }
}
