#!/usr/bin/env python
# encoding: utf-8
"""
@module : Department
@author : Rinkako
@time   : 2017/12/27
"""
from Entity.Organizable import Organizable


class Department(Organizable):
    """
    Entity class for `department` data package.
    Department is a work place of long-term grouped workers.
    """

    def __init__(self, name, description, note, belongTo):
        """
        Create a new department.
        :param name: unique id name
        :param description: description of this department
        :param note: note of this department
        :param belongTo: belong to which department
        """
        Organizable.__init__(self, "", name, description, note)
        self.BelongToDepartment = belongTo
