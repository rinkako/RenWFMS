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
    """
    Model Class: Data model operation for human resources
    """
    def __init__(self):
        pass

    @staticmethod
    def Initialize(forced=False):
        """
        Initialize the model.
        :param forced: forced reinitialize
        """
        if forced is False and HumanModel._persistDAO is not None:
            return
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
        """
        Add a human by Human package data.
        :param hp: Human instance
        :return: uid
        """
        assert isinstance(hp, Human)
        uid = "Human_%s" % uuid.uuid1()
        sql = "INSERT INTO ren_human(id, person_id, firstname, lastname, note) VALUES " \
              "('%s', '%s', '%s', '%s', '%s')" % (uid, hp.PersonId, hp.FirstName, hp.LastName, hp.Note)
        HumanModel._persistDAO.ExecuteSQL(sql, needRet=False)
        return uid

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
        Remove a human and its connection.
        :param personId: human unique id
        """
        rObj = HumanModel.Retrieve(personId)
        if rObj is None:
            return False
        sql = "DELETE FROM ren_human WHERE person_id = '%s'" % personId
        HumanModel._persistDAO.ExecuteSQL(sql, needRet=False)
        sql = "DELETE FROM ren_connect WHERE workerId = '%s'" % rObj.GlobalId
        HumanModel._persistDAO.ExecuteSQL(sql, needRet=False)
        return True

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
        sqlBuilder += " WHERE person_id = '%s'" % personId
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
    def GetGlobalId(personId):
        """
        Get global id of a human.
        :param personId: human person id
        :return: human global id
        """
        sql = "SELECT id, person_id FROM ren_human WHERE person_id = '%s'" % personId
        ret = HumanModel._persistDAO.ExecuteSQL(sql, needRet=True)
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
        sql = "SELECT * FROM ren_human WHERE id = '%s'" % gid
        ret = HumanModel._persistDAO.ExecuteSQL(sql, needRet=True)
        if len(ret) > 0:
            rd = HumanModel._dispatchRetObj(ret[0])
            rd.GlobalId = ret[0]["id"]
            return rd
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
        retHuman.GlobalId = retObj["id"]
        return retHuman

    """
    Persist DAO
    """
    _persistDAO = None
