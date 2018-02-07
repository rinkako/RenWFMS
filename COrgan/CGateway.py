#!/usr/bin/env python
# encoding: utf-8
"""
@module : CGateway
@author : Rinkako
@time   : 2018/1/8
"""
import json
import CController
import GlobalConfigContext
import GlobalConfigContext as GCC
from datetime import datetime
from Utility.EncryptUtil import EncryptUtil


class CGateway:
    """
    COrgan Gateway Interface

    This class is used to handle the HTTP request from other part of this
    solution project and perform them. The HTTP request will be decomposed
    to some key-value pairs which represents the action and its parameters.
    The action will be executed and the result will be presented as JSON.
    It is highlighted that all request from any part of the solution ought
    to be passed to here as a web service request parameter dictionary and
    return a JSON response string.
    """

    """
    CController Static Instance
    """
    core = CController.CControllerCore

    def __init__(self):
        pass

    """
    Helper methods
    """
    @staticmethod
    def _DumpEntityHandler(obj):
        """
        Handle how to dump a Luminous entity into JSON serializable object.
        :param obj: entity object
        :return: JSON serializable object
        """
        if isinstance(obj, datetime):
            return obj.strftime('%Y-%m-%d %H:%M:%S')
        else:
            raise TypeError('%r is not JSON serializable' % obj)

    @staticmethod
    def _DumpResponse(response_dict):
        """
        Dump the response dictionary into structure XML string
        :param response_dict: A dictionary, key is the label and value is the body
        :return: JSON string
        """
        dumpResp = json.dumps(response_dict, skipkeys=True, ensure_ascii=False, default=lambda t: t.__dict__)
        return u"%s\n" % dumpResp

    @staticmethod
    def _SuccessResponse(args_dict=None):
        """
        Dump the response of simple success.
        :param args_dict: a dict, contains optional response arguments
        :return: the success JSON string
        """
        if args_dict is None:
            args_dict = {}
        args_dict["code"] = "OK"
        return CGateway._DumpResponse(args_dict)

    @staticmethod
    def _FailureResponse(args_dict=None):
        """
        Dump the response of simple failure.
        :param args_dict: a dict, contains optional response arguments
        :return: the failure JSON string
        """
        if args_dict is None:
            args_dict = {}
        args_dict["code"] = "Fail"
        return CGateway._DumpResponse(args_dict)

    @staticmethod
    def _ExceptionResponse(args_dict=None):
        """
        Dump the response of simple exception.
        :param args_dict: a dict, contains optional response arguments
        :return: the failure JSON string
        """
        if args_dict is None:
            args_dict = {}
        args_dict["code"] = "Exception"
        return CGateway._DumpResponse(args_dict)

    @staticmethod
    def _UnauthorizedServiceResponse(session):
        """
        Dump the response of unauthorized exception.
        :param session: session id
        :return: the unauthorized exception JSON string
        """
        return CGateway._DumpResponse({"code": "Unauthorized",
                                       "return": "Unauthorized Service Request. session: %s" % session})

    @staticmethod
    def _HandleExceptionAndUnauthorized(flagVal, retVal, session=None):
        """
        Handle the standard validation of CController return values.
        :param flagVal: flag of Exception-Raise-While-Execution-In-Engine
        :param retVal: return value package
        :param session: session id
        :return: immediate return JSON
        """
        if flagVal is False and retVal == GCC.UNAUTHORIZED:
            return CGateway._UnauthorizedServiceResponse(session)
        if flagVal is False:
            return CGateway._ExceptionResponse()
        return None

    """
    Authority API
    """
    @staticmethod
    def Connect(**argd):
        """
        Restful API for authority connection.
        :param argd: request argument dictionary
        :return: dumped json string
        """
        flag, ret = CController.CController.Connect(argd["username"], EncryptUtil.EncryptSHA256(argd["password"]))
        if flag is False:
            return CGateway._ExceptionResponse()
        if ret is None:
            return CGateway._FailureResponse({"return": "invalid user id or password"})
        return CGateway._SuccessResponse({"session": ret})

    @staticmethod
    def CheckConnect(**argd):
        """
        Restful API for authority token validation check.
        :param argd: request argument dictionary
        :return: dumped json string
        """
        flag, ret = CController.CController.CheckConnect(argd["session"])
        xFlag = CGateway._HandleExceptionAndUnauthorized(flag, ret, argd["session"])
        if xFlag is not None:
            return xFlag
        return CGateway._SuccessResponse() if ret is True else CGateway._FailureResponse()

    @staticmethod
    def Disconnect(**argd):
        """
        Restful API for authority token destroy.
        :param argd: request argument dictionary
        :return: dumped json string
        """
        flag, ret = CController.CController.Disconnect(argd["session"])
        xFlag = CGateway._HandleExceptionAndUnauthorized(flag, ret, argd["session"])
        if xFlag is not None:
            return xFlag
        return CGateway._SuccessResponse() if ret is True else CGateway._FailureResponse()

    """
    Data Retrieving API
    """
    @staticmethod
    def GetOrganization(**argd):
        """
        Restful API for getting organization name.
        :param argd: request argument dictionary
        :return: dumped json string
        """
        flag, ret = CGateway.core.GetOrganizationName(argd["session"])
        xFlag = CGateway._HandleExceptionAndUnauthorized(flag, ret, argd["session"])
        if xFlag is not None:
            return xFlag
        return CGateway._SuccessResponse({'return': ret})

    @staticmethod
    def GetDataVersion(**argd):
        """
        Restful API for getting data version string.
        :param argd: request argument dictionary
        :return: dumped json string
        """
        flag, ret = CGateway.core.GetCurrentDataVersion(argd["session"])
        xFlag = CGateway._HandleExceptionAndUnauthorized(flag, ret, argd["session"])
        if xFlag is not None:
            return xFlag
        return CGateway._SuccessResponse({'return': ret})

    @staticmethod
    def RetrieveAllHuman(**argd):
        """
        Restful API for getting all human.
        :param argd: request argument dictionary
        :return: dumped json string
        """
        flag, ret = CGateway.core.RetrieveAllHuman(argd["session"])
        xFlag = CGateway._HandleExceptionAndUnauthorized(flag, ret, argd["session"])
        if xFlag is not None:
            return xFlag
        hmBuilder = []
        for hm in ret:
            hmBuilder.append(hm.ToJsonDict())
        return CGateway._SuccessResponse({'return': hmBuilder})

    @staticmethod
    def RetrieveAllAgent(**argd):
        """
        Restful API for getting all agent.
        :param argd: request argument dictionary
        :return: dumped json string
        """
        flag, ret = CGateway.core.RetrieveAllAgent(argd["session"])
        xFlag = CGateway._HandleExceptionAndUnauthorized(flag, ret, argd["session"])
        if xFlag is not None:
            return xFlag
        hmBuilder = []
        for hm in ret:
            hmBuilder.append(hm.ToJsonDict())
        return CGateway._SuccessResponse({'return': hmBuilder})

    @staticmethod
    def RetrieveAllGroups(**argd):
        """
        Restful API for getting all group.
        :param argd: request argument dictionary
        :return: dumped json string
        """
        flag, ret = CGateway.core.RetrieveAllGroup(argd["session"])
        xFlag = CGateway._HandleExceptionAndUnauthorized(flag, ret, argd["session"])
        if xFlag is not None:
            return xFlag
        hmBuilder = []
        for hm in ret:
            hmBuilder.append(hm.ToJsonDict())
        return CGateway._SuccessResponse({'return': hmBuilder})

    @staticmethod
    def RetrieveAllPositions(**argd):
        """
        Restful API for getting all position.
        :param argd: request argument dictionary
        :return: dumped json string
        """
        flag, ret = CGateway.core.RetrieveAllPosition(argd["session"])
        xFlag = CGateway._HandleExceptionAndUnauthorized(flag, ret, argd["session"])
        if xFlag is not None:
            return xFlag
        hmBuilder = []
        for hm in ret:
            hmBuilder.append(hm.ToJsonDict())
        return CGateway._SuccessResponse({'return': hmBuilder})

    @staticmethod
    def RetrieveAllCapabilities(**argd):
        """
        Restful API for getting all capability.
        :param argd: request argument dictionary
        :return: dumped json string
        """
        flag, ret = CGateway.core.RetrieveAllCapabilities(argd["session"])
        xFlag = CGateway._HandleExceptionAndUnauthorized(flag, ret, argd["session"])
        if xFlag is not None:
            return xFlag
        hmBuilder = []
        for hm in ret:
            hmBuilder.append(hm.ToJsonDict())
        return CGateway._SuccessResponse({'return': hmBuilder})

    @staticmethod
    def RetrieveHumanInWhatGroup(**argd):
        """
        Restful API for getting a set of groups that a specific human in.
        :param argd: request argument dictionary
        :return: dumped json string
        """
        flag, ret = CGateway.core.RetrieveHumanInWhatGroup(argd["session"], argd["personId"])
        xFlag = CGateway._HandleExceptionAndUnauthorized(flag, ret, argd["session"])
        if xFlag is not None:
            return xFlag
        return CGateway._SuccessResponse({'return': ret})

    @staticmethod
    def RetrieveHumanInWhatPosition(**argd):
        """
        Restful API for getting a set of positions that a specific human at.
        :param argd: request argument dictionary
        :return: dumped json string
        """
        flag, ret = CGateway.core.RetrieveHumanInWhatPosition(argd["session"], argd["personId"])
        xFlag = CGateway._HandleExceptionAndUnauthorized(flag, ret, argd["session"])
        if xFlag is not None:
            return xFlag
        return CGateway._SuccessResponse({'return': ret})

    @staticmethod
    def RetrieveHumanWithWhatCapability(**argd):
        """
        Restful API for getting a set of capabilities that a specific human with.
        :param argd: request argument dictionary
        :return: dumped json string
        """
        flag, ret = CGateway.core.RetrieveHumanWithWhatCapability(argd["session"], argd["personId"])
        xFlag = CGateway._HandleExceptionAndUnauthorized(flag, ret, argd["session"])
        if xFlag is not None:
            return xFlag
        return CGateway._SuccessResponse({'return': ret})

    @staticmethod
    def RetrieveAgentInWhatGroup(**argd):
        """
        Restful API for getting a set of groups that a specific agent in.
        :param argd: request argument dictionary
        :return: dumped json string
        """
        flag, ret = CGateway.core.RetrieveAgentInWhatGroup(argd["session"], argd["personId"])
        xFlag = CGateway._HandleExceptionAndUnauthorized(flag, ret, argd["session"])
        if xFlag is not None:
            return xFlag
        return CGateway._SuccessResponse({'return': ret})

    @staticmethod
    def RetrieveAgentInWhatPosition(**argd):
        """
        Restful API for getting a set of positions that a specific agent at.
        :param argd: request argument dictionary
        :return: dumped json string
        """
        flag, ret = CGateway.core.RetrieveAgentInWhatPosition(argd["session"], argd["personId"])
        xFlag = CGateway._HandleExceptionAndUnauthorized(flag, ret, argd["session"])
        if xFlag is not None:
            return xFlag
        return CGateway._SuccessResponse({'return': ret})

    @staticmethod
    def RetrieveAgentWithWhatCapability(**argd):
        """
        Restful API for getting a set of capabilities that a specific agent with.
        :param argd: request argument dictionary
        :return: dumped json string
        """
        flag, ret = CGateway.core.RetrieveAgentWithWhatCapability(argd["session"], argd["personId"])
        xFlag = CGateway._HandleExceptionAndUnauthorized(flag, ret, argd["session"])
        if xFlag is not None:
            return xFlag
        return CGateway._SuccessResponse({'return': ret})

    @staticmethod
    def RetrieveHumanInGroup(**argd):
        """
        Restful API for getting a set of human that a specific group contains.
        :param argd: request argument dictionary
        :return: dumped json string
        """
        flag, ret = CGateway.core.RetrieveHumanInGroup(argd["session"], argd["name"])
        xFlag = CGateway._HandleExceptionAndUnauthorized(flag, ret, argd["session"])
        if xFlag is not None:
            return xFlag
        return CGateway._SuccessResponse({'return': ret})

    @staticmethod
    def RetrieveAgentInGroup(**argd):
        """
        Restful API for getting a set of agent that a specific group contains.
        :param argd: request argument dictionary
        :return: dumped json string
        """
        flag, ret = CGateway.core.RetrieveAgentInGroup(argd["session"], argd["name"])
        xFlag = CGateway._HandleExceptionAndUnauthorized(flag, ret, argd["session"])
        if xFlag is not None:
            return xFlag
        return CGateway._SuccessResponse({'return': ret})

    @staticmethod
    def RetrieveHumanInPosition(**argd):
        """
        Restful API for getting a set of humans that a specific position contains.
        :param argd: request argument dictionary
        :return: dumped json string
        """
        flag, ret = CGateway.core.RetrieveHumanInPosition(argd["session"], argd["name"])
        xFlag = CGateway._HandleExceptionAndUnauthorized(flag, ret, argd["session"])
        if xFlag is not None:
            return xFlag
        return CGateway._SuccessResponse({'return': ret})

    @staticmethod
    def RetrieveAgentInPosition(**argd):
        """
        Restful API for getting a set of agents that a specific position contains.
        :param argd: request argument dictionary
        :return: dumped json string
        """
        flag, ret = CGateway.core.RetrieveAgentInPosition(argd["session"], argd["name"])
        xFlag = CGateway._HandleExceptionAndUnauthorized(flag, ret, argd["session"])
        if xFlag is not None:
            return xFlag
        return CGateway._SuccessResponse({'return': ret})

    @staticmethod
    def RetrieveHumanWithCapability(**argd):
        """
        Restful API for getting a set of humans that a specific capability category contains.
        :param argd: request argument dictionary
        :return: dumped json string
        """
        flag, ret = CGateway.core.RetrieveHumanWithCapability(argd["session"], argd["name"])
        xFlag = CGateway._HandleExceptionAndUnauthorized(flag, ret, argd["session"])
        if xFlag is not None:
            return xFlag
        return CGateway._SuccessResponse({'return': ret})

    @staticmethod
    def RetrieveAgentWithCapability(**argd):
        """
        Restful API for getting a set of agents that a specific capability category contains.
        :param argd: request argument dictionary
        :return: dumped json string
        """
        flag, ret = CGateway.core.RetrieveAgentWithCapability(argd["session"], argd["name"])
        xFlag = CGateway._HandleExceptionAndUnauthorized(flag, ret, argd["session"])
        if xFlag is not None:
            return xFlag
        return CGateway._SuccessResponse({'return': ret})

    @staticmethod
    def RetrieveWorkerInGroup(**argd):
        """
        Restful API for getting a set of workers that a specific group contains, ONLY USE BY NAME SERVICE.
        :param argd: request argument dictionary
        :return: dumped json string
        """
        checkSign = argd["nsid"] + "," + argd["renid"]
        token = EncryptUtil.DecodeURLSafeBase64(argd["token"])
        try:
            tokenRet = EncryptUtil.VerifySign(checkSign, token, GlobalConfigContext.AUTH_NameService_PublicKey)
        except:
            tokenRet = False
        if tokenRet is False:
            return CGateway._UnauthorizedServiceResponse(token)
        flag1, ret1 = CGateway.core.RetrieveHumanInGroup(GlobalConfigContext.AUTH_INTERNAL_SESSION, argd["groupName"])
        flag2, ret2 = CGateway.core.RetrieveAgentInGroup(GlobalConfigContext.AUTH_INTERNAL_SESSION, argd["groupName"])
        return CGateway._DumpResponse(ret1 + ret2)

    @staticmethod
    def RetrieveWorkerInPosition(**argd):
        """
        Restful API for getting a set of workers that a specific position contains, ONLY USE BY NAME SERVICE.
        :param argd: request argument dictionary
        :return: dumped json string
        """
        checkSign = argd["nsid"] + "," + argd["renid"]
        token = EncryptUtil.DecodeURLSafeBase64(argd["token"])
        try:
            tokenRet = EncryptUtil.VerifySign(checkSign, token, GlobalConfigContext.AUTH_NameService_PublicKey)
        except:
            tokenRet = False
        if tokenRet is False:
            return CGateway._UnauthorizedServiceResponse(token)
        flag1, ret1 = CGateway.core.RetrieveHumanInPosition(GlobalConfigContext.AUTH_INTERNAL_SESSION, argd["positionName"])
        flag2, ret2 = CGateway.core.RetrieveAgentInPosition(GlobalConfigContext.AUTH_INTERNAL_SESSION, argd["positionName"])
        return CGateway._DumpResponse(ret1 + ret2)

    @staticmethod
    def RetrieveWorkerInCapability(**argd):
        """
        Restful API for getting a set of workers that a specific capability contains, ONLY USE BY NAME SERVICE.
        :param argd: request argument dictionary
        :return: dumped json string
        """
        checkSign = argd["nsid"] + "," + argd["renid"]
        token = EncryptUtil.DecodeURLSafeBase64(argd["token"])
        try:
            tokenRet = EncryptUtil.VerifySign(checkSign, token, GlobalConfigContext.AUTH_NameService_PublicKey)
        except:
            tokenRet = False
        if tokenRet is False:
            return CGateway._UnauthorizedServiceResponse(token)
        flag1, ret1 = CGateway.core.RetrieveHumanWithCapability(GlobalConfigContext.AUTH_INTERNAL_SESSION, argd["capabilityName"])
        flag2, ret2 = CGateway.core.RetrieveAgentWithCapability(GlobalConfigContext.AUTH_INTERNAL_SESSION, argd["capabilityName"])
        return CGateway._DumpResponse(ret1 + ret2)

    @staticmethod
    def RetrieveWorkerByOrganizable(**argd):
        """
        Restful API for getting workers in a organizable in the COrgan, ONLY USE BY NAME SERVICE.
        :param argd: request argument dictionary
        :return: dumped json string
        """
        checkSign = argd["nsid"] + "," + argd["renid"]
        token = EncryptUtil.DecodeURLSafeBase64(argd["token"])
        try:
            tokenRet = EncryptUtil.VerifySign(checkSign, token, GlobalConfigContext.AUTH_NameService_PublicKey)
        except:
            tokenRet = False
        if tokenRet is False:
            return CGateway._UnauthorizedServiceResponse(token)
        flag, ret = CGateway.core.RetrieveWorkerByOrganizableGid(GlobalConfigContext.AUTH_INTERNAL_SESSION, argd["gid"])
        xFlag = CGateway._HandleExceptionAndUnauthorized(flag, ret, GlobalConfigContext.AUTH_INTERNAL_SESSION)
        if xFlag is not None:
            return xFlag
        return CGateway._DumpResponse(ret)

    @staticmethod
    def RetrieveAllEntity(**argd):
        """
        Restful API for getting all entities in the COrgan, ONLY USE BY NAME SERVICE.
        :param argd: request argument dictionary
        :return: dumped json string
        """
        checkSign = argd["nsid"] + "," + argd["renid"]
        token = EncryptUtil.DecodeURLSafeBase64(argd["token"])
        try:
            tokenRet = EncryptUtil.VerifySign(checkSign, token, GlobalConfigContext.AUTH_NameService_PublicKey)
        except:
            tokenRet = False
        if tokenRet is False:
            return CGateway._UnauthorizedServiceResponse(token)
        flag1, ret1 = CGateway.core.RetrieveAllHuman(GlobalConfigContext.AUTH_INTERNAL_SESSION)
        flag2, ret2 = CGateway.core.RetrieveAllAgent(GlobalConfigContext.AUTH_INTERNAL_SESSION)
        flag3, ret3 = CGateway.core.RetrieveAllGroup(GlobalConfigContext.AUTH_INTERNAL_SESSION)
        flag4, ret4 = CGateway.core.RetrieveAllPosition(GlobalConfigContext.AUTH_INTERNAL_SESSION)
        flag5, ret5 = CGateway.core.RetrieveAllCapabilities(GlobalConfigContext.AUTH_INTERNAL_SESSION)
        retDict = dict()
        retDict["human"] = ret1
        retDict["agent"] = ret2
        retDict["group"] = ret3
        retDict["position"] = ret4
        retDict["capability"] = ret5
        return CGateway._DumpResponse(retDict)

    @staticmethod
    def RetrieveWorkerEntityByGid(**argd):
        """
        Restful API for getting a list of worker entity, ONLY USE BY NAME SERVICE.
        :param argd: request argument dictionary
        :return: dumped json string
        """
        checkSign = argd["nsid"] + "," + argd["renid"]
        token = EncryptUtil.DecodeURLSafeBase64(argd["token"])
        try:
            tokenRet = EncryptUtil.VerifySign(checkSign, token, GlobalConfigContext.AUTH_NameService_PublicKey)
        except:
            tokenRet = False
        if tokenRet is False:
            return CGateway._UnauthorizedServiceResponse(token)
        flag, ret = CGateway.core.RetrieveWorkersEntity(GlobalConfigContext.AUTH_INTERNAL_SESSION, argd["gids"])
        return CGateway._DumpResponse(ret)

    @staticmethod
    def RetrieveAllConnection(**argd):
        """
        Restful API for getting all connections in the COrgan, ONLY USE BY NAME SERVICE.
        :param argd: request argument dictionary
        :return: dumped json string
        """
        checkSign = argd["nsid"] + "," + argd["renid"]
        token = EncryptUtil.DecodeURLSafeBase64(argd["token"])
        try:
            tokenRet = EncryptUtil.VerifySign(checkSign, token, GlobalConfigContext.AUTH_NameService_PublicKey)
        except:
            tokenRet = False
        if tokenRet is False:
            return CGateway._UnauthorizedServiceResponse(token)
        flag, ret = CGateway.core.RetrieveAllConnection(GlobalConfigContext.AUTH_INTERNAL_SESSION)
        xFlag = CGateway._HandleExceptionAndUnauthorized(flag, ret, GlobalConfigContext.AUTH_INTERNAL_SESSION)
        if xFlag is not None:
            return xFlag
        return CGateway._DumpResponse(ret)

    @staticmethod
    def RetrieveDataVersionGid(**argd):
        """
        Restful API for getting data version of COrgan, ONLY USE BY NAME SERVICE.
        :param argd: request argument dictionary
        :return: dumped json string
        """
        checkSign = argd["nsid"] + "," + argd["renid"]
        token = EncryptUtil.DecodeURLSafeBase64(argd["token"])
        try:
            tokenRet = EncryptUtil.VerifySign(checkSign, token, GlobalConfigContext.AUTH_NameService_PublicKey)
        except:
            tokenRet = False
        if tokenRet is False:
            return CGateway._UnauthorizedServiceResponse(token)
        flag1, ret1 = CGateway.core.GetCurrentDataVersion(GlobalConfigContext.AUTH_INTERNAL_SESSION)
        flag2, ret2 = CGateway.core.GetOrganizationId(GlobalConfigContext.AUTH_INTERNAL_SESSION)
        xFlag = CGateway._HandleExceptionAndUnauthorized(flag1 & flag2, ret1, GlobalConfigContext.AUTH_INTERNAL_SESSION)
        if xFlag is not None:
            return xFlag
        return CGateway._DumpResponse("%s,%s" % (ret1, ret2))
