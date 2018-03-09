/*
 * Project Ren @ 2018
 * Rinkako, Ariana, Gordan. SYSU SDCS.
 */
package org.sysu.renNameService.entity;

import javax.persistence.*;
import java.util.Arrays;
import java.util.Objects;

/**
 * Author: Rinkako
 * Date  : 2018/1/30
 * Usage :
 */
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
        return state == that.state &&
                Objects.equals(boid, that.boid) &&
                Objects.equals(boName, that.boName) &&
                Objects.equals(pid, that.pid) &&
                Objects.equals(boContent, that.boContent) &&
                Arrays.equals(serialized, that.serialized) &&
                Objects.equals(broles, that.broles);
    }

    @Override
    public int hashCode() {

        int result = Objects.hash(boid, boName, pid, state, boContent, broles);
        result = 31 * result + Arrays.hashCode(serialized);
        return result;
    }
}
