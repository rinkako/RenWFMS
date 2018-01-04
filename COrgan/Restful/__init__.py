#!/usr/bin/env python
# encoding: utf-8
"""
@module : __init__.py
@author : Rinkako
@time   : 2018/1/4

This module is a Flask blueprint for web service request handing,
all RESTful APIs are defined here.
"""
from functools import wraps
from flask import Blueprint, render_template, request, redirect, url_for, session

dasher = Blueprint('LDashboard', __name__,
                   template_folder='templates',
                   static_folder='static',
                   url_prefix='/api')
