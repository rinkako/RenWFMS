#!/usr/bin/env python
# encoding: utf-8
"""
@module : Worker
@author : Rinkako
@time   : 2017/12/27
"""
from abc import ABCMeta


class Worker:
    """
    Entity class for worker, which is the basic data package
    for intelligent resource like Human or Agent.
    """
    __metaclass__ = ABCMeta

    def __init__(self, Id, note):
        """
        Create new worker instance.
        :param Id: unique id
        :param note: description of this worker
        """
        self.Id = Id
        self.Note = note
        self.positionList = []
        self.departmentList = []
        self.capabilityList = []
        self.privilegeList = []

    def AddPosition(self, positionId):
        """
        Add a position to this worker.
        :param positionId: position id
        :return: whether operation success
        """
        if positionId in self.positionList:
            return False
        self.positionList.append(positionId)
        return True

    def RemovePosition(self, positionId):
        """
        Remove a position from this worker.
        :param positionId: position id
        :return: whether operation success
        """
        if positionId not in self.positionList:
            return False
        self.positionList.remove(positionId)
        return True

    def AddDepartment(self, departmentId):
        """
        Add this worker to a department.
        :param departmentId: department id
        :return: whether operation success
        """
        if departmentId in self.departmentList:
            return False
        self.departmentList.append(departmentId)
        return True

    def RemoveDepartment(self, departmentId):
        """
        Remove this worker from a department.
        :param departmentId: department id
        :return: whether operation success
        """
        if departmentId not in self.departmentList:
            return False
        self.departmentList.remove(departmentId)
        return True

    def AddCapability(self, capabilityId):
        """
        Add a capability to this worker.
        :param capabilityId: capability id
        :return: whether operation success
        """
        if capabilityId in self.capabilityList:
            return False
        self.capabilityList.append(capabilityId)
        return True

    def RemoveCapability(self, capabilityId):
        """
        Remove a capability from this worker.
        :param capabilityId: capability id
        :return: whether operation success
        """
        if capabilityId not in self.capabilityList:
            return False
        self.capabilityList.remove(capabilityId)
        return True

    def AddPrivilege(self, privilege):
        """
        Add a privilege to this worker.
        :param privilege: privilege id name
        :return: whether operation success
        """
        if privilege in self.privilegeList:
            return False
        self.privilegeList.append(privilege)
        return True

    def RemovePrivilege(self, privilege):
        """
        Remove a privilege from this worker.
        :param privilege: privilege id name
        :return: whether operation success
        """
        if privilege not in self.privilegeList:
            return False
        self.privilegeList.remove(privilege)
        return True
