#!/usr/bin/env python
# encoding: utf-8
"""
@module : Position
@author : Rinkako
@time   : 2017/12/27
"""
from Entity.Organizable import Organizable


class Position(Organizable):
    """
    entity class for `position` data package.
    Position means a worker in what place of a department.
    """

    def __init__(self, name, description, note, belongTo, reportTo):
        """
        Create a new position.
        :param name: position unique name
        :param description: description of this position
        :param note: note of this position
        :param belongTo: belong to which group
        """
        Organizable.__init__(self, "", name, description, note)
        self.BelongToGroup = belongTo
        self.ReportToPosition = reportTo

    def ToJsonDict(self):
        """
        Get json dict.
        """
        return {
            'GlobalId': self.GlobalId,
            'Name': self.Name,
            'Description': self.Description,
            'Note': self.Note,
            'BelongToGroupId': self.BelongToGroup,
            'ReportToPosition': self.ReportToPosition
        }
