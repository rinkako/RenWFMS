#!/usr/bin/env python
# encoding: utf-8


class AuthorizationModel:
    def __init__(self):
        pass

    @staticmethod
    def Initialize(forced=False):
        """
        Initialize the logger.
        :param forced: forced reinitialize
        """
        if forced is False and AuthorizationModel._persistDAO is not None:
            return
        from DAO import MySQLDAO
        AuthorizationModel._persistDAO = MySQLDAO.MySQLDAO()
        AuthorizationModel._persistDAO.Initialize()

    @staticmethod
    def Dispose():
        """
        Dispose the resource of runtime logger.
        """
        if AuthorizationModel._persistDAO is not None:
            AuthorizationModel._persistDAO.Dispose()
        AuthorizationModel._persistDAO = None

    @staticmethod
    def Retrieve(username, domain, encryptedPassword):
        """
        Check if an auth user is valid.
        :param username: user id name
        :param domain: domain name
        :param encryptedPassword: password in encrypted string
        :return: true if user exists and password right
        """
        sql = "SELECT * FROM ren_authuser AS au WHERE au.username = '%s' AND au.domain = '%s' AND au.password = '%s' AND au.status = 0" \
              % (username, domain, encryptedPassword)
        ret = AuthorizationModel._persistDAO.ExecuteSQL(sql, True, 0)
        if len(ret) > 0:
            return ret[0]
        else:
            return None

    """
    Persist DAO reference
    """
    _persistDAO = None
