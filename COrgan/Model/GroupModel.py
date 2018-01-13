#!/usr/bin/env python
# encoding: utf-8
"""
@module : GroupModel
@author : Rinkako
@time   : 2018/1/2
"""
import uuid
from Entity.Group import Group


class GroupModel:
    """
    Model Class: Data model operation for Group resources
    """
    def __init__(self):
        pass

    @staticmethod
    def Initialize(forced=False):
        """
        Initialize the model.
        :param forced: forced reinitialize
        """
        if forced is False and GroupModel._persistDAO is not None:
            return
        from DAO import MySQLDAO
        GroupModel._persistDAO = MySQLDAO.MySQLDAO()
        GroupModel._persistDAO.Initialize()

    @staticmethod
    def Dispose():
        """
        Dispose the model.
        """
        if GroupModel._persistDAO is not None:
            GroupModel._persistDAO.Dispose()
        GroupModel._persistDAO = None

    @staticmethod
    def Add(name, description, note, belongToGroupId, groupType):
        """
        Add a group.
        :param name: group unique name
        :param description: description text
        :param note: note text
        :param belongToGroupId: belong to which group id
        :param groupType: group type enum
        :return: if execution success
        """
        tpd = Group(name, description, note, belongToGroupId, groupType)
        return GroupModel.AddPackage(tpd)

    @staticmethod
    def AddPackage(dp):
        """
        Add a group by its data package.
        :param dp: Group instance
        :return: global id
        """
        assert isinstance(dp, Group)
        uid = "Dept_%s" % uuid.uuid1()
        sql = "INSERT INTO ren_group(id, name, description, note, belongToId, groupType) " \
              "VALUES ('%s', '%s', '%s', '%s', '%s', %s)" % \
              (uid, dp.Name, dp.Description, dp.Note, dp.BelongToGroupId, dp.GroupType)
        GroupModel._persistDAO.ExecuteSQL(sql, needRet=False)
        return uid

    @staticmethod
    def Remove(name):
        """
        Remove a group.
        :param name: group id name
        """
        rObj = GroupModel.Retrieve(name)
        if rObj is None:
            return False
        sql = "DELETE FROM ren_group WHERE name = '%s'" % name
        GroupModel._persistDAO.ExecuteSQL(sql, needRet=False)
        sql = "DELETE FROM ren_connect WHERE belongToOrganizableId = '%s'" % rObj.GlobalId
        GroupModel._persistDAO.ExecuteSQL(sql, needRet=False)
        return True

    @staticmethod
    def Retrieve(name):
        """
        Retrieve a group by its name.
        :param name: unique name
        :return: Group instance
        """
        sql = "SELECT * FROM ren_group WHERE name = '%s'" % name
        ret = GroupModel._persistDAO.ExecuteSQL(sql, needRet=True)
        if len(ret) > 0:
            rd = GroupModel._dispatchRetObj(ret[0])
            rd.GlobalId = ret[0]["id"]
            return rd
        else:
            return None

    @staticmethod
    def RetrieveAll():
        """
        Retrieve all groups.
        :return: a list of Group instance
        """
        sql = "SELECT * FROM ren_group"
        ret = GroupModel._persistDAO.ExecuteSQL(sql, needRet=True)
        rList = []
        for r in ret:
            rd = GroupModel._dispatchRetObj(r)
            rd.GlobalId = r["id"]
            rList.append(rd)
        return rList

    @staticmethod
    def Contains(name):
        # type: (str) -> bool
        """
        Check if a group exists.
        :param name: group unique name
        :return: bool of existence
        """
        return GroupModel.Retrieve(name) is not None

    @staticmethod
    def Update(name, **kwargs):
        """
        Update fields of group.
        :param name: group unique id
        :param kwargs: update field and its new value, string should warp `'` by caller
        """
        if len(kwargs) == 0:
            return
        sqlBuilder = "UPDATE ren_group SET "
        kBuilder = ""
        for k in kwargs:
            kBuilder += "%s = %s, " % (k, kwargs[k])
        kBuilder = kBuilder[0:len(kBuilder) - 2]
        sqlBuilder += kBuilder
        sqlBuilder += " WHERE name = '%s'" % name
        GroupModel._persistDAO.ExecuteSQL(sqlBuilder, needRet=False)

    @staticmethod
    def GetGlobalId(name):
        """
        Get global id of a group.
        :param name: group name
        :return: group global id
        """
        sql = "SELECT id, name FROM ren_group WHERE name = '%s'" % name
        ret = GroupModel._persistDAO.ExecuteSQL(sql, needRet=True)
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
        sql = "SELECT * FROM ren_group WHERE id = '%s'" % gid
        ret = GroupModel._persistDAO.ExecuteSQL(sql, needRet=True)
        if len(ret) > 0:
            rd = GroupModel._dispatchRetObj(ret[0])
            rd.GlobalId = ret[0]["id"]
            return rd
        else:
            return None

    @staticmethod
    def _dispatchRetObj(retObj):
        """
        Dispatch retObj dictionary to Group instance.
        :param retObj: a dictionary of group package
        :return: Group instance
        """
        assert retObj is not None
        return Group(retObj["name"], retObj["description"], retObj["note"], retObj["belongToId"], retObj["groupType"])

    """
    Persist DAO
    """
    _persistDAO = None
