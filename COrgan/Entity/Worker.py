#!/usr/bin/env python
# encoding: utf-8
"""
@module : Worker
@author : Rinkako
@time   : 2017/12/27
"""
from abc import ABCMeta


class Worker:
    """
    entity class for worker, which is the basic data package
    for intelligent resource like Human or Agent.
    """
    __metaclass__ = ABCMeta

    def __init__(self, Id, note):
        """
        Create new worker instance.
        :param Id: unique id
        :param note: description of this worker
        """
        self.GlobalId = Id
        self.Note = note
