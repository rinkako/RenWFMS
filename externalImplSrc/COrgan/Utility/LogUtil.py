#!/usr/bin/env python
# encoding: utf-8
"""
@module : LogUtil
@author : Rinkako
@time   : 2017/12/27
"""
import time
from Model.RuntimeLogModel import RuntimeLogModel


class LogUtil:
    """
    This utility contains functions for runtime information storing and notifying,
    and provides a common interface of logging the runtime state.
    """
    def __init__(self):
        pass

    @staticmethod
    def Log(message, label="", level="Notify", always_persist=False, dp=0):
        """
        Log and notify a runtime message.
        :param message: the message to be notified
        :param label: message label
        :param level: message importance level (notify, info, warning, error)
        :param always_persist: store to the steady even when its level is "notify"
        :param dp: depth of exception stack
        """
        ct, ts = LogUtil.GetTimeStampString()
        print "[%s](%s) %s - %s" % (level.upper(), label, ts, message)
        if always_persist is True or level != "Notify":
            RuntimeLogModel.LogToSteady(label, level, message, ct, dp)

    @staticmethod
    def ErrorLog(message, label, dp=0):
        """
        Log and notify a runtime error.
        :param message: the message to be notified
        :param label: message label
        :param dp: depth of exception stack
        """
        LogUtil.Log(message, label, "Error", True, dp)

    @staticmethod
    def GetTimeStampString():
        """
        Get the current timestamp in string format.
        :return: a string represent current time, like "2017-07-11 13:56:35.940"
        """
        ct = time.time()
        data_head = time.strftime("%Y-%m-%d %H:%M:%S", time.localtime(ct))
        return ct, "%s.%03d" % (data_head, (ct - long(ct)) * 1000)

    @staticmethod
    def GetTimeStampStringSimple():
        """
        Get the current timestamp in string format, can save as DATETIME.
        :return: a string represent current time, like "2017-07-11 13:56:35"
        """
        return time.strftime("%Y-%m-%d %H:%M:%S", time.localtime(time.time()))
