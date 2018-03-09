package org.sysu.renNameService.entity;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "ren_domain", schema = "renboengine", catalog = "")
public class RenDomainEntity {
    private String name;
    private int level;
    private int status;
    private Timestamp createtimestamp;
    private String corganGateway;
    private String urlsafeSignature;

    @Id
    @Column(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Basic
    @Column(name = "level")
    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    @Basic
    @Column(name = "status")
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Basic
    @Column(name = "createtimestamp")
    public Timestamp getCreatetimestamp() {
        return createtimestamp;
    }

    public void setCreatetimestamp(Timestamp createtimestamp) {
        this.createtimestamp = createtimestamp;
    }

    @Basic
    @Column(name = "corgan_gateway")
    public String getCorganGateway() {
        return corganGateway;
    }

    public void setCorganGateway(String corganGateway) {
        this.corganGateway = corganGateway;
    }

    @Basic
    @Column(name = "urlsafe_signature")
    public String getUrlsafeSignature() {
        return urlsafeSignature;
    }

    public void setUrlsafeSignature(String urlsafeSignature) {
        this.urlsafeSignature = urlsafeSignature;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RenDomainEntity that = (RenDomainEntity) o;
        return level == that.level &&
                status == that.status &&
                Objects.equals(name, that.name) &&
                Objects.equals(createtimestamp, that.createtimestamp) &&
                Objects.equals(corganGateway, that.corganGateway) &&
                Objects.equals(urlsafeSignature, that.urlsafeSignature);
    }

    @Override
    public int hashCode() {

        return Objects.hash(name, level, status, createtimestamp, corganGateway, urlsafeSignature);
    }
}
