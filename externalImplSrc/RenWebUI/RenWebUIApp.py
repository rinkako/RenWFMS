#!/usr/bin/env python
# encoding: utf-8
from functools import wraps

import time
from flask import Flask, render_template, redirect, url_for, request, session

import GlobalConfigContext
import RenUIController

app = Flask(__name__, template_folder='templates', static_folder='./../StaticAssets')
core = RenUIController.RenUIControllerInstance

"""
Warppers of funcs
"""


def authorizeRequire(fn):
    """
    Decorator for login required router.
    """
    @wraps(fn)
    def wrapper(*args, **kwds):
        user = session.get('SID', None)
        if user:
            return fn(*args, **kwds)
        else:
            return redirect(url_for('Login3'))
    return wrapper


def adminRequire(fn):
    """
    Decorator for admin required router.
    """
    @wraps(fn)
    def wrapper(*args, **kwds):
        auType = session.get('AuType', None)
        if auType is not None and auType > 0:
            return fn(*args, **kwds)
        else:
            return redirect(url_for('AccessErrorPage', dt='unauthorized'))
    return wrapper


def superRequire(fn):
    """
    Decorator for admin required router.
    """
    @wraps(fn)
    def wrapper(*args, **kwds):
        auType = session.get('AuType', None)
        if auType is not None and auType > 1:
            return fn(*args, **kwds)
        else:
            return redirect(url_for('AccessErrorPage', dt='unauthorized'))
    return wrapper


"""
Router funcs.
"""


@app.route('/', methods=["GET"])
@authorizeRequire
def home():
    return redirect(url_for("MyWorkitemManagement"))


@app.route('/domain/', methods=["GET"])
@superRequire
def DomainManagement():
    flag, res = core.DomainGetAll(session['SID'])
    if flag is False:
        return redirect(url_for('AccessErrorPage', dt='x'))
    t = {'L_PageTitle': u'域管理',
         'L_PageDescription': u'管理平台上的租户（域）',
         'domainList': res,
         'changetime': time.localtime,
         'strtime': time.strftime}
    return render_template('domainmanagement.html', **t)


@app.route('/domain/add/', methods=["GET"])
@superRequire
def DomainAdd():
    t = {'L_PageTitle': u'添加域',
         'L_PageDescription': u'为平台添加一个域租户'}
    return render_template('domainmanagement_add.html', **t)


@app.route('/domain/performAdd/', methods=["POST"])
@superRequire
def PerformDomainAdd():
    flag, res = core.DomainAdd(session['SID'],
                               request.form['f_username'],
                               request.form['f_nPassword'],
                               request.form['f_nBindingLoc'])
    if flag is False or res is None:
        return redirect(url_for('AccessErrorPage', dt='add'))
    return redirect(url_for('DomainManagement'))


@app.route('/domain/edit/<uname>/', methods=["GET"])
@superRequire
def DomainEdit(uname):
    flag, res = core.DomainGet(session['SID'], uname)
    if flag is False:
        return redirect(url_for('AccessErrorPage', dt='x'))
    t = {'L_PageTitle': u'域: ' + uname,
         'L_PageDescription': u'编辑域',
         'UserObj': res,
         'fromConfig': 0}
    return render_template('domainmanagement_edit.html', **t)


@app.route('/domain/performEdit/', methods=["POST"])
@adminRequire
def PerformDomainEdit():
    flag, res = core.DomainUpdate(session['SID'],
                                  request.values['h_username'],
                                  request.values['f_nBindingLoc'])
    if flag is False:
        return redirect(url_for('AccessErrorPage', dt='x'))
    if request.values["h_cfg"] == 0:
        return redirect(url_for('DomainManagement'))
    else:
        return redirect(url_for('DomainConfigManagement'))


@app.route('/domain/performDelete/<uname>/', methods=["GET", "POST"])
@superRequire
def PerformDomainDelete(uname):
    flag, res = core.DomainStop(session['SID'], uname)
    if flag is False or res is None:
        return redirect(url_for('AccessErrorPage', dt='add'))
    return redirect(url_for('DomainManagement'))


@app.route('/domain/performResume/<uname>/', methods=["GET", "POST"])
@superRequire
def PerformDomainResume(uname):
    flag, res = core.DomainResume(session['SID'], uname)
    if flag is False or res is None:
        return redirect(url_for('AccessErrorPage', dt='add'))
    return redirect(url_for('DomainManagement'))


