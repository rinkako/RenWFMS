#!/usr/bin/env python
# encoding: utf-8
from Model.AuthorizationModel import AuthorizationModel
from Model.WebUILogModel import WebUILogModel
from SessionManager import SessionManager
from Utility.EncryptUtil import EncryptUtil


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
        retVal = SessionManager.Login(username, EncryptUtil.EncryptSHA256(rawPassword))
        return retVal is not None, retVal

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

    AuthorizationModel.Initialize(forced=True)
    WebUILogModel.Initialize(forced=True)
