#!/usr/bin/env python
# encoding: utf-8
"""
@module : __init__.py
@author : Rinkako
@time   : 2018/1/4

This module is a Flask blueprint for web service request handing,
all RESTful APIs are defined here.
"""
from flask import Blueprint, request
from CGateway import CGateway

restfulBp = Blueprint('Restful', __name__, url_prefix='/api')


"""
Restful API Routers
"""


@restfulBp.route('/')
def home():
    return "Welcome to COrgan Gateway."


@restfulBp.route('/connect', methods=["GET", "POST"])
def Connect():
    username = request.values.get("username")
    password = request.values.get("password")
    argd = {"#username": username,
            "#password": password}
    return StartDash(argd, CGateway.Connect, _ArgsException)


@restfulBp.route("/disconnect", methods=["GET", "POST"])
def DisConnect():
    sid = request.values.get('session')
    argd = {"#session": sid}
    return StartDash(argd, CGateway.Disconnect, _ArgsException)


@restfulBp.route("/check", methods=["GET", "POST"])
def Check():
    sid = request.values.get('session')
    argd = {"#session": sid}
    return StartDash(argd, CGateway.CheckConnect, _ArgsException)


@restfulBp.route("/config/getorganization", methods=["GET", "POST"])
def GetOrganization():
    sid = request.values.get('session')
    argd = {"#session": sid}
    return StartDash(argd, CGateway.GetOrganization, _ArgsException)


@restfulBp.route("/config/getdataversion", methods=["GET", "POST"])
def GetDataVersion():
    sid = request.values.get('session')
    argd = {"#session": sid}
    return StartDash(argd, CGateway.GetDataVersion, _ArgsException)


@restfulBp.route("/human/getall", methods=["GET", "POST"])
def GetAllHuman():
    sid = request.values.get('session')
    argd = {"#session": sid}
    return StartDash(argd, CGateway.RetrieveAllHuman, _ArgsException)


@restfulBp.route("/agent/getall", methods=["GET", "POST"])
def GetAllAgent():
    sid = request.values.get('session')
    argd = {"#session": sid}
    return StartDash(argd, CGateway.RetrieveAllAgent, _ArgsException)


@restfulBp.route("/group/getall", methods=["GET", "POST"])
def GetAllGroup():
    sid = request.values.get('session')
    argd = {"#session": sid}
    return StartDash(argd, CGateway.RetrieveAllGroups, _ArgsException)


@restfulBp.route("/position/getall", methods=["GET", "POST"])
def GetAllPosition():
    sid = request.values.get('session')
    argd = {"#session": sid}
    return StartDash(argd, CGateway.RetrieveAllPositions, _ArgsException)


@restfulBp.route("/capability/getall", methods=["GET", "POST"])
def GetAllCapability():
    sid = request.values.get('session')
    argd = {"#session": sid}
    return StartDash(argd, CGateway.RetrieveAllCapabilities, _ArgsException)


@restfulBp.route("/relation/gethumangroup", methods=["GET", "POST"])
def GetHumanGroup():
    sid = request.values.get('session')
    personId = request.values.get('personId')
    argd = {"#session": sid,
            "#personId": personId}
    return StartDash(argd, CGateway.RetrieveHumanInWhatGroup, _ArgsException)


@restfulBp.route("/relation/gethumanposition", methods=["GET", "POST"])
def GetHumanPosition():
    sid = request.values.get('session')
    personId = request.values.get('personId')
    argd = {"#session": sid,
            "#personId": personId}
    return StartDash(argd, CGateway.RetrieveHumanInWhatPosition, _ArgsException)


@restfulBp.route("/relation/gethumancapability", methods=["GET", "POST"])
def GetHumanCapability():
    sid = request.values.get('session')
    personId = request.values.get('personId')
    argd = {"#session": sid,
            "#personId": personId}
    return StartDash(argd, CGateway.RetrieveHumanWithWhatCapability, _ArgsException)


