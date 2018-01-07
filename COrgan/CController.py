#!/usr/bin/env python
# encoding: utf-8
"""
@module : CController
@author : Rinkako
@time   : 2018/1/4
"""
import GlobalConfigContext as GCC
from functools import wraps
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
    def wrapper(*args, **kwargs):
        try:
            session = args[1]
            if SessionManager.Check(session) is True:
                return fn(*args, **kwargs)
            else:
                return False, CController._Unauthorized(session)
        except Exception as e:
            print "Exception in COrgan: %s" % str(e)
            return False, e
    return wrapper


def adminRequireWarp(fn):
    """
    Decorator for session admin valid required.
    """
    @wraps(fn)
    def wrapper(*args, **kwargs):
        try:
            session = kwargs["session"]
            if SessionManager.CheckAdmin(session) is True:
                return fn(*args, **kwargs)
            else:
                return False, CController._Unauthorized(session)
        except Exception as e:
            print "Exception in COrgan: %s" % str(e)
            return False, e
    return wrapper


def ExceptionWarp(fn):
    """
    Decorator for session admin valid required.
    """
    @wraps(fn)
    def wrapper(*args, **kwargs):
        try:
            return fn(*args, **kwargs)
        except Exception as e:
            print "Exception in COrgan: %s" % str(e)
            return False, e
    return wrapper


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
        pass

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
    def AddHuman(self, session, personId, firstName, lastName, note):
        pass

    def RemoveHuman(self, session, personId):
        pass

    def UpdateHuman(self, session, personId, **kwargs):
        pass

    def RetrieveHuman(self, session, personId):
        pass

    def RetrieveAllHuman(self, session):
        pass

    def AddAgent(self, session, name, location, rType, note):
        pass

    def RemoveAgent(self, session, name):
        pass

    def UpdateAgent(self, session, name, **kwargs):
        pass

    def RetrieveAgent(self, session, name):
        pass

    def RetrieveAllAgent(self, session):
        pass

    def RetrieveAllWorker(self, session):
        pass

    """
    Organization Methods
    """
    def AddGroup(self, session, name, description, note, belongToName, groupType):
        pass

    def RemoveGroup(self, session, name):
        pass

    def UpdateGroup(self, session, name, **kwargs):
        pass

    def RetrieveGroup(self, session, name):
        pass

    def RetrieveAllGroup(self, session):
        pass

    def AddPosition(self, session, name, description, note, belongToName):
        pass

    def RemovePosition(self, session, name):
        pass

    def UpdatePosition(self, session, name, **kwargs):
        pass

    def RetrievePosition(self, session, name):
        pass

    def RetrieveAllPosition(self, session):
        pass

    """
    Connection Constrain Methods
    """
    def RetrieveHumanInGroup(self, session, groupName):
        pass

    def RetrieveAgentInGroup(self, session, groupName):
        pass

    def RetrieveHumanInPosition(self, session, posName):
        pass

    def RetrieveAgentInPosition(self, session, posName):
        pass

    def RetrieveHumanInWhatGroup(self, session, personId):
        pass

    def RetrieveHumanInWhatPosition(self, session, personId):
        pass

    def SpanTreeOfGroup(self, session, groupName):
        pass

    def SpanTreeOfPosition(self, session, posName):
        pass

    def SpanTreeOfOrganizationInGroup(self, session):
        pass

    def SpanTreeOfOrganizationInPosition(self, session):
        pass

    def AddHumanToGroup(self, session, personId, groupName):
        pass

    def RemoveHumanFromGroup(self, session, personId, groupName):
        pass

    def AddHumanPosition(self, session, personId, posName):
        pass

    def RemoveHumanPosition(self, session, personId, posName):
        pass

    """
    COrgan Configuration 
    """
    def SetOrganizationName(self, session, orgName):
        pass

    def GetOrganizationName(self, session):
        pass

    def GetCurrentDataVersion(self, session):
        pass

    """
    Support Methods
    """
    @staticmethod
    def _Unauthorized(session):
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

    @authorizeRequireWarp
    def Contest(self, session, pr):
        print pr

"""
Static Code
"""
CControllerCore = CController()


if __name__ == '__main__':
    CControllerCore.Contest('testadmin2', 'testprint')
    pass
