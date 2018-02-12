/*
 * Project Ren @ 2018
 * Rinkako, Ariana, Gordan. SYSU SDCS.
 */
package org.sysu.renResourcing.authorization;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.sysu.renResourcing.GlobalContext;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

/**
 * Author: Rinkako
 * Date  : 2018/2/13
 * Usage : This class maintaining authorization of service from InterfaceW.
 */
public class AuthorizationManager {
    /**
     * Request for a auth token by authorization user info.
     * @param username user unique id
     * @param domain domain name
     * @param password password
     * @return a token if authorization success, otherwise a string start with `#` for failure reason
     */
    public static String Auth(String username, String domain, String password) {
        return "";
    }

    /**
     * Destroy a token.
     * @param token auth token
     */
    public static void Destroy(String token) {
        return;
    }

    /**
     * Check if a token is valid.
     * @param token auth token to be checked
     * @return whether token is valid
     */
    public static boolean CheckValid(String token) {
        return true;
    }

    /**
     * Check if a token is valid and get level.
     * @param token auth token to be checked
     * @return token level, -1 if token is invalid
     */
    public static int CheckValidLevel(String token) {
        return 0;
    }

    /**
     * Get the domain name of a token.
     * @param token auth token
     * @return token owner belong to domain name
     */
    public static String GetDomain(String token) {
        return "";
    }

    /**
     * Destroy all auth token to a specific domain.
     * Only called by auth domain admin user or WFMS admin
     * @param domain domain name
     */
    public static void DestoryAllInDomain(String domain) {

    }

    /**
     * Destroy all auth token.
     * Only called by WFMS admin
     */
    public static void DestoryAll() {

    }
}
