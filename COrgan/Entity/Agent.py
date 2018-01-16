#!/usr/bin/env python
# encoding: utf-8
"""
@module : Agent
@author : Rinkako
@time   : 2017/12/27
"""
from Entity.Worker import Worker


class AgentType:
    """
    Enum of agent type
    """
    def __init__(self):
        pass

    Reentrant = 0

    NotReentrant = 1


class Agent(Worker):
    """
    entity class for `agent` data package.
    """

    def __init__(self, name, location, note, reentrantType=AgentType.Reentrant):
        """
        Create new agent instance.
        :param name: name
        :param location: call location of this agent, usually URL
        :param note: description of this agent
        :param reentrantType: agent reentrant flag
        """
        Worker.__init__(self, '', note)
        self.Name = name
        self.Location = location
        self.Type = reentrantType

    def ToJsonDict(self):
        """
        Get json dict.
        :return:
        """
        return {
            'GlobalId': self.GlobalId,
            'Note': self.Note,
            'Name': self.Name,
            'Location': self.Location,
            'Type': self.Type
        }
