#!/usr/bin/env python
# encoding: utf-8

from functools import wraps
from flask import Flask, render_template, redirect, url_for, request, session

import GlobalConfigContext
from RenUIController import RenUIController

app = Flask(__name__, template_folder='templates', static_folder='static')


@app.route('/', methods=["GET"])
def home():
    return "hello ren!"


@app.route('/domain/', methods=["GET"])
def DomainManagement():
    pass


@app.route('/domain/add/', methods=["GET"])
def DomainAdd():
    pass


@app.route('/domain/performAdd/', methods=["POST"])
def PerformDomainAdd():
    pass


@app.route('/domain/edit/', methods=["GET"])
def DomainEdit():
    pass


@app.route('/domain/performEdit/', methods=["POST"])
def PerformDomainEdit():
    pass


@app.route('/domain/performDelete/', methods=["POST"])
def PerformDomainDelete():
    pass


@app.route('/authuser/', methods=["GET"])
def AuthUserManagement():
    pass


@app.route('/authuser/add/', methods=["GET"])
def AuthUserAdd():
    pass


@app.route('/authuser/performAdd/', methods=["POST"])
def PerformAuthUserAdd():
    pass


@app.route('/authuser/edit/', methods=["GET"])
def AuthUserEdit():
    pass


@app.route('/authuser/performEdit/', methods=["POST"])
def PerformAuthUserEdit():
    pass


@app.route('/authuser/performDelete/', methods=["POST"])
def PerformAuthUserDelete():
    pass


@app.route('/process/', methods=["GET"])
def ProcessManagement():
    pass


@app.route('/process/performStart/', methods=["POST"])
def PerformProcessStart():
    pass


@app.route('/process/performDelete/', methods=["POST"])
def PerformProcessDelete():
    pass


@app.route('/activeProcess/', methods=["GET"])
def ActiveProcessManagement():
    pass


@app.route('/activeProcess/instanceTree', methods=["GET"])
def ActiveProcessViewInstanceTree():
    pass


@app.route('/login/', methods=["GET"])
def Login():
    pass


@app.route('/performLogin/', methods=["POST"])
def performLogin():
    if request.method == 'GET':
        return redirect(url_for('Login'))
    usrId = request.form["passedUserId"]
    usrPwd = request.form["passedUserPwd"]
    from Utility.EncryptUtil import EncryptUtil
    usrPwd = EncryptUtil.EncryptSHA256(usrPwd)
    flag, ret = RenUIController.Auth(usrId, usrPwd)
    if flag is False or ret is None:
        return redirect(url_for('Login2'))
    session['AuID'] = usrId
    session['SID'] = ret
    session['AuType'] = 1 if RenUIController.AmIAdmin(ret)[1] is True else 0
    return redirect(url_for('home'))


@app.route('/logout', methods=["GET", "POST"])
def logout():
    """
    Perform Logout logic and clear the session.
    """
    if 'SID' in session:
        sid = session['SID']
        if sid != "" and sid is not None:
            flag, ret = RenUIController.Disconnect(session['SID'])
            session.clear()


if __name__ == '__main__':
    app.secret_key = GlobalConfigContext.RAPPKEY
    app.debug = True
    app.run(host='127.0.0.1', port=10239)
