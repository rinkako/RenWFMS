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
from flask import Flask, render_template, redirect, url_for, request

import CController
import GlobalConfigContext
from Restful import restfulBp

app = Flask(__name__, template_folder='templates', static_folder='static')
core = CController.CControllerCore


@app.route('/')
def home():
    t = {'L_PageTitle': 'homepage'}
    return render_template('index.html', **t)


@app.route('/page/failure/<dt>/')
def AccessErrorPage(dt):
    t = {'L_PageTitle': u'访问失败',
         'L_PageDescription': u''}
    if dt == "unauthorized":
        t["msg"] = u'COrgan无法为您提供请求的服务，请确认您拥有访问它的权限。'
    else:
        t["msg"] = u'COrgan无法为您提供请求的服务。请确认服务器正常，并且您拥有访问它的权限。'
    return render_template('info_cannotaccess.html', **t)


"""
User Management Routers
"""


@app.route('/userManagement/')
def userManagement():
    flag, res = core.PlatformUserGetAll('testadmin')
    if flag is False:
        return redirect(url_for('AccessErrorPage', dt='x'))
    t = {'L_PageTitle': u'授权管理',
         'L_PageDescription': u'管理COrgan中的授权访问的用户账户',
         'userList': res}
    return render_template('usermanagement.html', **t)


@app.route('/userManagement/add/')
def addPlatformUser():
    t = {'L_PageTitle': u'添加授权',
         'L_PageDescription': u'为COrgan添加一个授权访问的用户账户'}
    return render_template('usermanagement_add.html', **t)


@app.route('/userManagement/performadd/', methods=["POST"])
def performAddPlatformUser():
    from Utility.EncryptUtil import EncryptUtil
    flag, res = core.PlatformUserAdd('testadmin',
                                     request.form['f_username'],
                                     EncryptUtil.EncryptSHA256(request.form['f_nPassword']),
                                     1 if request.form['f_level'] == u"管理员" else 0)
    if flag is False:
        return redirect(url_for('AccessErrorPage', dt='x'))
    return redirect(url_for('userManagement'))


@app.route('/userManagement/performdelete/<uname>/', methods=["GET"])
def performDeletePlatformUser(uname):
    flag, res = core.PlatformUserRemove('testadmin', uname)
    if flag is False:
        return redirect(url_for('AccessErrorPage', dt='x'))
    return redirect(url_for('userManagement'))


@app.route('/userManagement/edit/<uname>/', methods=["GET"])
def editPlatformUser(uname):
    flag, res = core.PlatformUserGet('testadmin', uname)
    if flag is False:
        return redirect(url_for('AccessErrorPage', dt='x'))
    t = {'L_PageTitle': u'编辑: ' + uname,
         'L_PageDescription': u'编辑授权',
         'UserObj': res}
    return render_template('usermanagement_edit.html', **t)


@app.route('/userManagement/performedit/', methods=["POST"])
def performEditPlatformUser():
    pwd = None
    if request.form['f_nPassword'] != "":
        from Utility.EncryptUtil import EncryptUtil
        pwd = EncryptUtil.EncryptSHA256(request.form['f_nPassword'])
    flag, res = core.PlatformUserUpdate('testadmin',
                                        request.form['h_username'],
                                        pwd,
                                        1 if request.form['f_level'] == u"管理员" else 0)
    if flag is False:
        return redirect(url_for('AccessErrorPage', dt='x'))
    return redirect(url_for('userManagement'))


if __name__ == '__main__':
    app.secret_key = GlobalConfigContext.RAPPKEY
    app.debug = True
    app.register_blueprint(restfulBp)
    app.run(host='127.0.0.1', port=10236)
