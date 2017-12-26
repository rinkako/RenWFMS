package com.sysu.workflow.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.*;

/**
 * Created with IntelliJ IDEA
 * Date: 2015/9/17
 * Time: 13:16
 * User: ThinerZQ
 * GitHub: <a>https://github.com/ThinerZQ</a>
 * Blog: <a>http://blog.csdn.net/c601097836</a>
 * Email: 601097836@qq.com
 *
 */
@Entity
@Table(name = "t_user")
public class UserEntity implements WorkflowEntity {

    @Id
    @GeneratedValue(generator = "generator")
    @GenericGenerator(name = "generator", strategy = "identity")
    private long userId;

    @Basic
    private String userName;
    @Basic
    private String userRealName;
    @Basic
    private String userPassword;
    @Basic
    private String userAge;
    @Basic
    private String userGender;
    @Basic
    private String userEmail;
    @Basic
    private String userActivateCode;
    @Basic
    private String userStatus;
    @Temporal(TemporalType.TIMESTAMP)
    private Date userRegisterDate;

    @ManyToMany(fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    private Set<GroupEntity> groupEntitySet = new HashSet<GroupEntity>();

    @OneToMany(mappedBy = "itemAssigneeEntity", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<UserWorkItemEntity> workItemEntitySet = new HashSet<UserWorkItemEntity>();


    public UserEntity(String realname) {
        this.userRealName = realname;
    }

    public UserEntity() {
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public UserEntity setUserName(String userName) {
        this.userName = userName;
        return this;
    }

    public String getUserRealName() {
        return userRealName;
    }

    public UserEntity setUserRealName(String userRealName) {
        this.userRealName = userRealName;
        return this;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public UserEntity setUserPassword(String userPassword) {
        this.userPassword = userPassword;
        return this;
    }

    public String getUserAge() {
        return userAge;
    }

    public UserEntity setUserAge(String userAge) {
        this.userAge = userAge;
        return this;
    }

    public String getUserGender() {
        return userGender;
    }

    public UserEntity setUserGender(String userGender) {
        this.userGender = userGender;
        return this;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public UserEntity setUserEmail(String userEmail) {
        this.userEmail = userEmail;
        return this;
    }

    public String getUserActivateCode() {
        return userActivateCode;
    }

    public UserEntity setUserActivateCode(String userActivateCode) {
        this.userActivateCode = userActivateCode;
        return this;
    }

    public String getUserStatus() {
        return userStatus;
    }

    public UserEntity setUserStatus(String userStatus) {
        this.userStatus = userStatus;
        return this;
    }

    public Date getUserRegisterDate() {
        return userRegisterDate;
    }

    public UserEntity setUserRegisterDate(Date userRegisterDate) {
        this.userRegisterDate = userRegisterDate;
        return this;
    }

    public Set<GroupEntity> getGroupEntitySet() {
        return groupEntitySet;
    }

    public void setGroupEntitySet(Set<GroupEntity> groupEntitySet) {
        this.groupEntitySet = groupEntitySet;
    }


    @Override
    public String toString() {
        return "UserEntity{" +
                "userId=" + userId +
                ", userName='" + userName + '\'' +
                ", userRealName='" + userRealName + '\'' +
                ", userPassword='" + userPassword + '\'' +
                ", userAge='" + userAge + '\'' +
                ", userGender='" + userGender + '\'' +
                ", userEmail='" + userEmail + '\'' +
                ", userActivateCode='" + userActivateCode + '\'' +
                ", userStatus='" + userStatus + '\'' +
                ", userRegisterDate=" + userRegisterDate +
                ", groupEntitySet=" + groupEntitySet +
                ", workItemEntitySet=" + workItemEntitySet +
                '}';
    }

    public Map<String, Object> getNotNullPropertyMap() {
        Map<String, Object> map = new HashMap<String, Object>();

        if (getUserId() != 0) {
            map.put("userId", getUserId());
        }
        if (getUserRealName() != null) {
            map.put("userRealName", getUserRealName());
        }
        if (getUserAge() != null) {
            map.put("userAge", getUserAge());
        }
        if (getUserEmail() != null) {
            map.put("userEmail", getUserEmail());
        }
        if (getUserName() != null) {
            map.put("userName", getUserName());
        }
        if (getUserGender() != null) {
            map.put("userGender", getUserGender());
        }
        if (getUserPassword() != null) {
            map.put("userPassword", getUserPassword());
        }
        return map;
    }
}
