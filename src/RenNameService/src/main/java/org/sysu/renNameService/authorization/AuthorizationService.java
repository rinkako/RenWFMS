/*
 * Project Ren @ 2018
 * Rinkako, Ariana, Gordan. SYSU SDCS.
 */
package org.sysu.renNameService.authorization;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.sysu.renCommon.utility.CommonUtil;
import org.sysu.renCommon.utility.EncryptUtil;
import org.sysu.renCommon.utility.TimestampUtil;
import org.sysu.renNameService.GlobalContext;
import org.sysu.renNameService.entity.RenAuthuserEntity;
import org.sysu.renNameService.entity.RenAuthuserEntityPK;
import org.sysu.renNameService.entity.RenDomainEntity;
import org.sysu.renNameService.entity.RenWorkitemEntity;
import org.sysu.renNameService.utility.*;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;

/**
 * Author: Rinkako
 * Date  : 2018/1/28
 * Usage : All BO environment authorization services will be handled in this service module.
 */
public class AuthorizationService {

    /**
     * Connect and get a auth token for BO environment user.
     *
     * @param username user unique name
     * @param password password without encryption
     * @return a token if authorization success, otherwise a string start with `#` for failure reason
     */
    public static String Connect(String username, String password) {
        return AuthTokenManager.Auth(username, password);
    }

    /**
     * Disconnect and destroy the auth token.
     *
     * @param token token to be destroy
     */
    public static void Disconnect(String token) {
        AuthTokenManager.Destroy(token);
    }

    /**
     * Check if a token valid.
     *
     * @param token token to be checked
     * @return boolean of validation
     */
    public static boolean CheckValid(String token) {
        return AuthTokenManager.CheckValid(token);
    }

    /**
     * Check if a token is valid and get its level.
     *
     * @param token token to be checked
     * @return token level, -1 if token is invalid
     */
    public static int CheckValidLevel(String token) {
        return AuthTokenManager.CheckValidLevel(token);
    }

    /**
     * Add a domain.
     *
     * @param name          domain unique name
     * @param password      domain admin password
     * @param level         domain level
     * @param corganGateway binding COrgan gateway URL
     * @return domain private signature key
     */
    public static String AddDomain(String name, String password, String level, String corganGateway) {
        Session session = HibernateUtil.GetLocalSession();
        Transaction transaction = session.beginTransaction();
        try {
            // check existence
            RenDomainEntity existRae = session.get(RenDomainEntity.class, name);
            if (existRae != null || name.trim().equals("")) {
                LogUtil.Log(String.format("AddAuthorizationUser but username already exist (%s), service rollback.", name),
                        AuthorizationService.class.getName(), LogUtil.LogLevelType.ERROR, "");
                transaction.commit();
                return "#duplicate_domain";
            }
            // create new
            Timestamp createTs = TimestampUtil.GetCurrentTimestamp();
            RenDomainEntity rde = new RenDomainEntity();
            rde.setName(name);
            rde.setLevel(Integer.valueOf(level));
            rde.setCorganGateway(corganGateway);
            rde.setStatus(0);
            rde.setLevel(0);
            rde.setCreatetimestamp(createTs);
            String signature = RSASignatureUtil.Signature(name, GlobalContext.PRIVATE_KEY);
            assert signature != null;
            String safeSignature = RSASignatureUtil.SafeUrlBase64Encode(signature);
            rde.setUrlsafeSignature(safeSignature);
            session.save(rde);
            // create admin auth user
            RenAuthuserEntity rae = new RenAuthuserEntity();
            rae.setUsername(GlobalContext.DOMAIN_ADMIN_NAME);
            rae.setDomain(name);
            rae.setStatus(0);
            rae.setCreatetimestamp(createTs);
            rae.setPassword(EncryptUtil.EncryptSHA256(password));
            rae.setLevel(1);
            session.save(rae);
            transaction.commit();
            return safeSignature;
        } catch (Exception ex) {
            LogUtil.Log(String.format("Add domain but exception occurred (%s), service rollback, %s", name, ex),
                    AuthorizationService.class.getName(), LogUtil.LogLevelType.ERROR, "");
            transaction.rollback();
            return "#exception";
        } finally {
            HibernateUtil.CloseLocalSession();
        }
    }

