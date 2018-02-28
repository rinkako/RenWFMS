/*
 * Project Ren @ 2018
 * Rinkako, Ariana, Gordan. SYSU SDCS.
 */
package org.sysu.renCommon.utility;
import org.apache.commons.codec.digest.DigestUtils;
import org.sysu.renCommon.GlobalConfigContext;


/**
 * Author: Rinkako
 * Date  : 2018/1/28
 * Usage : Methods for data encryption and authorization verification.
 */
public class EncryptUtil {

    /**
     * Encrypt a string using SHA256 with salt.
     * @param orgStr string to be encrypted
     * @return encrypted string
     */
    public static String EncryptSHA256(String orgStr) {
        return DigestUtils.sha256Hex(orgStr + GlobalConfigContext.AUTHORITY_SALT);
    }

    /**
     * Encrypt a string using SHA256 with custom salt.
     * @param orgStr string to be encrypted
     * @param salt custom salt string
     * @return encrypted string
     */
    public static String EncryptSHA256(String orgStr, String salt) {
        return DigestUtils.sha256Hex(orgStr + salt);
    }
}
