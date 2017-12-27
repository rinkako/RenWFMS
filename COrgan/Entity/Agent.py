#!/usr/bin/env python
# encoding: utf-8
"""
@module : Agent
@author : Rinkako
@time   : 2017/12/27
"""
from Entity.Worker import Worker


class Agent(Worker):
    """
    Entity class for `agent` data package.
    """

    def __init__(self, agentId, name, location, note, reentrantType = AgentType.Reentrant):
        """
        Create new agent instance.
        :param agentId: unique agent id
        :param name: name
        :param location: call location of this agent, usually URL
        :param note: description of this agent
        :param reentrantType: agent reentrant flag
        """
        Worker.__init__(self, agentId, note)
        self.Name = name
        self.Location = location
        self.Type = reentrantType


class AgentType:
    """
    Enum of agent type
    """
    def __init__(self):
        pass

    Reentrant = 1

    NotReentrant = 2
