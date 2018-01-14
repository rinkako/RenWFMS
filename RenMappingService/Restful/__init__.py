#!/usr/bin/env python
# encoding: utf-8
"""
@module : __init__.py
@author : Rinkako
@time   : 2018/1/14

This module is a Flask blueprint for web service request handing,
all RESTful APIs are defined here.
"""
from flask import Blueprint

restfulBp = Blueprint('Restful', __name__, url_prefix='/api')
