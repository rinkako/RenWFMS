#!/usr/bin/env python
# encoding: utf-8
"""
@module : EncryptUtil
@author : Rinkako
@time   : 2018/1/8
"""
import hashlib
import GlobalConfigContext as GCC


class EncryptUtil:
    """
    This utility contains functions for encryption.
    """
    def __init__(self):
        pass

    @staticmethod
    def EncryptSHA256(inStr):
        """
        Encrypt String to SHA1.
        :param inStr: string to be encrypted.
        :return: encrypted string
        """
        return hashlib.sha256(inStr + GCC.LPWDSALT).hexdigest()
