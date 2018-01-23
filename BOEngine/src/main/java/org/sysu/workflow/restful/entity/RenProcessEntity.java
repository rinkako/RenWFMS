package org.sysu.workflow.restful.entity;

import javax.persistence.*;

@Entity
@Table(name = "ren_process", schema = "renboengine")
public class RenProcessEntity {
    private String pid;
    private String processName;

    @Id
    @Column(name = "pid", nullable = false, length = 64)
    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    @Basic
    @Column(name = "process_name", nullable = false, length = -1)
    public String getProcessName() {
        return processName;
    }

    public void setProcessName(String processName) {
        this.processName = processName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RenProcessEntity that = (RenProcessEntity) o;

        if (pid != null ? !pid.equals(that.pid) : that.pid != null) return false;
        if (processName != null ? !processName.equals(that.processName) : that.processName != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = pid != null ? pid.hashCode() : 0;
        result = 31 * result + (processName != null ? processName.hashCode() : 0);
        return result;
    }
}
