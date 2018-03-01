/*
 * Project Ren @ 2018
 * Rinkako, Ariana, Gordan. SYSU SDCS.
 */
package org.sysu.renNameService.nameSpacing;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.sysu.renCommon.utility.TimestampUtil;
import org.sysu.renNameService.GlobalContext;
import org.sysu.renNameService.entity.RenBoEntity;
import org.sysu.renNameService.entity.RenProcessEntity;
import org.sysu.renNameService.entity.RenRuntimerecordEntity;
import org.sysu.renNameService.utility.*;

import java.util.*;

/**
 * Author: Rinkako
 * Date  : 2018/1/26
 * Usage : All name space service will be handled in this service module.
 */
public class NameSpacingService {

    /**
     * Create a new process.
     *
     * @param renid       creator renid
     * @param processName process unique name for a specific renid
     * @param mainBOName  process entry point BO name
     * @return process pid
     */
    public static String CreateProcess(String renid, String processName, String mainBOName) {
        Session session = HibernateUtil.GetLocalSession();
        Transaction transaction = session.beginTransaction();
        try {
            RenProcessEntity rpe = new RenProcessEntity();
            String pid = "Process_" + UUID.randomUUID().toString();
            rpe.setPid(pid);
            rpe.setCreateTimestamp(TimestampUtil.GetCurrentTimestamp());
            rpe.setCreatorRenid(renid);
            rpe.setMainBo(mainBOName);
            rpe.setProcessName(processName);
            rpe.setAverageCost(0L);
            rpe.setLaunchCount(0);
            rpe.setSuccessCount(0);
            session.save(rpe);
            transaction.commit();
            return pid;
        } catch (Exception ex) {
            transaction.rollback();
            LogUtil.Log("Create process but exception occurred, service rollback, " + ex, NameSpacingService.class.getName(), LogUtil.LogLevelType.ERROR, "");
        } finally {
            HibernateUtil.CloseLocalSession();
        }
        return "";
    }

    /**
     * Upload a BO for a specific process.
     *
     * @param pid     belong to pid
     * @param name    BO name
     * @param content BO content string
     * @return pair of boid - involved business role names string
     */
    public static AbstractMap.SimpleEntry<String, String> UploadBOContent(String pid, String name, String content) {
        Session session = HibernateUtil.GetLocalSession();
        Transaction transaction = session.beginTransaction();
        boolean cmtFlag = false;
        try {
            String boid = "BO_" + UUID.randomUUID().toString();
            RenBoEntity rbe = new RenBoEntity();
            rbe.setBoid(boid);
            rbe.setPid(pid);
            rbe.setBoName(name);
            rbe.setBoContent(content);
            session.save(rbe);
            // send to engine for get business role
            HashMap<String, String> args = new HashMap<>();
            args.put("boidlist", boid);
            transaction.commit();
            cmtFlag = true;
            String involveBRs = GlobalContext.Interaction.Send(GlobalContext.URL_BOENGINE_SERIALIZEBO, args, "");
            return new AbstractMap.SimpleEntry<>(boid, involveBRs);
            //return new AbstractMap.SimpleEntry<>(boid, "TEST_INVOLVED");
        } catch (Exception ex) {
            if (!cmtFlag) {
                transaction.rollback();
                LogUtil.Log("Upload BO but exception occurred, service rollback, " + ex, NameSpacingService.class.getName(), LogUtil.LogLevelType.ERROR, "");
            }
            LogUtil.Log("Upload BO but exception occurred, " + ex, NameSpacingService.class.getName(), LogUtil.LogLevelType.ERROR, "");
        } finally {
            HibernateUtil.CloseLocalSession();
        }
        return null;
    }

    /**
     * Get all processes of one ren user.
     *
     * @param renid ren user id
     * @return a list of process
     */
    @SuppressWarnings("unchecked")
    public static ArrayList<RenProcessEntity> GetProcessByRenId(String renid) {
        Session session = HibernateUtil.GetLocalSession();
        Transaction transaction = session.beginTransaction();
        try {
            ArrayList<RenProcessEntity> qRet = (ArrayList<RenProcessEntity>) session.createQuery(String.format("FROM RenProcessEntity WHERE creatorRenid = '%s' AND state = 0", renid)).list();
            transaction.commit();
            return qRet;
        } catch (Exception ex) {
            transaction.rollback();
            LogUtil.Log("Get Processes of Ren User but exception occurred, service rollback, " + ex, NameSpacingService.class.getName(), LogUtil.LogLevelType.ERROR, "");
        } finally {
            HibernateUtil.CloseLocalSession();
        }
        return null;
    }

    /**
     * Get the BOs in a process.
     *
     * @param pid process id
     * @return a list of BO in the specific process
     */
    @SuppressWarnings("unchecked")
    public static ArrayList<Object> GetProcessBOList(String pid) {
        Session session = HibernateUtil.GetLocalSession();
        Transaction transaction = session.beginTransaction();
        try {
            ArrayList<Object> qRet = (ArrayList<Object>) session.createQuery(String.format("SELECT boid, boName FROM RenBoEntity WHERE pid = '%s'", pid)).list();
            transaction.commit();
            return qRet;
        } catch (Exception ex) {
            transaction.rollback();
            LogUtil.Log("Get BO in Process but exception occurred, service rollback, " + ex, NameSpacingService.class.getName(), LogUtil.LogLevelType.ERROR, "");
        } finally {
            HibernateUtil.CloseLocalSession();
        }
        return null;
    }

