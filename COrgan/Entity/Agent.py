#!/usr/bin/env python
# encoding: utf-8
"""
@module : Agent
@author : Rinkako
@time   : 2017/12/27
"""
import threading
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
        if self.Type == AgentType.NotReentrant:
            self._mutex = threading.Lock()
        self._semaphore = 0

    def PreviewAgent(self):
        """
        Deprecate.
        """
        if self.Type == AgentType.NotReentrant:
            if self._mutex is not None:
                self._mutex.acquire()
        self._semaphore += 1

    def TryPreviewAgent(self):
        """
        Deprecate.
        """
        if self.Type == AgentType.Reentrant:
            self._semaphore += 1
            return True
        else:
            if self._semaphore > 0:
                return False
            elif self._mutex is not None:
                self._mutex.acquire()
                self._semaphore += 1
                return True
            else:
                return False

    def FinishAgent(self):
        """
        Deprecate.
        """
        if self.Type == AgentType.NotReentrant:
            if self._mutex is not None:
                self._mutex.release()
        self._semaphore -= 1


class AgentType:
    """
    Enum of agent type
    """
    def __init__(self):
        pass

    Reentrant = 1

    NotReentrant = 2