@restfulBp.route("/relation/getagentgroup", methods=["GET", "POST"])
def GetAgentGroup():
    sid = request.values.get('session')
    personId = request.values.get('personId')
    argd = {"#session": sid,
            "#personId": personId}
    return StartDash(argd, CGateway.RetrieveAgentInWhatGroup, _ArgsException)


@restfulBp.route("/relation/getagentposition", methods=["GET", "POST"])
def GetAgentPosition():
    sid = request.values.get('session')
    personId = request.values.get('personId')
    argd = {"#session": sid,
            "#personId": personId}
    return StartDash(argd, CGateway.RetrieveAgentInWhatPosition, _ArgsException)


@restfulBp.route("/relation/getagentcapability", methods=["GET", "POST"])
def GetAgentCapability():
    sid = request.values.get('session')
    personId = request.values.get('personId')
    argd = {"#session": sid,
            "#personId": personId}
    return StartDash(argd, CGateway.RetrieveAgentWithWhatCapability(), _ArgsException)


@restfulBp.route("/relation/getgrouphuman", methods=["GET", "POST"])
def GetGroupHuman():
    sid = request.values.get('session')
    orgName = request.values.get('name')
    argd = {"#session": sid,
            "#name": orgName}
    return StartDash(argd, CGateway.RetrieveHumanInGroup, _ArgsException)


@restfulBp.route("/relation/getpositionhuman", methods=["GET", "POST"])
def GetPositionHuman():
    sid = request.values.get('session')
    orgName = request.values.get('name')
    argd = {"#session": sid,
            "#name": orgName}
    return StartDash(argd, CGateway.RetrieveHumanInPosition, _ArgsException)


@restfulBp.route("/relation/getcapabilityhuman", methods=["GET", "POST"])
def GetCapabilityHuman():
    sid = request.values.get('session')
    orgName = request.values.get('name')
    argd = {"#session": sid,
            "#name": orgName}
    return StartDash(argd, CGateway.RetrieveHumanWithCapability, _ArgsException)


@restfulBp.route("/relation/getgroupagent", methods=["GET", "POST"])
def GetGroupAgent():
    sid = request.values.get('session')
    orgName = request.values.get('name')
    argd = {"#session": sid,
            "#name": orgName}
    return StartDash(argd, CGateway.RetrieveAgentInGroup, _ArgsException)


@restfulBp.route("/relation/getpositionagent", methods=["GET", "POST"])
def GetPositionAgent():
    sid = request.values.get('session')
    orgName = request.values.get('name')
    argd = {"#session": sid,
            "#name": orgName}
    return StartDash(argd, CGateway.RetrieveAgentInPosition, _ArgsException)


@restfulBp.route("/relation/getcapabilityagent", methods=["GET", "POST"])
def GetCapabilityAgent():
    sid = request.values.get('session')
    orgName = request.values.get('name')
    argd = {"#session": sid,
            "#name": orgName}
    return StartDash(argd, CGateway.RetrieveAgentWithCapability, _ArgsException)


@restfulBp.route("/ns/getworkeringroup", methods=["GET", "POST"])
def GetWorkerInGroup():
    token = request.values.get('token')
    renid = request.values.get('renid')
    nsid = request.values.get('nsid')
    groupName = request.values.get('groupName')
    argd = {"#token": token,
            "#renid": renid,
            "#nsid": nsid,
            "#groupName": groupName}
    return StartDash(argd, CGateway.RetrieveWorkerInGroup, _ArgsException)


@restfulBp.route("/ns/getworkerinposition", methods=["GET", "POST"])
def GetWorkerInPosition():
    token = request.values.get('token')
    renid = request.values.get('renid')
    nsid = request.values.get('nsid')
    positionName = request.values.get('positionName')
    argd = {"#token": token,
            "#renid": renid,
            "#nsid": nsid,
            "#positionName": positionName}
    return StartDash(argd, CGateway.RetrieveWorkerInPosition, _ArgsException)


