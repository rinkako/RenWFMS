/*
 * Project Ren @ 2018
 * Rinkako, Ariana, Gordan. SYSU SDCS.
 */
package org.sysu.renNameService.authorization;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.sysu.renCommon.utility.EncryptUtil;
import org.sysu.renCommon.utility.TimestampUtil;
import org.sysu.renNameService.GlobalContext;
import org.sysu.renNameService.entity.RenAuthuserEntity;
import org.sysu.renNameService.entity.RenAuthuserEntityPK;
import org.sysu.renNameService.entity.RenSessionEntity;
import org.sysu.renNameService.utility.HibernateUtil;
import org.sysu.renNameService.utility.LogUtil;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

/**
 * Author: Rinkako
 * Date  : 2018/1/28
 * Usage : This class maintaining authorization of service request token.
 */
public class AuthTokenManager {
    /**
     * Request for a auth token by authorization user info.
     * @param username user unique id, with domain name
     * @param password password
     * @return a token if authorization success, otherwise a string start with `#` for failure reason
     */
    @SuppressWarnings("unchecked")
    public static String Auth(String username, String password) {
        Session session = HibernateUtil.GetLocalSession();
        Transaction transaction = session.beginTransaction();
        try {
            // verify username and password
            String encryptedPassword = EncryptUtil.EncryptSHA256(password);
            String[] authItem = username.split("@");
            if (authItem.length != 2) {
                return "#user_not_valid";
            }
            RenAuthuserEntityPK pk = new RenAuthuserEntityPK();
            pk.setUsername(authItem[0]);
            pk.setDomain(authItem[1]);
            RenAuthuserEntity rae = session.get(RenAuthuserEntity.class, pk);
            if (rae == null || rae.getStatus() != 0) {
                transaction.commit();
                return "#user_not_valid";
            }
            else if (!rae.getPassword().equals(encryptedPassword)) {
                transaction.commit();
                return "#password_invalid";
            }
            // check if active session exist, ban it
            List<RenSessionEntity> oldRseList = session.createQuery(String.format("FROM RenSessionEntity WHERE username = '%s' AND destroy_timestamp = NULL", username)).list();
            Timestamp currentTS = TimestampUtil.GetCurrentTimestamp();
            for (RenSessionEntity rse : oldRseList) {
                if (rse.getUntilTimestamp().after(currentTS)) {
                    rse.setDestroyTimestamp(currentTS);
                }
            }
            // create new session
            String tokenId = String.format("AUTH_%s_%s", username, UUID.randomUUID());
            RenSessionEntity rse = new RenSessionEntity();
            long createTs = System.currentTimeMillis();
            rse.setLevel(rae.getLevel());
            rse.setToken(tokenId);
            rse.setUsername(username);
            rse.setCreateTimestamp(new Timestamp(createTs));
            if (GlobalContext.AUTHORITY_TOKEN_VALID_SECOND != 0) {
                rse.setUntilTimestamp(new Timestamp(createTs + 1000 * GlobalContext.AUTHORITY_TOKEN_VALID_SECOND));
            }
            session.save(rse);
            transaction.commit();
            return tokenId;
        }
        catch (Exception ex) {
            LogUtil.Log(String.format("Request for auth but exception occurred (%s), service rollback, %s", username, ex),
                    AuthTokenManager.class.getName(), LogUtil.LogLevelType.ERROR, "");
            transaction.rollback();
            return "#exception_occurred";
        }
        finally {
            HibernateUtil.CloseLocalSession();
        }
    }

    /**
     * Get domain of a token.
     * @param token auth token
     * @return domain name, null if invalid
     */
    public static String GetDomain(String token) {
        String[] tokenItem = token.split("_");
        if (tokenItem.length != 3) {
            return null;
        }
        String[] authNameItem = tokenItem[1].split("@");
        if (authNameItem.length != 2) {
            return null;
        }
        return authNameItem[1];
    }

    /**
     * Destroy a token.
     * @param token auth token
     */
    public static void Destroy(String token) {
        Session session = HibernateUtil.GetLocalSession();
        Transaction transaction = session.beginTransaction();
        try {
            RenSessionEntity rse = session.get(RenSessionEntity.class, token);
            if (rse == null) {
                return;
            }
            rse.setDestroyTimestamp(TimestampUtil.GetCurrentTimestamp());
            transaction.commit();
        }
        catch (Exception ex) {
            LogUtil.Log(String.format("Destroy auth but exception occurred (%s), service rollback, %s", token, ex),
                    AuthTokenManager.class.getName(), LogUtil.LogLevelType.ERROR, "");
            transaction.rollback();
        }
        finally {
            HibernateUtil.CloseLocalSession();
        }
    }

    /**
     * Check if a token is valid.
     * @param token auth token to be checked
     * @return whether token is valid
     */
    public static boolean CheckValid(String token) {
        // internal service call
        if (token.equals(GlobalContext.INTERNAL_TOKEN)) {
            return true;
        }
        Session session = HibernateUtil.GetLocalSession();
        Transaction transaction = session.beginTransaction();
        boolean retFlag = true;
        try {
            RenSessionEntity rse = session.get(RenSessionEntity.class, token);
            if (rse == null || rse.getDestroyTimestamp() != null ||
                rse.getUntilTimestamp().before(TimestampUtil.GetCurrentTimestamp())) {
                retFlag = false;
            }
            transaction.commit();
        }
        catch (Exception ex) {
            LogUtil.Log(String.format("Check auth validation but exception occurred (%s), service rollback, %s", token, ex),
                    AuthTokenManager.class.getName(), LogUtil.LogLevelType.ERROR, "");
            transaction.rollback();
            retFlag = false;
        }
        finally {
            HibernateUtil.CloseLocalSession();
        }
        return retFlag;
    }

    /**
     * Check if a token is valid and get level.
     * @param token auth token to be checked
     * @return token level, -1 if token is invalid
     */
    public static int CheckValidLevel(String token) {
        // internal service call
        if (token.equals(GlobalContext.INTERNAL_TOKEN)) {
            return 999;
        }
        Session session = HibernateUtil.GetLocalSession();
        Transaction transaction = session.beginTransaction();
        int retVal;
        try {
            RenSessionEntity rse = session.get(RenSessionEntity.class, token);
            if (rse == null || rse.getDestroyTimestamp() != null ||
                    rse.getUntilTimestamp().before(TimestampUtil.GetCurrentTimestamp())) {
                retVal = -1;
            }
            else {
                retVal = rse.getLevel();
            }
            transaction.commit();
        }
        catch (Exception ex) {
            LogUtil.Log(String.format("Check auth validation but exception occurred (%s), service rollback, %s", token, ex),
                    AuthTokenManager.class.getName(), LogUtil.LogLevelType.ERROR, "");
            transaction.rollback();
            retVal = -1;
        }
        finally {
            HibernateUtil.CloseLocalSession();
        }
        return retVal;
    }
}