@app.route('/authuser/', methods=["GET"])
@adminRequire
def AuthUserManagement():
    if session['Domain'] == "admin":
        flag, res = core.AuthUserGetAll(session['SID'])
        title = u"管理平台中的授权用户"
    else:
        flag, res = core.AuthUserGetAllForDomain(session['SID'], session['Domain'])
        title = u"管理域中的授权用户"
    if flag is False:
        return redirect(url_for('AccessErrorPage', dt='x'))
    t = {'L_PageTitle': u'授权用户管理',
         'L_PageDescription': title,
         'itemList': res,
         'changetime': time.localtime,
         'strtime': time.strftime}
    return render_template('authusermanagement.html', **t)


@app.route('/authuser/add/', methods=["GET"])
@adminRequire
def AuthUserAdd():
    t = {'L_PageTitle': u'添加授权用户',
         'L_PageDescription': u'为当前域添加一个授权用户'}
    return render_template('authusermanagement_add.html', **t)


@app.route('/authuser/performAdd/', methods=["POST"])
@adminRequire
def PerformAuthUserAdd():
    flag, res = core.AuthUserAdd(session['SID'],
                                 request.form['f_username'],
                                 session["Domain"],
                                 request.form['f_nPassword'],
                                 request.form['f_nGid'])
    if flag is False or res is None:
        return redirect(url_for('AccessErrorPage', dt='add'))
    return redirect(url_for('AuthUserManagement'))


@app.route('/authuser/edit/<uname>/', methods=["GET"])
@adminRequire
def AuthUserEdit(uname):
    flag, res = core.AuthUserGet(session['SID'], uname, session["Domain"])
    if flag is False:
        return redirect(url_for('AccessErrorPage', dt='x'))
    t = {'L_PageTitle': u'编辑: ' + uname,
         'L_PageDescription': u'编辑授权用户',
         'UserObj': res,
         'fromConfig': 0}
    return render_template('authusermanagement_edit.html', **t)


@app.route('/authuser/performEdit/', methods=["POST"])
@authorizeRequire
def PerformAuthUserEdit():
    if "f_nPassword" in request.values and request.values["f_nPassword"].strip() != "":
        pwd = request.values["f_nPassword"]
    else:
        pwd = None
    if request.values["f_nGid"].strip() != "":
        nGid = request.values["f_nGid"]
    else:
        nGid = None
    flag, res = core.AuthUserUpdate(session['SID'],
                                    request.values['h_username'],
                                    session["Domain"],
                                    pwd,
                                    nGid)
    if flag is False:
        return redirect(url_for('AccessErrorPage', dt='x'))
    if request.values["h_cfg"] == 0:
        return redirect(url_for('AuthUserManagement'))
    else:
        return redirect(url_for('SelfConfigManagement'))


@app.route('/authuser/performDelete/<uname>/', methods=["GET", "POST"])
@adminRequire
def PerformAuthUserDelete(uname):
    flag, res = core.AuthUserStop(session['SID'], uname, session["Domain"])
    if flag is False or res is None:
        return redirect(url_for('AccessErrorPage', dt='add'))
    return redirect(url_for('AuthUserManagement'))


@app.route('/authuser/performResume/<uname>/', methods=["GET", "POST"])
@adminRequire
def PerformAuthUserResume(uname):
    flag, res = core.AuthUserResume(session['SID'], uname, session["Domain"])
    if flag is False or res is None:
        return redirect(url_for('AccessErrorPage', dt='add'))
    return redirect(url_for('AuthUserManagement'))


@app.route('/process/', methods=["GET"])
@authorizeRequire
def ProcessManagement():
    flag, res = core.ProcessGetAllForDomain(session['SID'], session["Domain"])
    if flag is False:
        return redirect(url_for('AccessErrorPage', dt='x'))
    t = {'L_PageTitle': u'流程管理',
         'L_PageDescription': u'管理当前域下的流程',
         'itemList': res,
         'changetime': time.localtime,
         'strtime': time.strftime}
    return render_template('processmanagement.html', **t)


@app.route('/process/info/<pid>/', methods=["GET", "POST"])
@authorizeRequire
def ProcessInfo(pid):
    flag, res = core.ProcessGetByPid(session['SID'], pid)
    if flag is False:
        return redirect(url_for('AccessErrorPage', dt='x'))
    if res["lastLaunchTimestamp"] is None:
        res["lastLaunchTimestamp"] = -1
    t = {'L_PageTitle': u'查看流程详细信息',
         'L_PageDescription': u'流程：' + pid,
         'processObj': res,
         'changetime': time.localtime,
         'strtime': time.strftime}
    return render_template('processmanagement_view.html', **t)


