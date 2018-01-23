package org.sysu.workflow.restful.entity;

import javax.persistence.*;

@Entity
@Table(name = "ren_bo", schema = "renboengine")
public class RenBoEntity {
    private String boid;
    private String boName;
    private String boContent;

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
    @Column(name = "bo_content", nullable = true, length = -1)
    public String getBoContent() {
        return boContent;
    }

    public void setBoContent(String boContent) {
        this.boContent = boContent;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RenBoEntity that = (RenBoEntity) o;

        if (boid != null ? !boid.equals(that.boid) : that.boid != null) return false;
        if (boName != null ? !boName.equals(that.boName) : that.boName != null) return false;
        if (boContent != null ? !boContent.equals(that.boContent) : that.boContent != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = boid != null ? boid.hashCode() : 0;
        result = 31 * result + (boName != null ? boName.hashCode() : 0);
        result = 31 * result + (boContent != null ? boContent.hashCode() : 0);
        return result;
    }
}