    /**
     * Disable a domain, make it unable to connect.
     *
     * @param name domain unique name
     * @return boolean of whether execution success
     */
    public static boolean RemoveDomain(String name) {
        Session session = HibernateUtil.GetLocalSession();
        Transaction transaction = session.beginTransaction();
        try {
            RenDomainEntity rde = session.get(RenDomainEntity.class, name);
            if (rde != null) {
                rde.setStatus(1);
                transaction.commit();
                return true;
            } else {
                transaction.commit();
                return false;
            }
        } catch (Exception ex) {
            LogUtil.Log(String.format("Remove domain but exception occurred (%s), service rollback, %s", name, ex),
                    AuthorizationService.class.getName(), LogUtil.LogLevelType.ERROR, "");
            transaction.rollback();
            return false;
        } finally {
            HibernateUtil.CloseLocalSession();
        }
    }

    /**
     * Update a domain.
     *
     * @param name       domain unique name
     * @param updateArgs update argument name-value dictionary
     * @param isAdmin    is operator WFMS admin
     * @return boolean of whether execution success
     */
    public static boolean UpdateDomain(String name, HashMap<String, String> updateArgs, Boolean isAdmin) {
        Session session = HibernateUtil.GetLocalSession();
        Transaction transaction = session.beginTransaction();
        try {
            RenDomainEntity are = session.get(RenDomainEntity.class, name);
            if (are == null) {
                transaction.commit();
                return false;
            }
            if (updateArgs.containsKey("corgan")) {
                are.setCorganGateway(updateArgs.get("corgan"));
            }
            if (updateArgs.containsKey("status") && isAdmin) {
                are.setStatus(Integer.valueOf(updateArgs.get("status")));
            }
            if (updateArgs.containsKey("level") && isAdmin) {
                are.setLevel(Integer.valueOf(updateArgs.get("level")));
            }
            transaction.commit();
            return true;
        } catch (Exception ex) {
            LogUtil.Log(String.format("Update domain but exception occurred (%s), service rollback, %s", name, ex),
                    AuthorizationService.class.getName(), LogUtil.LogLevelType.ERROR, "");
            transaction.rollback();
            return false;
        } finally {
            HibernateUtil.CloseLocalSession();
        }
    }

    /**
     * Check if a domain is already exist.
     *
     * @param name domain unique name to be checked
     * @return boolean of existence
     */
    public static boolean ContainDomain(String name) {
        Session session = HibernateUtil.GetLocalSession();
        Transaction transaction = session.beginTransaction();
        try {
            RenDomainEntity rae = session.get(RenDomainEntity.class, name);
            transaction.commit();
            return rae != null;
        } catch (Exception ex) {
            LogUtil.Log(String.format("Contain domain check but exception occurred (%s), service rollback, %s", name, ex),
                    AuthorizationService.class.getName(), LogUtil.LogLevelType.ERROR, "");
            transaction.rollback();
            return true;
        } finally {
            HibernateUtil.CloseLocalSession();
        }
    }

    /**
     * Retrieve a domain.
     *
     * @param name domain unique name
     * @return {@code RenAuthEntity} instance
     */
    public static RenDomainEntity RetrieveDomain(String name) {
        Session session = HibernateUtil.GetLocalSession();
        Transaction transaction = session.beginTransaction();
        try {
            RenDomainEntity rae = session.get(RenDomainEntity.class, name);
            transaction.commit();
            return rae;
        } catch (Exception ex) {
            LogUtil.Log(String.format("Retrieve domain but exception occurred (%s), service rollback, %s", name, ex),
                    AuthorizationService.class.getName(), LogUtil.LogLevelType.ERROR, "");
            transaction.rollback();
            return null;
        } finally {
            HibernateUtil.CloseLocalSession();
        }
    }

    /**
     * Retrieve all domain.
     *
     * @return {@code RenAuthEntity} instance
     */
    public static List<RenDomainEntity> RetrieveAllDomain() {
        Session session = HibernateUtil.GetLocalSession();
        Transaction transaction = session.beginTransaction();
        try {
            List<RenDomainEntity> rae = session.createQuery("FROM RenDomainEntity").list();
            transaction.commit();
            return rae;
        } catch (Exception ex) {
            LogUtil.Log(String.format("Retrieve all domain but exception occurred, service rollback, %s", ex),
                    AuthorizationService.class.getName(), LogUtil.LogLevelType.ERROR, "");
            transaction.rollback();
            return null;
        } finally {
            HibernateUtil.CloseLocalSession();
        }
    }

