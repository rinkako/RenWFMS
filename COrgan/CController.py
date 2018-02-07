#!/usr/bin/env python
# encoding: utf-8
"""
@module : CController
@author : Rinkako
@time   : 2018/1/4
"""
import GlobalConfigContext as GCC
from functools import wraps
from Model.AgentModel import AgentModel
from Model.CConfigModel import CConfigModel
from Model.CapabilityModel import CapabilityModel
from Model.ConnectModel import ConnectModel
from Model.GroupModel import GroupModel
from Model.HumanModel import HumanModel
from Model.PositionModel import PositionModel
from Model.RuntimeLogModel import RuntimeLogModel
from Model.UserModel import UserModel
from SessionManager import SessionManager
from Utility.LogUtil import LogUtil


"""
Warppers
"""


def authorizeRequireWarp(fn):
    """
    Decorator for session valid required.
    """
    @wraps(fn)
    def wrapper(self, session, *args, **kwargs):
        try:
            if SessionManager.Check(session) is True:
                return fn(self, session, *args, **kwargs)
            else:
                return False, CController.Unauthorized(session)
        except Exception as e:
            print "Exception in COrgan: %s" % str(e)
            return False, e
    return wrapper


def adminRequireWarp(fn):
    """
    Decorator for session admin valid required.
    """
    @wraps(fn)
    def wrapper(self, session, *args, **kwargs):
        try:
            if SessionManager.CheckAdmin(session) is True:
                return fn(self, session, *args, **kwargs)
            else:
                return False, CController.Unauthorized(session)
        except Exception as e:
            print "Exception in COrgan: %s" % str(e)
            return False, e
    return wrapper


def ExceptionWarp(fn):
    """
    Decorator for COrgan std exception.
    """
    @wraps(fn)
    def wrapper(*args, **kwargs):
        try:
            return fn(*args, **kwargs)
        except Exception as e:
            print "Exception in COrgan: %s" % str(e)
            return False, e
    return wrapper