@app.route('/activeProcess/', methods=["GET"])
@authorizeRequire
def ActiveProcessManagement():
    if session["AuType"] == 999:
        flag, res = core.RuntimeRecordGetAll(session['SID'])
        desc = u"当前平台所有活动流程清单"
    elif session["AuType"] > 0:
        flag, res = core.RuntimeRecordGetAllByDomain(session['SID'], session["Domain"])
        desc = u"当前域所有活动流程清单"
    else:
        flag, res = core.RuntimeRecordGetAllByLauncher(session['SID'], session["AuID"])
        desc = u"当前活动流程清单"
    if flag is False:
        return redirect(url_for('AccessErrorPage', dt='x'))
    t = {'L_PageTitle': u'活动流程管理',
         'L_PageDescription': desc,
         'itemList': res,
         'changetime': time.localtime,
         'strtime': time.strftime}
    return render_template('runtimemanagement.html', **t)


@app.route('/activeProcess/info/<rtid>/', methods=["GET", "POST"])
@authorizeRequire
def ActiveProcessInfo(rtid):
    flag, res = core.RuntimeRecordGetAllByRTID(session['SID'], rtid)
    if flag is False or res is None:
        return redirect(url_for('AccessErrorPage', dt='x'))
    flag2, res2 = core.RuntimeLogGetByRTID(session['SID'], rtid)
    if flag2 is False or res2 is None:
        return redirect(url_for('AccessErrorPage', dt='x'))
    flag3, res3 = core.RuntimeSpanTreeGetByRTID(session['SID'], rtid)
    if flag3 is False or res3 is None:
        return redirect(url_for('AccessErrorPage', dt='x'))
    if res["finishTimestamp"] is None:
        res["finishTimestamp"] = -1
    if res["isSucceed"] == 1:
        pStyle = "progress-bar-success"
        successMsg = '<span class="label label-success arrow-out arrow-out-right">Succeed</span>'
    elif res["isSucceed"] == 0:
        pStyle = "progress-bar-info"
        successMsg = '<span class="label label-primary arrow-out arrow-out-right">Running</span>'
    else:
        pStyle = "progress-bar-danger"
        successMsg = '<span class="label label-danger arrow-out arrow-out-right">Failed</span>'
    t = {'L_PageTitle': u'查看活动流程详细信息',
         'L_PageDescription': u'RTID：' + rtid,
         'processObj': res,
         'logList': res2,
         'spanTree': res3,
         'successMsg': successMsg,
         'workflowProgressStyle': pStyle,
         'changetime': time.localtime,
         'strtime': time.strftime}
    return render_template('runtimemanagement_view.html', **t)


@app.route('/domainWorkitem/', methods=["GET"])
@authorizeRequire
def DomainWorkitemManagement():
    flag, res = core.WorkitemGetAllByDomain(session['SID'], session["Domain"])
    if flag is False or res is None:
        return redirect(url_for('AccessErrorPage', dt='x'))
    t = {'L_PageTitle': u'域工作项',
         'L_PageDescription': u'查看域中的工作项历史记录',
         'itemList': res,
         'changetime': time.localtime,
         'strtime': time.strftime}
    return render_template('domainworkitemmanagement.html', **t)


@app.route('/domainWorkitem/info/<wid>/', methods=["GET", "POST"])
@authorizeRequire
def DomainWorkitemInfo(wid):
    flag, res = core.WorkitemGet(session['SID'], wid)
    if flag is False or res is None:
        return redirect(url_for('AccessErrorPage', dt='x'))
    t = {'L_PageTitle': u'查看工作项详细信息',
         'L_PageDescription': u'WID：' + wid,
         'processObj': res,
         'fromMy': '0',
         'toLong': _toLong}
    return render_template('workitem_view.html', **t)


@app.route('/myWorkitem/', methods=["GET"])
@authorizeRequire
def MyWorkitemManagement():
    if session["GID"] is not None:
        flag, res = core.WorkitemGetByParticipant(session['SID'], session["GID"])
        if flag is False or res is None:
            return redirect(url_for('AccessErrorPage', dt='x'))
        gidSet = '1'
    else:
        res = []
        gidSet = '0'
    t = {'L_PageTitle': u'我的工作列表',
         'L_PageDescription': u'查看我的活跃工作项',
         'itemList': res,
         'gidSet': gidSet,
         'changetime': time.localtime,
         'strtime': time.strftime}
    return render_template('myworkitem.html', **t)


