package org.sysu.renNameService.entity;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

/**
 * Author: Rinkako
 * Date  : 2018/6/1
 * Usage :
 */
@Entity
@Table(name = "ren_authuser", schema = "renboengine", catalog = "")
@IdClass(RenAuthuserEntityPK.class)
public class RenAuthuserEntity {
    private String username;
    private String domain;
    private int level;
    private String password;
    private int status;
    private Timestamp createtimestamp;
    private Timestamp lastlogin;
    private String gid;

    @Id
    @Column(name = "username", nullable = false, length = 255)
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Id
    @Column(name = "domain", nullable = false, length = 255)
    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    @Basic
    @Column(name = "level", nullable = false)
    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    @Basic
    @Column(name = "password", nullable = false, length = 255)
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Basic
    @Column(name = "status", nullable = false)
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Basic
    @Column(name = "createtimestamp", nullable = true)
    public Timestamp getCreatetimestamp() {
        return createtimestamp;
    }

    public void setCreatetimestamp(Timestamp createtimestamp) {
        this.createtimestamp = createtimestamp;
    }

    @Basic
    @Column(name = "lastlogin", nullable = true)
    public Timestamp getLastlogin() {
        return lastlogin;
    }

    public void setLastlogin(Timestamp lastlogin) {
        this.lastlogin = lastlogin;
    }

    @Basic
    @Column(name = "gid", nullable = true, length = 255)
    public String getGid() {
        return gid;
    }

    public void setGid(String gid) {
        this.gid = gid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RenAuthuserEntity that = (RenAuthuserEntity) o;
        return level == that.level &&
                status == that.status &&
                Objects.equals(username, that.username) &&
                Objects.equals(domain, that.domain) &&
                Objects.equals(password, that.password) &&
                Objects.equals(createtimestamp, that.createtimestamp) &&
                Objects.equals(lastlogin, that.lastlogin) &&
                Objects.equals(gid, that.gid);
    }

    @Override
    public int hashCode() {

        return Objects.hash(username, domain, level, password, status, createtimestamp, lastlogin, gid);
    }
}
