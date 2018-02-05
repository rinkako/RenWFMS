/*
 * Project Ren @ 2018
 * Rinkako, Ariana, Gordan. SYSU SDCS.
 */
package org.sysu.renResourcing;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.sysu.renResourcing.context.TaskContext;
import org.sysu.renResourcing.context.steady.RenRuntimerecordEntity;
import org.sysu.renResourcing.utility.HibernateUtil;

/**
 * Author: Rinkako
 * Date  : 2018/2/4
 * Usage : This class performs the engine controller role. All service requests are
 *         passed to engine here from RESTful API by Spring router controllers, all
 *         service requests here are in a same view, means there no any concept of
 *         HTTP request, etc. LEngine is responsible for solving the request and
 *         give a response result to return immediately.
 *         This is a Singleton, use {@code GetInstance} instead of create new one.
 */
public class ResourcingEngine {

    /**
     * Handle resourcing submission request from BO Engine.
     * @param rtid process runtime record id
     * @param boName task belong to BO name
     * @param polymorphismName task name defined in BO XML.
     * @return response package
     */
    public String EngineSubmitTask(String rtid, String boName, String polymorphismName) {
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

        }
        catch (Exception ex) {
            if (!cmtFlag) {
                transaction.rollback();
            }
        }
        return "";
    }



    /**
     * RS engine global instance.
     */
    private static ResourcingEngine syncObject = new ResourcingEngine();

    /**
     * Get the singleton instance of resourcing engine.
     * @return {@code ResourcingEngine} global unique instance.
     */
    public static ResourcingEngine GetInstance() {
        return ResourcingEngine.syncObject;
    }

    /**
     * Private constructor for preventing instance create outside.
     */
    private ResourcingEngine() { }

}
