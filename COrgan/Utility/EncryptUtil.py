#!/usr/bin/env python
# encoding: utf-8
"""
@module : EncryptUtil
@author : Rinkako
@time   : 2018/1/8
"""
import hashlib
import base64
from Crypto.PublicKey import RSA
from Crypto.Signature import PKCS1_v1_5
from Crypto.Hash import MD5
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

    @staticmethod
    def Signature(data, privateKeyInBase64):
        """
        Sign data by a private key
        :param data:
        :param privateKeyInBase64: private key for signature, no PCKS8 convert.
        :return: Base64 encoded signature string
        """
        key = RSA.importKey(privateKeyInBase64)
        h = MD5.new(data)
        signer = PKCS1_v1_5.new(key)
        signature = signer.sign(h)
        return base64.b64encode(signature)

    @staticmethod
    def VerifySign(data, signature, pubKey):
        """
        :param data: data to be verified
        :param signature: signature of this data
        :param pubKey: public key for verifying in Base64
        :return:
        """
        key = RSA.importKey(pubKey)
        h = MD5.new(data)
        verifier = PKCS1_v1_5.new(key)
        if verifier.verify(h, base64.b64decode(signature)):
            return True
        return False

    @staticmethod
    def DecodeURLSafeBase64(safeBase64Str):
        """
        Decode a URL safe Base64 string to original string.
        :param safeBase64Str: URL safe Base64 string
        :return: original string
        """
        base64Str = safeBase64Str.replace('-', '+')
        base64Str = base64Str.replace('_', '/')
        mod4 = len(base64Str) % 4
        if mod4 > 0:
            base64Str = base64Str + "===="[0:mod4]
        return base64Str


if __name__ == '__main__':
    test_sign = "VESLo+bDgNu9G0hD0eGvRDJ701t+DLMHqbEH615uHgoCCRGYdnBNpq+QsUS5RFoq140Vx4kNj6RhVfWtNOP556ZWeFQbE1jekv8c/7gy5ltPaFoNIgP3r5wrnYldgeUDAVUJtIuYW6Hq4epWtz6jJV+J87DVLk/GtUFgYK1ZZh8="
    test_sign_org = "123456"
    print EncryptUtil.VerifySign(test_sign_org, test_sign, GCC.AUTH_NameService_PublicKey)
