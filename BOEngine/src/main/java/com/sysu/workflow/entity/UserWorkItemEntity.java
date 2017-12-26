package com.sysu.workflow.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA
 * Date: 2016/1/16
 * Time: 15:45
 * User: ThinerZQ
 * GitHub: <a>https://github.com/ThinerZQ</a>
 * Blog: <a>http://blog.csdn.net/c601097836</a>
 * Email: 601097836@qq.com
 */

@Entity
@Table(name = "t_userworkitem")
public class UserWorkItemEntity implements WorkflowEntity {

    @Id
    @GeneratedValue(generator = "generator")
    @GenericGenerator(name = "generator", strategy = "identity")
    private long itemId;
    @Basic
    private String itemName;
    @Basic
    private String itemCreateTime;
    @Basic
    private String itemDueTime;
    @Basic
    private String itemStateId;
    @Basic
    private String itemProcessId;
    @Basic
    private String itemFinish = "no";


    @ManyToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "itemAssignee")
    private UserEntity itemAssigneeEntity;

    @OneToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "formEntity")
    private FormEntity itemFormEntity;

    @ManyToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "groupWorkItemEntity")
    private GroupWorkItemEntity itemGroupWorkItemEntity;

    @ManyToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "processInstanceEntity")
    private ProcessInstanceEntity itemProcessInstanceEntity;


    public String getItemFinish() {
        return itemFinish;
    }

    public UserWorkItemEntity setItemFinish(String itemFinish) {
        this.itemFinish = itemFinish;
        return this;
    }

    public long getItemId() {
        return itemId;
    }

    public UserWorkItemEntity setItemId(long itemId) {
        this.itemId = itemId;
        return this;
    }

    public String getItemName() {
        return itemName;
    }

    public UserWorkItemEntity setItemName(String itemName) {
        this.itemName = itemName;
        return this;
    }

    public String getItemCreateTime() {
        return itemCreateTime;
    }

    public UserWorkItemEntity setItemCreateTime(String itemCreateTime) {
        this.itemCreateTime = itemCreateTime;
        return this;
    }

    public String getItemDueTime() {
        return itemDueTime;
    }

    public UserWorkItemEntity setItemDueTime(String itemDueTime) {
        this.itemDueTime = itemDueTime;
        return this;
    }

    public String getItemStateId() {
        return itemStateId;
    }

    public UserWorkItemEntity setItemStateId(String itemStateId) {
        this.itemStateId = itemStateId;
        return this;
    }

    public String getItemProcessId() {
        return itemProcessId;
    }

    public UserWorkItemEntity setItemProcessId(String itemProcessId) {
        this.itemProcessId = itemProcessId;
        return this;
    }

    public UserEntity getItemAssigneeEntity() {
        return itemAssigneeEntity;
    }

    public UserWorkItemEntity setItemAssigneeEntity(UserEntity itemAssigneeEntity) {
        this.itemAssigneeEntity = itemAssigneeEntity;
        return this;
    }

    public FormEntity getItemFormEntity() {
        return itemFormEntity;
    }

    public UserWorkItemEntity setItemFormEntity(FormEntity itemFormEntity) {
        this.itemFormEntity = itemFormEntity;
        return this;
    }

    public GroupWorkItemEntity getItemGroupWorkItemEntity() {
        return itemGroupWorkItemEntity;
    }

    public UserWorkItemEntity setItemGroupWorkItemEntity(GroupWorkItemEntity itemGroupWorkItemEntity) {
        this.itemGroupWorkItemEntity = itemGroupWorkItemEntity;
        return this;
    }

    public ProcessInstanceEntity getItemProcessInstanceEntity() {
        return itemProcessInstanceEntity;
    }

    public void setItemProcessInstanceEntity(ProcessInstanceEntity itemProcessInstanceEntity) {
        this.itemProcessInstanceEntity = itemProcessInstanceEntity;
    }

    public Map<String, Object> getNotNullPropertyMap() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("currentObject", this.getClass().getName());
        if (getItemId() != 0) {
            map.put("itemId", getItemId());
        }
        if (getItemName() != null) {
            map.put("itemName", getItemName());
        }
        if (getItemCreateTime() != null) {
            map.put("itemCreateTime", getItemCreateTime());
        }
        if (getItemDueTime() != null) {
            map.put("itemDueDate", getItemDueTime());
        }
        if (getItemStateId() != null) {
            map.put("itemStateId", getItemStateId());
        }
        if (getItemProcessId() != null) {
            map.put("itemProcessId", getItemProcessId());
        }

        if (getItemAssigneeEntity() != null) {
            map.put("itemAssigneeEntity", getItemAssigneeEntity());
        }
        if (getItemFinish() != null) {
            map.put("itemFinish", getItemFinish());
        }
        System.out.println("Query Condition: " + map);
        map.remove("currentObject");
        return map;
    }
}
