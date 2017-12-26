#!/usr/bin/env python
# encoding: utf-8
"""
@module : Human
@author : Rinkako
@time   : 2017/12/26
"""


class Human:
    """
    Entity class for `human` data package.
    """

    def __init__(self, humanId, fName, lName):
        """
        Create new human instance.
        :param humanId: unique human id
        :param fName: first name
        :param lName: last name
        """
        self.Id = humanId
        self.firstName = fName
        self.lastName = lName
        self.roleList = []
        self.departmentList = []
        self.capabilityList = []

    def AddRole(self, roleId):
        """
        Add a role to this human.
        :param roleId: role id
        :return: whether operation success
        """
        if roleId in self.roleList:
            return False
        self.roleList.append(roleId)
        return True

    def RemoveRole(self, roleId):
        """
        Remove a role from this human.
        :param roleId: role id
        :return: whether operation success
        """
        if roleId not in self.roleList:
            return False
        self.roleList.remove(roleId)
        return True

    def AddDepartment(self, departmentId):
        """
        Add this human to a department.
        :param departmentId: department id
        :return: whether operation success
        """
        if departmentId in self.departmentList:
            return False
        self.departmentList.append(departmentId)
        return True

    def RemoveDepartment(self, departmentId):
        """
        Remove this human from a department.
        :param departmentId: department id
        :return: whether operation success
        """
        if departmentId not in self.departmentList:
            return False
        self.departmentList.remove(departmentId)
        return True

    def AddCapability(self, capabilityId):
        """
        Add a capability to this human.
        :param capabilityId: capability id
        :return: whether operation success
        """
        if capabilityId in self.capabilityList:
            return False
        self.capabilityList.append(capabilityId)
        return True

    def RemoveCapability(self, capabilityId):
        """
        Remove a capability from this human.
        :param capabilityId: capability id
        :return: whether operation success
        """
        if capabilityId not in self.capabilityList:
            return False
        self.capabilityList.remove(capabilityId)
        return True
