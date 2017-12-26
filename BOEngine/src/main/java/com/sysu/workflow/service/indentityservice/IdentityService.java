package com.sysu.workflow.service.indentityservice;

import com.sysu.workflow.entity.GroupEntity;
import com.sysu.workflow.entity.UserEntity;

/**
 * Created with IntelliJ IDEA
 * Date: 2015/12/14
 * Time: 13:16
 * User: ThinerZQ
 * GitHub: <a>https://github.com/ThinerZQ</a>
 * Blog: <a>http://blog.csdn.net/c601097836</a>
 * Email: 601097836@qq.com
 *
 */
public class IdentityService {

    UserDao userDao = null;
    GroupDao groupDao = null;

    public static UserQuery createUserQuery() {

        return new UserQuery();
    }

    public static GroupQuery createGroupQuery() {

        return new GroupQuery();
    }

    public UserEntity newUser(String realname) {
        return new UserEntity(realname);
    }

    public UserEntity newUser(){
        return new UserEntity();
    }

    public boolean saveUser(UserEntity userEntity) {
        if (userDao == null) {
            userDao = new UserDao();
        }
        return userDao.addUsers(userEntity);
    }

    public boolean checkUser(UserEntity userEntity) {
        if (userDao == null) {
            userDao = new UserDao();
        }
        return userDao.checkUser(userEntity.getUserEmail(), userEntity.getUserPassword());
    }

    public boolean delete() {
        return false;
    }

    public boolean update() {
        return false;
    }

    public GroupEntity newGroup(String name) {
        return new GroupEntity(name);
    }

    public boolean saveGroup(GroupEntity group) {
        if (groupDao == null) {
            groupDao = new GroupDao();
        }
        return groupDao.addGroup(group);
    }

}
