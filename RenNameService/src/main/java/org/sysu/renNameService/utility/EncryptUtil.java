/*
 * Project Ren @ 2018
 * Rinkako, Ariana, Gordan. SYSU SDCS.
 */
package org.sysu.renNameService.utility;
import org.apache.commons.codec.digest.DigestUtils;
import org.sysu.renNameService.GlobalContext;


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
        return DigestUtils.sha256Hex(orgStr + GlobalContext.AUTHORITY_SALT);
    }

}
