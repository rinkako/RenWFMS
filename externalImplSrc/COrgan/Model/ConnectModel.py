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
    def Initialize(forced=False):
        """
        Initialize the model.
        :param forced: forced reinitialize
        """
        if forced is False and ConnectModel._persistDAO is not None:
            return
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
        sql = "INSERT INTO ren_connect(workerId, belongToOrganizableId) VALUES ('%s', '%s')" % \
              (workerId, groupId)
        return ConnectModel._persistDAO.ExecuteSQL(sql, needRet=False)

    @staticmethod
    def Remove(workerId, groupId):
        """
        Remove a connection.
        :param workerId: worker id
        :param groupId: organizable id
        """
        sql = "DELETE FROM ren_connect WHERE workerId = '%s' AND belongToOrganizableId = '%s'" % \
              (workerId, groupId)
        ConnectModel._persistDAO.ExecuteSQL(sql, needRet=False)

    @staticmethod
    def RemoveByWorker(workerId):
        """
        Remove connections of worker.
        :param workerId: worker id
        """
        sql = "DELETE FROM ren_connect WHERE workerId = '%s'" % workerId
        ConnectModel._persistDAO.ExecuteSQL(sql, needRet=False)

    @staticmethod
    def RetrieveByWorkerGlobalId(workerId):
        """
        Retrieve connections into a list by worker id.
        :param workerId: worker id
        :return: a list of connection dictionary
        """
        sql = "SELECT workerId, belongToOrganizableId FROM ren_connect WHERE workerId = '%s'" % workerId
        return ConnectModel._persistDAO.ExecuteSQL(sql, needRet=True)

    @staticmethod
    def RetrieveByOrganizableGlobalId(groupId):
        """
        Retrieve connections into a list by group id.
        :param groupId: organizable id
        :return: a list of connection dictionary
        """
        sql = "SELECT workerId, belongToOrganizableId FROM ren_connect WHERE belongToOrganizableId = '%s'" % groupId
        return ConnectModel._persistDAO.ExecuteSQL(sql, needRet=True)

    @staticmethod
    def RetrieveHumanByGlobalId(gid):
        """
        Retrieve connections joined table by group global id.
        :param gid:
        :return:
        """
        sql = "SELECT * FROM ren_connect INNER JOIN ren_human " \
              "ON ren_human.id = ren_connect.workerId " \
              "WHERE ren_connect.belongToOrganizableId = '%s'" % gid
        return ConnectModel._persistDAO.ExecuteSQL(sql, needRet=True)

    @staticmethod
    def RetrieveAgentByGlobalId(gid):
        """
        Retrieve connections joined table by group global id.
        :param gid:
        :return:
        """
        sql = "SELECT * FROM ren_connect INNER JOIN ren_agent " \
              "ON ren_agent.id = ren_connect.workerId " \
              "WHERE ren_connect.belongToOrganizableId = '%s'" % gid
        return ConnectModel._persistDAO.ExecuteSQL(sql, needRet=True)

    @staticmethod
    def RetrieveAll():
        """
        Retrieve all connections into a list.
        :return: a list of connection dictionary
        """
        sql = "SELECT workerId, belongToOrganizableId FROM ren_connect"
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
            sql = "INSERT INTO ren_connect(workerId, belongToOrganizableId) VALUES ('%s', '%s')" % \
                  (p["workerId"], p["belongToGroupId"])
            ConnectModel._persistDAO.ExecuteSQL(sql, needRet=True)

    @staticmethod
    def UpdateConnectForGroup(groupId, kvp):
        """
        Update connections of a group by a list of key-value pair.
        :param groupId: worker unique id name
        :param kvp: list of dict
        """
        sql = "DELETE * FROM ren_connect WHERE belongToOrganizableId = '%s'" % groupId
        ConnectModel._persistDAO.ExecuteSQL(sql, needRet=False)
        for p in kvp:
            sql = "INSERT INTO ren_connect(workerId, belongToOrganizableId) VALUES ('%s', '%s')" % \
                  (p["workerId"], p["belongToGroupId"])
            ConnectModel._persistDAO.ExecuteSQL(sql, needRet=True)

    """
    Persist DAO
    """
    _persistDAO = None
