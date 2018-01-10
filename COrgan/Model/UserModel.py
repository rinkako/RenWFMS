#!/usr/bin/env python
# encoding: utf-8
"""
@module : UserModel
@author : Rinkako
@time   : 2018/1/4
"""
import time


class UserModel:
    """
    Model Class: Data model operation for users in platform
    """

    def __init__(self):
        pass

    @staticmethod
    def Initialize(forced=False):
        """
        Initialize the model.
        :param forced: forced reinitialize
        """
        if forced is False and UserModel._persistDAO is not None:
            return
        from DAO import MySQLDAO
        UserModel._persistDAO = MySQLDAO.MySQLDAO()
        UserModel._persistDAO.Initialize()

    @staticmethod
    def Dispose():
        """
        Dispose the resource of model.
        """
        if UserModel._persistDAO is not None:
            UserModel._persistDAO.Dispose()
            UserModel._persistDAO = None

    @staticmethod
    def Add(username, password, level):
        """
        Add a new user to database
        :param username: new user name, should be unique
        :param password: user password with encryption
        :param level: user level flag
        :return Have operation successfully finished
        """
        t = time.strftime("%Y-%m-%d %H:%M:%S", time.localtime(time.time()))
        sql = """INSERT INTO ren_user (username, password, level, state, createtimestamp) 
                 VALUES ('%s', '%s', %s, %s, '%s')""" % (username, password, level, 0, t)
        return UserModel._persistDAO.ExecuteSQL(sql, needRet=False)

    @staticmethod
    def Delete(username):
        """
        Remove a user from database
        :param username: user's name to be removed
        :return Have operation successfully finished
        """
        sql = "UPDATE ren_user SET state = 1 WHERE username = '%s'" % username
        return UserModel._persistDAO.ExecuteSQL(sql, needRet=True)

    @staticmethod
    def Update(username, new_encrypted_password, new_level):
        """
        Update a exist user's information in database
        :param username: user's name to be updated
        :param new_encrypted_password: new password with encryption, empty if not change
        :param new_level: new level flag value, empty if not change
        :return Have operation successfully finished
        """
        if new_encrypted_password is None and new_level is None:
            return True
        sqlBuilder = "UPDATE ren_user SET"
        if new_encrypted_password is not None:
            sqlBuilder += " password = '%s'," % new_encrypted_password
        if new_level is not None:
            sqlBuilder += " level = %s," % new_level
        if sqlBuilder[len(sqlBuilder) - 1] == ',':
            sqlBuilder = sqlBuilder[0: len(sqlBuilder) - 1]
        sqlBuilder += " WHERE username = '%s'" % username
        return UserModel._persistDAO.ExecuteSQL(sqlBuilder, needRet=True)

    @staticmethod
    def Retrieve(username):
        """
        Retrieve a exist user's information in database
        :param username: user's name to be retrieve
        :return Have operation successfully finished & User information dictionary (None if failed)
        """
        sql = "SELECT * FROM ren_user WHERE username = '%s'" % username
        ret = UserModel._persistDAO.ExecuteSQL(sql, needRet=True)
        if len(ret) > 0:
            return ret[0]
        else:
            return None

    @staticmethod
    def RetrieveAllValid():
        """
        Retrieve all valid user's information in database
        :return a list with entry of user's information dictionary
        """
        sql = "SELECT * FROM ren_user WHERE state = 0"
        return UserModel._persistDAO.ExecuteSQL(sql, needRet=True)

    @staticmethod
    def RetrieveAll():
        """
        Retrieve all user's information in database
        :return a list with entry of user's information dictionary
        """
        sql = "SELECT * FROM ren_user"
        return UserModel._persistDAO.ExecuteSQL(sql, needRet=True)

    @staticmethod
    def Verify(username, password):
        """
        Verify a user login
        :param username: user's name to login
        :param password: user's password with encryption
        :return: Can the verification pass
        """
        sql = "SELECT * FROM ren_user WHERE username = '%s' AND password = '%s' AND state = 0" % (username, password)
        ret = UserModel._persistDAO.ExecuteSQL(sql, needRet=True)
        return True if len(ret) > 0 else False

    """
    Persist DAO
    """
    _persistDAO = None
