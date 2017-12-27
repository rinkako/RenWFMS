#!/usr/bin/env python
# encoding: utf-8
"""
@module : Team
@author : Rinkako
@time   : 2017/12/27
"""
from Entity.Organizable import Organizable


class Team(Organizable):
    """
    Entity class for `team` data package.
    Team is a work place of short-term grouped workers.
    """

    def __init__(self, teamId, name, description, note, belongTo):
        """
        Create a new team.
        :param teamId:
        :param name:
        :param description:
        :param note:
        :param belongTo:
        """
        Organizable.__init__(self, teamId, name, description, note)
        self.BelongToDepartmentId = belongTo
