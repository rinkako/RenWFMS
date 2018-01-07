#!/usr/bin/env python
# encoding: utf-8
"""
@module : CConfigModel
@author : Rinkako
@time   : 2018/1/7
"""


class CConfigModel:
    """
    Model Class: Data model operation for COrgan platform configuration
    """
    def __init__(self):
        pass

    @staticmethod
    def Initialize(forced=False):
        """
        Initialize the model.
        :param forced: forced reinitialize
        """
        if forced is False and CConfigModel._persistDAO is not None:
            return
        from DAO import MySQLDAO
        CConfigModel._persistDAO = MySQLDAO.MySQLDAO()
        CConfigModel._persistDAO.Initialize()

    @staticmethod
    def Dispose():
        """
        Dispose the model.
        """
        if CConfigModel._persistDAO is not None:
            CConfigModel._persistDAO.Dispose()
        CConfigModel._persistDAO = None

    @staticmethod
    def AddOrUpdate(key, value):
        """
        Add a configuration KVP.
        :param key: unique key name
        :param value: value text
        """
        if CConfigModel.Contains(key) is True:
            CConfigModel.Update(key, value)
        else:
            sql = "INSERT INTO ren_cconfig (rkey, rvalue) VALUES ('%s', '%s')" % (key, value)
            CConfigModel._persistDAO.ExecuteSQL(sql, needRet=False)

    @staticmethod
    def Remove(key):
        """
        Remove a configuration KVP.
        :param key: key name
        """
        sql = "DELETE FROM ren_cconfig WHERE rkey = '%s'" % key
        CConfigModel._persistDAO.ExecuteSQL(sql, needRet=False)

    @staticmethod
    def Retrieve(key):
        """
        Retrieve a config KVP by its key.
        :param key: key name
        :return: KVP
        """
        sql = "SELECT * FROM ren_cconfig WHERE rkey = '%s'" % key
        ret = CConfigModel._persistDAO.ExecuteSQL(sql, needRet=True)
        if len(ret) > 0:
            retObj = ret[0]
            return retObj["rvalue"]
        else:
            return None

    @staticmethod
    def RetrieveAll():
        """
        Retrieve all configuration KVP.
        :return: a list of KVPs
        """
        sql = "SELECT * FROM ren_cconfig"
        ret = CConfigModel._persistDAO.ExecuteSQL(sql, needRet=True)
        rList = []
        for r in ret:
            rList.append((r["rkey"], r["rvalue"]))
        return rList

    @staticmethod
    def Contains(key):
        # type: (str) -> bool
        """
        Check if a KVP exists.
        :param key: key name
        :return: bool of existence
        """
        return CConfigModel.Retrieve(key) is not None

    @staticmethod
    def Update(key, value):
        """
        Update value corresponding to a key.
        :param key: key name
        :param value: new value text
        """
        sql = "UPDATE ren_cconfig SET rvalue = '%s' WHERE rkey = '%s'" % (value, key)
        CConfigModel._persistDAO.ExecuteSQL(sql, needRet=False)

    """
    Persist DAO
    """
    _persistDAO = None
