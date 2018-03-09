#!/usr/bin/env python
# encoding: utf-8
"""
@module : CClient
@author : Rinkako
@time   : 2018/1/7
"""
import urllib
import urllib2


class CClient:
    """
    COrgan Platform Client

    This class is used to post data by HTTP request from the platform
    to the outside world.
    """

    def __init__(self):
        pass

    @staticmethod
    def SendRequest(url, args=None, method="post"):
        """
        Perform a request to a specific gateway
        :param url: gateway address
        :param method: "get" or "post" by default
        :param args: parameter dictionary
        :return gateway response string
        """
        return CClient.PerformGet(url, args) if method.lower() == "get" \
            else CClient.PerformPost(url, args)

    @staticmethod
    def PerformGet(url, args=None):
        """
        Perform a request to a specific gateway by GET method
        :param url: gateway address
        :param args: parameter dictionary
        :return gateway response string
        """
        paras_url = ""
        if args is not None:
            paras_url = "?"
            for key in args:
                paras_url += "%s=%s&" % (key, args[key])
            paras_url = paras_url[0: len(paras_url) - 1]
        request_url = url + paras_url
        req = urllib2.Request(request_url)
        return urllib2.urlopen(req).read()

    @staticmethod
    def PerformPost(url, args=None):
        """
        Perform a request to a specific gateway by POST method
        :param url: gateway address
        :param args: parameter dictionary
        :return gateway response string
        """
        data_url_encoded = None
        if args is not None:
            data_url_encoded = urllib.urlencode(args)
        req = urllib2.Request(url=url, data=data_url_encoded)
        return urllib2.urlopen(req).read()


if __name__ == '__main__':
    test_data = {'ServiceCode': 'doAction', 'arg': 'ha'}
    res = CClient.SendRequest("http://127.0.0.1:5000/", test_data, "GET")
    t = 3
