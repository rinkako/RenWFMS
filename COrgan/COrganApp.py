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
    elif dt == "add":
        t["msg"] = u'添加新资源失败，请确认资源字段的正确性，以及名字是否唯一。'
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
    if flag is False or res is None:
        return redirect(url_for('AccessErrorPage', dt='add'))
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


"""
Capability Management Routers
"""


@app.route('/capability/')
def capabilityManagement():
    flag, res = core.RetrieveAllCapabilities('testadmin')
    if flag is False:
        return redirect(url_for('AccessErrorPage', dt='x'))
    t = {'L_PageTitle': u'能力管理',
         'L_PageDescription': u'管理组织中的能力清单',
         'bindList': res}
    return render_template('capabilitymanagement.html', **t)


@app.route('/capability/add')
def addCapability():
    t = {'L_PageTitle': u'添加人力',
         'L_PageDescription': u'为组织添加一个人力资源'}
    return render_template('capabilitymanagement_add.html', **t)


@app.route('/capability/performadd/', methods=["POST"])
def performAddCapability():
    flag, res = core.AddCapability('testadmin',
                                   request.form['f_capaname'],
                                   request.form['f_description'],
                                   request.form['f_note'])
    if flag is False or res is None:
        return redirect(url_for('AccessErrorPage', dt='add'))
    return redirect(url_for('capabilityManagement'))


@app.route('/capability/edit/<uname>/', methods=["GET"])
def editCapability(uname):
    flag, res = core.RetrieveCapability('testadmin', uname)
    if flag is False:
        return redirect(url_for('AccessErrorPage', dt='x'))
    t = {'L_PageTitle': u'编辑: ' + uname,
         'L_PageDescription': u'编辑能力描述项',
         'packItem': res}
    return render_template('capabilitymanagement_edit.html', **t)


@app.route('/capability/performedit/', methods=["POST"])
def performEditCapability():
    updateDict = {
        "description": "'%s'" % request.form['f_description'],
        "note": "'%s'" % request.form['f_note']
    }
    flag, res = core.UpdateCapability('testadmin',
                                      request.form['h_capaname'],
                                      **updateDict)
    if flag is False:
        return redirect(url_for('AccessErrorPage', dt='x'))
    return redirect(url_for('capabilityManagement'))


@app.route('/capability/performdelete/<uname>/', methods=["GET"])
def performDeleteCapability(uname):
    flag, res = core.RemoveCapability('testadmin', uname)
    if flag is False:
        return redirect(url_for('AccessErrorPage', dt='x'))
    return redirect(url_for('capabilityManagement'))


"""
Group Resources Management Routers
"""


@app.route('/group/')
def groupManagement():
    flag, res = core.RetrieveAllGroup('testadmin')
    if flag is False:
        return redirect(url_for('AccessErrorPage', dt='x'))
    belongToList = []
    typeList = []
    for group in res:
        if group.BelongToGroupId is not '':
            xflag, belonger = core.RetrieveGroupById('testadmin', group.BelongToGroupId)
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
def addGroup():
    flag, ret = core.RetrieveAllGroup('testadmin')
    if flag is False:
        return redirect(url_for('AccessErrorPage', dt='x'))
    t = {'L_PageTitle': u'添加子组',
         'L_PageDescription': u'为组织添加一个子组',
         'groupList': ret}
    return render_template('groupmanagement_add.html', **t)


@app.route('/group/performadd/', methods=["POST"])
def performAddGroup():
    belongToId = request.form['f_belong']
    gid = ''
    if belongToId != '(None)':
        xflag, gid = core.RetrieveGroupId('testadmin', belongToId)
        if xflag is False or gid is None:
            return redirect(url_for('AccessErrorPage', dt='x'))
    flag, res = core.AddGroup('testadmin',
                              request.form['f_groupname'],
                              request.form['f_description'],
                              request.form['f_note'],
                              gid,
                              int(request.form['f_type']))
    if flag is False or res is None:
        return redirect(url_for('AccessErrorPage', dt='add'))
    return redirect(url_for('groupManagement'))


