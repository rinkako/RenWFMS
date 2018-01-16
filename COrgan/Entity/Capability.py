#!/usr/bin/env python
# encoding: utf-8
"""
@module : Capability
@author : Rinkako
@time   : 2018/1/10
"""
from Entity.Organizable import Organizable


class Capability(Organizable):
    """
    entity class for `capability` data package.
    Group is a set of workers having a specific capability.
    """

    def __init__(self, name, description, note):
        """
        Create a new capability.
        :param name: unique id name
        :param description: description of this group
        :param note: note of this group
        """
        Organizable.__init__(self, "", name, description, note)

    def ToJsonDict(self):
        """
        Get json dict.
        :return:
        """
        return {
            'GlobalId': self.GlobalId,
            'Name': self.Name,
            'Description': self.Description,
            'Note': self.Note
        }
