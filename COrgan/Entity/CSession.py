#!/usr/bin/env python
# encoding: utf-8
"""
@module : CSession
@author : Rinkako
@time   : 2018/1/4
"""


class CSession:
    """
    COrgan Platform Connection Session entity

    The class is used to save the state of a connection between
    gateway and services invoker.
    """

    def __init__(self, session_id, username, encrypted_password, begin_time):
        # Session uuid
        self.sid = session_id
        # Login username
        self.Username = username
        # Login encrypted password
        self.PasswordWithEncryption = encrypted_password
        # User Level
        self.Level = 0
        # Login timestamp
        self.BeginTimeStamp = begin_time
        # Logout timestamp
        self.EndTimeStamp = None
        # Tag object
        self.Tag = None
        # Alive
        self.IsAlive = True
