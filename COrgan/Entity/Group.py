#!/usr/bin/env python
# encoding: utf-8
"""
@module : Group
@author : Rinkako
@time   : 2017/12/27
"""
from Entity.Organizable import Organizable


class Group(Organizable):
    """
    Entity class for `group` data package.
    Group is a set of workers in some types.
    """

    def __init__(self, name, description, note, belongTo, groupType):
        """
        Create a new group.
        :param name: unique id name
        :param description: description of this group
        :param note: note of this group
        :param belongTo: belong to which group
        """
        Organizable.__init__(self, "", name, description, note)
        self.BelongToGroupId = belongTo
        self.GroupType = groupType


class GroupType:
    """
    Enum: Group Type
    """

    def __init__(self):
        pass

    Group = 1

    Team = 2

    Department = 4

    Cluster = 8

    Division = 16

    Branch = 32

    Unit = 64