#!/usr/bin/env python
# encoding: utf-8
"""
@module : GlobalConfigContext
@author : Rinkako
@time   : 2017/12/26

This module is used to store some constant configuration fields for the engine
initialization and runtime environment.
"""

RAPPKEY = '@\xc2\xbbK\xd1\xb0\x95i\x8d\xfa#\xf3\xebX\xa9\xde\xa3Sb\xfb\xf5\x00\xa1\xe1'
RDBHOST = '127.0.0.1'
RDBUSER = 'boengine'
RDBPASSWORD = 'boengine'
RDBPORT = '3306'
RDBPATH = 'rencorgan'
UNAUTHORIZED = "__COrgan_Unauthorized__"
LPWDSALT = 'LuMiNous'

CONFIG_ORGANIZATION_KEY = 'organizationName'
CONFIG_DATA_UPDATE_ROUTER = 'updateNotifyRouterUrl'
CONFIG_DATA_VERSION_KEY = 'dataVersion'
CONFIG_ORGANIZATION_ID_KEY = 'organizationId'

AUTH_INTERNAL_SESSION = '&?@\xc2\xbbK\xd1\xb0\x95i\x8d\xfa#\xf3\xebX\xa9\xde\xa3Sb\xfb\xf5\x00\xa1\xe1'
AUTH_NameService_PublicKey = """-----BEGIN PUBLIC KEY-----
MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCh5g4pc8+YD8QLXQSI441vkY+q
uXJfSS4m4QV4FTOCyTBR9rTqHGpvYRTITAV5nPHVFeej1c0+WSJ3DUopMdItuAMB
dKUlvPigOQwWJShXl5IzlWGGYFC4tCAX34PyoXQ4ec/+Uj2YUA0b4/3t3HG077kp
u13rGGKQ4uECtOyzAwIDAQAB
-----END PUBLIC KEY-----"""