    /**
     * Add a auth user.
     *
     * @param username user unique name
     * @param password user password without encryption
     * @param level    user level
     * @param domain   domain name
     * @param gid      global id of binding resources
     * @return `OK` if success otherwise failed
     */
    public static String AddAuthUser(String username, String password, int level, String domain, String gid) {
        Session session = HibernateUtil.GetLocalSession();
        Transaction transaction = session.beginTransaction();
        try {
            // check existence
            RenAuthuserEntityPK pk = new RenAuthuserEntityPK();
            pk.setDomain(domain);
            pk.setUsername(username);
            RenAuthuserEntity existRae = session.get(RenAuthuserEntity.class, pk);
            if (existRae != null) {
                LogUtil.Log(String.format("AddAuthorizationUser but username already exist (%s@%s), service rollback.", username, domain),
                        AuthorizationService.class.getName(), LogUtil.LogLevelType.ERROR, "");
                transaction.commit();
                return "#duplicate_username";
            }
            // create new
            RenAuthuserEntity rae = new RenAuthuserEntity();
            rae.setUsername(username);
            rae.setLevel(level);
            rae.setPassword(EncryptUtil.EncryptSHA256(password));
            rae.setDomain(domain);
            rae.setCreatetimestamp(TimestampUtil.GetCurrentTimestamp());
            rae.setStatus(0);
            rae.setGid(gid == null ? "" : gid);
            session.save(rae);
            transaction.commit();
            return "OK";
        } catch (Exception ex) {
            LogUtil.Log(String.format("AddAuthorizationUser but exception occurred (%s@%s), service rollback, %s", username, domain, ex),
                    AuthorizationService.class.getName(), LogUtil.LogLevelType.ERROR, "");
            transaction.rollback();
            return "#exception";
        } finally {
            HibernateUtil.CloseLocalSession();
        }
    }

    /**
     * Disable a BO environment user, make it unable to connect.
     *
     * @param username user unique name
     * @param domain   domain name
     * @return boolean of whether execution success
     */
    public static boolean RemoveAuthorizationUser(String username, String domain) {
        Session session = HibernateUtil.GetLocalSession();
        Transaction transaction = session.beginTransaction();
        try {
            RenAuthuserEntityPK pk = new RenAuthuserEntityPK();
            pk.setUsername(username);
            pk.setDomain(domain);
            RenAuthuserEntity are = session.get(RenAuthuserEntity.class, pk);
            if (are != null) {
                are.setStatus(1);
                transaction.commit();
                return true;
            } else {
                transaction.commit();
                return false;
            }
        } catch (Exception ex) {
            LogUtil.Log(String.format("RemoveAuthorizationUser but exception occurred (%s@%s), service rollback, %s", username, domain, ex),
                    AuthorizationService.class.getName(), LogUtil.LogLevelType.ERROR, "");
            transaction.rollback();
            return false;
        } finally {
            HibernateUtil.CloseLocalSession();
        }
    }

    /**
     * Update a BO environment user profile.
     *
     * @param username   user unique name
     * @param domain     domain name
     * @param updateArgs update argument name-value dictionary
     * @return boolean of whether execution success
     */
    public static boolean UpdateAuthorizationUser(String username, String domain, HashMap<String, String> updateArgs, Boolean isAdmin) {
        Session session = HibernateUtil.GetLocalSession();
        Transaction transaction = session.beginTransaction();
        try {
            RenAuthuserEntityPK pk = new RenAuthuserEntityPK();
            pk.setDomain(domain);
            pk.setUsername(username);
            RenAuthuserEntity rae = session.get(RenAuthuserEntity.class, pk);
            if (rae == null) {
                transaction.commit();
                return false;
            }
            if (updateArgs.containsKey("password")) {
                rae.setPassword(EncryptUtil.EncryptSHA256(updateArgs.get("password")));
            }
            if (updateArgs.containsKey("gid")) {
                rae.setGid(updateArgs.get("gid"));
            }
            if (updateArgs.containsKey("status") && isAdmin) {
                rae.setStatus(Integer.valueOf(updateArgs.get("status")));
            }
            if (updateArgs.containsKey("level") && isAdmin) {
                rae.setLevel(Integer.valueOf(updateArgs.get("level")));
            }
            transaction.commit();
            return true;
        } catch (Exception ex) {
            LogUtil.Log(String.format("UpdateAuthorizationUser but exception occurred (%s@%s), service rollback, %s", username, domain, ex),
                    AuthorizationService.class.getName(), LogUtil.LogLevelType.ERROR, "");
            transaction.rollback();
            return false;
        } finally {
            HibernateUtil.CloseLocalSession();
        }
    }

    /**
     * Check if a BO environment user is already exist.
     *
     * @param username user unique name to be checked
     * @param domain   domain name
     * @return boolean of existence
     */
    public static boolean ContainAuthorizationUser(String username, String domain) {
        Session session = HibernateUtil.GetLocalSession();
        Transaction transaction = session.beginTransaction();
        try {
            RenAuthuserEntityPK pk = new RenAuthuserEntityPK();
            pk.setDomain(domain);
            pk.setUsername(username);
            RenAuthuserEntity rae = session.get(RenAuthuserEntity.class, pk);
            transaction.commit();
            return rae != null;
        } catch (Exception ex) {
            LogUtil.Log(String.format("ContainAuthorizationUser but exception occurred (%s@%s), service rollback, %s", username, domain, ex),
                    AuthorizationService.class.getName(), LogUtil.LogLevelType.ERROR, "");
            transaction.rollback();
            return true;
        } finally {
            HibernateUtil.CloseLocalSession();
        }
    }

