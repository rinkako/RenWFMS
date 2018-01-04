#!/usr/bin/env python
# encoding: utf-8
"""
@module : CController
@author : Rinkako
@time   : 2018/1/4
"""
import GlobalConfigContext as GCC
from Model.UserModel import UserModel
from SessionManager import SessionManager
from Utility.LogUtil import LogUtil


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
    @staticmethod
    def PlatformUserAdd(session, username, encrypted_password, level):
        """
        Add a platform user.
        :param session: session id
        :param username: new user's name
        :param encrypted_password: new user's password with encryption
        :param level: new user level flag
        """
        try:
            if SessionManager.CheckAdmin(session) is True:
                success_flag = UserModel.Add(username, encrypted_password, level)
                return True, success_flag
            else:
                return False, CController._Unauthorized(session)
        except Exception as e:
            print "Exception in COrgan: %s" % str(e)
            return False, e
    
    @staticmethod
    def PlatformUserRemove(session, username):
        """
        Remove a platform user.
        :param session: session id
        :param username: user's name to be removed
        """
        try:
            if SessionManager.CheckAdmin(session) is True:
                success_flag = UserModel.Delete(username)
                return True, success_flag
            else:
                return False, CController._Unauthorized(session)
        except Exception as e:
            print "Exception in COrgan: %s" % str(e)
            return False, e
    
    @staticmethod
    def PlatformUserUpdate(session, username, new_encrypted_password, new_level):
        """
        Update a platform user.
        :param session: session id
        :param username: user's name to be updated
        :param new_encrypted_password: user's new password with encryption
        :param new_level: new level flag of user
        """
        try:
            if SessionManager.Check(session) is True:
                success_flag = UserModel.Update(username, new_encrypted_password, new_level)
                return True, success_flag
            else:
                return False, CController._Unauthorized(session)
        except Exception as e:
            print "Exception in COrgan: %s" % str(e)
            return False, e
    
    @staticmethod
    def PlatformUserGet(session, username):
        """
        Get a platform user.
        :param session: session id
        :param username: user's name to be retrieve
        """
        try:
            if SessionManager.Check(session) is True:
                return True, UserModel.Retrieve(username)
            else:
                return False, CController._Unauthorized(session)
        except Exception as e:
            print "Exception in COrgan: %s" % str(e)
            return False, e
    
    @staticmethod
    def PlatformUserGetAll(session):
        """
        Get all platform user as a list.
        :param session: session id
        """
        try:
            if SessionManager.CheckAdmin(session) is True:
                return True, UserModel.RetrieveAllValid()
            else:
                return False, CController._Unauthorized(session)
        except Exception as e:
            print "Exception in COrgan: %s" % str(e)
            return False, e

    """
    Worker Methods
    """
    @staticmethod
    def AddHuman(personId, firstName, lastName, note):
        pass

    @staticmethod
    def RemoveHuman(personId):
        pass

    @staticmethod
    def UpdateHuman(personId, **kwargs):
        pass

    @staticmethod
    def RetrieveHuman(personId):
        pass

    @staticmethod
    def RetrieveAllHuman():
        pass

    @staticmethod
    def AddAgent(name, location, rType, note):
        pass

    @staticmethod
    def RemoveAgent(name):
        pass

    @staticmethod
    def UpdateAgent(name, **kwargs):
        pass

    @staticmethod
    def RetrieveAgent(name):
        pass

    @staticmethod
    def RetrieveAllAgent():
        pass

    @staticmethod
    def RetrieveAllWorker():
        pass

    """
    Organization Methods
    """
    @staticmethod
    def AddGroup(name, description, note, belongToName, groupType):
        pass

    @staticmethod
    def RemoveGroup(name):
        pass

    @staticmethod
    def UpdateGroup(name, **kwargs):
        pass

    @staticmethod
    def RetrieveGroup(name):
        pass

    @staticmethod
    def RetrieveAllGroup():
        pass

    @staticmethod
    def AddPosition(name, description, note, belongToName):
        pass

    @staticmethod
    def RemovePosition(name):
        pass

    @staticmethod
    def UpdatePosition(name, **kwargs):
        pass

    @staticmethod
    def RetrievePosition(name):
        pass

    @staticmethod
    def RetrieveAllPosition():
        pass

    """
    Connection Constrain Methods
    """
    @staticmethod
    def RetrieveHumanInGroup(groupName):
        pass

    @staticmethod
    def RetrieveAgentInGroup(groupName):
        pass

    @staticmethod
    def RetrieveHumanInPosition(posName):
        pass

    @staticmethod
    def RetrieveAgentInPosition(posName):
        pass

    @staticmethod
    def RetrieveHumanInWhatGroup(personId):
        pass

    @staticmethod
    def RetrieveHumanInWhatPosition(personId):
        pass

    @staticmethod
    def SpanTreeOfGroup(groupName):
        pass

    @staticmethod
    def SpanTreeOfPosition(posName):
        pass

    @staticmethod
    def SpanTreeOfOrganizationInGroup():
        pass

    @staticmethod
    def SpanTreeOfOrganizationInPosition():
        pass

    @staticmethod
    def AddHumanToGroup(personId, groupName):
        pass

    @staticmethod
    def RemoveHumanFromGroup(personId, groupName):
        pass

    @staticmethod
    def AddHumanPosition(personId, posName):
        pass

    @staticmethod
    def RemoveHumanPosition(personId, posName):
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