@app.route('/group/edit/<uname>/', methods=["GET"])
def editGroup(uname):
    flag, res = core.RetrieveGroup('testadmin', uname)
    if flag is False or res is None:
        return redirect(url_for('AccessErrorPage', dt='x'))
    flag, grpList = core.RetrieveAllGroup('testadmin')
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
        flag, belongToObj = core.RetrieveGroupById('testadmin', res.BelongToGroupId)
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
def performEditGroup():
    belongToId = request.form['f_belong']
    gid = None
    if belongToId != '(None)':
        xflag, gid = core.RetrieveGroupId('testadmin', belongToId)
        if xflag is False or gid is None:
            return redirect(url_for('AccessErrorPage', dt='x'))
    belongToIdText = "'%s'" % gid if gid is not None else "''"
    updateDict = {
        "description": "'%s'" % request.form['f_description'],
        "note": "'%s'" % request.form['f_note'],
        "groupType": "%s" % int(request.form['f_type']),
        "belongToId": belongToIdText
    }
    flag, res = core.UpdateGroup('testadmin',
                                 request.form['h_groupname'],
                                 **updateDict)
    if flag is False:
        return redirect(url_for('AccessErrorPage', dt='x'))
    return redirect(url_for('groupManagement'))


@app.route('/group/performdelete/<uname>/', methods=["GET"])
def performDeleteGroup(uname):
    flag, res = core.RemoveGroup('testadmin', uname)
    if (flag & res) is False:
        return redirect(url_for('AccessErrorPage', dt='x'))
    return redirect(url_for('groupManagement'))


"""
Position Resources Management Routers
"""


@app.route('/position/')
def positionManagement():
    flag, res = core.RetrieveAllPosition('testadmin')
    if flag is False:
        return redirect(url_for('AccessErrorPage', dt='x'))
    belongToList = []
    reportToList = []
    for pos in res:
        from Entity.Position import Position
        assert isinstance(pos, Position)
        # report
        if pos.ReportToPosition != '':
            xflag, reporter = core.RetrievePositionById('testadmin', pos.ReportToPosition)
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
            xflag, belonger = core.RetrieveGroupById('testadmin', pos.BelongToGroup)
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
def addPosition():
    gflag, groups = core.RetrieveAllGroup('testadmin')
    pflag, positions = core.RetrieveAllPosition('testadmin')
    if (gflag & pflag) is False:
        return redirect(url_for('AccessErrorPage', dt='x'))
    t = {'L_PageTitle': u'添加职位',
         'L_PageDescription': u'为组织添加一个职务',
         'groupList': groups,
         'positionList': positions}
    return render_template('positionmanagement_add.html', **t)


@app.route('/position/performadd/', methods=["POST"])
def performAddPosition():
    reportToId = request.form['f_report']
    reportGid = ''
    if reportToId != '(None)':
        xflag, reportGid = core.RetrievePositionId('testadmin', reportToId)
        if xflag is False or reportGid is None:
            return redirect(url_for('AccessErrorPage', dt='x'))
    belongToId = request.form['f_belong']
    xflag, belongGid = core.RetrieveGroupId('testadmin', belongToId)
    if xflag is False or belongGid is None:
        return redirect(url_for('AccessErrorPage', dt='x'))
    flag, res = core.AddPosition('testadmin',
                                 request.form['f_positionname'],
                                 request.form['f_description'],
                                 request.form['f_note'],
                                 belongGid,
                                 reportGid)
    if flag is False or res is None:
        return redirect(url_for('AccessErrorPage', dt='add'))
    return redirect(url_for('positionManagement'))