    /**
     * Retrieve a BO environment user.
     *
     * @param username user unique name to be checked
     * @param domain   domain which user in
     * @return {@code RenAuthEntity} instance
     */
    public static RenAuthuserEntity RetrieveAuthorizationUser(String username, String domain) {
        Session session = HibernateUtil.GetLocalSession();
        Transaction transaction = session.beginTransaction();
        try {
            RenAuthuserEntityPK pk = new RenAuthuserEntityPK();
            pk.setDomain(domain);
            pk.setUsername(username);
            RenAuthuserEntity rae = session.get(RenAuthuserEntity.class, pk);
            transaction.commit();
            return rae;
        } catch (Exception ex) {
            LogUtil.Log(String.format("RetrieveAuthorizationUser but exception occurred (%s@%s), service rollback, %s", username, domain, ex),
                    AuthorizationService.class.getName(), LogUtil.LogLevelType.ERROR, "");
            transaction.rollback();
            return null;
        } finally {
            HibernateUtil.CloseLocalSession();
        }
    }

    /**
     * Retrieve all BO environment users.
     *
     * @return {@code RenAuthEntity} instance
     */
    public static List<RenAuthuserEntity> RetrieveAllAuthorizationUser(String domain) {
        Session session = HibernateUtil.GetLocalSession();
        Transaction transaction = session.beginTransaction();
        try {
            List<RenAuthuserEntity> rae;
            if (CommonUtil.IsNullOrEmpty(domain)) {
                rae = session.createQuery("FROM RenAuthuserEntity").list();
            }
            else {
                rae = session.createQuery(String.format("FROM RenAuthuserEntity WHERE domain = '%s'", domain)).list();
            }
            transaction.commit();
            return rae;
        } catch (Exception ex) {
            LogUtil.Log(String.format("RetrieveAllAuthorizationUser but exception occurred (%s), service rollback, %s", domain, ex),
                    AuthorizationService.class.getName(), LogUtil.LogLevelType.ERROR, "");
            transaction.rollback();
            return null;
        } finally {
            HibernateUtil.CloseLocalSession();
        }
    }

    /**
     * Get the owner domain of signature.
     * @param signature signature key string
     * @return owner domain name, null if not exist
     * todo process private signature key check
     */
    public static String GetSignatureOwner(String signature) {
        Session session = HibernateUtil.GetLocalSession();
        try {
            RenDomainEntity domain = (RenDomainEntity) session.createQuery(String.format("FROM RenDomainEntity WHERE urlsafe_signature = '%s'", signature)).uniqueResult();
            if (domain == null) {
                return null;
            }
            return domain.getName();
        }
        finally {
            HibernateUtil.CloseLocalSession();
        }
    }

    /**
     * Check if a workitem belong to signature owner domain.
     * @param signature signature key string
     * @param workitemId workitem global id
     * @return boolean of check result
     */
    public static boolean CheckWorkitemSignature(String signature, String workitemId) {
        Session session = HibernateUtil.GetLocalSession();
        Transaction transaction = session.beginTransaction();
        boolean cmtFlag = false;
        try {
            RenWorkitemEntity rwe = session.get(RenWorkitemEntity.class, workitemId);
            transaction.commit();
            cmtFlag = true;
            String rtid = rwe.getRtid();
            return AuthorizationService.CheckRTIDSignature(signature, rtid);
        }
        catch (Exception ex) {
            if (!cmtFlag) {
                transaction.rollback();
            }
            LogUtil.Log(String.format("CheckWorkitemSignature(KEY:%s, WID:%s) but exception occurred, %s",
                    signature, workitemId, ex), AuthorizationService.class.getName(), LogUtil.LogLevelType.ERROR, "");
            throw ex;
        }
        finally {
            HibernateUtil.CloseLocalSession();
        }
    }

    /**
     * Check if a workitem belong to signature owner domain.
     * @param signature signature key string
     * @param rtid process rtid
     * @return boolean of check result
     */
    public static boolean CheckRTIDSignature(String signature, String rtid) {
        String owner = AuthorizationService.GetSignatureOwner(signature);
        String rtidDomain = rtid.split("_")[1].split("@")[1];
        return owner != null && owner.equals(rtidDomain);
    }
}