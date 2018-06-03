package org.sysu.workflow.entity;

import javax.persistence.*;
import java.util.Objects;

/**
 * Author: Rinkako
 * Date  : 2018/6/3
 * Usage :
 */
@Entity
@Table(name = "ren_archived_tree", schema = "renboengine", catalog = "")
public class RenArchivedTreeEntity {
    private String rtid;
    private String tree;

    @Id
    @Column(name = "rtid", nullable = false, length = 64)
    public String getRtid() {
        return rtid;
    }

    public void setRtid(String rtid) {
        this.rtid = rtid;
    }

    @Basic
    @Column(name = "tree", nullable = false, length = -1)
    public String getTree() {
        return tree;
    }

    public void setTree(String tree) {
        this.tree = tree;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RenArchivedTreeEntity that = (RenArchivedTreeEntity) o;
        return Objects.equals(rtid, that.rtid) &&
                Objects.equals(tree, that.tree);
    }

    @Override
    public int hashCode() {

        return Objects.hash(rtid, tree);
    }
}