@app.route('/position/edit/<uname>/', methods=["GET"])
def editPosition(uname):
    flag, res = core.RetrievePosition('testadmin', uname)
    if flag is False or res is None:
        return redirect(url_for('AccessErrorPage', dt='x'))
    flag, grpList = core.RetrieveAllGroup('testadmin')
    if flag is False or grpList is None:
        return redirect(url_for('AccessErrorPage', dt='x'))
    flag, posList = core.RetrieveAllPosition('testadmin')
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
    flag, belongToObj = core.RetrieveGroupById('testadmin', res.BelongToGroup)
    if flag is False or belongToObj is None:
        return redirect(url_for('AccessErrorPage', dt='x'))
    for x in range(0, len(grpList)):
        if grpList[x].Name == belongToObj.Name:
            belongIdx = x
            break
    reportIdx = 0
    if res.ReportToPosition != '':
        flag, reportToObj = core.RetrievePositionById('testadmin', res.ReportToPosition)
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
def performEditPosition():
    belongToId = request.form['f_belong']
    xflag, belongGid = core.RetrieveGroupId('testadmin', belongToId)
    if xflag is False or belongGid is None:
        return redirect(url_for('AccessErrorPage', dt='x'))
    belongToIdText = "'%s'" % belongGid if belongGid is not None else "''"

    reportToId = request.form['f_report']
    reportGid = None
    if reportToId != '(None)':
        xflag, reportGid = core.RetrievePositionId('testadmin', reportToId)
        if xflag is False or reportGid is None:
            return redirect(url_for('AccessErrorPage', dt='x'))
    reportToIdText = "'%s'" % reportGid if reportGid is not None else "''"
    updateDict = {
        "description": "'%s'" % request.form['f_description'],
        "note": "'%s'" % request.form['f_note'],
        "belongToId": belongToIdText,
        "reportToId": reportToIdText
    }
    flag, res = core.UpdatePosition('testadmin',
                                    request.form['h_positionname'],
                                    **updateDict)
    if flag is False:
        return redirect(url_for('AccessErrorPage', dt='x'))
    return redirect(url_for('positionManagement'))


@app.route('/position/performdelete/<uname>/', methods=["GET"])
def performDeletePosition(uname):
    flag, res = core.RemovePosition('testadmin', uname)
    if (flag & res) is False:
        return redirect(url_for('AccessErrorPage', dt='x'))
    return redirect(url_for('positionManagement'))


"""
Human Resources Management Routers
"""


@app.route('/human/')
def humanManagement():
    flag, res = core.RetrieveAllHuman('testadmin')
    if flag is False or res is None:
        return redirect(url_for('AccessErrorPage', dt='x'))
    capabilityList = []
    groupList = []
    positionList = []
    for h in res:
        xflag, groups = core.RetrieveHumanInWhatGroup('testadmin', h.PersonId)
        if xflag is False or groups is None:
            return redirect(url_for('AccessErrorPage', dt='add'))
        groupList.append('<br/>'.join(groups) + '<br/>')
        xflag, capabilities = core.RetrieveHumanWithWhatCapability('testadmin', h.PersonId)
        if xflag is False or capabilities is None:
            return redirect(url_for('AccessErrorPage', dt='add'))
        capabilityList.append('<br/>'.join(capabilities) + '<br/>')
        xflag, positions = core.RetrieveHumanInWhatPosition('testadmin', h.PersonId)
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
def addHuman():
    flag1, groupList = core.RetrieveAllGroup('testadmin')
    flag2, positionList = core.RetrieveAllPosition('testadmin')
    flag3, capabilityList = core.RetrieveAllCapabilities('testadmin')
    if (flag1 & flag2 & flag3) is False or groupList is None or positionList is None or capabilityList is None:
        return redirect(url_for('AccessErrorPage', dt='x'))
    t = {'L_PageTitle': u'添加人力',
         'L_PageDescription': u'为组织添加一个人力资源',
         'groupList': groupList,
         'positionList': positionList,
         'capabilityList': capabilityList}
    return render_template('humanmanagement_add.html', **t)


