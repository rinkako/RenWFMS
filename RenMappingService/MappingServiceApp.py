#!/usr/bin/env python
# encoding: utf-8
"""
@module : MappingServiceApp
@author : Rinkako
@time   : 2018/1/14
"""
from flask import Flask
import GlobalConfigContext
from Restful import restfulBp

app = Flask(__name__)


@app.route('/')
def home():
    return """Welcome to Ren Mapping Service Gateway!<br/>
    This Gateway acts as a bridge between the Mapping Service and outside world.
    """


"""
COrgan App Entry Point
"""
if __name__ == '__main__':
    app.secret_key = GlobalConfigContext.RAPPKEY
    app.debug = True
    app.register_blueprint(restfulBp)
    app.run(host='127.0.0.1', port=10234)
