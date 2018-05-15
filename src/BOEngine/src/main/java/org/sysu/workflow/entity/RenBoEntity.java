package org.sysu.workflow.entity;

import javax.persistence.*;
import java.util.Arrays;

@Entity
@Table(name = "ren_bo", schema = "renboengine", catalog = "")
public class RenBoEntity {
    private String boid;
    private String boName;
    private String pid;
    private int state;
    private String boContent;
    private byte[] serialized;
    private String broles;

    @Id
    @Column(name = "boid", nullable = false, length = 64)
    public String getBoid() {
        return boid;
    }

    public void setBoid(String boid) {
        this.boid = boid;
    }

    @Basic
    @Column(name = "bo_name", nullable = false, length = -1)
    public String getBoName() {
        return boName;
    }

    public void setBoName(String boName) {
        this.boName = boName;
    }

    @Basic
    @Column(name = "pid", nullable = false, length = 64)
    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    @Basic
    @Column(name = "state", nullable = false)
    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    @Basic
    @Column(name = "bo_content", nullable = true, length = -1)
    public String getBoContent() {
        return boContent;
    }

    public void setBoContent(String boContent) {
        this.boContent = boContent;
    }

    @Basic
    @Column(name = "serialized", nullable = true)
    public byte[] getSerialized() {
        return serialized;
    }

    public void setSerialized(byte[] serialized) {
        this.serialized = serialized;
    }

    @Basic
    @Column(name = "broles", nullable = true, length = -1)
    public String getBroles() {
        return broles;
    }

    public void setBroles(String broles) {
        this.broles = broles;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RenBoEntity that = (RenBoEntity) o;

        if (state != that.state) return false;
        if (boid != null ? !boid.equals(that.boid) : that.boid != null) return false;
        if (boName != null ? !boName.equals(that.boName) : that.boName != null) return false;
        if (pid != null ? !pid.equals(that.pid) : that.pid != null) return false;
        if (boContent != null ? !boContent.equals(that.boContent) : that.boContent != null) return false;
        if (!Arrays.equals(serialized, that.serialized)) return false;
        if (broles != null ? !broles.equals(that.broles) : that.broles != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = boid != null ? boid.hashCode() : 0;
        result = 31 * result + (boName != null ? boName.hashCode() : 0);
        result = 31 * result + (pid != null ? pid.hashCode() : 0);
        result = 31 * result + state;
        result = 31 * result + (boContent != null ? boContent.hashCode() : 0);
        result = 31 * result + Arrays.hashCode(serialized);
        result = 31 * result + (broles != null ? broles.hashCode() : 0);
        return result;
    }
}
