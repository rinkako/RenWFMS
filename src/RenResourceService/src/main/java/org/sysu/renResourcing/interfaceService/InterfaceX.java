/*
 * Project Ren @ 2018
 * Rinkako, Ariana, Gordan. SYSU SDCS.
 */
package org.sysu.renResourcing.interfaceService;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.sysu.renCommon.enums.FailedWorkitemStatusType;
import org.sysu.renCommon.enums.FailedWorkitemVisibilityType;
import org.sysu.renCommon.enums.RSEventType;
import org.sysu.renCommon.utility.AuthDomainHelper;
import org.sysu.renCommon.utility.TimestampUtil;
import org.sysu.renResourcing.context.WorkitemContext;
import org.sysu.renResourcing.context.steady.RenExitemEntity;
import org.sysu.renResourcing.utility.HibernateUtil;

/**
 * Author: Rinkako
 * Date  : 2018/2/9
 * Usage : Implementation of Interface X of Resource Service.
 *         Interface X is responsible for process exception handling.
 */
public class InterfaceX {

    /**
     * Signal a workitem is failed, and redirect it to its admin launcher exception workitem pool.
     *
     * @param workitem failed workitem
     * @param reason failed reason
     */
    public static void FailedRedirectToLauncherDomainPool(WorkitemContext workitem, String reason) {
        InterfaceE.WriteLog(workitem, "", RSEventType.exception_lifecycle);
        InterfaceX.RouteFailedWorkitem(workitem, reason, FailedWorkitemVisibilityType.DomainOnly);
    }

    /**
     * Signal a workitem is failed, and redirect it to BO WFMS admin exception workitem pool.
     *
     * @param workitem failed workitem
     * @param reason failed reason
     */
    public static void FailedRedirecToWFMSAdminPool(WorkitemContext workitem, String reason) {
        InterfaceE.WriteLog(workitem, "", RSEventType.exception_lifecycle);
        InterfaceX.RouteFailedWorkitem(workitem, reason, FailedWorkitemVisibilityType.DomainAndWFMSAdmin);
    }

    /**
     * Signal a workitem which principle parsing failed, and redirect it to its admin launcher exception workitem pool.
     *
     * @param workitem failed workitem
     */
    public static void PrincipleParseFailedRedirectToDomainPool(WorkitemContext workitem) {
        InterfaceE.WriteLog(workitem, "", RSEventType.exception_principle);
        InterfaceX.RouteFailedWorkitem(workitem, "Principle Parse Failed.", FailedWorkitemVisibilityType.DomainOnly);
    }

    /**
     * Signal that failed workitem is redirected to launcher offered queue.
     * @param workitem failed workitem
     * @param rtid process rtid
     */
    public static void RedirectToUnofferedQueue(WorkitemContext workitem, String rtid) {
        String handler = AuthDomainHelper.GetAuthNameByRTID(rtid);
        Session session = HibernateUtil.GetLocalSession();
        Transaction transaction = session.beginTransaction();
        try {
            RenExitemEntity ree = session.get(RenExitemEntity.class, workitem.getEntity().getWid());
            ree.setStatus(FailedWorkitemStatusType.Redo.ordinal());
            ree.setHandlerAuthName(handler);
            ree.setTimestamp(TimestampUtil.GetCurrentTimestamp());
            session.update(ree);
            transaction.commit();
        }
        catch (Exception ex) {
            transaction.rollback();
        }
        finally {
            HibernateUtil.CloseLocalSession();
        }
    }

    /**
     * Signal that failed workitem is ignored.
     * @param workitem failed workitem
     * @param rtid process rtid
     */
    public static void RedirectToIgnored(WorkitemContext workitem, String rtid) {
        String handler = AuthDomainHelper.GetAuthNameByRTID(rtid);
        Session session = HibernateUtil.GetLocalSession();
        Transaction transaction = session.beginTransaction();
        try {
            RenExitemEntity ree = session.get(RenExitemEntity.class, workitem.getEntity().getWid());
            ree.setStatus(FailedWorkitemStatusType.Ignored.ordinal());
            ree.setHandlerAuthName(handler);
            ree.setTimestamp(TimestampUtil.GetCurrentTimestamp());
            session.update(ree);
            transaction.commit();
        }
        catch (Exception ex) {
            transaction.rollback();
        }
        finally {
            HibernateUtil.CloseLocalSession();
        }
    }

    /**
     * Route a workitem to exception pool.
     *
     * @param workitem failed workitem
     * @param reason reason of failure
     */
    public static void RouteFailedWorkitem(WorkitemContext workitem, String reason, FailedWorkitemVisibilityType visibility) {
        Session session = HibernateUtil.GetLocalSession();
        Transaction transaction = session.beginTransaction();
        try {
            RenExitemEntity ree = new RenExitemEntity();
            ree.setRtid(workitem.getEntity().getRtid());
            ree.setWorkitemId(workitem.getEntity().getWid());
            ree.setReason(reason);
            ree.setStatus(FailedWorkitemStatusType.Unhandled.ordinal());
            ree.setVisibility(visibility.ordinal());
            ree.setTimestamp(TimestampUtil.GetCurrentTimestamp());
            session.saveOrUpdate(ree);
            transaction.commit();
        }
        catch (Exception ex) {
            transaction.rollback();
        }
        finally {
            HibernateUtil.CloseLocalSession();
        }
        InterfaceX.NotifyException(workitem);
    }

    /**
     * Handle fast fail of a process runtime.
     *
     * @param rtid process rtid
     */
    public static void HandleFastFail(String rtid) {
        // todo
    }

    /**
     * Notify the auth user about exception happened by notify its binding hook URL.
     *
     * @param workitem failed workitem
     */
    public static void NotifyException(WorkitemContext workitem) {
        String rtid = workitem.getEntity().getRtid();
        String launcher = AuthDomainHelper.GetAuthNameByRTID(rtid);
        // todo here do notification.
    }
}
