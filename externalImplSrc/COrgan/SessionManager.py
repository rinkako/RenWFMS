#!/usr/bin/env python
# encoding: utf-8
"""
@module : SessionManager
@author : Rinkako
@time   : 2018/1/4
"""
import uuid
import time

import GlobalConfigContext
from Entity.CSession import CSession


class SessionManager:
    """
    COrgan Platform Session Manager

    This class is used to maintain the session between engine and outside
    part of the project. User login/logout action was perform here and its
    state is maintained.
    """

    def __init__(self):
        pass

    @staticmethod
    def Login(username, password_encrypted):
        """
        Log in a session.
        :param username: user name string
        :param password_encrypted: password string with specific encryption
        :return session id
        """
        session_id = str(uuid.uuid1())
        while SessionManager.ActiveSessionDict.get(session_id, None) is not None:
            session_id = str(uuid.uuid1())
        session_id = "%s_%s" % (session_id, username)
        cur_session = CSession(session_id, username, password_encrypted, time.time())
        from Model.UserModel import UserModel
        uObj = UserModel.Retrieve(username)
        assert uObj is not None
        cur_session.Level = uObj["level"]
        SessionManager.ActiveSessionDict[session_id] = cur_session
        return session_id

    @staticmethod
    def Logout(session):
        """
        Log out a session.
        :param session: session id of logging out connection
        :return Did successfully log out
        """
        cur_session = SessionManager.GetSession(session)
        if cur_session is not None:
            cur_session.EndTimeStamp = time.time()
            cur_session.IsAlive = False
            return True
        else:
            return False

    @staticmethod
    def Check(session):
        """
        Check a session is valid.
        :param session: session id
        :return Is this session valid
        """
        cur = SessionManager.GetSession(session)
        return cur is not None and cur.IsAlive is True

    @staticmethod
    def CheckAdmin(session):
        """
        Check a session is valid and admin certificated.
        :param session: session id
        :return: Is this session valid and admin
        """
        cur = SessionManager.GetSession(session)
        return cur is not None and cur.Level >= 1 and cur.IsAlive is True

    @staticmethod
    def GetSession(session):
        # type: (str) -> CSession
        """
        Get session instance by its session id.
        :param session: the session want to retrieve
        """
        return SessionManager.ActiveSessionDict.get(session, None)

    @staticmethod
    def Count():
        """
        Get the count of current active session.
        :return current connection session count
        """
        return len(filter(lambda x: x.IsAlive is True, SessionManager.ActiveSessionDict))

    @staticmethod
    def Reset():
        """
        Reset the session manager, clear all cached session.
        """
        SessionManager.ActiveSessionDict.clear()

    @staticmethod
    def SaveSteady():
        """
        Write session information to the steady memory.
        """
        pass

    @staticmethod
    def LoadSteady():
        """
        Load session information to the steady memory.
        """
        pass

    """ Active Session Dictionary """
    ActiveSessionDict = {}


# Engine Calling Session
tr = CSession(GlobalConfigContext.AUTH_INTERNAL_SESSION, '&&EngineCall', '&&EngineCall', time.time())
tr.Level = 1
SessionManager.ActiveSessionDict[GlobalConfigContext.AUTH_INTERNAL_SESSION] = tr
