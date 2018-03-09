/*
 * Project Ren @ 2018
 * Rinkako, Ariana, Gordan. SYSU SDCS.
 */
package org.sysu.renNameService.entity;

import javax.persistence.*;
import java.util.Objects;

/**
 * Author: Rinkako
 * Date  : 2018/2/1
 * Usage :
 */
@Entity
@Table(name = "ren_rolemap_archived", schema = "renboengine")
public class RenRolemapArchivedEntity {
    private String mapId;
    private String rtid;
    private String broleName;
    private String corganGid;
    private String mappedGid;
    private String dataVersion;

    @Id
    @Column(name = "map_id", nullable = false, length = 64)
    public String getMapId() {
        return mapId;
    }

    public void setMapId(String mapId) {
        this.mapId = mapId;
    }

    @Basic
    @Column(name = "rtid", nullable = false, length = 64)
    public String getRtid() {
        return rtid;
    }

    public void setRtid(String rtid) {
        this.rtid = rtid;
    }

    @Basic
    @Column(name = "brole_name", nullable = false, length = -1)
    public String getBroleName() {
        return broleName;
    }

    public void setBroleName(String broleName) {
        this.broleName = broleName;
    }

    @Basic
    @Column(name = "corgan_gid", nullable = false, length = 64)
    public String getCorganGid() {
        return corganGid;
    }

    public void setCorganGid(String corganGid) {
        this.corganGid = corganGid;
    }

    @Basic
    @Column(name = "mapped_gid", nullable = false, length = 64)
    public String getMappedGid() {
        return mappedGid;
    }

    public void setMappedGid(String mappedGid) {
        this.mappedGid = mappedGid;
    }

    @Basic
    @Column(name = "data_version", nullable = false, length = 64)
    public String getDataVersion() {
        return dataVersion;
    }

    public void setDataVersion(String dataVersion) {
        this.dataVersion = dataVersion;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RenRolemapArchivedEntity that = (RenRolemapArchivedEntity) o;
        return Objects.equals(mapId, that.mapId) &&
                Objects.equals(rtid, that.rtid) &&
                Objects.equals(broleName, that.broleName) &&
                Objects.equals(corganGid, that.corganGid) &&
                Objects.equals(mappedGid, that.mappedGid) &&
                Objects.equals(dataVersion, that.dataVersion);
    }

    @Override
    public int hashCode() {

        return Objects.hash(mapId, rtid, broleName, corganGid, mappedGid, dataVersion);
    }
}
