#!/usr/bin/env python
# encoding: utf-8
"""
@module : InteractionUtil
@author : Rinkako
@time   : 2018/6/1
"""
import requests
import json
import GlobalConfigContext as GCC


class InteractionUtil:
    def __init__(self):
        pass

    @staticmethod
    def PostJson(url, paraDict=None):
        r = requests.post(url, data=paraDict)
        return json.loads(r.text, encoding='utf-8')

    @staticmethod
    def Send(url, paraDict=None):
        preDict = {"token": GCC.INTERNAL_TOKEN}
        if paraDict is not None:
            preDict.update(paraDict)
        retVal = InteractionUtil.PostJson(url, preDict)
        if retVal["code"] == "OK":
            return retVal["returnElement"]
        else:
            return None


if __name__ == "__main__":
    x = InteractionUtil.Send("http://127.0.0.1:10234/auth/domain/getall")
    pass
