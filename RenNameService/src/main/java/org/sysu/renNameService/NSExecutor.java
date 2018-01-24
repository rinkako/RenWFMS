/*
 * Project Ren @ 2018
 * Rinkako, Ariana, Gordan. SYSU SDCS.
 */
package org.sysu.renNameService;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.sysu.renNameService.entity.RenNsTransactionEntity;
import org.sysu.renNameService.entity.RenRolemapEntity;
import org.sysu.renNameService.roleMapping.RoleMappingService;
import org.sysu.renNameService.transaction.NameServiceTransaction;
import org.sysu.renNameService.transaction.TransactionType;
import org.sysu.renNameService.utility.HibernateUtil;
import org.sysu.renNameService.utility.LogUtil;
import org.sysu.renNameService.utility.SerializationUtil;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Observable;

/**
 * Author: Rinkako
 * Date  : 2018/1/24
 * Usage : This class is used to actually process a transaction.
 */
public class NSExecutor extends Observable {

    /**
     * Execute name service transaction synchronously.
     * @param nst {@code NameServiceTransaction} instance
     * @return execution result
     */
    public Object ExecuteSync(NameServiceTransaction nst) {
        RenNsTransactionEntity context = nst.getTransactionContext();
        try {
            TransactionType tType = TransactionType.values()[context.getType()];
            switch (tType) {
                case BusinessRoleMapping:
                    String act = (String) nst.getParameterDictionary().get(GlobalConfigContext.TRANSACTION_ACTION_KEY);
                    Hashtable<String, Object> args = nst.getParameterDictionary();
                    String rtid = (String) args.get("rtid");
                    String retStr = null;
                    switch (act) {
                        case "getWorkerByBRole":
                            ArrayList<String> bRoles = RoleMappingService.GetWorkerByBusinessRole(rtid, (String) args.get("brole"));
                            retStr = SerializationUtil.JsonSerilization(bRoles, rtid);
                        case "getBRoleByWorker":
                            ArrayList<String> gidList = RoleMappingService.GetBusinessRoleByGlobalId(rtid, (String) args.get("gid"));
                            retStr = SerializationUtil.JsonSerilization(gidList, rtid);
                        case "register":
                            RoleMappingService.RegisterRoleMapService(rtid, (String) args.get("organGid"), (String) args.get("dataVersion"), Integer.valueOf((String) args.get("isolationType")), (String) args.get("map"));
                            retStr = "OK";
                        case "fin":
                            RoleMappingService.FinishRoleMapService(rtid);
                            retStr = "OK";
                        case "getInvolved":
                            ArrayList<RenRolemapEntity> involves = RoleMappingService.GetInvolvedResource(rtid);
                            retStr = SerializationUtil.JsonSerilization(involves, rtid);
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
                    // prepare execution result
                    Hashtable<String, Object> execResult = new Hashtable<>();
                    execResult.put("execCode", GlobalConfigContext.TRANSACTION_EXECUTOR_SUCCESS);
                    execResult.put("execType", TransactionType.BusinessRoleMapping.name());
                    execResult.put("context", nst);
                    execResult.put("rtid", rtid);
                    execResult.put("nsid", context.getNsid());
                    execResult.put("action", act);
                    this.setChanged();
                    this.notifyObservers(execResult);
                    return retStr;
                case Namespacing:
                    throw new NotImplementedException();
                default:
                    LogUtil.Log("Execute sync failed, wrong type code", NSExecutor.class.getName(),
                            LogUtil.LogLevelType.WARNNING, context.getRtid());
            }
        }
        catch (Exception ex) {
            LogUtil.Log("Execute sync failed, " + ex, NSExecutor.class.getName(),
                    LogUtil.LogLevelType.ERROR, context.getRtid());
        }
        return null;
    }
}
