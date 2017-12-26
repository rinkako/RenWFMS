package com.sysu.workflow.service.indentityservice;

import com.sysu.workflow.entity.UserEntity;

import java.util.ArrayList;

/**
 * Created by zhengshouzi on 2015/12/14.
 */
public class UserQuery {

    private UserEntity user = null;
    private UserDao userDao = null;

    public UserQuery(UserEntity user) {
        this.user = user;
    }

    public UserQuery() {
        user = new UserEntity();
    }

    public UserQuery userRealName(String realname) {
        user.setUserRealName(realname);
        return this;
    }

    public UserQuery userId(int id) {
        user.setUserId(id);
        return this;
    }

    public UserEntity SingleResult() {
        if (userDao == null) {
            userDao = new UserDao();
        }
        ArrayList<UserEntity> arrayList = userDao.findUser(user);
        int size = arrayList.size();
        return size >= 1 ? arrayList.get(0) : null;
    }
}
