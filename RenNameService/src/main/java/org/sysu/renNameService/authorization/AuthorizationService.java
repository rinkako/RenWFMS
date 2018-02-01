/*
 * Project Ren @ 2018
 * Rinkako, Ariana, Gordan. SYSU SDCS.
 */
package org.sysu.renNameService.authorization;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.sysu.renNameService.GlobalContext;
import org.sysu.renNameService.entity.RenAuthEntity;
import org.sysu.renNameService.utility.EncryptUtil;
import org.sysu.renNameService.utility.HibernateUtil;
import org.sysu.renNameService.utility.LogUtil;
import org.sysu.renNameService.utility.RSASignatureUtil;

import java.sql.Timestamp;
import java.util.HashMap;

/**
 * Author: Rinkako
 * Date  : 2018/1/28
 * Usage : All BO environment authorization services will be handled in this service module.
 */
public class AuthorizationService {
    /**
     * Connect and get a auth token for BO environment user.
     * @param username user unique name
     * @param password password without encryption
     * @return a token if authorization success, otherwise a string start with `#` for failure reason
     */
    public static String Connect(String username, String password) {
        return AuthTokenManager.Auth(username, password);
    }

    /**
     * Disconnect and destroy the auth token.
     * @param token token to be destroy
     */
    public static void Disconnect(String token) {
        AuthTokenManager.Destroy(token);
    }

    /**
     * Check if a token valid.
     * @param token token to be checked
     * @return boolean of validation
     */
    public static boolean CheckValid(String token) {
        return AuthTokenManager.CheckValid(token);
    }

    /**
     * Check if a token is valid and get its level.
     * @param token token to be checked
     * @return token level, -1 if token is invalid
     */
    public static int CheckValidLevel(String token) {
        return AuthTokenManager.CheckValidLevel(token);
    }

    /**
     * Add a BO environment user.
     * @param username user unique name
     * @param password user password without encryption
     * @param level user level
     * @param corganGateway binding COrgan gateway URL
     * @return string of user ren id, empty string if failed to create
     */
    public static String AddAuthorizationUser(String username, String password, int level, String corganGateway) {
        Session session = HibernateUtil.GetLocalThreadSession();
        Transaction transaction = session.beginTransaction();
        try {
            // check existence
            RenAuthEntity existRae = session.get(RenAuthEntity.class, username);
            if (existRae != null) {
                LogUtil.Log(String.format("AddAuthorizationUser but username already exist (%s), service rollback.", username),
                        AuthorizationService.class.getName(), LogUtil.LogLevelType.ERROR, "");
                transaction.commit();
                return "#duplicate_username";
            }
            // create new
            RenAuthEntity rae = new RenAuthEntity();
            rae.setUsername(username);
            rae.setLevel(level);
            rae.setPassword(EncryptUtil.EncryptSHA256(password));
            rae.setCorganGateway(corganGateway);
            rae.setState(0);
            rae.setCreatetimestamp(new Timestamp(System.currentTimeMillis()));
            String signature = RSASignatureUtil.Signature(username, GlobalContext.PRIVATE_KEY);
            assert signature != null;
            String safeSignature = RSASignatureUtil.SafeUrlBase64Encode(signature);
            rae.setUrlsafeSignature(safeSignature);
            session.save(rae);
            transaction.commit();
            return safeSignature;
        }
        catch (Exception ex) {
            LogUtil.Log(String.format("AddAuthorizationUser but exception occurred (%s), service rollback, %s", username, ex),
                    AuthorizationService.class.getName(), LogUtil.LogLevelType.ERROR, "");
            transaction.rollback();
            return "#exception";
        }
    }

    /**
     * Disable a BO environment user, make it unable to connect.
     * @param username user unique name
     * @return boolean of whether execution success
     */
    public static boolean RemoveAuthorizationUser(String username) {
        Session session = HibernateUtil.GetLocalThreadSession();
        Transaction transaction = session.beginTransaction();
        try {
            RenAuthEntity are = session.get(RenAuthEntity.class, username);
            if (are != null) {
                are.setState(1);
                transaction.commit();
                return true;
            }
            else {
                transaction.commit();
                return false;
            }
        }
        catch (Exception ex) {
            LogUtil.Log(String.format("RemoveAuthorizationUser but exception occurred (%s), service rollback, %s", username, ex),
                    AuthorizationService.class.getName(), LogUtil.LogLevelType.ERROR, "");
            transaction.rollback();
            return false;
        }
    }

    /**
     * Update a BO environment user profile.
     * @param username user unique name
     * @param updateArgs update argument name-value dictionary
     * @return boolean of whether execution success
     */
    public static boolean UpdateAuthorizationUser(String username, HashMap<String, String> updateArgs, Boolean isAdmin) {
        Session session = HibernateUtil.GetLocalThreadSession();
        Transaction transaction = session.beginTransaction();
        try {
            RenAuthEntity are = session.get(RenAuthEntity.class, username);
            if (are == null) {
                transaction.commit();
                return false;
            }
            if (updateArgs.containsKey("password")) {
                are.setPassword(EncryptUtil.EncryptSHA256(updateArgs.get("password")));
            }
            if (updateArgs.containsKey("corgan")) {
                are.setCorganGateway(updateArgs.get("corgan"));
            }
            if (updateArgs.containsKey("state") && isAdmin) {
                are.setState(Integer.valueOf(updateArgs.get("state")));
            }
            if (updateArgs.containsKey("level") && isAdmin) {
                are.setLevel(Integer.valueOf(updateArgs.get("level")));
            }
            transaction.commit();
            return true;
        }
        catch (Exception ex) {
            LogUtil.Log(String.format("UpdateAuthorizationUser but exception occurred (%s), service rollback, %s", username, ex),
                    AuthorizationService.class.getName(), LogUtil.LogLevelType.ERROR, "");
            transaction.rollback();
            return false;
        }
    }

    /**
     * Check if a BO environment user is already exist.
     * @param username user unique name to be checked
     * @return boolean of existence
     */
    public static boolean ContainAuthorizationUser(String username) {
        Session session = HibernateUtil.GetLocalThreadSession();
        Transaction transaction = session.beginTransaction();
        try {
            RenAuthEntity rae = session.get(RenAuthEntity.class, username);
            transaction.commit();
            return rae != null;
        }
        catch (Exception ex) {
            LogUtil.Log(String.format("ContainAuthorizationUser but exception occurred (%s), service rollback, %s", username, ex),
                    AuthorizationService.class.getName(), LogUtil.LogLevelType.ERROR, "");
            transaction.rollback();
            return true;
        }
    }

    /**
     * Retrieve a BO environment user.
     * @param username user unique name to be checked
     * @return {@code RenAuthEntity} instance
     */
    public static RenAuthEntity RetrieveAuthorizationUser(String username) {
        Session session = HibernateUtil.GetLocalThreadSession();
        Transaction transaction = session.beginTransaction();
        try {
            RenAuthEntity rae = session.get(RenAuthEntity.class, username);
            transaction.commit();
            return rae;
        }
        catch (Exception ex) {
            LogUtil.Log(String.format("RetrieveAuthorizationUser but exception occurred (%s), service rollback, %s", username, ex),
                    AuthorizationService.class.getName(), LogUtil.LogLevelType.ERROR, "");
            transaction.rollback();
            return null;
        }
    }
}
