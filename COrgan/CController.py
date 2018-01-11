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
        return True, UserModel.Add(username, encrypted_password, level)

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

        :param session:
        :param personId:
        :param firstName:
        :param lastName:
        :param note:
        :return:
        """
        if CController._humanModel.Contains(personId) is True:
            return True, None
        return True, CController._humanModel.Add(personId, firstName, lastName, note)

    @authorizeRequireWarp
    def RemoveHuman(self, session, personId):
        """

        :param session:
        :param personId:
        :return:
        """
        return True, CController._humanModel.Remove(personId)

    @authorizeRequireWarp
    def UpdateHuman(self, session, personId, **kwargs):
        """

        :param session:
        :param personId:
        :param kwargs:
        :return:
        """
        CController._humanModel.Update(personId, **kwargs)
        return True, True

    @authorizeRequireWarp
    def RetrieveHuman(self, session, personId):
        """

        :param session:
        :param personId:
        :return:
        """
        return True, CController._humanModel.Retrieve(personId)

    @authorizeRequireWarp
    def RetrieveAllHuman(self, session):
        """

        :param session:
        :return:
        """
        return True, CController._humanModel.RetrieveAll()

    @authorizeRequireWarp
    def RetrieveHumanById(self, session, gid):
        """

        :param session:
        :param gid:
        :return:
        """
        return True, CController._humanModel.GetByGlobalId(gid)

    @authorizeRequireWarp
    def AddAgent(self, session, name, location, rType, note):
        """

        :param session:
        :param name:
        :param location:
        :param rType:
        :param note:
        :return:
        """
        if CController._agentModel.Contains(name) is True:
            return True, None
        return True, CController._agentModel.Add(name, location, note, rType)

    @authorizeRequireWarp
    def RemoveAgent(self, session, name):
        """

        :param session:
        :param name:
        :return:
        """
        return True, CController._agentModel.Remove(name)

    @authorizeRequireWarp
    def UpdateAgent(self, session, name, **kwargs):
        """

        :param session:
        :param name:
        :param kwargs:
        :return:
        """
        CController._agentModel.Update(name, **kwargs)
        return True, True

    @authorizeRequireWarp
    def RetrieveAgent(self, session, name):
        """

        :param session:
        :param name:
        :return:
        """
        return True, CController._agentModel.Retrieve(name)

    @authorizeRequireWarp
    def RetrieveAllAgent(self, session):
        """

        :param session:
        :return:
        """
        return True, CController._agentModel.RetrieveAll()

    @authorizeRequireWarp
    def RetrieveAgentById(self, session, gid):
        """

        :param session:
        :param gid:
        :return:
        """
        return True, CController._agentModel.GetByGlobalId(gid)

    @authorizeRequireWarp
    def RetrieveAllWorker(self, session):
        """

        :param session:
        :return:
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

        :param session:
        :param name:
        :param description:
        :param note:
        :param belongToId:
        :param groupType:
        :return:
        """
        if CController._groupModel.Contains(name) is True:
            return True, None
        return True, CController._groupModel.Add(name, description, note, belongToId, groupType)

    @authorizeRequireWarp
    def RemoveGroup(self, session, name):
        """

        :param session:
        :param name:
        :return:
        """
        return True, CController._groupModel.Remove(name)

    @authorizeRequireWarp
    def UpdateGroup(self, session, name, **kwargs):
        """

        :param session:
        :param name:
        :param kwargs:
        :return:
        """
        CController._groupModel.Update(name, **kwargs)
        return True, True

    @authorizeRequireWarp
    def RetrieveGroup(self, session, name):
        """

        :param session:
        :param name:
        :return:
        """
        return True, CController._groupModel.Retrieve(name)

    @authorizeRequireWarp
    def RetrieveAllGroup(self, session):
        """

        :param session:
        :return:
        """
        return True, CController._groupModel.RetrieveAll()

    @authorizeRequireWarp
    def RetrieveGroupById(self, session, gid):
        """

        :param session:
        :param gid:
        :return:
        """
        return True, CController._groupModel.GetByGlobalId(gid)

    @authorizeRequireWarp
    def RetrieveGroupId(self, session, name):
        """

        :param session:
        :param name:
        :return:
        """
        return True, CController._groupModel.GetGlobalId(name)

    @authorizeRequireWarp
    def AddPosition(self, session, name, description, note, belongToId, reportToId):
        """

        :param session:
        :param name:
        :param description:
        :param note:
        :param belongToId:
        :param reportToId:
        :return:
        """
        if CController._positionModel.Contains(name) is True:
            return True, None
        return True, CController._positionModel.Add(name, description, note, belongToId, reportToId)

    @authorizeRequireWarp
    def RemovePosition(self, session, name):
        """

        :param session:
        :param name:
        :return:
        """
        return True, CController._positionModel.Remove(name)

    @authorizeRequireWarp
    def UpdatePosition(self, session, name, **kwargs):
        """

        :param session:
        :param name:
        :param kwargs:
        :return:
        """
        CController._positionModel.Update(name, **kwargs)
        return True, True

    @authorizeRequireWarp
    def RetrievePosition(self, session, name):
        """

        :param session:
        :param name:
        :return:
        """
        return True, CController._positionModel.Retrieve(name)

    @authorizeRequireWarp
    def RetrieveAllPosition(self, session):
        """

        :param session:
        :return:
        """
        return True, CController._positionModel.RetrieveAll()

    @authorizeRequireWarp
    def RetrievePositionById(self, session, gid):
        """

        :param session:
        :param gid:
        :return:
        """
        return True, CController._positionModel.GetByGlobalId(gid)

    @authorizeRequireWarp
    def RetrievePositionId(self, session, name):
        """

        :param session:
        :param name:
        :return:
        """
        return True, CController._positionModel.GetGlobalId(name)

    @authorizeRequireWarp
    def AddCapability(self, session, name, description, note):
        """

        :param session:
        :param name:
        :param description:
        :param note:
        :return:
        """
        if CController._capabilityModel.Contains(name) is True:
            return True, None
        return True, CController._capabilityModel.Add(name, description, note)

    @authorizeRequireWarp
    def RemoveCapability(self, session, name):
        """

        :param session:
        :param name:
        :return:
        """
        return True, CController._capabilityModel.Remove(name)

    @authorizeRequireWarp
    def UpdateCapability(self, session, name, **kwargs):
        """

        :param session:
        :param name:
        :param kwargs:
        :return:
        """
        CController._capabilityModel.Update(name, **kwargs)
        return True, True

    @authorizeRequireWarp
    def RetrieveCapability(self, session, name):
        """

        :param session:
        :param name:
        :return:
        """
        return True, CController._capabilityModel.Retrieve(name)

    @authorizeRequireWarp
    def RetrieveAllCapabilities(self, session):
        """

        :param session:
        :return:
        """
        return True, CController._capabilityModel.RetrieveAll()

    @authorizeRequireWarp
    def RetrieveCapabilityById(self, session, gid):
        """

        :param session:
        :param gid:
        :return:
        """
        return True, CController._capabilityModel.GetByGlobalId(gid)

    """
    Connection Constrain Methods
    """
    @authorizeRequireWarp
    def RemoveHumanConnection(self, session, personId):
        """

        :param session:
        :param personId:
        :return:
        """
        try:
            flag, human = self.RetrieveHuman(session, personId)
            if flag is False or human is None:
                return False, None
            CController._connectModel.RemoveByWorker(human.GlobalId)
            return True, True
        except:
            return False, False

    def RetrieveHumanInGroup(self, session, groupName):
        pass

    def RetrieveAgentInGroup(self, session, groupName):
        pass

    def RetrieveHumanInPosition(self, session, posName):
        pass

    def RetrieveAgentInPosition(self, session, posName):
        pass

    @authorizeRequireWarp
    def RetrieveHumanInWhatGroup(self, session, personId):
        """

        :param session:
        :param personId:
        :return:
        """
        flag, human = self.RetrieveHuman(session, personId)
        if flag is False or human is None:
            return False, None
        cons = CController._connectModel.RetrieveByWorker(human.GlobalId)
        filteredId = []
        for kvp in cons:
            groupKey = kvp["belongToGroupId"]
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

        :param session:
        :param personId:
        :return:
        """
        flag, human = self.RetrieveHuman(session, personId)
        if flag is False or human is None:
            return False, None
        cons = CController._connectModel.RetrieveByWorker(human.GlobalId)
        filteredId = []
        for kvp in cons:
            groupKey = kvp["belongToGroupId"]
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

        :param session:
        :param personId:
        :return:
        """
        flag, human = self.RetrieveHuman(session, personId)
        if flag is False or human is None:
            return False, None
        cons = CController._connectModel.RetrieveByWorker(human.GlobalId)
        filteredId = []
        for kvp in cons:
            groupKey = kvp["belongToGroupId"]
            if groupKey.startswith("Capa_") is True:
                filteredId.append(groupKey)
        retList = []
        for gk in filteredId:
            flag, groupObj = self.RetrieveCapabilityById(session, gk)
            if flag is False or groupObj is None:
                return False, None
            retList.append(groupObj.Name)
        return True, retList

    def SpanTreeOfGroup(self, session, groupName):
        pass

    def SpanTreeOfPosition(self, session, posName):
        pass

    def SpanTreeOfOrganizationInGroup(self, session):
        pass

    def SpanTreeOfOrganizationInPosition(self, session):
        pass

    @authorizeRequireWarp
    def AddHumanToGroup(self, session, personId, groupName):
        """

        :param session:
        :param personId:
        :param groupName:
        :return:
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

        :param session:
        :param personId:
        :param groupName:
        :return:
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

        :param session:
        :param personId:
        :param positionName:
        :return:
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

        :param session:
        :param personId:
        :param positionName:
        :return:
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

        :param session:
        :param personId:
        :param capabilityName:
        :return:
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

        :param session:
        :param personId:
        :param capabilityName:
        :return:
        """
        flag1, human = self.RetrieveHuman(session, personId)
        flag2, capability = self.RetrieveCapability(session, capabilityName)
        if (flag1 & flag2) is False or human is None or capability is None:
            return False, None
        CController._connectModel.Remove(human.GlobalId, capability.GlobalId)
        return True, True

    """
    COrgan Configuration 
    """
    @adminRequireWarp
    def SetOrganizationName(self, session, orgName):
        """
        Set organization name.
        :param session: session id
        :param orgName: organization name
        """
        CController._configModel.AddOrUpdate(GCC.CONFIG_ORGANIZATION_KEY, orgName)
        return True, True

    @authorizeRequireWarp
    def GetOrganizationName(self, session):
        """
        Get organization name.
        :param session: session id
        """
        return True, CController._configModel.Retrieve(GCC.CONFIG_ORGANIZATION_KEY)

    @adminRequireWarp
    def SetUpdateNotifyRouter(self, session, routerUrl):
        """
        Set update notify router gateway.
        :param session: session id
        :param routerUrl: gateway url
        """
        CController._configModel.AddOrUpdate(GCC.CONFIG_DATA_UPDATE_ROUTER, routerUrl)
        return True, True

    @adminRequireWarp
    def GetUpdateNotifyRouter(self, session):
        """
        Get update notify router gateway.
        :param session: session id
        """
        return True, CController._configModel.Retrieve(GCC.CONFIG_DATA_UPDATE_ROUTER)

    @authorizeRequireWarp
    def GetCurrentDataVersion(self, session):
        """
        Get the data version of current moment.
        :param session: session id
        """
        return True, CController._configModel.Retrieve(GCC.CONFIG_DATA_VERSION_KEY)

    """
    Support Methods
    """
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

    @authorizeRequireWarp
    def EchoTest(self, session, pr):
        """
        Debug echo function.
        :param session: session id
        :param pr: echo text
        """
        print pr

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


if __name__ == '__main__':
    CControllerCore.EchoTest('testadmin2', 'testprint')
    pass
