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
    entity class for `group` data package.
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

    def ToJsonDict(self):
        """
        Get json dict.
        """
        return {
            'GlobalId': self.GlobalId,
            'Name': self.Name,
            'Description': self.Description,
            'Note': self.Note,
            'BelongToGroupId': self.BelongToGroupId,
            'GroupType': self.GroupType
        }


class GroupType:
    """
    Enum: Group Type
    """

    def __init__(self):
        pass

    Department = 0

    Team = 1

    Group = 2

    Cluster = 3

    Division = 4

    Branch = 5

    Unit = 6
