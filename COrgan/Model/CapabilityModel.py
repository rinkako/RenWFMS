#!/usr/bin/env python
# encoding: utf-8
"""
@module : CapabilityModel
@author : Rinkako
@time   : 2018/1/10
"""
import uuid
from Entity.Capability import Capability


class CapabilityModel:
    """
    Model Class: Data model operation for Capability resources
    """
    def __init__(self):
        pass

    @staticmethod
    def Initialize(forced=False):
        """
        Initialize the model.
        :param forced: forced reinitialize
        """
        if forced is False and CapabilityModel._persistDAO is not None:
            return
        from DAO import MySQLDAO
        CapabilityModel._persistDAO = MySQLDAO.MySQLDAO()
        CapabilityModel._persistDAO.Initialize()

    @staticmethod
    def Dispose():
        """
        Dispose the model.
        """
        if CapabilityModel._persistDAO is not None:
            CapabilityModel._persistDAO.Dispose()
        CapabilityModel._persistDAO = None

    @staticmethod
    def Add(name, description, note):
        """
        Add a group.
        :param name: capability unique name
        :param description: description text
        :param note: note text
        :return: if execution success
        """
        tpd = Capability(name, description, note)
        return CapabilityModel.AddPackage(tpd)

    @staticmethod
    def AddPackage(dp):
        """
        Add a capability by its data package.
        :param dp: Group instance
        :return: uid
        """
        assert isinstance(dp, Capability)
        uid = "Capa_%s" % uuid.uuid1()
        sql = "INSERT INTO ren_capability(id, name, description, note) " \
              "VALUES ('%s', '%s', '%s', '%s')" % \
              (uid, dp.Name, dp.Description, dp.Note)
        CapabilityModel._persistDAO.ExecuteSQL(sql, needRet=False)
        return uid

    @staticmethod
    def Remove(name):
        """
        Remove a capability.
        :param name: capability id name
        """
        rObj = CapabilityModel.Retrieve(name)
        if rObj is None:
            return False
        sql = "DELETE FROM ren_capability WHERE name = '%s'" % name
        CapabilityModel._persistDAO.ExecuteSQL(sql, needRet=False)
        sql = "DELETE FROM ren_connect WHERE belongToOrganizableId = '%s'" % rObj.GlobalId
        CapabilityModel._persistDAO.ExecuteSQL(sql, needRet=False)
        return True

    @staticmethod
    def Retrieve(name):
        """
        Retrieve a capability by its name.
        :param name: unique name
        :return: Capability instance
        """
        sql = "SELECT * FROM ren_capability WHERE name = '%s'" % name
        ret = CapabilityModel._persistDAO.ExecuteSQL(sql, needRet=True)
        if len(ret) > 0:
            rd = CapabilityModel._dispatchRetObj(ret[0])
            rd.GlobalId = ret[0]["id"]
            return rd
        else:
            return None

    @staticmethod
    def RetrieveAll():
        """
        Retrieve all capabilities.
        :return: a list of Capability instance
        """
        sql = "SELECT * FROM ren_capability"
        ret = CapabilityModel._persistDAO.ExecuteSQL(sql, needRet=True)
        rList = []
        for r in ret:
            rd = CapabilityModel._dispatchRetObj(r)
            rd.GlobalId = r["id"]
            rList.append(rd)
        return rList

    @staticmethod
    def Contains(name):
        # type: (str) -> bool
        """
        Check if a capability exists.
        :param name: group unique name
        :return: bool of existence
        """
        return CapabilityModel.Retrieve(name) is not None

    @staticmethod
    def Update(name, **kwargs):
        """
        Update fields of capability.
        :param name: capability unique id
        :param kwargs: update field and its new value, string should warp `'` by caller
        """
        if len(kwargs) == 0:
            return
        sqlBuilder = "UPDATE ren_capability SET "
        kBuilder = ""
        for k in kwargs:
            kBuilder += "%s = %s, " % (k, kwargs[k])
        kBuilder = kBuilder[0:len(kBuilder) - 2]
        sqlBuilder += kBuilder
        sqlBuilder += " WHERE name = '%s'" % name
        CapabilityModel._persistDAO.ExecuteSQL(sqlBuilder, needRet=False)

    @staticmethod
    def GetGlobalId(name):
        """
        Get global id of a capability.
        :param name: capability name
        :return: capability global id
        """
        sql = "SELECT id, name FROM ren_group WHERE name = '%s'" % name
        ret = CapabilityModel._persistDAO.ExecuteSQL(sql, needRet=True)
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
        sql = "SELECT * FROM ren_capability WHERE id = '%s'" % gid
        ret = CapabilityModel._persistDAO.ExecuteSQL(sql, needRet=True)
        if len(ret) > 0:
            rd = CapabilityModel._dispatchRetObj(ret[0])
            rd.GlobalId = ret[0]["id"]
            return rd
        else:
            return None

    @staticmethod
    def _dispatchRetObj(retObj):
        """
        Dispatch retObj dictionary to Capability instance.
        :param retObj: a dictionary of capability package
        :return: Capability instance
        """
        assert retObj is not None
        return Capability(retObj["name"], retObj["description"], retObj["note"])

    """
    Persist DAO
    """
    _persistDAO = None
