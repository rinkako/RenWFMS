#!/usr/bin/env python
# encoding: utf-8
"""
@module : AbstractDAO
@author : Rinkako
@time   : 2017/12/27
"""
from abc import ABCMeta, abstractmethod


class AbstractDAO:
    """
    COrgan Data Access Object Class

    This abstract class is the base class for all DAO.
    """
    __metaclass__ = ABCMeta

    def __init__(self):
        """
        Create a new DAO.
        """
        # connection object
        self.Connection = None
        # binding data source object
        self.DataSource = None
        # config cache
        self.Config = None
        # connection built up ready flag
        self._connectedFlag = False

    def Dispose(self):
        """
        Dispose the DAO resources.
        """
        if self.Connection is not None:
            self._dispose()
            self.Connection = None
        self._connectedFlag = False

    def Initialize(self):
        try:
            if self._connectedFlag is True:
                return
            self._initialize()
            self._connectedFlag = True
        except:
            self._connectedFlag = False

    @abstractmethod
    def _initialize(self):
        """
        Abstract method for initializing the specific DAO.
        """
        pass

    @abstractmethod
    def _dispose(self):
        """
        Abstract method for disposing the specific DAO.
        """
        pass
