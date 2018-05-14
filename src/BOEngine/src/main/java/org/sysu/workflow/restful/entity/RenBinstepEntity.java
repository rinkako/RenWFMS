package org.sysu.workflow.restful.entity;

import javax.persistence.*;
import java.util.Arrays;
import java.util.Objects;

/**
 * Author: Rinkako
 * Date  : 2018/5/15
 * Usage :
 */
@Entity
@Table(name = "ren_binstep", schema = "renboengine", catalog = "")
public class RenBinstepEntity {
    private String rtid;
    private byte[] binlog;

    @Id
    @Column(name = "rtid", nullable = false, length = 64)
    public String getRtid() {
        return rtid;
    }

    public void setRtid(String rtid) {
        this.rtid = rtid;
    }

    @Basic
    @Column(name = "binlog", nullable = true)
    public byte[] getBinlog() {
        return binlog;
    }

    public void setBinlog(byte[] binlog) {
        this.binlog = binlog;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RenBinstepEntity that = (RenBinstepEntity) o;
        return Objects.equals(rtid, that.rtid) &&
                Arrays.equals(binlog, that.binlog);
    }

    @Override
    public int hashCode() {

        int result = Objects.hash(rtid);
        result = 31 * result + Arrays.hashCode(binlog);
        return result;
    }
}
