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
import CController
import GlobalConfigContext
from functools import wraps
from flask import Flask, render_template, redirect, url_for, request, session
from Restful import restfulBp

app = Flask(__name__, template_folder='templates', static_folder='./../StaticAssets')
core = CController.CControllerCore


"""
Warppers
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


"""
Common Routers
"""


@app.route('/')
@authorizeRequire
def home():
    t = {'L_PageTitle': 'homepage'}
    return render_template('index.html', **t)


@app.route('/page/failure/<dt>/')
def AccessErrorPage(dt):
    t = {'L_PageTitle': u'访问失败',
         'L_PageDescription': u''}
    if dt == "unauthorized":
        t["msg"] = u'COrgan无法为您提供请求的服务，请确认您拥有访问它的权限。'
    elif dt == "add":
        t["msg"] = u'添加新资源失败，请确认资源字段的正确性，以及名字是否唯一。'
    else:
        t["msg"] = u'COrgan无法为您提供请求的服务。请确认服务器正常，并且您拥有访问它的权限。'
    return render_template('info_cannotaccess.html', **t)


"""
User Management Routers
"""


@app.route('/userManagement/')
@authorizeRequire
@adminRequire
def userManagement():
    flag, res = core.PlatformUserGetAll(session['SID'])
    if flag is False:
        return redirect(url_for('AccessErrorPage', dt='x'))
    t = {'L_PageTitle': u'授权管理',
         'L_PageDescription': u'管理COrgan中的授权访问的用户账户',
         'userList': res}
    return render_template('usermanagement.html', **t)


@app.route('/userManagement/add/')
@authorizeRequire
@adminRequire
def addPlatformUser():
    t = {'L_PageTitle': u'添加授权',
         'L_PageDescription': u'为COrgan添加一个授权访问的用户账户'}
    return render_template('usermanagement_add.html', **t)


@app.route('/userManagement/performadd/', methods=["POST"])
@authorizeRequire
@adminRequire
def performAddPlatformUser():
    from Utility.EncryptUtil import EncryptUtil
    flag, res = core.PlatformUserAdd(session['SID'],
                                     request.form['f_username'],
                                     EncryptUtil.EncryptSHA256(request.form['f_nPassword']),
                                     1 if request.form['f_level'] == u"管理员" else 0)
    if (flag & res) is False:
        return redirect(url_for('AccessErrorPage', dt='add'))
    return redirect(url_for('userManagement'))


@app.route('/userManagement/performdelete/<uname>/', methods=["GET"])
@authorizeRequire
@adminRequire
def performDeletePlatformUser(uname):
    flag, res = core.PlatformUserRemove(session['SID'], uname)
    if flag is False:
        return redirect(url_for('AccessErrorPage', dt='x'))
    return redirect(url_for('userManagement'))


@app.route('/userManagement/edit/<uname>/', methods=["GET"])
@authorizeRequire
@adminRequire
def editPlatformUser(uname):
    flag, res = core.PlatformUserGet(session['SID'], uname)
    if flag is False:
        return redirect(url_for('AccessErrorPage', dt='x'))
    t = {'L_PageTitle': u'编辑: ' + uname,
         'L_PageDescription': u'编辑授权',
         'UserObj': res}
    return render_template('usermanagement_edit.html', **t)


@app.route('/userManagement/performedit/', methods=["POST"])
@authorizeRequire
@adminRequire
def performEditPlatformUser():
    pwd = None
    if request.form['f_nPassword'] != "":
        from Utility.EncryptUtil import EncryptUtil
        pwd = EncryptUtil.EncryptSHA256(request.form['f_nPassword'])
    flag, res = core.PlatformUserUpdate(session['SID'],
                                        request.form['h_username'],
                                        pwd,
                                        1 if request.form['f_level'] == u"管理员" else 0)
    if flag is False:
        return redirect(url_for('AccessErrorPage', dt='x'))
    return redirect(url_for('userManagement'))


"""
Capability Management Routers
"""


@app.route('/capability/')
@authorizeRequire
def capabilityManagement():
    flag, res = core.RetrieveAllCapabilities(session['SID'])
    if flag is False:
        return redirect(url_for('AccessErrorPage', dt='x'))
    t = {'L_PageTitle': u'能力管理',
         'L_PageDescription': u'管理组织中的能力清单',
         'bindList': res}
    return render_template('capabilitymanagement.html', **t)


@app.route('/capability/add')
@authorizeRequire
def addCapability():
    t = {'L_PageTitle': u'添加人力',
         'L_PageDescription': u'为组织添加一个人力资源'}
    return render_template('capabilitymanagement_add.html', **t)


@app.route('/capability/performadd/', methods=["POST"])
@authorizeRequire
def performAddCapability():
    flag, res = core.AddCapability(session['SID'],
                                   request.form['f_capaname'],
                                   request.form['f_description'],
                                   request.form['f_note'])
    if flag is False or res is None:
        return redirect(url_for('AccessErrorPage', dt='add'))
    return redirect(url_for('capabilityManagement'))


@app.route('/capability/edit/<uname>/', methods=["GET"])
@authorizeRequire
def editCapability(uname):
    flag, res = core.RetrieveCapability(session['SID'], uname)
    if flag is False:
        return redirect(url_for('AccessErrorPage', dt='x'))
    t = {'L_PageTitle': u'编辑: ' + uname,
         'L_PageDescription': u'编辑能力描述项',
         'packItem': res}
    return render_template('capabilitymanagement_edit.html', **t)


@app.route('/capability/performedit/', methods=["POST"])
@authorizeRequire
def performEditCapability():
    updateDict = {
        "description": "'%s'" % request.form['f_description'],
        "note": "'%s'" % request.form['f_note']
    }
    flag, res = core.UpdateCapability(session['SID'],
                                      request.form['h_capaname'],
                                      **updateDict)
    if flag is False:
        return redirect(url_for('AccessErrorPage', dt='x'))
    return redirect(url_for('capabilityManagement'))


@app.route('/capability/performdelete/<uname>/', methods=["GET"])
@authorizeRequire
def performDeleteCapability(uname):
    flag, res = core.RemoveCapability(session['SID'], uname)
    if flag is False:
        return redirect(url_for('AccessErrorPage', dt='x'))
    return redirect(url_for('capabilityManagement'))


"""
Group Resources Management Routers
"""


@app.route('/group/')
@authorizeRequire
def groupManagement():
    flag, res = core.RetrieveAllGroup(session['SID'])
    if flag is False:
        return redirect(url_for('AccessErrorPage', dt='x'))
    belongToList = []
    typeList = []
    for group in res:
        if group.BelongToGroupId is not '':
            xflag, belonger = core.RetrieveGroupById(session['SID'], group.BelongToGroupId)
            if xflag is False:
                redirect(url_for('AccessErrorPage', dt='x'))
            if belonger is not None:
                belongToList.append(belonger.Name)
            else:
                belongToList.append('')
        else:
            belongToList.append('')
        typeList.append(CController.CController.ParseGroupTypeEnum(group.GroupType))
    t = {'L_PageTitle': u'子组管理',
         'L_PageDescription': u'管理组织中的子组清单',
         'bindList': res,
         'belongToList': belongToList,
         'typeList': typeList}
    return render_template('groupmanagement.html', **t)


@app.route('/group/add')
@authorizeRequire
def addGroup():
    flag, ret = core.RetrieveAllGroup(session['SID'])
    if flag is False:
        return redirect(url_for('AccessErrorPage', dt='x'))
    t = {'L_PageTitle': u'添加子组',
         'L_PageDescription': u'为组织添加一个子组',
         'groupList': ret}
    return render_template('groupmanagement_add.html', **t)


@app.route('/group/performadd/', methods=["POST"])
@authorizeRequire
def performAddGroup():
    belongToId = request.form['f_belong']
    gid = ''
    if belongToId != '(None)':
        xflag, gid = core.RetrieveGroupId(session['SID'], belongToId)
        if xflag is False or gid is None:
            return redirect(url_for('AccessErrorPage', dt='x'))
    flag, res = core.AddGroup(session['SID'],
                              request.form['f_groupname'],
                              request.form['f_description'],
                              request.form['f_note'],
                              gid,
                              int(request.form['f_type']))
    if flag is False or res is None:
        return redirect(url_for('AccessErrorPage', dt='add'))
    return redirect(url_for('groupManagement'))


@app.route('/group/edit/<uname>/', methods=["GET"])
@authorizeRequire
def editGroup(uname):
    flag, res = core.RetrieveGroup(session['SID'], uname)
    if flag is False or res is None:
        return redirect(url_for('AccessErrorPage', dt='x'))
    flag, grpList = core.RetrieveAllGroup(session['SID'])
    if flag is False or grpList is None:
        return redirect(url_for('AccessErrorPage', dt='x'))
    # remove self
    rIdx = None
    for grp in grpList:
        if grp.Name == uname:
            rIdx = grp
            break
    if rIdx is not None:
        grpList.remove(rIdx)
    else:
        return redirect(url_for('AccessErrorPage', dt='x'))
    belongIdx = 0
    if res.BelongToGroupId != '':
        flag, belongToObj = core.RetrieveGroupById(session['SID'], res.BelongToGroupId)
        if flag is False or belongToObj is None:
            return redirect(url_for('AccessErrorPage', dt='x'))
        for x in range(0, len(grpList)):
            if grpList[x].Name == belongToObj.Name:
                belongIdx = x + 1
                break
    t = {'L_PageTitle': u'编辑: ' + uname,
         'L_PageDescription': u'编辑子组描述项',
         'packItem': res,
         'groupList': grpList,
         'belongIdx': belongIdx}
    return render_template('groupmanagement_edit.html', **t)


@app.route('/group/performedit/', methods=["POST"])
@authorizeRequire
def performEditGroup():
    belongToId = request.form['f_belong']
    gid = None
    if belongToId != '(None)':
        xflag, gid = core.RetrieveGroupId(session['SID'], belongToId)
        if xflag is False or gid is None:
            return redirect(url_for('AccessErrorPage', dt='x'))
    belongToIdText = "'%s'" % gid if gid is not None else "''"
    updateDict = {
        "description": "'%s'" % request.form['f_description'],
        "note": "'%s'" % request.form['f_note'],
        "groupType": "%s" % int(request.form['f_type']),
        "belongToId": belongToIdText
    }
    flag, res = core.UpdateGroup(session['SID'],
                                 request.form['h_groupname'],
                                 **updateDict)
    if flag is False:
        return redirect(url_for('AccessErrorPage', dt='x'))
    return redirect(url_for('groupManagement'))


@app.route('/group/performdelete/<uname>/', methods=["GET"])
@authorizeRequire
def performDeleteGroup(uname):
    flag, res = core.RemoveGroup(session['SID'], uname)
    if (flag & res) is False:
        return redirect(url_for('AccessErrorPage', dt='x'))
    return redirect(url_for('groupManagement'))


"""
Position Resources Management Routers
"""


@app.route('/position/')
@authorizeRequire
def positionManagement():
    flag, res = core.RetrieveAllPosition(session['SID'])
    if flag is False:
        return redirect(url_for('AccessErrorPage', dt='x'))
    belongToList = []
    reportToList = []
    for pos in res:
        from Entity.Position import Position
        assert isinstance(pos, Position)
        # report
        if pos.ReportToPosition != '':
            xflag, reporter = core.RetrievePositionById(session['SID'], pos.ReportToPosition)
            if xflag is False:
                redirect(url_for('AccessErrorPage', dt='x'))
            if reporter is not None:
                reportToList.append(reporter.Name)
            else:
                reportToList.append('')
        else:
            reportToList.append('')
        # belong
        if pos.BelongToGroup != '':
            xflag, belonger = core.RetrieveGroupById(session['SID'], pos.BelongToGroup)
            if xflag is False:
                redirect(url_for('AccessErrorPage', dt='x'))
            if belonger is not None:
                belongToList.append(belonger.Name)
            else:
                belongToList.append('')
        else:
            belongToList.append('')
    t = {'L_PageTitle': u'职位管理',
         'L_PageDescription': u'管理组织中的职位清单',
         'bindList': res,
         'belongToList': belongToList,
         'reportToList': reportToList}
    return render_template('positionmanagement.html', **t)


@app.route('/position/add')
@authorizeRequire
def addPosition():
    gflag, groups = core.RetrieveAllGroup(session['SID'])
    pflag, positions = core.RetrieveAllPosition(session['SID'])
    if (gflag & pflag) is False:
        return redirect(url_for('AccessErrorPage', dt='x'))
    t = {'L_PageTitle': u'添加职位',
         'L_PageDescription': u'为组织添加一个职务',
         'groupList': groups,
         'positionList': positions}
    return render_template('positionmanagement_add.html', **t)


@app.route('/position/performadd/', methods=["POST"])
@authorizeRequire
def performAddPosition():
    reportToId = request.form['f_report']
    reportGid = ''
    if reportToId != '(None)':
        xflag, reportGid = core.RetrievePositionId(session['SID'], reportToId)
        if xflag is False or reportGid is None:
            return redirect(url_for('AccessErrorPage', dt='x'))
    if 'f_belong' in request.form:
        belongToId = request.form['f_belong']
    else:
        belongToId = ''
    xflag, belongGid = core.RetrieveGroupId(session['SID'], belongToId)
    if xflag is False or belongGid is None:
        return redirect(url_for('AccessErrorPage', dt='x'))
    flag, res = core.AddPosition(session['SID'],
                                 request.form['f_positionname'],
                                 request.form['f_description'],
                                 request.form['f_note'],
                                 belongGid,
                                 reportGid)
    if flag is False or res is None:
        return redirect(url_for('AccessErrorPage', dt='add'))
    return redirect(url_for('positionManagement'))


@app.route('/position/edit/<uname>/', methods=["GET"])
@authorizeRequire
def editPosition(uname):
    flag, res = core.RetrievePosition(session['SID'], uname)
    if flag is False or res is None:
        return redirect(url_for('AccessErrorPage', dt='x'))
    flag, grpList = core.RetrieveAllGroup(session['SID'])
    if flag is False or grpList is None:
        return redirect(url_for('AccessErrorPage', dt='x'))
    flag, posList = core.RetrieveAllPosition(session['SID'])
    if flag is False or posList is None:
        return redirect(url_for('AccessErrorPage', dt='x'))
    # remove self
    rIdx = None
    for pos in posList:
        if pos.Name == uname:
            rIdx = pos
            break
    if rIdx is not None:
        posList.remove(rIdx)
    else:
        return redirect(url_for('AccessErrorPage', dt='x'))
    belongIdx = 0
    flag, belongToObj = core.RetrieveGroupById(session['SID'], res.BelongToGroup)
    if flag is False or belongToObj is None:
        return redirect(url_for('AccessErrorPage', dt='x'))
    for x in range(0, len(grpList)):
        if grpList[x].Name == belongToObj.Name:
            belongIdx = x
            break
    reportIdx = 0
    if res.ReportToPosition != '':
        flag, reportToObj = core.RetrievePositionById(session['SID'], res.ReportToPosition)
        if flag is False or reportToObj is None:
            return redirect(url_for('AccessErrorPage', dt='x'))
        for x in range(0, len(posList)):
            if posList[x].Name == reportToObj.Name:
                reportIdx = x + 1
                break
    t = {'L_PageTitle': u'编辑: ' + uname,
         'L_PageDescription': u'编辑子组描述项',
         'packItem': res,
         'groupList': grpList,
         'positionList': posList,
         'belongIdx': belongIdx,
         'reportIdx': reportIdx}
    return render_template('positionmanagement_edit.html', **t)


@app.route('/position/performedit/', methods=["POST"])
@authorizeRequire
def performEditPosition():
    belongToId = request.form['f_belong']
    xflag, belongGid = core.RetrieveGroupId(session['SID'], belongToId)
    if xflag is False or belongGid is None:
        return redirect(url_for('AccessErrorPage', dt='x'))
    belongToIdText = "'%s'" % belongGid if belongGid is not None else "''"

    reportToId = request.form['f_report']
    reportGid = None
    if reportToId != '(None)':
        xflag, reportGid = core.RetrievePositionId(session['SID'], reportToId)
        if xflag is False or reportGid is None:
            return redirect(url_for('AccessErrorPage', dt='x'))
    reportToIdText = "'%s'" % reportGid if reportGid is not None else "''"
    updateDict = {
        "description": "'%s'" % request.form['f_description'],
        "note": "'%s'" % request.form['f_note'],
        "belongToId": belongToIdText,
        "reportToId": reportToIdText
    }
    flag, res = core.UpdatePosition(session['SID'],
                                    request.form['h_positionname'],
                                    **updateDict)
    if flag is False:
        return redirect(url_for('AccessErrorPage', dt='x'))
    return redirect(url_for('positionManagement'))


@app.route('/position/performdelete/<uname>/', methods=["GET"])
@authorizeRequire
def performDeletePosition(uname):
    flag, res = core.RemovePosition(session['SID'], uname)
    if (flag & res) is False:
        return redirect(url_for('AccessErrorPage', dt='x'))
    return redirect(url_for('positionManagement'))


"""
Human Resources Management Routers
"""


@app.route('/human/')
@authorizeRequire
def humanManagement():
    flag, res = core.RetrieveAllHuman(session['SID'])
    if flag is False or res is None:
        return redirect(url_for('AccessErrorPage', dt='x'))
    capabilityList = []
    groupList = []
    positionList = []
    for h in res:
        xflag, groups = core.RetrieveHumanInWhatGroup(session['SID'], h.PersonId)
        if xflag is False or groups is None:
            return redirect(url_for('AccessErrorPage', dt='add'))
        groupList.append('<br/>'.join(groups) + '<br/>')
        xflag, capabilities = core.RetrieveHumanWithWhatCapability(session['SID'], h.PersonId)
        if xflag is False or capabilities is None:
            return redirect(url_for('AccessErrorPage', dt='add'))
        capabilityList.append('<br/>'.join(capabilities) + '<br/>')
        xflag, positions = core.RetrieveHumanInWhatPosition(session['SID'], h.PersonId)
        if xflag is False or positions is None:
            return redirect(url_for('AccessErrorPage', dt='add'))
        positionList.append('<br/>'.join(positions) + '<br/>')
    t = {'L_PageTitle': u'人力资源管理',
         'L_PageDescription': u'管理组织中的人力资源',
         'userList': res,
         'capabilityList': capabilityList,
         'groupList': groupList,
         'positionList': positionList}
    return render_template('humanmanagement.html', **t)


@app.route('/human/add/')
@authorizeRequire
def addHuman():
    flag1, groupList = core.RetrieveAllGroup(session['SID'])
    flag2, positionList = core.RetrieveAllPosition(session['SID'])
    flag3, capabilityList = core.RetrieveAllCapabilities(session['SID'])
    if (flag1 & flag2 & flag3) is False or groupList is None or positionList is None or capabilityList is None:
        return redirect(url_for('AccessErrorPage', dt='x'))
    t = {'L_PageTitle': u'添加人力',
         'L_PageDescription': u'为组织添加一个人力资源',
         'groupList': groupList,
         'positionList': positionList,
         'capabilityList': capabilityList}
    return render_template('humanmanagement_add.html', **t)


@app.route('/human/performadd/', methods=["POST"])
@authorizeRequire
def performAddHuman():
    pid = request.form['f_personid']
    flag, res = core.AddHuman(session['SID'],
                              pid,
                              request.form['f_firstname'],
                              request.form['f_lastname'],
                              request.form['f_note'])
    if flag is False or res is None:
        return redirect(url_for('AccessErrorPage', dt='add'))
    capaVec = []
    groupVec = []
    posVec = []
    if request.form['output_capability'] != '':
        capaVec = request.form['output_capability'].split(';')
    if request.form['output_group'] != '':
        groupVec = request.form['output_group'].split(';')
    if request.form['output_position'] != '':
        posVec = request.form['output_position'].split(';')
    for cp in capaVec:
        xflag, xret = core.AddHumanCapability(session['SID'], pid, cp)
        if xflag is False or xret is None:
            return redirect(url_for('AccessErrorPage', dt='x'))
    for gr in groupVec:
        xflag, xret = core.AddHumanToGroup(session['SID'], pid, gr)
        if xflag is False or xret is None:
            return redirect(url_for('AccessErrorPage', dt='x'))
    for ps in posVec:
        xflag, xret = core.AddHumanPosition(session['SID'], pid, ps)
        if xflag is False or xret is None:
            return redirect(url_for('AccessErrorPage', dt='x'))
    return redirect(url_for('humanManagement'))


@app.route('/human/edit/<uname>/', methods=["GET"])
@authorizeRequire
def editHuman(uname):
    flag, res = core.RetrieveHuman(session['SID'], uname)
    if flag is False or res is None:
        return redirect(url_for('AccessErrorPage', dt='x'))
    xflag, groups = core.RetrieveHumanInWhatGroup(session['SID'], res.PersonId)
    if xflag is False or groups is None:
        return redirect(url_for('AccessErrorPage', dt='x'))
    groupStr = ';'.join(groups)
    xflag, capabilities = core.RetrieveHumanWithWhatCapability(session['SID'], res.PersonId)
    if xflag is False or capabilities is None:
        return redirect(url_for('AccessErrorPage', dt='x'))
    capabilityStr = ';'.join(capabilities)
    xflag, positions = core.RetrieveHumanInWhatPosition(session['SID'], res.PersonId)
    if xflag is False or positions is None:
        return redirect(url_for('AccessErrorPage', dt='x'))
    positionStr = ';'.join(positions)
    flag1, groupList = core.RetrieveAllGroup(session['SID'])
    flag2, positionList = core.RetrieveAllPosition(session['SID'])
    flag3, capabilityList = core.RetrieveAllCapabilities(session['SID'])
    if (flag1 & flag2 & flag3) is False or groupList is None or positionList is None or capabilityList is None:
        return redirect(url_for('AccessErrorPage', dt='x'))
    t = {'L_PageTitle': u'编辑: ' + uname,
         'L_PageDescription': u'编辑子组描述项',
         'packItem': res,
         'groupList': groupList,
         'positionList': positionList,
         'capabilityList': capabilityList,
         'groupStr': groupStr,
         'capabilityStr': capabilityStr,
         'positionStr': positionStr}
    return render_template('humanmanagement_edit.html', **t)


@app.route('/human/performedit/', methods=["POST"])
@authorizeRequire
def performEditHuman():
    pid = request.form['h_personid']
    updateDict = {
        "firstname": "'%s'" % request.form['f_firstname'],
        "lastname": "'%s'" % request.form['f_lastname'],
        "note": "'%s'" % request.form['f_note'],
    }
    flag, res = core.UpdateHuman(session['SID'],
                                 pid,
                                 **updateDict)
    if (flag & res) is False:
        return redirect(url_for('AccessErrorPage', dt='x'))
    flag, removeFlag = core.RemoveHumanConnection(session['SID'], pid)
    if (flag & removeFlag) is False:
        return redirect(url_for('AccessErrorPage', dt='x'))
    capaVec = []
    groupVec = []
    posVec = []
    if request.form['output_capability'] != '':
        capaVec = request.form['output_capability'].split(';')
    if request.form['output_group'] != '':
        groupVec = request.form['output_group'].split(';')
    if request.form['output_position'] != '':
        posVec = request.form['output_position'].split(';')
    for cp in capaVec:
        xflag, xret = core.AddHumanCapability(session['SID'], pid, cp)
        if xflag is False or xret is None:
            return redirect(url_for('AccessErrorPage', dt='x'))
    for gr in groupVec:
        xflag, xret = core.AddHumanToGroup(session['SID'], pid, gr)
        if xflag is False or xret is None:
            return redirect(url_for('AccessErrorPage', dt='x'))
    for ps in posVec:
        xflag, xret = core.AddHumanPosition(session['SID'], pid, ps)
        if xflag is False or xret is None:
            return redirect(url_for('AccessErrorPage', dt='x'))
    return redirect(url_for('humanManagement'))


@app.route('/human/performdelete/<uname>/', methods=["GET"])
@authorizeRequire
def performDeleteHuman(uname):
    flag, res = core.RemoveHuman(session['SID'], uname)
    if (flag & res) is False:
        return redirect(url_for('AccessErrorPage', dt='x'))
    return redirect(url_for('humanManagement'))


"""
Agent Resources Management Routers
"""


@app.route('/agent/')
@authorizeRequire
def agentManagement():
    flag, res = core.RetrieveAllAgent(session['SID'])
    if flag is False or res is None:
        return redirect(url_for('AccessErrorPage', dt='x'))
    capabilityList = []
    groupList = []
    positionList = []
    for h in res:
        xflag, groups = core.RetrieveAgentInWhatGroup(session['SID'], h.Name)
        if xflag is False or groups is None:
            return redirect(url_for('AccessErrorPage', dt='add'))
        groupList.append('<br/>'.join(groups) + '<br/>')
        xflag, capabilities = core.RetrieveAgentWithWhatCapability(session['SID'], h.Name)
        if xflag is False or capabilities is None:
            return redirect(url_for('AccessErrorPage', dt='add'))
        capabilityList.append('<br/>'.join(capabilities) + '<br/>')
        xflag, positions = core.RetrieveAgentInWhatPosition(session['SID'], h.Name)
        if xflag is False or positions is None:
            return redirect(url_for('AccessErrorPage', dt='add'))
        positionList.append('<br/>'.join(positions) + '<br/>')
    t = {'L_PageTitle': u'Agent资源管理',
         'L_PageDescription': u'管理组织中的Agent资源',
         'userList': res,
         'capabilityList': capabilityList,
         'groupList': groupList,
         'positionList': positionList}
    return render_template('agentmanagement.html', **t)


@app.route('/agent/add/')
@authorizeRequire
def addAgent():
    flag1, groupList = core.RetrieveAllGroup(session['SID'])
    flag2, positionList = core.RetrieveAllPosition(session['SID'])
    flag3, capabilityList = core.RetrieveAllCapabilities(session['SID'])
    if (flag1 & flag2 & flag3) is False or groupList is None or positionList is None or capabilityList is None:
        return redirect(url_for('AccessErrorPage', dt='x'))
    t = {'L_PageTitle': u'添加Agent',
         'L_PageDescription': u'为组织添加一个Agent资源',
         'groupList': groupList,
         'positionList': positionList,
         'capabilityList': capabilityList}
    return render_template('agentmanagement_add.html', **t)


@app.route('/agent/performadd/', methods=["POST"])
@authorizeRequire
def performAddAgent():
    agentName = request.form['f_name']
    flag, res = core.AddAgent(session['SID'],
                              agentName,
                              request.form['f_location'],
                              int(request.form['f_type']),
                              request.form['f_note'])
    if flag is False or res is None:
        return redirect(url_for('AccessErrorPage', dt='add'))
    capaVec = []
    groupVec = []
    posVec = []
    if request.form['output_capability'] != '':
        capaVec = request.form['output_capability'].split(';')
    if request.form['output_group'] != '':
        groupVec = request.form['output_group'].split(';')
    if request.form['output_position'] != '':
        posVec = request.form['output_position'].split(';')
    for cp in capaVec:
        xflag, xret = core.AddAgentCapability(session['SID'], agentName, cp)
        if xflag is False or xret is None:
            return redirect(url_for('AccessErrorPage', dt='x'))
    for gr in groupVec:
        xflag, xret = core.AddAgentToGroup(session['SID'], agentName, gr)
        if xflag is False or xret is None:
            return redirect(url_for('AccessErrorPage', dt='x'))
    for ps in posVec:
        xflag, xret = core.AddAgentPosition(session['SID'], agentName, ps)
        if xflag is False or xret is None:
            return redirect(url_for('AccessErrorPage', dt='x'))
    return redirect(url_for('agentManagement'))


@app.route('/agent/edit/<uname>/', methods=["GET"])
@authorizeRequire
def editAgent(uname):
    flag, res = core.RetrieveAgent(session['SID'], uname)
    if flag is False or res is None:
        return redirect(url_for('AccessErrorPage', dt='x'))
    xflag, groups = core.RetrieveAgentInWhatGroup(session['SID'], res.Name)
    if xflag is False or groups is None:
        return redirect(url_for('AccessErrorPage', dt='x'))
    groupStr = ';'.join(groups)
    xflag, capabilities = core.RetrieveAgentWithWhatCapability(session['SID'], res.Name)
    if xflag is False or capabilities is None:
        return redirect(url_for('AccessErrorPage', dt='x'))
    capabilityStr = ';'.join(capabilities)
    xflag, positions = core.RetrieveAgentInWhatPosition(session['SID'], res.Name)
    if xflag is False or positions is None:
        return redirect(url_for('AccessErrorPage', dt='x'))
    positionStr = ';'.join(positions)
    flag1, groupList = core.RetrieveAllGroup(session['SID'])
    flag2, positionList = core.RetrieveAllPosition(session['SID'])
    flag3, capabilityList = core.RetrieveAllCapabilities(session['SID'])
    if (flag1 & flag2 & flag3) is False or groupList is None or positionList is None or capabilityList is None:
        return redirect(url_for('AccessErrorPage', dt='x'))
    t = {'L_PageTitle': u'编辑: ' + uname,
         'L_PageDescription': u'编辑Agent描述项',
         'packItem': res,
         'groupList': groupList,
         'positionList': positionList,
         'capabilityList': capabilityList,
         'groupStr': groupStr,
         'capabilityStr': capabilityStr,
         'positionStr': positionStr}
    return render_template('agentmanagement_edit.html', **t)


@app.route('/agent/performedit/', methods=["POST"])
@authorizeRequire
def performEditAgent():
    agentName = request.form['h_name']
    updateDict = {
        "location": "'%s'" % request.form['f_location'],
        "type": "%s" % int(request.form['f_type']),
        "note": "'%s'" % request.form['f_note'],
    }
    flag, res = core.UpdateAgent(session['SID'],
                                 agentName,
                                 **updateDict)
    if (flag & res) is False:
        return redirect(url_for('AccessErrorPage', dt='x'))
    flag, removeFlag = core.RemoveAgentConnection(session['SID'], agentName)
    if (flag & removeFlag) is False:
        return redirect(url_for('AccessErrorPage', dt='x'))
    capaVec = []
    groupVec = []
    posVec = []
    if request.form['output_capability'] != '':
        capaVec = request.form['output_capability'].split(';')
    if request.form['output_group'] != '':
        groupVec = request.form['output_group'].split(';')
    if request.form['output_position'] != '':
        posVec = request.form['output_position'].split(';')
    for cp in capaVec:
        xflag, xret = core.AddAgentCapability(session['SID'], agentName, cp)
        if xflag is False or xret is None:
            return redirect(url_for('AccessErrorPage', dt='x'))
    for gr in groupVec:
        xflag, xret = core.AddAgentToGroup(session['SID'], agentName, gr)
        if xflag is False or xret is None:
            return redirect(url_for('AccessErrorPage', dt='x'))
    for ps in posVec:
        xflag, xret = core.AddAgentPosition(session['SID'], agentName, ps)
        if xflag is False or xret is None:
            return redirect(url_for('AccessErrorPage', dt='x'))
    return redirect(url_for('agentManagement'))


@app.route('/agent/performdelete/<uname>/', methods=["GET"])
@authorizeRequire
def performDeleteAgent(uname):
    flag, res = core.RemoveAgent(session['SID'], uname)
    if (flag & res) is False:
        return redirect(url_for('AccessErrorPage', dt='x'))
    return redirect(url_for('agentManagement'))


"""
Config Management Routers
"""


@app.route('/config/')
@authorizeRequire
@adminRequire
def configManagement():
    flag1, organ = core.GetOrganizationName(session['SID'])
    flag2, notifier = core.GetUpdateNotifyRouter(session['SID'])
    flag3, dataversion = core.GetCurrentDataVersion(session['SID'])
    flag4, oid = core.GetOrganizationId(session['SID'])
    if (flag1 & flag2 & flag3 & flag4) is False:
        return redirect(url_for('AccessErrorPage', dt='x'))
    t = {'L_PageTitle': u'设置',
         'L_PageDescription': u'管理COrgan的配置设置',
         'my_organ': organ,
         'my_notifier': notifier,
         'my_dataversion': dataversion,
         'my_orgid': oid
         }
    return render_template('configpage.html', **t)


@app.route('/config/performUpdate', methods=["POST"])
@authorizeRequire
@adminRequire
def performUpdateConfig():
    flag1, ret1 = core.SetOrganizationName(session['SID'], request.form['f_organ'])
    flag2, ret2 = core.SetUpdateNotifyRouter(session['SID'], request.form['f_notifier'])
    if (flag1 & flag2) is False:
        return redirect(url_for('AccessErrorPage', dt='x'))
    return redirect(url_for('configManagement'))


"""
Login Routers
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


