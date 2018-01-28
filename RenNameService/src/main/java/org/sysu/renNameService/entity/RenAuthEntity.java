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
@Table(name = "ren_auth", schema = "renboengine", catalog = "")
public class RenAuthEntity {
    private String username;
    private String password;
    private Integer level;
    private Integer state;
    private Timestamp createtimestamp;
    private String corganGateway;

    public void setLevel(int level) {
        this.level = level;
    }

    public void setState(int state) {
        this.state = state;
    }

    @Id
    @Column(name = "username", nullable = false, length = 64)
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Basic
    @Column(name = "password", nullable = false, length = 128)
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Basic
    @Column(name = "level", nullable = false)
    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    @Basic
    @Column(name = "state", nullable = false)
    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    @Basic
    @Column(name = "createtimestamp", nullable = true)
    public Timestamp getCreatetimestamp() {
        return createtimestamp;
    }

    public void setCreatetimestamp(Timestamp createtimestamp) {
        this.createtimestamp = createtimestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RenAuthEntity that = (RenAuthEntity) o;
        return Objects.equals(username, that.username) &&
                Objects.equals(password, that.password) &&
                Objects.equals(level, that.level) &&
                Objects.equals(state, that.state) &&
                Objects.equals(createtimestamp, that.createtimestamp);
    }

    @Override
    public int hashCode() {

        return Objects.hash(username, password, level, state, createtimestamp);
    }

    @Basic
    @Column(name = "corgan_gateway", nullable = true, length = -1)
    public String getCorganGateway() {
        return corganGateway;
    }

    public void setCorganGateway(String corganGateway) {
        this.corganGateway = corganGateway;
    }
}
