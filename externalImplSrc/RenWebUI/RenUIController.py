#!/usr/bin/env python
# encoding: utf-8
import json
from functools import wraps

import LocationContext
from Model.AuthorizationModel import AuthorizationModel
from Model.WebUILogModel import WebUILogModel
from SessionManager import SessionManager
from Utility.EncryptUtil import EncryptUtil
from Utility.InteractionUtil import InteractionUtil
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
            retSession, retGid = SessionManager.Login(username, EncryptUtil.EncryptSHA256(rawPassword))
            return retSession is not None, retSession, retGid
        except:
            return False, None, None

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
    def GetSessionLevel(session):
        """
        Get session level
        :param session: session id
        :return: session level value in int
        """
        try:
            return True, SessionManager.GetSession(session).Level
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
    Domain Management Methods
    """
    @adminRequireWarp
    @ExceptionWarp
    def DomainAdd(self, session, name, raw_password, corgan):
        """
        Add a domain.
        :param session: session id
        :param name: domain name
        :param raw_password: domain password
        :param corgan: domain binding COrgan location
        """
        pd = {"name": name, "password": raw_password, "corgan": corgan}
        return True, InteractionUtil.Send(LocationContext.URL_Domain_Add, pd)

    @adminRequireWarp
    @ExceptionWarp
    def DomainStop(self, session, name):
        """
        Ban a domain.
        :param session: session id
        :param name: domain's name to be stopped
        """
        pd = {"name": name, "status": 1}
        dt = InteractionUtil.Send(LocationContext.URL_Domain_Update, pd)
        return True, json.loads(dt["data"], encoding="utf8")

    @adminRequireWarp
    @ExceptionWarp
    def DomainResume(self, session, name):
        """
        Resume a domain.
        :param session: session id
        :param name: domain's name to be resumed
        """
        pd = {"name": name, "status": 0}
        dt = InteractionUtil.Send(LocationContext.URL_Domain_Update, pd)
        return True, json.loads(dt["data"], encoding="utf8")

    @authorizeRequireWarp
    @ExceptionWarp
    def DomainUpdate(self, session, name, new_corgan):
        """
        Update a domain info.
        :param session: session id
        :param name: domain's name to be updated
        :param new_corgan: domain new COrgan location
        """
        pd = {"name": name, "corgan": new_corgan}
        dt = InteractionUtil.Send(LocationContext.URL_Domain_Update, pd)
        return True, json.loads(dt["data"], encoding="utf8")

    @authorizeRequireWarp
    @ExceptionWarp
    def DomainGet(self, session, name):
        """
        Get a domain.
        :param session: session id
        :param name: domain to be retrieve
        """
        pd = {"name": name}
        dt = InteractionUtil.Send(LocationContext.URL_Domain_Get, pd)
        return True, json.loads(dt["data"], encoding="utf8")

    @adminRequireWarp
    @ExceptionWarp
    def DomainGetAll(self, session):
        """
        Get all domain as a list.
        :param session: session id
        """
        dt = InteractionUtil.Send(LocationContext.URL_Domain_GetAll)
        return True, json.loads(dt["data"], encoding="utf8")

    """
    Auth user Management Methods
    """
    @authorizeRequireWarp
    @ExceptionWarp
    def AuthUserAdd(self, session, name, domain, raw_password, gid):
        """
        Add an auth user.
        :param session: session id
        :param name: user name
        :param domain: domain name
        :param raw_password: user raw password
        :param gid: binding resource global id, empty string if none
        """
        pd = {"username": name, "domain": domain, "password": raw_password, "gid": gid, "level": 0}
        return True, InteractionUtil.Send(LocationContext.URL_AuthUser_Add, pd)

    @authorizeRequireWarp
    @ExceptionWarp
    def AuthUserStop(self, session, name, domain):
        """
        Ban an auth user.
        :param session: session id
        :param name: user name to be stopped
        :param domain: domain name
        """
        pd = {"username": name, "domain": domain, "status": 1}
        dt = InteractionUtil.Send(LocationContext.URL_AuthUser_Update, pd)
        return True, json.loads(dt["data"], encoding="utf8")

    @authorizeRequireWarp
    @ExceptionWarp
    def AuthUserResume(self, session, name, domain):
        """
        Resume an auth user.
        :param session: session id
        :param name: user name to be resumed
        :param domain: domain name
        """
        pd = {"username": name, "domain": domain, "status": 0}
        dt = InteractionUtil.Send(LocationContext.URL_AuthUser_Update, pd)
        return True, json.loads(dt["data"], encoding="utf8")

    @authorizeRequireWarp
    @ExceptionWarp
    def AuthUserUpdate(self, session, name, domain, new_password, new_gid):
        """
        Update an auth user info.
        :param session: session id
        :param name: user name to be updated
        :param domain: domain name
        :param new_password: new auth user password
        :param new_gid: new global id
        """
        pd = {"username": name, "domain": domain}
        updateFlag = False
        if new_gid is not None:
            pd["gid"] = new_gid
            updateFlag = True
        if new_password is not None:
            pd["password"] = new_password
            updateFlag = True
        if updateFlag is False:
            return True, True
        dt = InteractionUtil.Send(LocationContext.URL_AuthUser_Update, pd)
        return True, json.loads(dt["data"], encoding="utf8")

    @authorizeRequireWarp
    @ExceptionWarp
    def AuthUserGet(self, session, name, domain):
        """
        Get an auth user.
        :param session: session id
        :param name: user to be retrieve
        :param domain: domain name
        """
        pd = {"username": name, "domain": domain}
        dt = InteractionUtil.Send(LocationContext.URL_AuthUser_Get, pd)
        return True, json.loads(dt["data"], encoding="utf8")

    @authorizeRequireWarp
    @ExceptionWarp
    def AuthUserGetAllForDomain(self, session, domain):
        """
        Get all users as a list for a domain.
        :param session: session id
        :param domain: domain name
        """
        d = {"domain": domain}
        dt = InteractionUtil.Send(LocationContext.URL_AuthUser_GetAll, d)
        return True, json.loads(dt["data"], encoding="utf8")

    @adminRequireWarp
    @ExceptionWarp
    def AuthUserGetAll(self, session):
        """
        Get all users as a list.
        :param session: session id
        """
        d = {"domain": ""}
        dt = InteractionUtil.Send(LocationContext.URL_AuthUser_GetAll, d)
        return True, json.loads(dt["data"], encoding="utf8")

    """
    Process Management Methods
    """
    @authorizeRequireWarp
    @ExceptionWarp
    def ProcessGetAllForDomain(self, session, domain):
        """
        Get all processes as a list for a domain.
        :param session: session id
        :param domain: domain name
        """
        d = {"domain": domain}
        dt = InteractionUtil.Send(LocationContext.URL_Process_GetAllForDomain, d)
        return True, json.loads(dt["data"], encoding="utf8")

    @authorizeRequireWarp
    @ExceptionWarp
    def ProcessGetByPid(self, session, pid):
        """
        Get all processes as a list for a domain.
        :param session: session id
        :param pid: process global id
        """
        d = {"pid": pid}
        dt = InteractionUtil.Send(LocationContext.URL_Process_GetByPid, d)
        return True, json.loads(dt["data"], encoding="utf8")

    """
    Runtime Record Management Methods
    """
    @adminRequireWarp
    @ExceptionWarp
    def RuntimeRecordGetAll(self, session):
        """
        Get all runtime record.
        :param session: session id
        """
        dt = InteractionUtil.Send(LocationContext.URL_RTC_GetAll)
        return True, json.loads(dt["data"], encoding="utf8")

    @adminRequireWarp
    @ExceptionWarp
    def RuntimeRecordGetAllByDomain(self, session, domain):
        """
        Get all runtime record for a domain.
        :param session: session id
        :param domain: domain name
        """
        d = {"domain": domain}
        dt = InteractionUtil.Send(LocationContext.URL_RTC_GetForDomain, d)
        return True, json.loads(dt["data"], encoding="utf8")

    @authorizeRequireWarp
    @ExceptionWarp
    def RuntimeRecordGetAllByLauncher(self, session, launcher):
        """
        Get all runtime record for a launcher.
        :param session: session id
        :param domain: domain name
        """
        d = {"launcher": launcher}
        dt = InteractionUtil.Send(LocationContext.URL_RTC_GetForLauncher, d)
        return True, json.loads(dt["data"], encoding="utf8")

    @authorizeRequireWarp
    @ExceptionWarp
    def RuntimeRecordGetAllByRTID(self, session, rtid):
        """
        Get all runtime record for a launcher.
        :param session: session id
        :param rtid: runtime record id
        """
        d = {"rtid": rtid}
        dt = InteractionUtil.Send(LocationContext.URL_RTC_Get, d)
        return True, json.loads(dt["data"], encoding="utf8")

    @authorizeRequireWarp
    @ExceptionWarp
    def RuntimeLogGetByRTID(self, session, rtid):
        """
        Get all log for a runtime record.
        :param session: session id
        :param rtid: runtime record id
        """
        d = {"rtid": rtid}
        dt = InteractionUtil.Send(LocationContext.URL_RTC_GetLogByRTID, d)
        return True, json.loads(dt["data"], encoding="utf8")

    @authorizeRequireWarp
    @ExceptionWarp
    def RuntimeSpanTreeGetByRTID(self, session, rtid):
        """
        Get all log for a runtime record.
        :param session: session id
        :param rtid: runtime record id
        """
        d = {"rtid": rtid, "signature": GCC.INTERNAL_TOKEN}
        dt = InteractionUtil.Send(LocationContext.URL_RTC_GetSpanTreeByRTID, d)
        return True, json.loads(dt["data"], encoding="utf8")

    """
    Workitem Management Methods
    """
    @adminRequireWarp
    @ExceptionWarp
    def WorkitemGetAllByDomain(self, session, domain):
        """
        Get all workitem for a domain.
        :param session: session id
        :param domain: domain name
        """
        d = {"domain": domain}
        dt = InteractionUtil.Send(LocationContext.URL_Workitem_GetAllForDomain, d)
        return True, json.loads(dt["data"], encoding="utf8")

    @authorizeRequireWarp
    @ExceptionWarp
    def WorkitemGet(self, session, wid):
        """
        Get all workitem for a domain.
        :param session: session id
        :param wid: workitem id
        """
        d = {"wid": wid}
        dt = InteractionUtil.Send(LocationContext.URL_Workitem_Get, d)
        return True, json.loads(dt["data"], encoding="utf8")

    @authorizeRequireWarp
    @ExceptionWarp
    def WorkitemGetByParticipant(self, session, workerId):
        """
        Get all workitem for a participant worker.
        :param session: session id
        :param workerId: worker resource global id
        """
        d = {"workerId": workerId}
        dt = InteractionUtil.Send(LocationContext.URL_Workitem_GetAllForParticipant, d)
        return True, json.loads(dt["data"], encoding="utf8")

    @authorizeRequireWarp
    @ExceptionWarp
    def WorkitemAction(self, session, action, wid, workerId):
        """
        Do workitem action.
        :param session: session id
        :param action: action name
        :param wid: workitem id
        :param workerId: participant worker id
        """
        d = {"workitemId": wid, "workerId": workerId, "signature": GCC.INTERNAL_TOKEN}
        dt = InteractionUtil.Send(LocationContext.URL_Workitem_ActionPrefix + action, d)
        return True, json.loads(dt["data"], encoding="utf8")

    AuthorizationModel.Initialize(forced=True)
    WebUILogModel.Initialize(forced=True)


RenUIControllerInstance = RenUIController()
