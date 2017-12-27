#!/usr/bin/env python
# encoding: utf-8
"""
@module : COrganApp
@author : Rinkako
@time   : 17-12-26

Ren COrgan App Point
This module is the entry point of the COrgan program, which will initialize
the environment and set up essential connection for other part of sub system.
Actually, it launches as a Flask application, for web service request handing.
All RESTful APIs are defined here.
"""
from flask import Flask

import GlobalConfigContext

app = Flask(__name__)


@app.route('/')
def hello_world():
    return 'Hello World!'


if __name__ == '__main__':
    app.secret_key = GlobalConfigContext.RAPPKEY
    app.debug = True
    app.run(host='127.0.0.1', port=10236)
