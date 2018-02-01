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

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Observable;
import java.util.Observer;

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
        try {
            TransactionType tType = TransactionType.values()[context.getType()];
            Hashtable<String, Object> execResult = new Hashtable<>();
            String act = (String) nst.getParameterDictionary().get(GlobalContext.TRANSACTION_ACTION_KEY);
            Hashtable<String, Object> args = nst.getParameterDictionary();
            String retStr = null;
            switch (tType) {
                case BusinessRoleMapping:

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
                        case "getInvolved":
                            ArrayList<RenRolemapEntity> involves = RoleMappingService.GetInvolvedResource(rtid);
                            retStr = SerializationUtil.JsonSerialization(involves, rtid);
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
                    }
                    // prepare execution result
                    execResult.put("execCode", GlobalContext.TRANSACTION_EXECUTOR_SUCCESS);
                    execResult.put("execType", TransactionType.BusinessRoleMapping.name());
                    execResult.put("context", nst);
                    execResult.put("nsid", context.getNsid());
                    execResult.put("action", act);
                    break;
                case Namespacing:
                    String nsAct = (String) nst.getParameterDictionary().get(GlobalContext.TRANSACTION_ACTION_KEY);
                    switch (nsAct) {
                        case "generateRtid":
                            retStr = NameSpacingService.GenerateRTID();
                            break;
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
                        case "getProcessBOList":
                            ArrayList<Object> processBOList = NameSpacingService.GetProcessBOList((String) args.get("pid"));
                            retStr = SerializationUtil.JsonSerialization(processBOList, "");
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
                            retStr = NameSpacingService.SubmitProcess((String) args.get("pid"), (String) args.get("from"), (String) args.get("renid"), (String) args.get("authoritySession"), Integer.parseInt((String) args.get("bindingType")), Integer.parseInt((String) args.get("launchType")), Integer.parseInt((String) args.get("failureType")), (String) args.get("binding"));
                            break;
                    }
                    // prepare execution result
                    execResult.put("execCode", GlobalContext.TRANSACTION_EXECUTOR_SUCCESS);
                    execResult.put("execType", TransactionType.Namespacing.name());
                    execResult.put("context", nst);
                    execResult.put("nsid", context.getNsid());
                    execResult.put("action", act);
                    break;
                default:
                    LogUtil.Log("Execute sync failed, wrong type code", NSExecutor.class.getName(),
                            LogUtil.LogLevelType.WARNING, context.getRtid());
                    break;
            }
            // write success info to db
            Session session = HibernateUtil.GetLocalThreadSession();
            Transaction dbTrans = session.beginTransaction();
            try {
                context.setFinishTimestamp(new Timestamp(System.currentTimeMillis()));
                session.update(context);
                dbTrans.commit();
            }
            catch (Exception dbEx) {
                dbTrans.rollback();
                throw dbEx;
            }
            // bubble notification to scheduler or tracker which supervise this executor
            this.setChanged();
            this.notifyObservers(execResult);
            return retStr;
        }
        catch (Exception ex) {
            LogUtil.Log("Executor Exception Occurred, " + ex, NSExecutor.class.getName(),
                    LogUtil.LogLevelType.ERROR, context.getRtid());
        }
        return null;
    }
}
