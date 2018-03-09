#!/usr/bin/env python
# encoding: utf-8
"""
@module : AgentModel
@author : Rinkako
@time   : 2018/1/2
"""
import uuid
from Entity.Agent import Agent


class AgentModel:
    """
    Model Class: Data model operation for Agent resources
    """
    def __init__(self):
        pass

    @staticmethod
    def Initialize(forced=False):
        """
        Initialize the model.
        :param forced: forced reinitialize
        """
        if forced is False and AgentModel._persistDAO is not None:
            return
        from DAO import MySQLDAO
        AgentModel._persistDAO = MySQLDAO.MySQLDAO()
        AgentModel._persistDAO.Initialize()

    @staticmethod
    def Dispose():
        """
        Dispose the model.
        """
        if AgentModel._persistDAO is not None:
            AgentModel._persistDAO.Dispose()
        AgentModel._persistDAO = None

    @staticmethod
    def Add(name, location, note, rType):
        """
        Add a agent to steady.
        :param name: agent name
        :param location: agent location, usually URL
        :param note: description
        :param rType: reentrant type
        :return Have operation successfully finished
        """
        thp = Agent(name, location, note, rType)
        return AgentModel.AddByPackage(thp)

    @staticmethod
    def AddByPackage(ap):
        """
        Add an agent by Agent package data.
        :param ap: agent instance
        :return: uid
        """
        assert isinstance(ap, Agent)
        uid = "Agent_%s" % uuid.uuid1()
        sql = "INSERT INTO ren_agent(id, name, location, type, note) VALUES " \
              "('%s', '%s', '%s', %s, '%s')" % (uid, ap.Name, ap.Location, ap.Type, ap.Note)
        AgentModel._persistDAO.ExecuteSQL(sql, needRet=False)
        return uid

    @staticmethod
    def Contains(agentName):
        """
        Check if an agent already existed.
        :param agentName: agent unique name to be checked
        :return: boolean of existence
        """
        return AgentModel.Retrieve(agentName) is not None

    @staticmethod
    def Remove(agentName):
        """
        Remove
        :param agentName: agent unique name to be removed
        """
        rObj = AgentModel.Retrieve(agentName)
        if rObj is None:
            return False
        sql = "DELETE FROM ren_agent WHERE name = '%s'" % agentName
        AgentModel._persistDAO.ExecuteSQL(sql, needRet=False)
        sql = "DELETE FROM ren_connect WHERE workerId = '%s'" % rObj.GlobalId
        AgentModel._persistDAO.ExecuteSQL(sql, needRet=False)
        return True

    @staticmethod
    def Update(agentName, **kwargs):
        """
        Update fields of agent.
        :param agentName: agent unique name
        :param kwargs: update field and its new value, string should warp `'` by caller
        """
        if len(kwargs) == 0:
            return
        sqlBuilder = "UPDATE ren_agent SET "
        kBuilder = ""
        for k in kwargs:
            kBuilder += "%s = %s, " % (k, kwargs[k])
        kBuilder = kBuilder[0:len(kBuilder) - 2]
        sqlBuilder += kBuilder
        sqlBuilder += " WHERE name = '%s'" % agentName
        AgentModel._persistDAO.ExecuteSQL(sqlBuilder, needRet=False)

    @staticmethod
    def RetrieveAll():
        """
        Retrieve all agent.
        :return: Agent instance in list
        """
        sql = "SELECT * FROM ren_agent"
        ret = AgentModel._persistDAO.ExecuteSQL(sql, needRet=True)
        retList = []
        for r in ret:
            hm = AgentModel._dispatchRetObj(r)
            retList.append(hm)
        return retList

    @staticmethod
    def Retrieve(agentName):
        """
        Retrieve a agent by its name.
        :param agentName: unique id name
        :return: Agent instance
        """
        sql = "SELECT * FROM ren_agent WHERE name = '%s'" % agentName
        ret = AgentModel._persistDAO.ExecuteSQL(sql, needRet=True)
        if len(ret) > 0:
            return AgentModel._dispatchRetObj(ret[0])
        else:
            return None

    @staticmethod
    def RetrieveByGlobalId(gId):
        """
        Retrieve a agent by its global id.
        :param gId: global id
        :return: Agent instance
        """
        sql = "SELECT * FROM ren_agent WHERE id = '%s'" % gId
        ret = AgentModel._persistDAO.ExecuteSQL(sql, needRet=True)
        if len(ret) > 0:
            return AgentModel._dispatchRetObj(ret[0])
        else:
            return None

    @staticmethod
    def GetGlobalId(name):
        """
        Get global id of a agent.
        :param name: agent name
        :return: agent global id
        """
        sql = "SELECT id, name FROM ren_agent WHERE name = '%s'" % name
        ret = AgentModel._persistDAO.ExecuteSQL(sql, needRet=True)
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
        sql = "SELECT * FROM ren_agent WHERE id = '%s'" % gid
        ret = AgentModel._persistDAO.ExecuteSQL(sql, needRet=True)
        if len(ret) > 0:
            rd = AgentModel._dispatchRetObj(ret[0])
            rd.GlobalId = ret[0]["id"]
            return rd
        else:
            return None

    @staticmethod
    def _dispatchRetObj(retObj):
        """
        Dispatch retObj dictionary to Agent instance.
        :param retObj: a dictionary of agent package
        :return: Agent instance
        """
        assert retObj is not None
        retAgent = Agent(retObj["name"], retObj["location"], retObj["note"], retObj["type"])
        retAgent.GlobalId = retObj["id"]
        return retAgent

    _persistDAO = None
