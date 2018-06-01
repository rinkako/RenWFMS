#!/usr/bin/env python
# encoding: utf-8
from functools import wraps

from Model.AuthorizationModel import AuthorizationModel
from Model.WebUILogModel import WebUILogModel
from SessionManager import SessionManager
from Utility.EncryptUtil import EncryptUtil
from Utility.LogUtil import LogUtil
import GlobalConfigContext as GCC


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
                return False, RenUIController.Unauthorized(session)
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
                return False, RenUIController.Unauthorized(session)
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


class RenUIController:
    """
    Ren Web UI Controller.
    All requests will be handled here without difference of view.
    """

    def __init__(self):
        pass

    @staticmethod
    def Auth(username, rawPassword):
        """
        Get authorization token by username and password
        :param username: unique username string, in pattern of username@domain
        :param rawPassword: password without encryption
        :return:
        """
        try:
            retVal = SessionManager.Login(username, EncryptUtil.EncryptSHA256(rawPassword))
            return retVal is not None, retVal
        except:
            return None, False

    @staticmethod
    def Disconnect(token):
        """
        Destroy a auth token
        :param token: auth token string
        :return:
        """
        return True, SessionManager.Logout(token)

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
            print "Exception in WebUI: %s" % str(e)
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
                        RenUIController.__name__, "Warning", True)
        except Exception as e:
            print "Exception in RenWebUI authorization check: %s" % str(e)
        finally:
            return GCC.UNAUTHORIZED

    """
    COrgan Platform User Management Methods
    """
    @adminRequireWarp
    def PlatformUserAdd(self, session, username, encrypted_password, level):
        """
        Add a domain.
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


    AuthorizationModel.Initialize(forced=True)
    WebUILogModel.Initialize(forced=True)
