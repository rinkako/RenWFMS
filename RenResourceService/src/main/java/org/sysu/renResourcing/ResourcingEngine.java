/*
 * Project Ren @ 2018
 * Rinkako, Ariana, Gordan. SYSU SDCS.
 */
package org.sysu.renResourcing;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.sysu.renResourcing.basic.enums.*;
import org.sysu.renResourcing.context.*;
import org.sysu.renResourcing.context.steady.RenRuntimerecordEntity;
import org.sysu.renResourcing.utility.HibernateUtil;
import org.sysu.renResourcing.utility.LogUtil;

import java.util.Hashtable;

/**
 * Author: Rinkako
 * Date  : 2018/2/4
 * Usage : This class performs the engine controller role. All service requests are
 *         passed to engine here from RESTful API by Spring router controllers, all
 *         service requests here are in a same view, means there no any concept of
 *         HTTP request, etc. LEngine is responsible for solving the request and
 *         give a response result to return immediately.
 */
public class ResourcingEngine {

    /**
     * Handle resourcing submission request from BO Engine.
     * @param rtid process runtime record id
     * @param boName task belong to BO name
     * @param polymorphismName task name defined in BO XML.
     * @return response package
     */
    public static String EngineSubmitTask(String rtid, String boName, String polymorphismName) {
        Session session = HibernateUtil.GetLocalThreadSession();
        Transaction transaction = session.beginTransaction();
        boolean cmtFlag = false;
        try {
            RenRuntimerecordEntity rre = session.get(RenRuntimerecordEntity.class, rtid);
            assert rre != null;
            transaction.commit();
            cmtFlag = true;
            TaskContext taskContext = TaskContext.GetContext(rtid, boName, polymorphismName);
            assert taskContext != null;
            Hashtable<String, Object> args = new Hashtable<>();
            args.put("taskContext", taskContext);
            ResourcingContext ctx = ResourcingContext.GetContext(null, rtid, RServiceType.SubmitResourcingTask, args);
            ResourcingEngine.mainScheduler.Schedule(ctx);
            return GlobalContext.RESPONSE_SUCCESS;
        }
        catch (Exception ex) {
            if (!cmtFlag) {
                transaction.rollback();
            }
            LogUtil.Log("Exception in EngineSubmitTask, " + ex, ResourcingEngine.class.getName(),
                    LogUtil.LogLevelType.ERROR, rtid);
            throw ex;  // rethrow to cause exception response
        }
    }

    /**
     * Main scheduler reference.
     */
    private static RScheduler mainScheduler = RScheduler.GetInstance();
}
