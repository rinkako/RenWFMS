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
Here defines the routing of web UI, and all RESTful APIs are defined at another
Restful blueprint package.
"""
from flask import Flask, render_template, redirect, url_for

import CController
import GlobalConfigContext
from Restful import restfulBp

app = Flask(__name__, template_folder='templates', static_folder='static')
core = CController.CControllerCore


@app.route('/')
def home():
    t = {'L_PageTitle': 'homepage'}
    return render_template('index.html', **t)


if __name__ == '__main__':
    app.secret_key = GlobalConfigContext.RAPPKEY
    app.debug = True
    app.register_blueprint(restfulBp)
    app.run(host='127.0.0.1', port=10236)