@app.route('/performLogin/', methods=["GET", "POST"])
def performLogin():
    if request.method == 'GET':
        return redirect(url_for('Login'))
    usrId = request.form["passedUserId"]
    usrPwd = request.form["passedUserPwd"]
    import re
    if re.match('^[A-Za-z0-9@.]+$', usrId) is None:
        return redirect(url_for('Login2'))
    from Utility.EncryptUtil import EncryptUtil
    usrPwd = EncryptUtil.EncryptSHA256(usrPwd)
    flag, ret = core.Connect(usrId, usrPwd)
    if flag is False or ret is None:
        return redirect(url_for('Login2'))
    session['AuID'] = usrId
    session['SID'] = ret
    session['AuType'] = 1 if core.AmIAdmin(ret)[1] is True else 0
    return redirect(url_for('home'))


@app.route('/performLogout/', methods=["GET", "POST"])
def performLogout():
    """
    Perform logout action.
    """
    return redirect(url_for('Login'))


def _logout():
    """
    Perform Logout logic and clear the session.
    """
    if 'SID' in session:
        sid = session['SID']
        if sid != "" and sid is not None:
            flag, ret = core.Disconnect(session['SID'])
            session.clear()


"""
COrgan App Entry Point
"""
if __name__ == '__main__':
    app.secret_key = GlobalConfigContext.RAPPKEY
    app.debug = True
    app.register_blueprint(restfulBp)
    app.run(host='127.0.0.1', port=10235)