# noinspection PyUnusedLocal
class CController:
    """
    This class performs the COrgan controller role. All service requests are
    passed to engine here both from Dashboard and RESTful API by LGateway, all
    service requests here are in a same view, means there no any concept of HTTP
    request, etc. This class is responsible for solving the request and give a
    response result to return immediately. It should be noticed that all return
    value of functions in CController are ALWAYS in this pattern:

        <NoExceptionRaisedFlag: bool, ExecutionResult: tuple>

    The first return value is a boolean, signals that whether any exception
    was occurred in executing. The second return value is a tuple, contains
    the specific result variable should be returned.
    Notice that there is a special case, when the SESSION of service request
    is invalid for the required service, the first return value will be set
    to FALSE, and the second return value is set to UNAUTHORIZED, a constant
    string, in GlobalConfigContext and logged to the runtime DB.
    """

    def __init__(self):
        """
        Create a new core controller.
        """
        CController._agentModel.Initialize()
        CController._humanModel.Initialize()
        CController._groupModel.Initialize()
        CController._positionModel.Initialize()
        CController._capabilityModel.Initialize()
        CController._connectModel.Initialize()
        CController._userModel.Initialize()
        CController._logModel.Initialize()
        CController._configModel.Initialize()
        # First run time init
        if CController._configModel.Retrieve(GCC.CONFIG_ORGANIZATION_ID_KEY) is None:
            import uuid
            CController._configModel.AddOrUpdate(GCC.CONFIG_ORGANIZATION_ID_KEY, "COrg_%s" % uuid.uuid1())

    """
    Authorization Methods 
    """
    @staticmethod
    def Connect(username, encrypted_password):
        """
        Connect to the engine.
        :param username: username to connect
        :param encrypted_password: password string with specific encryption
        """
        try:
            success_flag = UserModel.Verify(username, encrypted_password)
            if success_flag is False:
                return True, None
            session_id = SessionManager.Login(username, encrypted_password)
            return True, session_id
        except Exception as e:
            print "Exception in COrgan: %s" % str(e)
            return False, e

    @staticmethod
    def CheckConnect(session):
        """
        Check a session is valid.
        :param session: session id
        """
        try:
            return True, SessionManager.Check(session)
        except Exception as e:
            print "Exception in COrgan: %s" % str(e)
            return False, e

    @staticmethod
    def Disconnect(session):
        """
        Disconnect from the engine.
        :param session: session id
        """
        try:
            return True, SessionManager.Logout(session)
        except Exception as e:
            print "Exception in COrgan: %s" % str(e)
            return False, e

    """
    COrgan Platform User Management Methods
    """
    @adminRequireWarp
    def PlatformUserAdd(self, session, username, encrypted_password, level):
        """
        Add a platform user.
        :param session: session id
        :param username: new user's name
        :param encrypted_password: new user's password with encryption
        :param level: new user level flag
        """
        try:
            UserModel.Add(username, encrypted_password, level)
            return True, True
        except:
            return True, False

    @adminRequireWarp
    def PlatformUserRemove(self, session, username):
        """
        Remove a platform user.
        :param session: session id
        :param username: user's name to be removed
        """
        return True, UserModel.Delete(username)

    @authorizeRequireWarp
    def PlatformUserUpdate(self, session, username, new_encrypted_password, new_level):
        """
        Update a platform user.
        :param session: session id
        :param username: user's name to be updated
        :param new_encrypted_password: user's new password with encryption
        :param new_level: new level flag of user
        """
        return True, UserModel.Update(username, new_encrypted_password, new_level)

    @authorizeRequireWarp
    def PlatformUserGet(self, session, username):
        """
        Get a platform user.
        :param session: session id
        :param username: user's name to be retrieve
        """
        return True, UserModel.Retrieve(username)

    @adminRequireWarp
    def PlatformUserGetAll(self, session):
        """
        Get all platform user as a list.
        :param session: session id
        """
        return True, UserModel.RetrieveAllValid()

    """
    Worker Methods
    """
    @authorizeRequireWarp
    def AddHuman(self, session, personId, firstName, lastName, note):
        """
        Add a human resource to organization.
        :param session: session id
        :param personId: person unique id
        :param firstName: human first name text
        :param lastName: human last name text
        :param note: note text
        :return: execution state tuple
        """
        if CController._humanModel.Contains(personId) is True:
            return True, None
        return True, CController._humanModel.Add(personId, firstName, lastName, note)

    @authorizeRequireWarp
    def RemoveHuman(self, session, personId):
        """
        Remove a human resource from organization.
        :param session: session id
        :param personId: person unique id
        :return: execution state tuple
        """
        return True, CController._humanModel.Remove(personId)

    @authorizeRequireWarp
    def UpdateHuman(self, session, personId, **kwargs):
        """
        Update a human resource info in organization.
        :param session: session id
        :param personId: person unique id
        :param kwargs:
        :return: execution state tuple
        """
        CController._humanModel.Update(personId, **kwargs)
        return True, True

    @authorizeRequireWarp
    def RetrieveHuman(self, session, personId):
        """
        Get a human resource data package in organization by name.
        :param session: session id
        :param personId: person unique id
        :return: execution state tuple
        """
        return True, CController._humanModel.Retrieve(personId)

    @authorizeRequireWarp
    def RetrieveAllHuman(self, session):
        """
        Get all human resource data packages in organization.
        :param session: session id
        :return: execution state tuple
        """
        return True, CController._humanModel.RetrieveAll()

    @authorizeRequireWarp
    def RetrieveHumanById(self, session, gid):
        """
        Get a human resource data package in organization by global id.
        :param session: session id
        :param gid: global id
        :return: execution state tuple
        """
        return True, CController._humanModel.GetByGlobalId(gid)

    @authorizeRequireWarp
    def AddAgent(self, session, name, location, rType, note):
        """
        Add an agent resource to organization.
        :param session: session id
        :param name: agent unique name
        :param location: agent call location
        :param rType: reentrant type enum value
        :param note: note text
        :return: execution state tuple
        """
        if CController._agentModel.Contains(name) is True:
            return True, None
        return True, CController._agentModel.Add(name, location, note, rType)

    @authorizeRequireWarp
    def RemoveAgent(self, session, name):
        """
        Remove an agent resource from organization.
        :param session: session id
        :param name: agent unique name
        :return: execution state tuple
        """
        return True, CController._agentModel.Remove(name)

    @authorizeRequireWarp
    def UpdateAgent(self, session, name, **kwargs):
        """
        Update an agent resource info in organization.
        :param session: session id
        :param name: agent unique name
        :param kwargs: update key-value pair dictionary
        :return: execution state tuple
        """
        CController._agentModel.Update(name, **kwargs)
        return True, True

    @authorizeRequireWarp
    def RetrieveAgent(self, session, name):
        """
        Get an agent resource data package in organization by name.
        :param session: session id
        :param name: agent unique name
        :return: execution state tuple
        """
        return True, CController._agentModel.Retrieve(name)

    @authorizeRequireWarp
    def RetrieveAllAgent(self, session):
        """
        Get all agent resource data packages in organization.
        :param session: session id
        :return: execution state tuple
        """
        return True, CController._agentModel.RetrieveAll()

    @authorizeRequireWarp
    def RetrieveAgentById(self, session, gid):
        """
        Get an agent resource data package in organization by global id.
        :param session: session id
        :param gid: global id
        :return: execution state tuple
        """
        return True, CController._agentModel.GetByGlobalId(gid)

    @authorizeRequireWarp
    def RetrieveAllWorker(self, session):
        """
        Get all worker resource data package in organization.
        :param session: session id
        :return: execution state tuple
        """
        humanList = CController._humanModel.RetrieveAll()
        agentList = CController._agentModel.RetrieveAll()
        return True, humanList + agentList

    """
    Organization Methods
    """
    @authorizeRequireWarp
    def AddGroup(self, session, name, description, note, belongToId, groupType):
        """
        Add a group to organization.
        :param session: session id
        :param name: group unique name
        :param description: description text
        :param note: note text
        :param belongToId: belong to group global id
        :param groupType: group type enum value
        :return: execution state tuple
        """
        if CController._groupModel.Contains(name) is True:
            return True, None
        return True, CController._groupModel.Add(name, description, note, belongToId, groupType)

    @authorizeRequireWarp
    def RemoveGroup(self, session, name):
        """
        Remove a group from organization.
        :param session: session id
        :param name: group unique name
        :return: execution state tuple
        """
        return True, CController._groupModel.Remove(name)

    @authorizeRequireWarp
    def UpdateGroup(self, session, name, **kwargs):
        """
        Update a group info in organization.
        :param session: session id
        :param name: group unique name
        :param kwargs: update key-value pair dictionary
        :return: execution state tuple
        """
        CController._groupModel.Update(name, **kwargs)
        return True, True

    @authorizeRequireWarp
    def RetrieveGroup(self, session, name):
        """
        Get an group data package in organization by name.
        :param session: session id
        :param name: group unique name
        :return: execution state tuple
        """
        return True, CController._groupModel.Retrieve(name)

    @authorizeRequireWarp
    def RetrieveAllGroup(self, session):
        """
        Get all group data packages in organization.
        :param session: session id
        :return: execution state tuple
        """
        return True, CController._groupModel.RetrieveAll()

    @authorizeRequireWarp
    def RetrieveGroupById(self, session, gid):
        """
        Get an group data package in organization by global id.
        :param session: session id
        :param gid: global id
        :return: execution state tuple
        """
        return True, CController._groupModel.GetByGlobalId(gid)

    @authorizeRequireWarp
    def RetrieveGroupId(self, session, name):
        """
        Get an group global id by name.
        :param session: session id
        :param name: group unique name
        :return: execution state tuple
        """
        return True, CController._groupModel.GetGlobalId(name)

    @authorizeRequireWarp
    def AddPosition(self, session, name, description, note, belongToId, reportToId):
        """
        Add a position to organization.
        :param session: session id
        :param name: position unique name
        :param description: description text
        :param note: note text
        :param belongToId: belong to group global id
        :param reportToId: report to position global id
        :return: execution state tuple
        """
        if CController._positionModel.Contains(name) is True:
            return True, None
        return True, CController._positionModel.Add(name, description, note, belongToId, reportToId)

    @authorizeRequireWarp
    def RemovePosition(self, session, name):
        """
        Remove a position from organization.
        :param session: session id
        :param name: position unique name
        :return: execution state tuple
        """
        return True, CController._positionModel.Remove(name)

    @authorizeRequireWarp
    def UpdatePosition(self, session, name, **kwargs):
        """
        Update a position info in organization.
        :param session: session id
        :param name: position unique name
        :param kwargs: update key-value pair dictionary
        :return: execution state tuple
        """
        CController._positionModel.Update(name, **kwargs)
        return True, True

    @authorizeRequireWarp
    def RetrievePosition(self, session, name):
        """
        Get a position data package in organization by name.
        :param session: session id
        :param name: position unique name
        :return: execution state tuple
        """
        return True, CController._positionModel.Retrieve(name)

    @authorizeRequireWarp
    def RetrieveAllPosition(self, session):
        """
        Get all position data packages in organization.
        :param session: session id
        :return: execution state tuple
        """
        return True, CController._positionModel.RetrieveAll()

    @authorizeRequireWarp
    def RetrievePositionById(self, session, gid):
        """
        Get a position data package in organization by global id.
        :param session: session id
        :param gid: global id
        :return: execution state tuple
        """
        return True, CController._positionModel.GetByGlobalId(gid)

    @authorizeRequireWarp
    def RetrievePositionId(self, session, name):
        """
        Get a position global id by name.
        :param session: session id
        :param name: position unique name
        :return: execution state tuple
        """
        return True, CController._positionModel.GetGlobalId(name)

    @authorizeRequireWarp
    def AddCapability(self, session, name, description, note):
        """
        Add a capability to organization.
        :param session: session id
        :param name: capability unique name
        :param description: description text
        :param note: note text
        :return: execution state tuple
        """
        if CController._capabilityModel.Contains(name) is True:
            return True, None
        return True, CController._capabilityModel.Add(name, description, note)

    @authorizeRequireWarp
    def RemoveCapability(self, session, name):
        """
        Remove a capability from organization.
        :param session: session id
        :param name: capability unique name
        :return: execution state tuple
        """
        return True, CController._capabilityModel.Remove(name)

    @authorizeRequireWarp
    def UpdateCapability(self, session, name, **kwargs):
        """
        Update a capability info in organization.
        :param session: session id
        :param name: capability unique name
        :param kwargs: update key-value pair dictionary
        :return: execution state tuple
        """
        CController._capabilityModel.Update(name, **kwargs)
        return True, True

    @authorizeRequireWarp
    def RetrieveCapability(self, session, name):
        """
        Get a capability data package in organization by name.
        :param session: session id
        :param name: capability unique name
        :return: execution state tuple
        """
        return True, CController._capabilityModel.Retrieve(name)

    @authorizeRequireWarp
    def RetrieveAllCapabilities(self, session):
        """
        Get all capability data packages in organization.
        :param session: session id
        :return: execution state tuple
        """
        return True, CController._capabilityModel.RetrieveAll()

    @authorizeRequireWarp
    def RetrieveCapabilityById(self, session, gid):
        """
        Get a capability data package in organization by global id.
        :param session: session id
        :param gid: global id
        :return: execution state tuple
        """
        return True, CController._capabilityModel.GetByGlobalId(gid)

    @authorizeRequireWarp
    def RetrieveCapabilityId(self, session, name):
        """
        Get a capability global id by name.
        :param session: session id
        :param name: position unique name
        :return: execution state tuple
        """
        return True, CController._capabilityModel.GetGlobalId(name)

    """
    Connection Constrain Methods
    """
    @authorizeRequireWarp
    def RemoveHumanConnection(self, session, personId):
        """
        Remove all connection from a human.
        :param session: session id
        :param personId: person unique id
        :return: execution state tuple
        """
        try:
            flag, human = self.RetrieveHuman(session, personId)
            if flag is False or human is None:
                return False, None
            CController._connectModel.RemoveByWorker(human.GlobalId)
            return True, True
        except:
            return False, False

    @authorizeRequireWarp
    def RemoveAgentConnection(self, session, agentName):
        """
        Remove all connection from an agent.
        :param session: session id
        :param agentName: agent unique name
        :return: execution state tuple
        """
        try:
            flag, agent = self.RetrieveAgent(session, agentName)
            if flag is False or agent is None:
                return False, None
            CController._connectModel.RemoveByWorker(agent.GlobalId)
            return True, True
        except:
            return False, False

    @authorizeRequireWarp
    def RetrieveHumanInWhatGroup(self, session, personId):
        """
        Get a list of name string what group human in.
        :param session: session id
        :param personId: person unique id
        :return: execution state tuple
        """
        flag, human = self.RetrieveHuman(session, personId)
        if flag is False or human is None:
            return False, None
        cons = CController._connectModel.RetrieveByWorkerGlobalId(human.GlobalId)
        filteredId = []
        for kvp in cons:
            groupKey = kvp["belongToOrganizableId"]
            if groupKey.startswith("Dept_") is True:
                filteredId.append(groupKey)
        retList = []
        for gk in filteredId:
            flag, groupObj = self.RetrieveGroupById(session, gk)
            if flag is False or groupObj is None:
                return False, None
            retList.append(groupObj.Name)
        return True, retList

    @authorizeRequireWarp
    def RetrieveHumanInWhatPosition(self, session, personId):
        """
        Get a list of name string what position human at.
        :param session: session id
        :param personId: person unique id
        :return: execution state tuple
        """
        flag, human = self.RetrieveHuman(session, personId)
        if flag is False or human is None:
            return False, None
        cons = CController._connectModel.RetrieveByWorkerGlobalId(human.GlobalId)
        filteredId = []
        for kvp in cons:
            groupKey = kvp["belongToOrganizableId"]
            if groupKey.startswith("Pos_") is True:
                filteredId.append(groupKey)
        retList = []
        for gk in filteredId:
            flag, groupObj = self.RetrievePositionById(session, gk)
            if flag is False or groupObj is None:
                return False, None
            retList.append(groupObj.Name)
        return True, retList

    @authorizeRequireWarp
    def RetrieveHumanWithWhatCapability(self, session, personId):
        """
        Get a list of name string what capability human with.
        :param session: session id
        :param personId: person unique id
        :return: execution state tuple
        """
        flag, human = self.RetrieveHuman(session, personId)
        if flag is False or human is None:
            return False, None
        cons = CController._connectModel.RetrieveByWorkerGlobalId(human.GlobalId)
        filteredId = []
        for kvp in cons:
            groupKey = kvp["belongToOrganizableId"]
            if groupKey.startswith("Capa_") is True:
                filteredId.append(groupKey)
        retList = []
        for gk in filteredId:
            flag, groupObj = self.RetrieveCapabilityById(session, gk)
            if flag is False or groupObj is None:
                return False, None
            retList.append(groupObj.Name)
        return True, retList

    @authorizeRequireWarp
    def RetrieveAgentInWhatGroup(self, session, agentName):
        """
        Get a list of name string what group agent in.
        :param session: session id
        :param agentName: agent unique name
        :return: execution state tuple
        """
        flag, agent = self.RetrieveAgent(session, agentName)
        if flag is False or agent is None:
            return False, None
        cons = CController._connectModel.RetrieveByWorkerGlobalId(agent.GlobalId)
        filteredId = []
        for kvp in cons:
            groupKey = kvp["belongToOrganizableId"]
            if groupKey.startswith("Dept_") is True:
                filteredId.append(groupKey)
        retList = []
        for gk in filteredId:
            flag, groupObj = self.RetrieveGroupById(session, gk)
            if flag is False or groupObj is None:
                return False, None
            retList.append(groupObj.Name)
        return True, retList

    @authorizeRequireWarp
    def RetrieveAgentInWhatPosition(self, session, agentName):
        """
        Get a list of name string what position agent at.
        :param session: session id
        :param agentName: agent unique name
        :return: execution state tuple
        """
        flag, agent = self.RetrieveAgent(session, agentName)
        if flag is False or agent is None:
            return False, None
        cons = CController._connectModel.RetrieveByWorkerGlobalId(agent.GlobalId)
        filteredId = []
        for kvp in cons:
            groupKey = kvp["belongToOrganizableId"]
            if groupKey.startswith("Pos_") is True:
                filteredId.append(groupKey)
        retList = []
        for gk in filteredId:
            flag, groupObj = self.RetrievePositionById(session, gk)
            if flag is False or groupObj is None:
                return False, None
            retList.append(groupObj.Name)
        return True, retList

    @authorizeRequireWarp
    def RetrieveAgentWithWhatCapability(self, session, agentName):
        """
        Get a list of name string what capability agent with.
        :param session: session id
        :param agentName: agent unique name
        :return: execution state tuple
        """
        flag, agent = self.RetrieveAgent(session, agentName)
        if flag is False or agent is None:
            return False, None
        cons = CController._connectModel.RetrieveByWorkerGlobalId(agent.GlobalId)
        filteredId = []
        for kvp in cons:
            groupKey = kvp["belongToOrganizableId"]
            if groupKey.startswith("Capa_") is True:
                filteredId.append(groupKey)
        retList = []
        for gk in filteredId:
            flag, groupObj = self.RetrieveCapabilityById(session, gk)
            if flag is False or groupObj is None:
                return False, None
            retList.append(groupObj.Name)
        return True, retList

    @authorizeRequireWarp
    def RetrieveHumanInGroup(self, session, groupName):
        """
        Get a list of human instances in a group.
        :param session: session id
        :param groupName: group unique name
        :return: execution state tuple
        """
        flag, org = self.RetrieveGroup(session, groupName)
        if flag is False or org is None:
            return False, None
        return True, CController._connectModel.RetrieveHumanByGlobalId(org.GlobalId)

    @authorizeRequireWarp
    def RetrieveAgentInGroup(self, session, groupName):
        """
        Get a list of agent instances in a group.
        :param session: session id
        :param groupName: group unique name
        :return: execution state tuple
        """
        flag, org = self.RetrieveGroup(session, groupName)
        if flag is False or org is None:
            return False, None
        return True, CController._connectModel.RetrieveAgentByGlobalId(org.GlobalId)

    @authorizeRequireWarp
    def RetrieveHumanInPosition(self, session, posName):
        """
        Get a list of human instances at a position.
        :param session: session id
        :param posName: position unique name
        :return: execution state tuple
        """
        flag, org = self.RetrievePosition(session, posName)
        if flag is False or org is None:
            return False, None
        return True, CController._connectModel.RetrieveHumanByGlobalId(org.GlobalId)

    @authorizeRequireWarp
    def RetrieveAgentInPosition(self, session, posName):
        """
        Get a list of agent instances at a position.
        :param session: session id
        :param posName: position unique name
        :return: execution state tuple
        """
        flag, org = self.RetrievePosition(session, posName)
        if flag is False or org is None:
            return False, None
        return True, CController._connectModel.RetrieveAgentByGlobalId(org.GlobalId)

    @authorizeRequireWarp
    def RetrieveHumanWithCapability(self, session, capabilityName):
        """
        Get a list of human instances with a capability.
        :param session: session id
        :param capabilityName: capability unique name
        :return: execution state tuple
        """
        flag, org = self.RetrieveCapability(session, capabilityName)
        if flag is False or org is None:
            return False, None
        return True, CController._connectModel.RetrieveHumanByGlobalId(org.GlobalId)

    @authorizeRequireWarp
    def RetrieveAgentWithCapability(self, session, capabilityName):
        """
        Get a list of agent instances with a capability.
        :param session: session id
        :param capabilityName: capability unique name
        :return: execution state tuple
        """
        flag, org = self.RetrieveCapability(session, capabilityName)
        if flag is False or org is None:
            return False, None
        return True, CController._connectModel.RetrieveAgentByGlobalId(org.GlobalId)

    @authorizeRequireWarp
    def RetrieveWorkerByOrganizableGid(self, session, groupId):
        """
        Get all workers in a organizable by group global id.
        THIS METHOD IS FOR NS ONLY.
        :param session: session id
        :param groupId: group global id
        :return: a list of workers gid
        """
        return True, CController._connectModel.RetrieveByOrganizableGlobalId(groupId)

    @authorizeRequireWarp
    def RetrieveWorkersEntity(self, session, gids):
        """
        Get all workers by global id.
        THIS METHOD IS FOR NS ONLY.
        :param session: session id
        :param gids: worker global id list
        :return: a list of workers gid
        """
        gidItem = gids.split(',')
        retList = list()
        for gid in gidItem:
            if gid.startswith("Human_"):
                retList.append(CController._humanModel.GetByGlobalId(gid))
            elif gid.startswith("Agent_"):
                retList.append(CController._agentModel.GetByGlobalId(gid))
        return True, retList

    @authorizeRequireWarp
    def AddHumanToGroup(self, session, personId, groupName):
        """
        Add a human to a group.
        :param session: session id
        :param personId: person unique id
        :param groupName: group unique name
        :return: execution state tuple
        """
        flag1, human = self.RetrieveHuman(session, personId)
        flag2, group = self.RetrieveGroup(session, groupName)
        if (flag1 & flag2) is False or human is None or group is None:
            return False, None
        CController._connectModel.Add(human.GlobalId, group.GlobalId)
        return True, True

    @authorizeRequireWarp
    def RemoveHumanFromGroup(self, session, personId, groupName):
        """
        Remove a human from a group.
        :param session: session id
        :param personId: person unique id
        :param groupName: group unique name
        :return: execution state tuple
        """
        flag1, human = self.RetrieveHuman(session, personId)
        flag2, group = self.RetrieveGroup(session, groupName)
        if (flag1 & flag2) is False or human is None or group is None:
            return False, None
        CController._connectModel.Remove(human.GlobalId, group.GlobalId)
        return True, True

    @authorizeRequireWarp
    def AddHumanPosition(self, session, personId, positionName):
        """
        Add a human to a position.
        :param session: session id
        :param personId: person unique id
        :param positionName: position unique name
        :return: execution state tuple
        """
        flag1, human = self.RetrieveHuman(session, personId)
        flag2, position = self.RetrievePosition(session, positionName)
        if (flag1 & flag2) is False or human is None or position is None:
            return False, None
        CController._connectModel.Add(human.GlobalId, position.GlobalId)
        return True, True

    @authorizeRequireWarp
    def RemoveHumanPosition(self, session, personId, positionName):
        """
        Remove a human from a position.
        :param session: session id
        :param personId: person unique id
        :param positionName: position unique name
        :return: execution state tuple
        """
        flag1, human = self.RetrieveHuman(session, personId)
        flag2, position = self.RetrievePosition(session, positionName)
        if (flag1 & flag2) is False or human is None or position is None:
            return False, None
        CController._connectModel.Remove(human.GlobalId, position.GlobalId)
        return True, True

    @authorizeRequireWarp
    def AddHumanCapability(self, session, personId, capabilityName):
        """
        Add a capability to a human.
        :param session: session id
        :param personId: person unique id
        :param capabilityName: capability unique name
        :return: execution state tuple
        """
        flag1, human = self.RetrieveHuman(session, personId)
        flag2, capability = self.RetrieveCapability(session, capabilityName)
        if (flag1 & flag2) is False or human is None or capability is None:
            return False, None
        CController._connectModel.Add(human.GlobalId, capability.GlobalId)
        return True, True

    @authorizeRequireWarp
    def RemoveHumanCapability(self, session, personId, capabilityName):
        """
        Remove a capability from a human.
        :param session: session id
        :param personId: person unique id
        :param capabilityName: capability unique name
        :return: execution state tuple
        """
        flag1, human = self.RetrieveHuman(session, personId)
        flag2, capability = self.RetrieveCapability(session, capabilityName)
        if (flag1 & flag2) is False or human is None or capability is None:
            return False, None
        CController._connectModel.Remove(human.GlobalId, capability.GlobalId)
        return True, True

    @authorizeRequireWarp
    def AddAgentToGroup(self, session, agentName, groupName):
        """
        Add an agent to a group.
        :param session: session id
        :param agentName: agent unique name
        :param groupName: group unique name
        :return: execution state tuple
        """
        flag1, agent = self.RetrieveAgent(session, agentName)
        flag2, group = self.RetrieveGroup(session, groupName)
        if (flag1 & flag2) is False or agent is None or group is None:
            return False, None
        CController._connectModel.Add(agent.GlobalId, group.GlobalId)
        return True, True

    @authorizeRequireWarp
    def RemoveAgentFromGroup(self, session, agentName, groupName):
        """
        Remove an agent from a group.
        :param session: session id
        :param agentName: agent unique name
        :param groupName: group unique name
        :return: execution state tuple
        """
        flag1, agent = self.RetrieveAgent(session, agentName)
        flag2, group = self.RetrieveGroup(session, groupName)
        if (flag1 & flag2) is False or agent is None or group is None:
            return False, None
        CController._connectModel.Remove(agent.GlobalId, group.GlobalId)
        return True, True

    @authorizeRequireWarp
    def AddAgentPosition(self, session, agentName, positionName):
        """
        Add an agent to a position.
        :param session: session id
        :param agentName: agent unique name
        :param positionName: position unique name
        :return: execution state tuple
        """
        flag1, agent = self.RetrieveAgent(session, agentName)
        flag2, position = self.RetrievePosition(session, positionName)
        if (flag1 & flag2) is False or agent is None or position is None:
            return False, None
        CController._connectModel.Add(agent.GlobalId, position.GlobalId)
        return True, True

    @authorizeRequireWarp
    def RemoveAgentPosition(self, session, agentName, positionName):
        """
        Remove an agent from a position.
        :param session: session id
        :param agentName: agent unique name
        :param positionName: position unique name
        :return: execution state tuple
        """
        flag1, agent = self.RetrieveAgent(session, agentName)
        flag2, position = self.RetrievePosition(session, positionName)
        if (flag1 & flag2) is False or agent is None or position is None:
            return False, None
        CController._connectModel.Remove(agent.GlobalId, position.GlobalId)
        return True, True

    @authorizeRequireWarp
    def AddAgentCapability(self, session, agentName, capabilityName):
        """
        Add a capability to an agent.
        :param session: session id
        :param agentName: agent unique name
        :param capabilityName: capability unique name
        :return: execution state tuple
        """
        flag1, agent = self.RetrieveAgent(session, agentName)
        flag2, capability = self.RetrieveCapability(session, capabilityName)
        if (flag1 & flag2) is False or agent is None or capability is None:
            return False, None
        CController._connectModel.Add(agent.GlobalId, capability.GlobalId)
        return True, True

    @authorizeRequireWarp
    def RemoveAgentCapability(self, session, agentName, capabilityName):
        """
        Remove a capability to an agent.
        :param session: session id
        :param agentName: agent unique name
        :param capabilityName: capability unique name
        :return: execution state tuple
        """
        flag1, agent = self.RetrieveAgent(session, agentName)
        flag2, capability = self.RetrieveCapability(session, capabilityName)
        if (flag1 & flag2) is False or agent is None or capability is None:
            return False, None
        CController._connectModel.Remove(agent.GlobalId, capability.GlobalId)
        return True, True

    @adminRequireWarp
    def RetrieveAllConnection(self, session):
        """
        Remove a capability to an agent.
        :param session: session id
        :return: execution state tuple
        """
        return True, CController._connectModel.RetrieveAll()

    """
    COrgan Configuration 
    """
    @adminRequireWarp
    def SetOrganizationName(self, session, orgName):
        """
        Set organization name.
        :param session: session id
        :param orgName: organization name
        :return: execution state tuple
        """
        try:
            CController._configModel.AddOrUpdate(GCC.CONFIG_ORGANIZATION_KEY, orgName)
            return True, True
        except:
            return False, False

    @authorizeRequireWarp
    def GetOrganizationName(self, session):
        """
        Get organization name.
        :param session: session id
        :return: execution state tuple
        """
        return True, CController._configModel.Retrieve(GCC.CONFIG_ORGANIZATION_KEY)

    @adminRequireWarp
    def SetUpdateNotifyRouter(self, session, routerUrl):
        """
        Set update notify router gateway.
        :param session: session id
        :param routerUrl: gateway url
        :return: execution state tuple
        """
        try:
            CController._configModel.AddOrUpdate(GCC.CONFIG_DATA_UPDATE_ROUTER, routerUrl)
            return True, True
        except:
            return False, False

    @adminRequireWarp
    def GetUpdateNotifyRouter(self, session):
        """
        Get update notify router gateway.
        :param session: session id
        :return: execution state tuple
        """
        return True, CController._configModel.Retrieve(GCC.CONFIG_DATA_UPDATE_ROUTER)

    @authorizeRequireWarp
    def GetCurrentDataVersion(self, session):
        """
        Get the data version of current moment.
        :param session: session id
        :return: execution state tuple
        """
        return True, CController._configModel.Retrieve(GCC.CONFIG_DATA_VERSION_KEY)

    @authorizeRequireWarp
    def GetOrganizationId(self, session):
        """
        Get the global id of this organization.
        :param session: session id
        :return: execution state tuple
        """
        return True, CController._configModel.Retrieve(GCC.CONFIG_ORGANIZATION_ID_KEY)

    """
    Support Methods
    """
    @staticmethod
    def AmIAdmin(session):
        """
        Get whether I am an admin.
        :param session: session id
        :return: True if admin session
        """
        try:
            return True, SessionManager.CheckAdmin(session)
        except Exception as e:
            print "Exception in COrgan: %s" % str(e)
            return False, e

    @staticmethod
    def Unauthorized(session):
        """
        Warp unauthorized service request feedback package.
        :param session: session id
        :return: unauthorized feedback
        """
        try:
            sObj = SessionManager.GetSession(session)
            sUser = ""
            if sObj is not None:
                sUser = sObj.Username
            LogUtil.Log("username:%s, session:%s unauthorized request." % (sUser, session),
                        CController.__name__, "Warning", True)
        except Exception as e:
            print "Exception in COrgan authorization check: %s" % str(e)
        finally:
            return GCC.UNAUTHORIZED

    @staticmethod
    def GetGroupTypeEnum(typeStr):
        # type: (str) -> int
        """
        Get GroupType enum value from its string.
        :param typeStr: type string
        :return: enum value
        """
        from Entity.Group import GroupType
        if typeStr == "Department":
            return GroupType.Department
        elif typeStr == "Team":
            return GroupType.Team
        elif typeStr == "Group":
            return GroupType.Group
        elif typeStr == "Cluster":
            return GroupType.Cluster
        elif typeStr == "Division":
            return GroupType.Division
        elif typeStr == "Branch":
            return GroupType.Branch
        else:
            return GroupType.Unit

    @staticmethod
    def ParseGroupTypeEnum(typeInt):
        # type: (int) -> str
        """
        Parse GroupType String to its enum value.
        :param typeInt: type value
        :return: string
        """
        from Entity.Group import GroupType
        if typeInt == GroupType.Department:
            return "Department"
        elif typeInt == GroupType.Team:
            return "Team"
        elif typeInt == GroupType.Group:
            return "Group"
        elif typeInt == GroupType.Cluster:
            return "Cluster"
        elif typeInt == GroupType.Division:
            return "Division"
        elif typeInt == GroupType.Branch:
            return "Branch"
        else:
            return "Unit"

    """
    Static Fields
    """
    _agentModel = AgentModel()
    _humanModel = HumanModel()
    _groupModel = GroupModel()
    _positionModel = PositionModel()
    _capabilityModel = CapabilityModel()
    _connectModel = ConnectModel()
    _userModel = UserModel()
    _logModel = RuntimeLogModel()
    _configModel = CConfigModel()


"""
Global Static Code
"""
CControllerCore = CController()
