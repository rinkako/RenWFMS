#!/usr/bin/env python
# encoding: utf-8
"""
@module : __init__.py
@author : Rinkako
@time   : 2018/1/4

This module is a Flask blueprint for web service request handing,
all RESTful APIs are defined here.
"""
from flask import Blueprint, request, redirect, url_for, session
from CGateway import CGateway

restfulBp = Blueprint('Restful', __name__,
                      template_folder='templates',
                      static_folder='static',
                      url_prefix='/api')


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
    return r"""{"message": "Missed required attribute: %s", "State": "Failed"}""" % key


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


@restfulBp.route('/')
def home():
    return "test echo"


@restfulBp.route('/connect/', methods=["GET", "POST"])
def connect():
    username = request.values.get("username")
    password = request.values.get("password")
    argd = {"#username": username,
            "#password": password}
    return StartDash(argd, CGateway.Connect, _ArgsException)


@restfulBp.route("/disconnect/", methods=["GET", "POST"])
def disconnect():
    sid = request.values.get('session')
    argd = {"#session": sid}
    return StartDash(argd, CGateway.Disconnect, _ArgsException)


@restfulBp.route("/check/", methods=["GET", "POST"])
def check():
    sid = request.values.get('session')
    argd = {"#session": sid}
    return StartDash(argd, CGateway.CheckConnect, _ArgsException)