@app.route('/human/performadd/', methods=["POST"])
def performAddHuman():
    pid = request.form['f_personid']
    flag, res = core.AddHuman('testadmin',
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
        xflag, xret = core.AddHumanCapability('testadmin', pid, cp)
        if xflag is False or xret is None:
            return redirect(url_for('AccessErrorPage', dt='x'))
    for gr in groupVec:
        xflag, xret = core.AddHumanToGroup('testadmin', pid, gr)
        if xflag is False or xret is None:
            return redirect(url_for('AccessErrorPage', dt='x'))
    for ps in posVec:
        xflag, xret = core.AddHumanPosition('testadmin', pid, ps)
        if xflag is False or xret is None:
            return redirect(url_for('AccessErrorPage', dt='x'))
    return redirect(url_for('humanManagement'))


@app.route('/human/edit/<uname>/', methods=["GET"])
def editHuman(uname):
    flag, res = core.RetrieveHuman('testadmin', uname)
    if flag is False or res is None:
        return redirect(url_for('AccessErrorPage', dt='x'))
    xflag, groups = core.RetrieveHumanInWhatGroup('testadmin', res.PersonId)
    if xflag is False or groups is None:
        return redirect(url_for('AccessErrorPage', dt='x'))
    groupStr = ';'.join(groups)
    xflag, capabilities = core.RetrieveHumanWithWhatCapability('testadmin', res.PersonId)
    if xflag is False or capabilities is None:
        return redirect(url_for('AccessErrorPage', dt='x'))
    capabilityStr = ';'.join(capabilities)
    xflag, positions = core.RetrieveHumanInWhatPosition('testadmin', res.PersonId)
    if xflag is False or positions is None:
        return redirect(url_for('AccessErrorPage', dt='x'))
    positionStr = ';'.join(positions)
    flag1, groupList = core.RetrieveAllGroup('testadmin')
    flag2, positionList = core.RetrieveAllPosition('testadmin')
    flag3, capabilityList = core.RetrieveAllCapabilities('testadmin')
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
def performEditHuman():
    pid = request.form['h_personid']
    updateDict = {
        "firstname": "'%s'" % request.form['f_firstname'],
        "lastname": "'%s'" % request.form['f_lastname'],
        "note": "'%s'" % request.form['f_note'],
    }
    flag, res = core.UpdateHuman('testadmin',
                                 pid,
                                 **updateDict)
    if (flag & res) is False:
        return redirect(url_for('AccessErrorPage', dt='x'))
    flag, removeFlag = core.RemoveHumanConnection('testadmin', pid)
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
        xflag, xret = core.AddHumanCapability('testadmin', pid, cp)
        if xflag is False or xret is None:
            return redirect(url_for('AccessErrorPage', dt='x'))
    for gr in groupVec:
        xflag, xret = core.AddHumanToGroup('testadmin', pid, gr)
        if xflag is False or xret is None:
            return redirect(url_for('AccessErrorPage', dt='x'))
    for ps in posVec:
        xflag, xret = core.AddHumanPosition('testadmin', pid, ps)
        if xflag is False or xret is None:
            return redirect(url_for('AccessErrorPage', dt='x'))
    return redirect(url_for('humanManagement'))


@app.route('/human/performdelete/<uname>/', methods=["GET"])
def performDeleteHuman(uname):
    flag, res = core.RemoveHuman('testadmin', uname)
    if (flag & res) is False:
        return redirect(url_for('AccessErrorPage', dt='x'))
    return redirect(url_for('humanManagement'))


"""
Agent Resources Management Routers
"""


@app.route('/agent/')
def agentManagement():
    flag, res = core.RetrieveAllAgent('testadmin')
    if flag is False or res is None:
        return redirect(url_for('AccessErrorPage', dt='x'))
    capabilityList = []
    groupList = []
    positionList = []
    for h in res:
        xflag, groups = core.RetrieveAgentInWhatGroup('testadmin', h.Name)
        if xflag is False or groups is None:
            return redirect(url_for('AccessErrorPage', dt='add'))
        groupList.append('<br/>'.join(groups) + '<br/>')
        xflag, capabilities = core.RetrieveAgentWithWhatCapability('testadmin', h.Name)
        if xflag is False or capabilities is None:
            return redirect(url_for('AccessErrorPage', dt='add'))
        capabilityList.append('<br/>'.join(capabilities) + '<br/>')
        xflag, positions = core.RetrieveAgentInWhatPosition('testadmin', h.Name)
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
def addAgent():
    flag1, groupList = core.RetrieveAllGroup('testadmin')
    flag2, positionList = core.RetrieveAllPosition('testadmin')
    flag3, capabilityList = core.RetrieveAllCapabilities('testadmin')
    if (flag1 & flag2 & flag3) is False or groupList is None or positionList is None or capabilityList is None:
        return redirect(url_for('AccessErrorPage', dt='x'))
    t = {'L_PageTitle': u'添加Agent',
         'L_PageDescription': u'为组织添加一个Agent资源',
         'groupList': groupList,
         'positionList': positionList,
         'capabilityList': capabilityList}
    return render_template('agentmanagement_add.html', **t)


@app.route('/agent/performadd/', methods=["POST"])
def performAddAgent():
    agentName = request.form['f_name']
    flag, res = core.AddAgent('testadmin',
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
        xflag, xret = core.AddAgentCapability('testadmin', agentName, cp)
        if xflag is False or xret is None:
            return redirect(url_for('AccessErrorPage', dt='x'))
    for gr in groupVec:
        xflag, xret = core.AddAgentToGroup('testadmin', agentName, gr)
        if xflag is False or xret is None:
            return redirect(url_for('AccessErrorPage', dt='x'))
    for ps in posVec:
        xflag, xret = core.AddAgentPosition('testadmin', agentName, ps)
        if xflag is False or xret is None:
            return redirect(url_for('AccessErrorPage', dt='x'))
    return redirect(url_for('agentManagement'))


@app.route('/agent/edit/<uname>/', methods=["GET"])
def editAgent(uname):
    flag, res = core.RetrieveAgent('testadmin', uname)
    if flag is False or res is None:
        return redirect(url_for('AccessErrorPage', dt='x'))
    xflag, groups = core.RetrieveAgentInWhatGroup('testadmin', res.Name)
    if xflag is False or groups is None:
        return redirect(url_for('AccessErrorPage', dt='x'))
    groupStr = ';'.join(groups)
    xflag, capabilities = core.RetrieveAgentWithWhatCapability('testadmin', res.Name)
    if xflag is False or capabilities is None:
        return redirect(url_for('AccessErrorPage', dt='x'))
    capabilityStr = ';'.join(capabilities)
    xflag, positions = core.RetrieveAgentInWhatPosition('testadmin', res.Name)
    if xflag is False or positions is None:
        return redirect(url_for('AccessErrorPage', dt='x'))
    positionStr = ';'.join(positions)
    flag1, groupList = core.RetrieveAllGroup('testadmin')
    flag2, positionList = core.RetrieveAllPosition('testadmin')
    flag3, capabilityList = core.RetrieveAllCapabilities('testadmin')
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
def performEditAgent():
    agentName = request.form['h_name']
    updateDict = {
        "location": "'%s'" % request.form['f_location'],
        "type": "%s" % int(request.form['f_type']),
        "note": "'%s'" % request.form['f_note'],
    }
    flag, res = core.UpdateAgent('testadmin',
                                 agentName,
                                 **updateDict)
    if (flag & res) is False:
        return redirect(url_for('AccessErrorPage', dt='x'))
    flag, removeFlag = core.RemoveAgentConnection('testadmin', agentName)
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
        xflag, xret = core.AddAgentCapability('testadmin', agentName, cp)
        if xflag is False or xret is None:
            return redirect(url_for('AccessErrorPage', dt='x'))
    for gr in groupVec:
        xflag, xret = core.AddAgentToGroup('testadmin', agentName, gr)
        if xflag is False or xret is None:
            return redirect(url_for('AccessErrorPage', dt='x'))
    for ps in posVec:
        xflag, xret = core.AddAgentPosition('testadmin', agentName, ps)
        if xflag is False or xret is None:
            return redirect(url_for('AccessErrorPage', dt='x'))
    return redirect(url_for('agentManagement'))


@app.route('/agent/performdelete/<uname>/', methods=["GET"])
def performDeleteAgent(uname):
    flag, res = core.RemoveAgent('testadmin', uname)
    if (flag & res) is False:
        return redirect(url_for('AccessErrorPage', dt='x'))
    return redirect(url_for('agentManagement'))


if __name__ == '__main__':
    app.secret_key = GlobalConfigContext.RAPPKEY
    app.debug = True
    app.register_blueprint(restfulBp)
    app.run(host='127.0.0.1', port=10236)
