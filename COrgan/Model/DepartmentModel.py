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
        tpd = Department()


    @staticmethod
    def AddPackage(dp):
        assert isinstance(dp, Department)
        uid = "Dept_%s_%s" % (dp.Name, uuid.uuid1())
        sql = "INSERT INTO ren_department(id, name, description, note, belongTo) " \
              "VALUES ('%s', '%s', '%s', '%s', '%s')" % \
              (uid, dp.Name, dp.Description, dp.Note, dp.BelongToDepartmentId)
        return DepartmentModel._persistDAO.ExecuteSQL(sql, needRet=True)

    _persistDAO = None
