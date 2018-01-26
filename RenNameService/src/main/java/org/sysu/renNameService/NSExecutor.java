/*
 * Project Ren @ 2018
 * Rinkako, Ariana, Gordan. SYSU SDCS.
 */
package org.sysu.renNameService;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.sysu.renNameService.entity.RenNsTransactionEntity;
import org.sysu.renNameService.entity.RenRolemapEntity;
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
 * Usage : This class is used to actually process a transaction.
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
            String act = (String) nst.getParameterDictionary().get(GlobalConfigContext.TRANSACTION_ACTION_KEY);
            String retStr = null;
            switch (tType) {
                case BusinessRoleMapping:
                    Hashtable<String, Object> args = nst.getParameterDictionary();
                    String rtid = (String) args.get("rtid");
                    switch (act) {
                        case "getWorkerByBRole":
                            ArrayList<String> bRoles = RoleMappingService.GetWorkerByBusinessRole(rtid, (String) args.get("brole"));
                            retStr = SerializationUtil.JsonSerilization(bRoles, rtid);
                            break;
                        case "getBRoleByWorker":
                            ArrayList<String> gidList = RoleMappingService.GetBusinessRoleByGlobalId(rtid, (String) args.get("gid"));
                            retStr = SerializationUtil.JsonSerilization(gidList, rtid);
                            break;
                        case "register":
                            RoleMappingService.RegisterRoleMapService(rtid, (String) args.get("organGid"), (String) args.get("dataVersion"), Integer.valueOf((String) args.get("isolationType")), (String) args.get("map"));
                            retStr = "OK";
                            break;
                        case "fin":
                            RoleMappingService.FinishRoleMapService(rtid);
                            retStr = "OK";
                            break;
                        case "getInvolved":
                            ArrayList<RenRolemapEntity> involves = RoleMappingService.GetInvolvedResource(rtid);
                            retStr = SerializationUtil.JsonSerilization(involves, rtid);
                            break;
                    }
                    // prepare execution result
                    execResult.put("execCode", GlobalConfigContext.TRANSACTION_EXECUTOR_SUCCESS);
                    execResult.put("execType", TransactionType.BusinessRoleMapping.name());
                    execResult.put("context", nst);
                    execResult.put("nsid", context.getNsid());
                    execResult.put("action", act);
                    execResult.put("rtid", rtid);
                case Namespacing:
                    String nsAct = (String) nst.getParameterDictionary().get(GlobalConfigContext.TRANSACTION_ACTION_KEY);
                    Hashtable<String, Object> nsArgs = nst.getParameterDictionary();
                    switch (nsAct) {
                        case "generateRtid":
                            retStr = NameSpacingService.GenerateRTID();
                            break;
                    }
                    // prepare execution result
                    execResult.put("execCode", GlobalConfigContext.TRANSACTION_EXECUTOR_SUCCESS);
                    execResult.put("execType", TransactionType.Namespacing.name());
                    execResult.put("context", nst);
                    execResult.put("nsid", context.getNsid());
                    execResult.put("action", act);
                default:
                    LogUtil.Log("Execute sync failed, wrong type code", NSExecutor.class.getName(),
                            LogUtil.LogLevelType.WARNNING, context.getRtid());
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
            // bubble notification to scheduler or tracker
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
