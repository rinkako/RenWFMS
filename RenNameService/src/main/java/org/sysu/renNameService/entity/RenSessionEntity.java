/*
 * Project Ren @ 2018
 * Rinkako, Ariana, Gordan. SYSU SDCS.
 */
package org.sysu.renNameService.entity;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

/**
 * Author: Rinkako
 * Date  : 2018/1/28
 * Usage :
 */
@Entity
@Table(name = "ren_session", schema = "renboengine")
public class RenSessionEntity {
    private String token;
    private String username;
    private int level;
    private Timestamp createTimestamp;
    private Timestamp untilTimestamp;
    private Timestamp destroyTimestamp;

    @Id
    @Column(name = "token", nullable = false, length = 64)
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Basic
    @Column(name = "username", nullable = false, length = -1)
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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
    @Column(name = "create_timestamp", nullable = false)
    public Timestamp getCreateTimestamp() {
        return createTimestamp;
    }

    public void setCreateTimestamp(Timestamp createTimestamp) {
        this.createTimestamp = createTimestamp;
    }

    @Basic
    @Column(name = "until_timestamp", nullable = true)
    public Timestamp getUntilTimestamp() {
        return untilTimestamp;
    }

    public void setUntilTimestamp(Timestamp untilTimestamp) {
        this.untilTimestamp = untilTimestamp;
    }

    @Basic
    @Column(name = "destroy_timestamp", nullable = true)
    public Timestamp getDestroyTimestamp() {
        return destroyTimestamp;
    }

    public void setDestroyTimestamp(Timestamp destroyTimestamp) {
        this.destroyTimestamp = destroyTimestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RenSessionEntity that = (RenSessionEntity) o;
        return level == that.level &&
                Objects.equals(token, that.token) &&
                Objects.equals(username, that.username) &&
                Objects.equals(createTimestamp, that.createTimestamp) &&
                Objects.equals(untilTimestamp, that.untilTimestamp) &&
                Objects.equals(destroyTimestamp, that.destroyTimestamp);
    }

    @Override
    public int hashCode() {

        return Objects.hash(token, username, level, createTimestamp, untilTimestamp, destroyTimestamp);
    }
}
