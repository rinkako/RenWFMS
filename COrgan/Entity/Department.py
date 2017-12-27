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

    def __init__(self, departmentId, name, description, note, belongTo):
        """
        Create a new department.
        :param departmentId:
        :param name:
        :param description:
        :param note:
        :param belongTo:
        """
        Organizable.__init__(self, departmentId, name, description, note)
        self.BelongToDepartmentId = belongTo
