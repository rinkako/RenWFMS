/*
 * Project Ren @ 2018
 * Rinkako, Ariana, Gordan. SYSU SDCS.
 */
package org.sysu.renNameService.transaction;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.sysu.renNameService.GlobalContext;
import org.sysu.renNameService.entity.RenNsTransactionEntity;
import org.sysu.renNameService.utility.HibernateUtil;
import org.sysu.renNameService.utility.LogUtil;
import org.sysu.renCommon.utility.TimestampUtil;

import java.util.HashMap;
import java.util.UUID;

/**
 * Author: Rinkako
 * Date  : 2018/1/24
 * Usage : Static method for name service transaction creator.
 */
public class TransactionCreator {
    /**
     * Create a name service transaction package and save to steady.
     * @param type transaction type
     * @param action action name
     * @param args argument dictionary
     * @return {@code NameServiceTransaction} instance
     */
    public static NameServiceTransaction Create(TransactionType type, String action, HashMap<String, String> args) {
//        Session session = HibernateUtil.GetLocalSession();
//        Transaction dbTrans = session.beginTransaction();
        try {
            String nsid = "NS_" + UUID.randomUUID().toString();
            String rtid = args.get("rtid");
            NameServiceTransaction nst = new NameServiceTransaction();
            nst.AddParameter(args);
            nst.AddParameter(GlobalContext.TRANSACTION_ACTION_KEY, action);
            RenNsTransactionEntity rnte = nst.getTransactionContext();
            rnte.setAcceptTimestamp(TimestampUtil.GetCurrentTimestamp());
            rnte.setNsid(nsid);
            rnte.setRtid(rtid == null ? "" : rtid);
            rnte.setPriority(0);
            rnte.setType(type.ordinal());
//            session.save(rnte);
//            dbTrans.commit();
//            LogUtil.Log(String.format("Name service transaction created: %s (%s, %s)", nsid, type.name(), action),
//                    TransactionCreator.class.getName(), rtid == null ? "" : rtid);
            return nst;
        }
        finally {
//            HibernateUtil.CloseLocalSession();
        }
    }
}