    /**
     * Check if a process name is already existing in a ren user process list.
     *
     * @param renid       ren user id
     * @param processName process name
     * @return boolean for process name existence
     */
    @SuppressWarnings("unchecked")
    public static boolean ContainProcess(String renid, String processName) {
        Session session = HibernateUtil.GetLocalSession();
        Transaction transaction = session.beginTransaction();
        try {
            ArrayList<RenProcessEntity> qRet = (ArrayList<RenProcessEntity>) session.createQuery(String.format("FROM RenProcessEntity WHERE creatorRenid = '%s' AND processName = '%s", renid, processName)).list();
            transaction.commit();
            return qRet.size() > 0;
        } catch (Exception ex) {
            transaction.rollback();
            LogUtil.Log("Get BO in Process but exception occurred, service rollback, " + ex, NameSpacingService.class.getName(), LogUtil.LogLevelType.ERROR, "");
        } finally {
            HibernateUtil.CloseLocalSession();
        }
        return false;
    }

    /**
     * Get a BO context by its id.
     *
     * @param boid BO unique id
     * @return {@code RenBoEntity} instance
     */
    public static RenBoEntity GetBO(String boid, String rtid) {
        Session session = HibernateUtil.GetLocalSession();
        Transaction transaction = session.beginTransaction();
        try {
            RenBoEntity rbe = session.get(RenBoEntity.class, boid);
            transaction.commit();
            return rbe;
        } catch (Exception ex) {
            transaction.rollback();
            LogUtil.Log("Get BO context but exception occurred, service rollback, " + ex, NameSpacingService.class.getName(), LogUtil.LogLevelType.ERROR, rtid);
        } finally {
            HibernateUtil.CloseLocalSession();
        }
        return null;
    }

    /**
     * Submit a process launch request.
     *
     * @param pid              process id to be launched
     * @param from             launch platform
     * @param renid            ren user id
     * @param authoritySession service authority session id
     * @param bindingType      resource binding type
     * @param launchType       process launch type
     * @param failureType      process failure catch type
     * @param binding          resource binding source, only useful when static XML binding
     * @return Runtime record package
     */
    public static String SubmitProcess(String pid,
                                       String from,
                                       String renid,
                                       String authoritySession,
                                       Integer bindingType,
                                       Integer launchType,
                                       Integer failureType,
                                       Integer authType,
                                       String binding) {
        Session session = HibernateUtil.GetLocalSession();
        Transaction transaction = session.beginTransaction();
        try {
            RenProcessEntity rpe = session.get(RenProcessEntity.class, pid);
            rpe.setAuthtype(authType);
            String authSign = "";
            if (authType != 0) {
                authSign = RSASignatureUtil.Signature(pid, GlobalContext.PRIVATE_KEY);
                assert authSign != null;
                rpe.setSelfsignature(authSign);
            }
            RenRuntimerecordEntity rrte = new RenRuntimerecordEntity();
            String rtid = String.format("RTID_%s_%s", renid, UUID.randomUUID());
            rrte.setRtid(rtid);
            rrte.setLaunchFrom(from);
            rrte.setLaunchAuthorityId(renid);
            rrte.setSessionId(authoritySession);
            rrte.setProcessId(pid);
            rrte.setProcessName(rpe.getProcessName());
            rrte.setResourceBindingType(bindingType);
            rrte.setLaunchType(launchType);
            rrte.setFailureType(failureType);
            rrte.setResourceBinding(binding);
            rrte.setLaunchTimestamp(TimestampUtil.GetCurrentTimestamp());
            session.save(rrte);
            transaction.commit();
            return String.format("%s,%s", rtid, authSign);
        } catch (Exception ex) {
            transaction.rollback();
            LogUtil.Log(String.format("Submit process but exception occurred(pid: %s), service rollback, %s", pid, ex), NameSpacingService.class.getName(), LogUtil.LogLevelType.ERROR, "");
        } finally {
            HibernateUtil.CloseLocalSession();
        }
        return null;
    }

    /**
     * Handle transshipment of callback event.
     *
     * @param args argument map to sent
     */
    public static String TransshipCallback(Hashtable<String, Object> args) throws Exception {
        HashMap<String, String> argMap = new HashMap<>();
        for (Map.Entry<String, Object> kvp : args.entrySet()) {
            argMap.put(kvp.getKey(), (String) kvp.getValue());
        }
        return GlobalContext.Interaction.Send(GlobalContext.URL_BOENGINE_CALLBACK, argMap, argMap.get("rtid"));
    }

    /**
     * Handle transshipment of workitem actions.
     *
     * @param action     action name
     * @param workitemId workitem global id
     * @param workerId   worker global id
     */
    public static String TransshipWorkitem(String action, String workitemId, String workerId) throws Exception {
        HashMap<String, String> argMap = new HashMap<>();
        argMap.put("workitemId", workitemId);
        argMap.put("workerId", workerId);
        return GlobalContext.Interaction.Send(GlobalContext.URL_RS_WORKITEM_GATEWAY + action, argMap, "");
    }
}