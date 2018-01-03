#!/usr/bin/env python
# encoding: utf-8
"""
@module : ConnectModel
@author : Rinkako
@time   : 2018/1/1
"""


class ConnectModel:
    """
    Model Class: Data model operation for connection constrain
    """
    def __init__(self):
        pass

    @staticmethod
    def Initialize():
        """
        Initialize the model.
        """
        from DAO import MySQLDAO
        ConnectModel._persistDAO = MySQLDAO.MySQLDAO()
        ConnectModel._persistDAO.Initialize()

    @staticmethod
    def Dispose():
        """
        Dispose the model.
        """
        if ConnectModel._persistDAO is not None:
            ConnectModel._persistDAO.Dispose()
        ConnectModel._persistDAO = None

    @staticmethod
    def Add(workerId, groupId):
        """
        Add a connection.
        :param workerId: worker id
        :param groupId: organizable id
        :return: execution result
        """
        sql = "INSERT INTO ren_connect(workerId, belongToGroupId) VALUES ('%s', '%s')" % \
              (workerId, groupId)
        return ConnectModel._persistDAO.ExecuteSQL(sql, needRet=True)

    @staticmethod
    def Remove(workerId, groupId):
        """
        Remove a connection.
        :param workerId: worker id
        :param groupId: organizable id
        :return: a list of connection dictionary
        """
        sql = "DELETE FROM ren_connect WHERE workerId = '%s' AND belongToGroupId = '%s'" % \
              (workerId, groupId)
        ConnectModel._persistDAO.ExecuteSQL(sql, needRet=False)

    @staticmethod
    def RetrieveByWorker(workerId):
        """
        Retrieve connections into a list by worker id.
        :param workerId: worker id
        :return: a list of connection dictionary
        """
        sql = "SELECT workerId, belongToGroupId FROM ren_connect WHERE workerId = '%s'" % workerId
        return ConnectModel._persistDAO.ExecuteSQL(sql, needRet=True)

    @staticmethod
    def RetrieveByGroup(groupId):
        """
        Retrieve connections into a list by group id.
        :param groupId: organizable id
        :return: a list of connection dictionary
        """
        sql = "SELECT workerId, belongToGroupId FROM ren_connect WHERE belongToGroupId = '%s'" % groupId
        return ConnectModel._persistDAO.ExecuteSQL(sql, needRet=True)

    @staticmethod
    def RetrieveAll():
        """
        Retrieve all connections into a list.
        :return: a list of connection dictionary
        """
        sql = "SELECT workerId, belongToGroupId FROM ren_connect"
        return ConnectModel._persistDAO.ExecuteSQL(sql, needRet=True)

    @staticmethod
    def UpdateConnectForWorker(workerId, kvp):
        """
        Update connections of a worker by a list of key-value pair.
        :param workerId: worker unique id name
        :param kvp: list of dict
        """
        sql = "DELETE * FROM ren_connect WHERE workerId = '%s'" % workerId
        ConnectModel._persistDAO.ExecuteSQL(sql, needRet=False)
        for p in kvp:
            sql = "INSERT INTO ren_connect(workerId, belongToGroupId) VALUES ('%s', '%s')" % \
                  (p["workerId"], p["belongToGroupId"])
            ConnectModel._persistDAO.ExecuteSQL(sql, needRet=True)

    @staticmethod
    def UpdateConnectForGroup(groupId, kvp):
        """
        Update connections of a group by a list of key-value pair.
        :param groupId: worker unique id name
        :param kvp: list of dict
        """
        sql = "DELETE * FROM ren_connect WHERE belongToGroupId = '%s'" % groupId
        ConnectModel._persistDAO.ExecuteSQL(sql, needRet=False)
        for p in kvp:
            sql = "INSERT INTO ren_connect(workerId, belongToGroupId) VALUES ('%s', '%s')" % \
                  (p["workerId"], p["belongToGroupId"])
            ConnectModel._persistDAO.ExecuteSQL(sql, needRet=True)

    _persistDAO = None
