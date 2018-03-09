#!/usr/bin/env python
# encoding: utf-8
"""
@module : Organizable
@author : Rinkako
@time   : 2017/12/27
"""
from abc import ABCMeta


class Organizable:
    """
    entity class for organizable group, which is the basic data
    package for intelligent resource like Team, Department, etc.
    """
    __metaclass__ = ABCMeta

    def __init__(self, Id, name, description, note):
        self.GlobalId = Id
        self.Name = name
        self.Description = description
        self.Note = note
