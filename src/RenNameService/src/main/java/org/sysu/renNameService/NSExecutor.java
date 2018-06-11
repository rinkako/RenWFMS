/*
 * Project Ren @ 2018
 * Rinkako, Ariana, Gordan. SYSU SDCS.
 */
package org.sysu.renNameService;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.sysu.renNameService.entity.*;
import org.sysu.renNameService.nameSpacing.NameSpacingService;
import org.sysu.renNameService.roleMapping.RoleMappingService;
import org.sysu.renNameService.transaction.NameServiceTransaction;
import org.sysu.renNameService.transaction.TransactionType;
import org.sysu.renNameService.utility.HibernateUtil;
import org.sysu.renNameService.utility.LogUtil;
import org.sysu.renNameService.utility.SerializationUtil;
import org.sysu.renCommon.utility.TimestampUtil;

import java.util.*;

/**
 * Author: Rinkako
 * Date  : 2018/1/24
 * Usage : This class actually handle a specific transaction.
 *         An executor should be supervise by the main scheduler or a tracker.
 */
public class NSExecutor extends Observable {
    /**
     * Create a new name service transaction executor.
     * @param executorObserver executor supervisor
     */
    NSExecutor(Observer executorObserver) {
        this.addObserver(executorObserver);
    }

    /**
     * Execute name service transaction synchronously.
     * @param nst {@code NameServiceTransaction} instance
     * @return execution result
     */
    public Object ExecuteSync(NameServiceTransaction nst) {
        RenNsTransactionEntity context = nst.getTransactionContext();
        Hashtable<String, Object> execResult = new Hashtable<>();
        try {
            TransactionType tType = TransactionType.values()[context.getType()];
            String act = (String) nst.getParameterDictionary().get(GlobalContext.TRANSACTION_ACTION_KEY);
            HashMap<String, Object> args = nst.getParameterDictionary();
            String retStr = null;
            switch (tType) {
                case BusinessRoleMapping:
                    execResult.put("execType", TransactionType.BusinessRoleMapping.name());
                    String rtid = (String) args.get("rtid");
                    switch (act) {
                        case "getWorkerByBRole":
                            ArrayList<String> bRoles = RoleMappingService.GetWorkerByBusinessRole(rtid, (String) args.get("brole"));
                            retStr = SerializationUtil.JsonSerialization(bRoles, rtid);
                            execResult.put("rtid", rtid);
                            break;
                        case "getBRoleByWorker":
                            ArrayList<String> gidList = RoleMappingService.GetBusinessRoleByGlobalId(rtid, (String) args.get("gid"));
                            retStr = SerializationUtil.JsonSerialization(gidList, rtid);
                            execResult.put("rtid", rtid);
                            break;
                        case "register":
                            RoleMappingService.RegisterRoleMapService(rtid, (String) args.get("organGid"), (String) args.get("dataVersion"), (String) args.get("map"));
                            retStr = "OK";
                            execResult.put("rtid", rtid);
                            break;
                        case "fin":
                            RoleMappingService.FinishRoleMapService(rtid);
                            retStr = "OK";
                            execResult.put("rtid", rtid);
                            break;
                        case "getAllResourceFromCOrgan":
                            retStr = RoleMappingService.GetAllResourceFromCOrgan((String) args.get("renid"), rtid == null ? "": rtid, nst.getTransactionContext().getNsid());
                            if (rtid != null) {
                                execResult.put("rtid", rtid);
                            }
                            break;
                        case "getAllConnectionFromCOrgan":
                            retStr = RoleMappingService.GetAllConnectionFromCOrgan((String) args.get("renid"), rtid == null ? "": rtid, nst.getTransactionContext().getNsid());
                            if (rtid != null) {
                                execResult.put("rtid", rtid);
                            }
                            break;
                        case "getDataVersionAndGidFromCOrgan":
                            retStr = RoleMappingService.GetDataVersionAndGidFromCOrgan((String) args.get("renid"), nst.getTransactionContext().getNsid());
                            break;
                        case "getInvolved":
                            ArrayList<RenRolemapEntity> involves = RoleMappingService.GetInvolvedResource(rtid);
                            retStr = SerializationUtil.JsonSerialization(involves, rtid);
                            execResult.put("rtid", rtid);
                            break;
                        case "loadParticipant":
                            RoleMappingService.LoadParticipant((String) args.get("renid"), rtid, nst.getTransactionContext().getNsid());
                            retStr = "OK";
                            break;
                        case "unloadParticipant":
                            RoleMappingService.UnloadParticipant(rtid);
                            retStr = "OK";
                            break;
                    }
                    // prepare execution result
                    execResult.put("execCode", GlobalContext.TRANSACTION_EXECUTOR_SUCCESS);
                    execResult.put("context", nst);
                    execResult.put("nsid", context.getNsid());
                    execResult.put("action", act);
                    execResult.put("content", retStr == null ? "" : retStr);
                    break;
                case Namespacing:
                    execResult.put("execType", TransactionType.Namespacing.name());
                    String nsAct = (String) nst.getParameterDictionary().get(GlobalContext.TRANSACTION_ACTION_KEY);
                    switch (nsAct) {
                        case "createProcess":
                            retStr = NameSpacingService.CreateProcess((String) args.get("renid"), (String) args.get("name"), (String) args.get("mainbo"));
                            break;
                        case "uploadBO":
                            retStr = SerializationUtil.JsonSerialization(NameSpacingService.UploadBOContent((String) args.get("pid"), (String) args.get("name"), (String) args.get("content")), "");
                            break;
                        case "getProcessByRenId":
                            ArrayList<RenProcessEntity> processByRenList = NameSpacingService.GetProcessByRenId((String) args.get("renid"));
                            retStr = SerializationUtil.JsonSerialization(processByRenList, "");
                            break;
                        case "getProcessByDomain":
                            ArrayList<RenProcessEntity> processByDomainList = NameSpacingService.GetProcessByDomain((String) args.get("domain"));
                            retStr = SerializationUtil.JsonSerialization(processByDomainList, "");
                            break;
                        case "getProcessByPid":
                            RenProcessEntity processByPid = NameSpacingService.GetProcessByPid((String) args.get("pid"));
                            retStr = SerializationUtil.JsonSerialization(processByPid, "");
                            break;
                        case "getProcessBOList":
                            ArrayList<Object> processBOList = NameSpacingService.GetProcessBOList((String) args.get("pid"));
                            retStr = SerializationUtil.JsonSerialization(processBOList, "");
                            break;
                        case "getRuntimeRecord":
                            RenRuntimerecordEntity RTC = NameSpacingService.GetRuntimeRecord((String) args.get("rtid"));
                            retStr = SerializationUtil.JsonSerialization(RTC, "");
                            break;
                        case "getAllRuntimeRecord":
                            ArrayList<RenRuntimerecordEntity> allRTCList = NameSpacingService.GetAllRuntimeRecord((String) args.get("activeOnly"));
                            retStr = SerializationUtil.JsonSerialization(allRTCList, "");
                            break;
                        case "getRuntimeRecordByDomain":
                            ArrayList<RenRuntimerecordEntity> domainRTCList = NameSpacingService.GetRuntimeRecordByDomain((String) args.get("domain"), (String) args.get("activeOnly"));
                            retStr = SerializationUtil.JsonSerialization(domainRTCList, "");
                            break;
                        case "getRuntimeRecordByLauncher":
                            ArrayList<RenRuntimerecordEntity> launcherRTCList = NameSpacingService.GetRuntimeRecordByLauncher((String) args.get("launcher"), (String) args.get("activeOnly"));
                            retStr = SerializationUtil.JsonSerialization(launcherRTCList, "");
                            break;
                        case "getRuntimeLogByRTID":
                            ArrayList<RenLogEntity> RTCLogList = NameSpacingService.GetRuntimeLog((String) args.get("rtid"));
                            retStr = SerializationUtil.JsonSerialization(RTCLogList, "");
                            break;
                        case "containProcess":
                            boolean containProcessFlag = NameSpacingService.ContainProcess((String) args.get("renid"), (String) args.get("processName"));
                            retStr = SerializationUtil.JsonSerialization(containProcessFlag, "");
                            break;
                        case "getBO":
                            RenBoEntity getBoEntity = NameSpacingService.GetBO((String) args.get("boid"), (String) args.get("rtid"));
                            retStr = SerializationUtil.JsonSerialization(getBoEntity, "");
                            break;
                        case "submitProcess":
                            retStr = NameSpacingService.SubmitProcess((String) args.get("pid"), (String) args.get("from"), (String) args.get("renid"), (String) args.get("authoritySession"), Integer.parseInt((String) args.get("bindingType")), Integer.parseInt((String) args.get("launchType")), Integer.parseInt((String) args.get("failureType")), Integer.parseInt((String) args.get("authType")), (String) args.get("binding"));
                            break;
                        case "startProcess":
                            NameSpacingService.StartProcess((String) args.get("rtid"));
                            retStr = "OK";
                            break;
                        case "checkFinish":
                            retStr = NameSpacingService.CheckFinish((String) args.get("rtid"));
                            break;
                        case "transshipGetSpanTree":
                            retStr = (String) NameSpacingService.TransshipGetSpanTree((String) args.get("rtid"));
                            break;
                        case "transshipCallback":
                            retStr = NameSpacingService.TransshipCallback(args);
                            break;
                        case "transshipWorkitem":
                            retStr = (String) NameSpacingService.TransshipWorkitem((String) args.get("action"), (String) args.get("workitemId"), (String) args.get("workerId"), (String) args.get("payload"));
                            break;
                        case "transshipWorkqueue":
                            retStr = (String) NameSpacingService.TransshipWorkqueue((String) args.get("action"), (String) args.get("rtid"), (String) args.get("workerId"), (String) args.get("type"));
                            break;
                        case "transshipGetAll":
                            retStr = (String) NameSpacingService.TransshipGetAll((String) args.get("rtid"));
                            break;
                        case "transshipGetAllWorkitemsForDomain":
                            retStr = (String) NameSpacingService.TransshipGetAllWorkitemsForDomain((String) args.get("domain"));
                            break;
                        case "transshipGetAllActiveForParticipant":
                            retStr = (String) NameSpacingService.TransshipGetAllActiveForParticipant((String) args.get("workerId"));
                            break;
                        case "transshipGetWorkitem":
                            retStr = (String) NameSpacingService.TransshipGetWorkitem((String) args.get("wid"));
                            break;
                    }
                    // prepare execution result
                    execResult.put("execCode", GlobalContext.TRANSACTION_EXECUTOR_SUCCESS);
                    execResult.put("context", nst);
                    execResult.put("nsid", context.getNsid());
                    execResult.put("action", act);
                    execResult.put("content", retStr == null ? "" : retStr);
                    break;
                default:
                    LogUtil.Log("Execute sync failed, wrong type code", NSExecutor.class.getName(),
                            LogUtil.LogLevelType.WARNING, context.getRtid());
                    break;
            }
            // write success info to db
//            Session session = HibernateUtil.GetLocalSession();
//            Transaction dbTrans = session.beginTransaction();
//            try {
//                context.setFinishTimestamp(TimestampUtil.GetCurrentTimestamp());
//                session.update(context);
//                dbTrans.commit();
//            }
//            catch (Exception dbEx) {
//                dbTrans.rollback();
//                throw dbEx;
//            }
//            finally {
//                HibernateUtil.CloseLocalSession();
//            }
            return retStr;
        }
        catch (Exception ex) {
            LogUtil.Log("Executor Exception Occurred, " + ex, NSExecutor.class.getName(),
                    LogUtil.LogLevelType.ERROR, context.getRtid());
            execResult.put("context", nst);
            execResult.put("nsid", context.getNsid());
            execResult.put("execCode", GlobalContext.TRANSACTION_EXECUTOR_FAILED);
        }
        finally {
            // bubble notification to scheduler or tracker which supervise this executor
            this.setChanged();
            this.notifyObservers(execResult);
        }
        return null;
    }
}
