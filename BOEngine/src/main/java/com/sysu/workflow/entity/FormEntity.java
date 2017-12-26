package com.sysu.workflow.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created with IntelliJ IDEA
 * Date: 2016/1/20
 * Time: 19:02
 * User: ThinerZQ
 * GitHub: <a>https://github.com/ThinerZQ</a>
 * Blog: <a>http://blog.csdn.net/c601097836</a>
 * Email: 601097836@qq.com
 */
@Entity
@Table(name = "t_form")
public class FormEntity implements WorkflowEntity {

    @Id
    @GeneratedValue(generator = "generator")
    @GenericGenerator(name = "generator", strategy = "identity")
    private long formId;

    @Basic
    private String formName;

    @Basic
    private String formSrc;


    @OneToOne(cascade = {CascadeType.ALL},mappedBy = "itemFormEntity")
    private UserWorkItemEntity userWorkItemEntity;

    @OneToOne(cascade = {CascadeType.ALL},mappedBy = "itemFormEntity")
    private GroupWorkItemEntity groupWorkItemEntity;

    @OneToMany(fetch = FetchType.EAGER, cascade = {CascadeType.ALL})
    private Set<FormItemEntity> formItemEntityLinkedHashSet = new LinkedHashSet<FormItemEntity>();


    public long getFormId() {
        return formId;
    }

    public void setFormId(long formId) {
        this.formId = formId;
    }

    public String getFormName() {
        return formName;
    }

    public void setFormName(String formName) {
        this.formName = formName;
    }

    public String getFormSrc() {
        return formSrc;
    }

    public void setFormSrc(String formSrc) {
        this.formSrc = formSrc;
    }

    public UserWorkItemEntity getUserWorkItemEntity() {
        return userWorkItemEntity;
    }

    public void setUserWorkItemEntity(UserWorkItemEntity userWorkItemEntity) {
        this.userWorkItemEntity = userWorkItemEntity;
    }

    public GroupWorkItemEntity getGroupWorkItemEntity() {
        return groupWorkItemEntity;
    }

    public void setGroupWorkItemEntity(GroupWorkItemEntity groupWorkItemEntity) {
        this.groupWorkItemEntity = groupWorkItemEntity;
    }

    public Set<FormItemEntity> getFormItemEntityLinkedHashSet() {
        return formItemEntityLinkedHashSet;
    }

    public void setFormItemEntityLinkedHashSet(Set<FormItemEntity> formItemEntityLinkedHashSet) {
        this.formItemEntityLinkedHashSet = formItemEntityLinkedHashSet;
    }

    public Map<String, Object> getNotNullPropertyMap() {
        return null;
    }


}
