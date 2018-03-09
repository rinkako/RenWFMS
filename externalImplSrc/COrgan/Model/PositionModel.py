#!/usr/bin/env python
# encoding: utf-8
"""
@module : PositionModel
@author : Rinkako
@time   : 2018/1/4
"""
import uuid
from Entity.Position import Position


class PositionModel:
    """
    Model Class: Data model operation for Position resources
    """
    def __init__(self):
        pass

    @staticmethod
    def Initialize(forced=False):
        """
        Initialize the model.
        :param forced: forced reinitialize
        """
        if forced is False and PositionModel._persistDAO is not None:
            return
        from DAO import MySQLDAO
        PositionModel._persistDAO = MySQLDAO.MySQLDAO()
        PositionModel._persistDAO.Initialize()

    @staticmethod
    def Dispose():
        """
        Dispose the model.
        """
        if PositionModel._persistDAO is not None:
            PositionModel._persistDAO.Dispose()
        PositionModel._persistDAO = None

    @staticmethod
    def Add(name, description, note, belongToDepartmentId, reportToPositionId):
        """
        Add a position.
        :param name: position unique name
        :param description: description text
        :param note: note text
        :param belongToDepartmentId: belong to department global id
        :param reportToPositionId: report to position global id
        :return: if execution success
        """
        tpd = Position(name, description, note, belongToDepartmentId, reportToPositionId)
        return PositionModel.AddPackage(tpd)

    @staticmethod
    def AddPackage(dp):
        """
        Add a position by data package.
        :param dp: Position instance
        :return: uid
        """
        assert isinstance(dp, Position)
        uid = "Pos_%s" % uuid.uuid1()
        sql = "INSERT INTO ren_position(id, name, description, note, belongToId, reportToId) " \
              "VALUES ('%s', '%s', '%s', '%s', '%s', '%s')" % \
              (uid, dp.Name, dp.Description, dp.Note, dp.BelongToGroup, dp.ReportToPosition)
        PositionModel._persistDAO.ExecuteSQL(sql, needRet=False)
        return uid

    @staticmethod
    def Remove(name):
        """
        Remove a position.
        :param name: position id name
        """
        rObj = PositionModel.Retrieve(name)
        if rObj is None:
            return False
        sql = "DELETE FROM ren_position WHERE name = '%s'" % name
        PositionModel._persistDAO.ExecuteSQL(sql, needRet=False)
        sql = "DELETE FROM ren_connect WHERE belongToOrganizableId = '%s'" % rObj.GlobalId
        PositionModel._persistDAO.ExecuteSQL(sql, needRet=False)
        return True

    @staticmethod
    def Retrieve(name):
        """
        Retrieve a position by its name.
        :param name: unique name
        :return: Position instance
        """
        sql = "SELECT * FROM ren_position WHERE name = '%s'" % name
        ret = PositionModel._persistDAO.ExecuteSQL(sql, needRet=True)
        if len(ret) > 0:
            rd = PositionModel._dispatchRetObj(ret[0])
            rd.GlobalId = ret[0]["id"]
            return rd
        else:
            return None

    @staticmethod
    def RetrieveAll():
        """
        Retrieve all positions.
        :return: a list of Position instance
        """
        sql = "SELECT * FROM ren_position"
        ret = PositionModel._persistDAO.ExecuteSQL(sql, needRet=True)
        rList = []
        for r in ret:
            rd = PositionModel._dispatchRetObj(r)
            rd.GlobalId = r["id"]
            rList.append(rd)
        return rList

    @staticmethod
    def Contains(name):
        # type: (str) -> bool
        """
        Check if a position exists.
        :param name: position unique name
        :return: bool of existence
        """
        return PositionModel.Retrieve(name) is not None

    @staticmethod
    def Update(name, **kwargs):
        """
        Update fields of position.
        :param name: position unique id
        :param kwargs: update field and its new value, string should warp `'` by caller
        """
        if len(kwargs) == 0:
            return
        sqlBuilder = "UPDATE ren_position SET "
        kBuilder = ""
        for k in kwargs:
            kBuilder += "%s = %s, " % (k, kwargs[k])
        kBuilder = kBuilder[0:len(kBuilder) - 2]
        sqlBuilder += kBuilder
        sqlBuilder += " WHERE name = '%s'" % name
        PositionModel._persistDAO.ExecuteSQL(sqlBuilder, needRet=False)

    @staticmethod
    def GetGlobalId(name):
        """
        Get global id of a position.
        :param name: position name
        :return: position global id
        """
        sql = "SELECT id, name FROM ren_position WHERE name = '%s'" % name
        ret = PositionModel._persistDAO.ExecuteSQL(sql, needRet=True)
        if len(ret) > 0:
            return ret[0]["id"]
        else:
            return None

    @staticmethod
    def GetByGlobalId(gid):
        """
        Get package by global id.
        :param gid: global id
        :return: package instance
        """
        sql = "SELECT * FROM ren_position WHERE id = '%s'" % gid
        ret = PositionModel._persistDAO.ExecuteSQL(sql, needRet=True)
        if len(ret) > 0:
            rd = PositionModel._dispatchRetObj(ret[0])
            rd.GlobalId = ret[0]["id"]
            return rd
        else:
            return None

    @staticmethod
    def _dispatchRetObj(retObj):
        """
        Dispatch retObj dictionary to Position instance.
        :param retObj: a dictionary of position package
        :return: Position instance
        """
        assert retObj is not None
        return Position(retObj["name"], retObj["description"], retObj["note"], retObj["belongToId"], retObj["reportToId"])

    """
    Persist DAO
    """
    _persistDAO = None
