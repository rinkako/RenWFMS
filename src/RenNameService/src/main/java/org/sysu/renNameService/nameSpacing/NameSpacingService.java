/*
 * Project Ren @ 2018
 * Rinkako, Ariana, Gordan. SYSU SDCS.
 */
package org.sysu.renNameService.nameSpacing;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.sysu.renCommon.interactionRouter.LocationContext;
import org.sysu.renCommon.utility.AuthDomainHelper;
import org.sysu.renCommon.utility.TimestampUtil;
import org.sysu.renNameService.GlobalContext;
import org.sysu.renNameService.entity.RenBoEntity;
import org.sysu.renNameService.entity.RenLogEntity;
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
            String involveBRs = GlobalContext.Interaction.Send(LocationContext.URL_BOENGINE_SERIALIZEBO, args, "");
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
     * Get all processes of one domain.
     *
     * @param domain domain name
     * @return a list of process
     */
    @SuppressWarnings("unchecked")
    public static ArrayList<RenProcessEntity> GetProcessByDomain(String domain) {
        Session session = HibernateUtil.GetLocalSession();
        Transaction transaction = session.beginTransaction();
        boolean cmtFlag = false;
        try {
            ArrayList<RenProcessEntity> qRet = (ArrayList<RenProcessEntity>) session.createQuery(String.format("FROM RenProcessEntity WHERE LOCATE('%s', creatorRenid) > 0 AND state = 0", "@" + domain)).list();
            transaction.commit();
            cmtFlag = true;
            ArrayList<RenProcessEntity> pureRet = new ArrayList<>();
            for (RenProcessEntity cp : qRet) {
                if (AuthDomainHelper.GetDomainByAuthName(cp.getCreatorRenid()).equals(domain)) {
                    pureRet.add(cp);
                }
            }
            return pureRet;
        } catch (Exception ex) {
            if (!cmtFlag) {
                transaction.rollback();
            }
            LogUtil.Log("Get Processes of domain but exception occurred, service rollback, " + ex, NameSpacingService.class.getName(), LogUtil.LogLevelType.ERROR, "");
        } finally {
            HibernateUtil.CloseLocalSession();
        }
        return null;
    }

    /**
     * Get all processes by process global id.
     *
     * @param pid process global id
     * @return process instance
     */
    @SuppressWarnings("unchecked")
    public static RenProcessEntity GetProcessByPid(String pid) {
        Session session = HibernateUtil.GetLocalSession();
        Transaction transaction = session.beginTransaction();
        try {
            RenProcessEntity rpe = session.get(RenProcessEntity.class, pid);
            transaction.commit();
            return rpe;
        } catch (Exception ex) {
            transaction.rollback();
            LogUtil.Log("Get Processes of pid but exception occurred, service rollback, " + ex, NameSpacingService.class.getName(), LogUtil.LogLevelType.ERROR, "");
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
            rrte.setSessionId(authoritySession);
            rrte.setProcessId(pid);
            rrte.setProcessName(rpe.getProcessName());
            rrte.setResourceBindingType(bindingType);
            rrte.setLaunchType(launchType);
            rrte.setFailureType(failureType);
            rrte.setResourceBinding(binding);
            rrte.setIsSucceed(0);
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
     * Start a process.
     *
     * @param rtid process rtid.
     */
    public static void StartProcess(String rtid) throws Exception {
        Session session = HibernateUtil.GetLocalSession();
        Transaction transaction = session.beginTransaction();
        try {
            RenRuntimerecordEntity rrte = session.get(RenRuntimerecordEntity.class, rtid);
            rrte.setLaunchTimestamp(TimestampUtil.GetCurrentTimestamp());
            String launcher = AuthDomainHelper.GetAuthNameByRTID(rtid);
            rrte.setLaunchAuthorityId(launcher);
            session.saveOrUpdate(rrte);
            RenProcessEntity rpe = session.get(RenProcessEntity.class, rrte.getProcessId());
            rpe.setLaunchCount(rpe.getLaunchCount() + 1);
            rpe.setLastLaunchTimestamp(TimestampUtil.GetCurrentTimestamp());
            session.saveOrUpdate(rpe);
            transaction.commit();
        } catch (Exception ex) {
            transaction.rollback();
            LogUtil.Log("Start process but exception occurred, service rollback, " + ex, NameSpacingService.class.getName(), LogUtil.LogLevelType.ERROR, rtid);
        } finally {
            HibernateUtil.CloseLocalSession();
        }
        // interaction with BO Engine
        HashMap<String, String> args = new HashMap<>();
        args.put("rtid", rtid);
        try {
            GlobalContext.Interaction.Send(LocationContext.URL_BOENGINE_START, args, rtid);
        } catch (Exception ex) {
            LogUtil.Log("Cannot interaction with BO Engine for RTID: " + rtid, NameSpacingService.class.getName(), LogUtil.LogLevelType.ERROR, rtid);
            throw ex;
        }
    }

    /**
     * Check a process runtime finish status.
     *
     * @param rtid process rtid.
     * @return a map of status description in JSON
     */
    public static String CheckFinish(String rtid) throws Exception {
        Session session = HibernateUtil.GetLocalSession();
        Transaction transaction = session.beginTransaction();
        RenRuntimerecordEntity rrte;
        try {
            rrte = session.get(RenRuntimerecordEntity.class, rtid);
            transaction.commit();
        } catch (Exception ex) {
            transaction.rollback();
            LogUtil.Log("CheckFinish but exception occurred, service rollback, " + ex, NameSpacingService.class.getName(), LogUtil.LogLevelType.ERROR, rtid);
            return null;
        } finally {
            HibernateUtil.CloseLocalSession();
        }
        if (rrte == null) {
            return null;
        }
        HashMap<String, String> retMap = new HashMap<>();
        boolean isFinished = rrte.getFinishTimestamp() != null;
        retMap.put("IsFinished", isFinished ? "true" : "false");
        retMap.put("FinishTimestamp", isFinished ? rrte.getFinishTimestamp().toString() : "");
        retMap.put("IsSucceed", rrte.getIsSucceed() == 1 ? "true" : "false");
        return SerializationUtil.JsonSerialization(retMap, rtid);
    }

    /**
     * Get a runtime record.
     *
     * @param rtid runtime record id
     * @return RTC instance
     */
    @SuppressWarnings("unchecked")
    public static RenRuntimerecordEntity GetRuntimeRecord(String rtid) {
        Session session = HibernateUtil.GetLocalSession();
        Transaction transaction = session.beginTransaction();
        try {
            RenRuntimerecordEntity rrte = session.get(RenRuntimerecordEntity.class, rtid);
            transaction.commit();
            return rrte;
        } catch (Exception ex) {
            transaction.rollback();
            LogUtil.Log("Get RTC but exception occurred, service rollback, " + ex, NameSpacingService.class.getName(), LogUtil.LogLevelType.ERROR, rtid);
        } finally {
            HibernateUtil.CloseLocalSession();
        }
        return null;
    }

    /**
     * Get all runtime record.
     *
     * @param activeOnly whether only get running record
     * @return a list of RTC
     */
    @SuppressWarnings("unchecked")
    public static ArrayList<RenRuntimerecordEntity> GetAllRuntimeRecord(String activeOnly) {
        Session session = HibernateUtil.GetLocalSession();
        Transaction transaction = session.beginTransaction();
        try {
            ArrayList<RenRuntimerecordEntity> qRet;
            if (activeOnly.equalsIgnoreCase("true")) {
                qRet = (ArrayList<RenRuntimerecordEntity>) session.createQuery("FROM RenRuntimerecordEntity WHERE isSucceed = 0").list();
            } else {
                qRet = (ArrayList<RenRuntimerecordEntity>) session.createQuery("FROM RenRuntimerecordEntity").list();
            }
            transaction.commit();
            return qRet;
        } catch (Exception ex) {
            transaction.rollback();
            LogUtil.Log("Get RTC but exception occurred, service rollback, " + ex, NameSpacingService.class.getName(), LogUtil.LogLevelType.ERROR, "");
        } finally {
            HibernateUtil.CloseLocalSession();
        }
        return null;
    }

    /**
     * Get all runtime record in a domain.
     *
     * @param domain     domain name
     * @param activeOnly whether only get running record
     * @return a list of RTC
     */
    @SuppressWarnings("unchecked")
    public static ArrayList<RenRuntimerecordEntity> GetRuntimeRecordByDomain(String domain, String activeOnly) {
        Session session = HibernateUtil.GetLocalSession();
        Transaction transaction = session.beginTransaction();
        boolean cmtFlag = false;
        try {
            ArrayList<RenRuntimerecordEntity> qRet;
            if (activeOnly.equalsIgnoreCase("true")) {
                qRet = (ArrayList<RenRuntimerecordEntity>) session.createQuery(String.format("FROM RenRuntimerecordEntity WHERE isSucceed = 0 AND LOCATE('%s', launchAuthorityId) > 0", "@" + domain)).list();
            } else {
                qRet = (ArrayList<RenRuntimerecordEntity>) session.createQuery(String.format("FROM RenRuntimerecordEntity WHERE LOCATE('%s', launchAuthorityId) > 0", "@" + domain)).list();
            }
            transaction.commit();
            cmtFlag = true;
            ArrayList<RenRuntimerecordEntity> pureRet = new ArrayList<>();
            for (RenRuntimerecordEntity rre : qRet) {
                if (AuthDomainHelper.GetDomainByAuthName(rre.getLaunchAuthorityId()).equals(domain)) {
                    pureRet.add(rre);
                }
            }
            return pureRet;
        } catch (Exception ex) {
            if (!cmtFlag) {
                transaction.rollback();
            }
            LogUtil.Log("Get RTC but exception occurred, service rollback, " + ex, NameSpacingService.class.getName(), LogUtil.LogLevelType.ERROR, "");
        } finally {
            HibernateUtil.CloseLocalSession();
        }
        return null;
    }

    /**
     * Get all runtime record launched by a user.
     *
     * @param launcher   launcher auth name
     * @param activeOnly whether only get running record
     * @return a list of RTC
     */
    @SuppressWarnings("unchecked")
    public static ArrayList<RenRuntimerecordEntity> GetRuntimeRecordByLauncher(String launcher, String activeOnly) {
        Session session = HibernateUtil.GetLocalSession();
        Transaction transaction = session.beginTransaction();
        try {
            ArrayList<RenRuntimerecordEntity> qRet;
            if (activeOnly.equalsIgnoreCase("true")) {
                qRet = (ArrayList<RenRuntimerecordEntity>) session.createQuery(String.format("FROM RenRuntimerecordEntity WHERE isSucceed = 0 AND launchAuthorityId = '%s'", launcher)).list();
            } else {
                qRet = (ArrayList<RenRuntimerecordEntity>) session.createQuery(String.format("FROM RenRuntimerecordEntity WHERE launchAuthorityId = '%s'", launcher)).list();
            }
            transaction.commit();
            return qRet;
        } catch (Exception ex) {
            transaction.rollback();
            LogUtil.Log("Get RTC but exception occurred, service rollback, " + ex, NameSpacingService.class.getName(), LogUtil.LogLevelType.ERROR, "");
        } finally {
            HibernateUtil.CloseLocalSession();
        }
        return null;
    }

    /**
     * Get all runtime record log for rtid.
     *
     * @param rtid process runtime record id
     * @return a list of RTC
     */
    @SuppressWarnings("unchecked")
    public static ArrayList<RenLogEntity> GetRuntimeLog(String rtid) {
        Session session = HibernateUtil.GetLocalSession();
        Transaction transaction = session.beginTransaction();
        try {
            ArrayList<RenLogEntity> qRet = (ArrayList<RenLogEntity>) session.createQuery(String.format("FROM RenLogEntity WHERE rtid = '%s'", rtid)).list();
            transaction.commit();
            return qRet;
        } catch (Exception ex) {
            transaction.rollback();
            LogUtil.Log("Get RTC Log but exception occurred, service rollback, " + ex, NameSpacingService.class.getName(), LogUtil.LogLevelType.ERROR, rtid);
        } finally {
            HibernateUtil.CloseLocalSession();
        }
        return null;
    }

    /**
     * Handle transshipment of get span tree descriptor.
     *
     * @param rtid process runtime record id
     */
    public static Object TransshipGetSpanTree(String rtid) throws Exception {
        HashMap<String, String> argMap = new HashMap<>();
        argMap.put("rtid", rtid);
        String ret = GlobalContext.Interaction.Send(LocationContext.URL_BOENGINE_SPANTREE, argMap, rtid);
        Map retObj = SerializationUtil.JsonDeserialization(ret, Map.class);
        return ((Map) retObj.get("returnElement")).get("data");
    }

    /**
     * Handle transshipment of callback event.
     *
     * @param args argument map to sent
     */
    public static String TransshipCallback(Map<String, Object> args) throws Exception {
        HashMap<String, String> argMap = new HashMap<>();
        for (Map.Entry<String, Object> kvp : args.entrySet()) {
            argMap.put(kvp.getKey(), (String) kvp.getValue());
        }
        return GlobalContext.Interaction.Send(LocationContext.URL_BOENGINE_CALLBACK, argMap, argMap.get("rtid"));
    }

    /**
     * Handle transshipment of workitem actions.
     *
     * @param action     action name
     * @param workitemId workitem global id
     * @param workerId   worker global id
     * @param payload    payload map in JSON encoded string
     */
    public static Object TransshipWorkitem(String action, String workitemId, String workerId, String payload) throws Exception {
        HashMap<String, String> argMap = new HashMap<>();
        argMap.put("workitemId", workitemId);
        argMap.put("workerId", workerId);
        if (payload != null) {
            argMap.put("payload", payload);
        }
        String ret = GlobalContext.Interaction.Send(LocationContext.GATEWAY_RS_WORKITEM + action, argMap, "");
        Map retObj = SerializationUtil.JsonDeserialization(ret, Map.class);
        return ((Map) retObj.get("returnElement")).get("data");
    }

    /**
     * Handle transshipment of workqueue actions.
     *
     * @param action   action name
     * @param rtid     process rtid
     * @param workerId worker global id
     * @param type     workqueue type
     */
    public static Object TransshipWorkqueue(String action, String rtid, String workerId, String type) throws Exception {
        HashMap<String, String> argMap = new HashMap<>();
        argMap.put("rtid", rtid);
        argMap.put("type", type);
        argMap.put("workerId", workerId);
        String ret = GlobalContext.Interaction.Send(LocationContext.GATEWAY_RS_QUEUE + action, argMap, rtid);
        Map retObj = SerializationUtil.JsonDeserialization(ret, Map.class);
        return ((Map) retObj.get("returnElement")).get("data");
    }

    /**
     * Handle transshipment of get all workitems.
     *
     * @param rtid process rtid
     */
    public static Object TransshipGetAll(String rtid) throws Exception {
        HashMap<String, String> argMap = new HashMap<>();
        argMap.put("rtid", rtid);
        String ret = GlobalContext.Interaction.Send(LocationContext.GATEWAY_RS_WORKITEM + "getAll", argMap, rtid);
        Map retObj = SerializationUtil.JsonDeserialization(ret, Map.class);
        return ((Map) retObj.get("returnElement")).get("data");
    }

    /**
     * Handle transshipment of get all workitems for a domain.
     *
     * @param domain domain name
     */
    public static Object TransshipGetAllWorkitemsForDomain(String domain) throws Exception {
        HashMap<String, String> argMap = new HashMap<>();
        argMap.put("domain", domain);
        String ret = GlobalContext.Interaction.Send(LocationContext.GATEWAY_RS_WORKITEM + "getAllForDomain", argMap, "");
        Map retObj = SerializationUtil.JsonDeserialization(ret, Map.class);
        return ((Map) retObj.get("returnElement")).get("data");
    }

    /**
     * Handle transshipment of get all workitems for a participant.
     *
     * @param workerId participant worker global id
     */
    public static Object TransshipGetAllActiveForParticipant(String workerId) throws Exception {
        HashMap<String, String> argMap = new HashMap<>();
        argMap.put("workerId", workerId);
        String ret = GlobalContext.Interaction.Send(LocationContext.GATEWAY_RS_WORKITEM + "getAllForParticipant", argMap, "");
        Map retObj = SerializationUtil.JsonDeserialization(ret, Map.class);
        return ((Map) retObj.get("returnElement")).get("data");
    }

    /**
     * Handle transshipment of workitem.
     *
     * @param wid workitem id
     */
    public static Object TransshipGetWorkitem(String wid) throws Exception {
        HashMap<String, String> argMap = new HashMap<>();
        argMap.put("wid", wid);
        String ret = GlobalContext.Interaction.Send(LocationContext.GATEWAY_RS_WORKITEM + "get", argMap, "");
        Map retObj = SerializationUtil.JsonDeserialization(ret, Map.class);
        return ((Map) retObj.get("returnElement")).get("data");
    }
}