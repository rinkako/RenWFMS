#!/usr/bin/env python
# encoding: utf-8
"""
@module : HumanModel
@author : Rinkako
@time   : 2017/12/28
"""
import uuid
from Entity.Human import Human


class HumanModel:
    def __init__(self):
        pass

    @staticmethod
    def Initialize():
        """
        Initialize the model.
        """
        from DAO import MySQLDAO
        HumanModel._persistDAO = MySQLDAO.MySQLDAO()
        HumanModel._persistDAO.Initialize()

    @staticmethod
    def Dispose():
        """
        Dispose the model.
        """
        if HumanModel._persistDAO is not None:
            HumanModel._persistDAO.Dispose()
        HumanModel._persistDAO = None

    @staticmethod
    def Add(personId, firstName, lastName, note):
        """
        Add a human to steady.
        :param personId: person unique id
        :param firstName: first name
        :param lastName: last name
        :param note: description
        :return Have operation successfully finished
        """
        thp = Human(personId, firstName, lastName, note)
        return HumanModel.AddByPackage(thp)

    @staticmethod
    def AddByPackage(hp):
        assert isinstance(hp, Human)
        uid = "Human_%s_%s" % (hp.Id, uuid.uuid1())
        sql = "INSERT INTO ren_human(id, person_id, firstname, lastname, note) VALUES " \
              "('%s', '%s', '%s', '%s', '%s')" % (uid, hp.Id, hp.FirstName, hp.LastName, hp.Note)
        return HumanModel._persistDAO.ExecuteSQL(sql, needRet=True)

    @staticmethod
    def Contains(personId):
        """
        Check if personId already existed.
        :param personId: person id to be checked
        :return: boolean of existence
        """
        return HumanModel.Retrieve(personId) is not None

    @staticmethod
    def Remove(personId):
        """
        Remove
        :param personId:
        :return:
        """
        sql = "DELETE FROM ren_human WHERE person_id = '%s'" % personId
        HumanModel._persistDAO.ExecuteSQL(sql, needRet=False)

    @staticmethod
    def Update(personId, **kwargs):
        """
        Update fields of human.
        :param personId: human unique id
        :param kwargs: update field and its new value, string should warp `'` by caller
        """
        if len(kwargs) == 0:
            return
        sqlBuilder = "UPDATE ren_human SET "
        kBuilder = ""
        for k in kwargs:
            kBuilder += "%s = %s, " % (k, kwargs[k])
        kBuilder = kBuilder[0:len(kBuilder) - 2]
        sqlBuilder += kBuilder
        sqlBuilder += " WHERE wid = '%s'" % personId
        HumanModel._persistDAO.ExecuteSQL(sqlBuilder, needRet=False)

    @staticmethod
    def RetrieveAll():
        """
        Retrieve all human.
        :return: Human instance in list
        """
        sql = "SELECT * FROM ren_human"
        ret = HumanModel._persistDAO.ExecuteSQL(sql, needRet=True)
        retList = []
        for r in ret:
            hm = HumanModel._dispatchRetObj(r)
            retList.append(hm)
        return retList

    @staticmethod
    def Retrieve(personId):
        """
        Retrieve a human by its person id.
        :param personId: unique id
        :return: Human instance
        """
        sql = "SELECT * FROM ren_human WHERE person_id = '%s'" % personId
        ret = HumanModel._persistDAO.ExecuteSQL(sql, needRet=True)
        if len(ret) > 0:
            return HumanModel._dispatchRetObj(ret[0])
        else:
            return None

    @staticmethod
    def RetrieveByGlobalId(gId):
        """
        Retrieve a human by its global id.
        :param gId: global id
        :return: Human instance
        """
        sql = "SELECT * FROM ren_human WHERE id = '%s'" % gId
        ret = HumanModel._persistDAO.ExecuteSQL(sql, needRet=True)
        if len(ret) > 0:
            return HumanModel._dispatchRetObj(ret[0])
        else:
            return None

    @staticmethod
    def _dispatchRetObj(retObj):
        """
        Dispatch retObj dictionary to Human instance.
        :param retObj: a dictionary of human package
        :return: Human instance
        """
        assert retObj is not None
        retHuman = Human(retObj["person_id"], retObj["firstname"], retObj["lastname"], retObj["note"])
        x = retObj["positions"].split(',')
        for t in x:
            retHuman.AddPosition(t)
        x = retObj["departments"].split(',')
        for t in x:
            retHuman.AddDepartment(t)
        x = retObj["capabilities"].split(',')
        for t in x:
            retHuman.AddCapability(t)
        x = retObj["privileges"].split(',')
        for t in x:
            retHuman.AddPrivilege(t)
        return retHuman

    _persistDAO = None