@restfulBp.route("/ns/getworkerwithcapability", methods=["GET", "POST"])
def GetWorkerWithCapability():
    token = request.values.get('token')
    renid = request.values.get('renid')
    nsid = request.values.get('nsid')
    capabilityName = request.values.get('capabilityName')
    argd = {"#token": token,
            "#renid": renid,
            "#nsid": nsid,
            "#capabilityName": capabilityName}
    return StartDash(argd, CGateway.RetrieveWorkerInCapability, _ArgsException)


@restfulBp.route("/ns/getworkerinorganizable", methods=["GET", "POST"])
def GetWorkerInOrganizable():
    token = request.values.get('token')
    renid = request.values.get('renid')
    nsid = request.values.get('nsid')
    gid = request.values.get('gid')
    argd = {"#token": token,
            "#renid": renid,
            "#nsid": nsid,
            "#gid": gid}
    return StartDash(argd, CGateway.RetrieveWorkerByOrganizable, _ArgsException)


@restfulBp.route("/ns/getworkerentity", methods=["GET", "POST"])
def GetWorkerEntity():
    token = request.values.get('token')
    renid = request.values.get('renid')
    nsid = request.values.get('nsid')
    gids = request.values.get('gids')
    argd = {"#token": token,
            "#renid": renid,
            "#nsid": nsid,
            "#gids": gids}
    return StartDash(argd, CGateway.RetrieveWorkerEntityByGid, _ArgsException)


@restfulBp.route("/ns/getresources", methods=["GET", "POST"])
def GetAllResources():
    token = request.values.get('token')
    renid = request.values.get('renid')
    nsid = request.values.get('nsid')
    argd = {"#token": token,
            "#renid": renid,
            "#nsid": nsid}
    return StartDash(argd, CGateway.RetrieveAllEntity, _ArgsException)


@restfulBp.route("/ns/getconnections", methods=["GET", "POST"])
def GetAllConnections():
    token = request.values.get('token')
    renid = request.values.get('renid')
    nsid = request.values.get('nsid')
    argd = {"#token": token,
            "#renid": renid,
            "#nsid": nsid}
    return StartDash(argd, CGateway.RetrieveAllConnection, _ArgsException)


@restfulBp.route("/ns/getdataversiongid", methods=["GET", "POST"])
def GetDataVersionGid():
    token = request.values.get('token')
    renid = request.values.get('renid')
    nsid = request.values.get('nsid')
    argd = {"#token": token,
            "#renid": renid,
            "#nsid": nsid}
    return StartDash(argd, CGateway.RetrieveDataVersionGid, _ArgsException)


"""
Helper Methods
"""


def _NormalizeArgd(rawDict):
    """
    Normalize the argument dictionary. All parameters start with '#' will be
    checked whether it is not None(client did not pass it). After checking a
    new dictionary will be generated and returned.
    :param rawDict: a dict, contains request args with required attribute flag
    :return: A pair. First flag is bool to signal whether this request is valid,
             Second is a dict for the generated dict or check failure key.
    """
    pureDict = {}
    for k in rawDict:
        if k[0] == '#' and rawDict[k] is None:
            return False, {"key": k[1:len(k)]}
        pureDict[k[1:len(k)]] = rawDict[k] if rawDict[k] != "None" else None
    return True, pureDict


def _ArgsException(key):
    """
    Dump Response of required attribute not exist exception.
    :param key: the missed required attribute
    :return: response string
    """
    return r"""{"return": "Missed required attribute: %s", "code": "Failed"}""" % key


def StartDash(rawDict, successServer, failureServer):
    """
    Process the service request.
    :param rawDict: argument dictionary with requirement signal flag
    :param successServer: success server function
    :param failureServer: failure server function
    :return: response string
    """
    if rawDict is not None:
        flag, pureDict = _NormalizeArgd(rawDict)
    else:
        pureDict = {}
        flag = True
    return successServer(**pureDict) if flag is True else failureServer(pureDict['key'])
