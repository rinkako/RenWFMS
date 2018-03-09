/*
 * Project Ren @ 2018
 * Rinkako, Ariana, Gordan. SYSU SDCS.
 */
package org.sysu.renCommon.utility;


/**
 * Author: Rinkako
 * Date  : 2018/2/21
 * Usage : Some useful helper methods for auth domain.
 */
public class AuthDomainHelper {

    /**
     * Get domain name by rtid.
     * @param rtid process rtid
     * @return domain name of this runtime
     */
    public static String GetDomainByRTID(String rtid) {
        return AuthDomainHelper.GetDomainByAuthName(AuthDomainHelper.GetAuthNameByRTID(rtid));
    }

    /**
     * Get auth name by rtid.
     * @param rtid process rtid
     * @return domain name of this runtime
     */
    public static String GetAuthNameByRTID(String rtid) {
        return rtid.split("_")[1];
    }

    /**
     * Get domain name by auth name.
     * @param authName full auth name
     * @return domain name of this runtime
     */
    public static String GetDomainByAuthName(String authName) {
        return authName.split("@")[1];
    }

    /**
     * Check if a auth user belong to a specific domain.
     * @param authName auth user name
     * @param domain domain name
     * @return true if auth user belong to domain
     */
    public static Boolean IsAuthUserInDomain(String authName, String domain) {
        return AuthDomainHelper.GetDomainByAuthName(authName).equals(domain);
    }
}
