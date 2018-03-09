#!/usr/bin/env python
# encoding: utf-8
"""
@module : Human
@author : Rinkako
@time   : 2017/12/26
"""
from Entity.Worker import Worker


class Human(Worker):
    """
    entity class for `human` data package.
    """

    def __init__(self, humanId, fName, lName, note):
        """
        Create new human instance.
        :param humanId: unique human id
        :param fName: first name
        :param lName: last name
        :param note: description of this human
        """
        Worker.__init__(self, "", note)
        self.PersonId = humanId
        self.FirstName = fName
        self.LastName = lName

    def ToJsonDict(self):
        """
        Get json dict.
        :return:
        """
        return {
            'GlobalId': self.GlobalId,
            'Note': self.Note,
            'PersonId': self.PersonId,
            'FirstName': self.FirstName,
            'LastName': self.LastName
        }
