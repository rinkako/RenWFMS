#!/usr/bin/env python
# encoding: utf-8
"""
@module : CGateway
@author : Rinkako
@time   : 2018/1/8
"""
import json
from datetime import datetime
import GlobalConfigContext as GCC


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

    def __init__(self):
        pass

    """
    Helper methods
    """
    @staticmethod
    def _DumpEntityHandler(obj):
        """
        Handle how to dump a Luminous Entity into JSON serializable object.
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
        dumpResp = json.dumps(response_dict, skipkeys=True, default=CGateway._DumpEntityHandler)
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
        args_dict["State"] = "Success"
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
        args_dict["State"] = "Failed"
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
        args_dict["State"] = "Exception"
        return CGateway._DumpResponse(args_dict)

    @staticmethod
    def _UnauthorizedServiceResponse(session):
        """
        Dump the response of unauthorized exception.
        :param session: session id
        :return: the unauthorized exception JSON string
        """
        return CGateway._DumpResponse({"Message": "Unauthorized Service Request. session:%s" % session,
                                       "State": "Unauthorized"})

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