@app.route('/myWorkitem/info/<wid>/', methods=["GET", "POST"])
@authorizeRequire
def MyWorkitemInfo(wid):
    flag, res = core.WorkitemGet(session['SID'], wid)
    if flag is False or res is None:
        return redirect(url_for('AccessErrorPage', dt='x'))
    t = {'L_PageTitle': u'查看工作项详细信息',
         'L_PageDescription': u'WID：' + wid,
         'processObj': res,
         'fromMy': '1',
         'toLong': _toLong}
    return render_template('workitem_view.html', **t)


@app.route('/performWorkitem/<action>/<wid>/', methods=["GET", "POST"])
@authorizeRequire
def PerformWorkitemAction(action, wid):
    if "GID" not in session:
        return redirect(url_for('AccessErrorPage', dt='unauthorized'))
    flag, res = core.WorkitemAction(session['SID'], action, wid, session["GID"])
    if flag is False or res is None:
        return redirect(url_for('AccessErrorPage', dt='x'))
    return redirect(url_for('MyWorkitemManagement'))


@app.route('/selfConfig/', methods=["GET"])
@authorizeRequire
def SelfConfigManagement():
    flag, res = core.AuthUserGet(session['SID'], session['PureUserName'], session["Domain"])
    if flag is False:
        return redirect(url_for('AccessErrorPage', dt='x'))
    if res["gid"] is None:
        res["gid"] = ''
    t = {'L_PageTitle': u'用户: ' + session['PureUserName'],
         'L_PageDescription': u'更新个人信息',
         'UserObj': res,
         'fromConfig': 1}
    return render_template('authusermanagement_edit.html', **t)


@app.route('/domainConfig/', methods=["GET"])
@adminRequire
def DomainConfigManagement():
    if session["AuType"] == 0:
        return redirect(url_for('AccessErrorPage', dt='unauthorized'))
    flag, res = core.DomainGet(session['SID'], session["Domain"])
    if flag is False:
        return redirect(url_for('AccessErrorPage', dt='x'))
    t = {'L_PageTitle': u'域: ' + session["Domain"],
         'L_PageDescription': u'编辑域',
         'UserObj': res,
         'fromConfig': 1}
    return render_template('domainmanagement_edit.html', **t)


"""
Common Pages Route Functions
"""


@app.route('/login/')
def Login():
    _logout()
    t = {
        'msg': ''
    }
    return render_template('login.html', **t)


@app.route('/login2/')
def Login2():
    _logout()
    t = {
        'msg': 'Invalid User ID or Password'
    }
    return render_template('login.html', **t)


@app.route('/loginRequire/')
def Login3():
    _logout()
    t = {
        'msg': 'Please login first'
    }
    return render_template('login.html', **t)


@app.route('/performLogin/', methods=["POST"])
def performLogin():
    if request.method == 'GET':
        return redirect(url_for('Login'))
    usrId = request.form["passedUserId"]
    usrPwd = request.form["passedUserPwd"]
    flag, retSession, retGid = RenUIController.RenUIController.Auth(usrId, usrPwd)
    if flag is False or retSession is None:
        return redirect(url_for('Login2'))
    session['AuID'] = usrId
    session['PureUserName'] = usrId.split('@')[0]
    session['Domain'] = usrId.split('@')[1]
    session['SID'] = retSession
    session['GID'] = retGid
    session['AuType'] = RenUIController.RenUIController.GetSessionLevel(retSession)[1]
    return redirect(url_for('home'))


@app.route('/performLogout/', methods=["GET", "POST"])
def performLogout():
    """
    Perform logout action.
    """
    return redirect(url_for('Login'))


@app.route('/logout/', methods=["GET", "POST"])
def logout():
    """
    Perform Logout logic and clear the session.
    """
    _logout()


def _logout():
    if 'SID' in session:
        sid = session['SID']
        if sid != "" and sid is not None:
            RenUIController.RenUIController.Disconnect(session['SID'])
            session.clear()


def _toLong(t):
    return long(t)


@app.route('/failure/<dt>/')
def AccessErrorPage(dt):
    t = {'L_PageTitle': u'访问失败',
         'L_PageDescription': u''}
    if dt == "unauthorized":
        t["msg"] = u'无法为您提供请求的服务，请确认您拥有访问它的权限。'
    elif dt == "add":
        t["msg"] = u'添加新资源失败，请确认资源字段的正确性，以及名字是否唯一。'
    else:
        t["msg"] = u'无法为您提供请求的服务。请确认服务器正常，并且您拥有访问它的权限。'
    return render_template('info_cannotaccess.html', **t)


if __name__ == '__main__':
    app.secret_key = GlobalConfigContext.RAPPKEY
    app.debug = True
    app.run(host='127.0.0.1', port=10238)
