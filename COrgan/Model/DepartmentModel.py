#!/usr/bin/env python
# encoding: utf-8
"""
@module : DepartmentModel
@author : Rinkako
@time   : 2018/1/2
"""
import uuid
from Entity.Department import Department


class DepartmentModel:
    """
    Model Class: Data model operation for Department resources
    """
    def __init__(self):
        pass

    @staticmethod
    def Initialize():
        """
        Initialize the model.
        """
        from DAO import MySQLDAO
        DepartmentModel._persistDAO = MySQLDAO.MySQLDAO()
        DepartmentModel._persistDAO.Initialize()

    @staticmethod
    def Dispose():
        """
        Dispose the model.
        """
        if DepartmentModel._persistDAO is not None:
            DepartmentModel._persistDAO.Dispose()
        DepartmentModel._persistDAO = None

    @staticmethod
    def Add(name, description, note, belongToDepartmentName):
        tpd = Department(name, description, note, belongToDepartmentName)
        return DepartmentModel.AddPackage(tpd)

    @staticmethod
    def AddPackage(dp):
        assert isinstance(dp, Department)
        uid = "Dept_%s_%s" % (dp.Name, uuid.uuid1())
        sql = "INSERT INTO ren_department(id, name, description, note, belongTo) " \
              "VALUES ('%s', '%s', '%s', '%s', '%s')" % \
              (uid, dp.Name, dp.Description, dp.Note, dp.BelongToDepartment)
        return DepartmentModel._persistDAO.ExecuteSQL(sql, needRet=True)

    @staticmethod
    def Remove(name):
        rObj = DepartmentModel.Retrieve(name)
        if rObj is None:
            return False
        sql = "DELETE FROM ren_department WHERE name = '%s'" % name
        DepartmentModel._persistDAO.ExecuteSQL(sql, needRet=False)
        sql = "DELETE FROM ren_connect WHERE belongToGroupId = '%s'" % rObj["id"]
        DepartmentModel._persistDAO.ExecuteSQL(sql, needRet=False)

    @staticmethod
    def Retrieve(name):
        """
        Retrieve a department by its name.
        :param name: unique name
        :return: Department instance
        """
        sql = "SELECT * FROM ren_department WHERE name = '%s'" % name
        ret = DepartmentModel._persistDAO.ExecuteSQL(sql, needRet=True)
        if len(ret) > 0:
            rd = DepartmentModel._dispatchRetObj(ret[0])
            rd.GlobalId = ret[0]["id"]
            return rd
        else:
            return None

    @staticmethod
    def RetrieveAll():
        """
        Retrieve all departments.
        :return: a list of Department instance
        """
        sql = "SELECT * FROM ren_department"
        ret = DepartmentModel._persistDAO.ExecuteSQL(sql, needRet=True)
        rList = []
        for r in ret:
            rd = DepartmentModel._dispatchRetObj(r)
            rd.GlobalId = r["id"]
            rList.append(rd)
        return rList

    @staticmethod
    def Contains(name):
        # type: (str) -> bool
        """
        Check if a department exists.
        :param name: department unique name
        :return: bool of existence
        """
        return DepartmentModel.Retrieve(name) is not None

    @staticmethod
    def Update(name, **kwargs):
        """
        Update fields of department.
        :param name: department unique id
        :param kwargs: update field and its new value, string should warp `'` by caller
        """
        if len(kwargs) == 0:
            return
        sqlBuilder = "UPDATE ren_department SET "
        kBuilder = ""
        for k in kwargs:
            kBuilder += "%s = %s, " % (k, kwargs[k])
        kBuilder = kBuilder[0:len(kBuilder) - 2]
        sqlBuilder += kBuilder
        sqlBuilder += " WHERE name = '%s'" % name
        DepartmentModel._persistDAO.ExecuteSQL(sqlBuilder, needRet=False)

    @staticmethod
    def _dispatchRetObj(retObj):
        """
        Dispatch retObj dictionary to Department instance.
        :param retObj: a dictionary of department package
        :return: Department instance
        """
        assert retObj is not None
        return Department(retObj["name"], retObj["description"], retObj["note"], retObj["belongToId"])

    """
    Persist DAO
    """
    _persistDAO = None
