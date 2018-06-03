#!/usr/bin/env python
# encoding: utf-8
"""
@module : LocationContext
@author : Rinkako
@time   : 2018/6/1
"""
URL_Domain_GetAll = "http://127.0.0.1:10234/auth/domain/getall"
URL_Domain_Get = "http://127.0.0.1:10234/auth/domain/get"
URL_Domain_Add = "http://127.0.0.1:10234/auth/domain/add"
URL_Domain_Contain = "http://127.0.0.1:10234/auth/domain/contain"
URL_Domain_Remove = "http://127.0.0.1:10234/auth/domain/remove"
URL_Domain_Update = "http://127.0.0.1:10234/auth/domain/update"

URL_AuthUser_GetAll = "http://127.0.0.1:10234/auth/user/getall"
URL_AuthUser_Get = "http://127.0.0.1:10234/auth/user/get"
URL_AuthUser_Add = "http://127.0.0.1:10234/auth/user/add"
URL_AuthUser_Contain = "http://127.0.0.1:10234/auth/user/contain"
URL_AuthUser_Remove = "http://127.0.0.1:10234/auth/user/remove"
URL_AuthUser_Update = "http://127.0.0.1:10234/auth/user/update"

URL_Process_GetAllForDomain = "http://127.0.0.1:10234/ns/getProcessByDomain"
URL_Process_GetByPid = "http://127.0.0.1:10234/ns/getProcessByPid"

URL_RTC_Get = "http://127.0.0.1:10234/ns/getRuntimeRecord"
URL_RTC_GetAll = "http://127.0.0.1:10234/ns/getAllRuntimeRecord"
URL_RTC_GetForDomain = "http://127.0.0.1:10234/ns/getRuntimeRecordByDomain"
URL_RTC_GetForLauncher = "http://127.0.0.1:10234/ns/getRuntimeRecordByLauncher"
URL_RTC_GetLogByRTID = "http://127.0.0.1:10234/ns/getRuntimeLogByRTID"
URL_RTC_GetSpanTreeByRTID = "http://127.0.0.1:10234/ns/getSpanTree"

URL_Workitem_Get = "http://127.0.0.1:10234/ns/workitem/get"
URL_Workitem_GetAllForDomain = "http://127.0.0.1:10234/ns/workitem/getAllForDomain"
URL_Workitem_GetAllForParticipant = "http://127.0.0.1:10234/ns/workitem/getAllActiveForParticipant"
URL_Workitem_ActionPrefix = "http://127.0.0.1:10234/ns/workitem/"
