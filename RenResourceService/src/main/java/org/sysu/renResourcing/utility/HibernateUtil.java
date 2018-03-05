/*
 * Project Ren @ 2018
 * Rinkako, Ariana, Gordan. SYSU SDCS.
 */
package org.sysu.renResourcing.utility;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.sysu.renCommon.enums.LogLevelType;

/**
 * Author: Rinkako
 * Date  : 2018/1/18
 * Usage : Common methods for hibernate.
 */
@Component
public class HibernateUtil {
    /**
     * Hibernate session factory instance, thread safe.
     */
    private static SessionFactory sessionFactory;


    /**
     * session object in thread local, thread safe.
     */
    private static ThreadLocal session = new ThreadLocal();

    /**
     * Construct hibernate utility, binding session factory.
     * @param sessionFactory session factory instance
     */
    @Autowired(required = true)
    public HibernateUtil(SessionFactory sessionFactory) {
        HibernateUtil.sessionFactory = sessionFactory;
    }

    /**
     * Get session factory, SessionFactory is thread safe.
     * @return session factory instance
     */
    private static SessionFactory GetSessionFactory() {
        return HibernateUtil.sessionFactory;
    }

    /**
     * Get session for hibernate in this thread.
     * @return hibernate session instance
     */
    @SuppressWarnings("unchecked")
    public static Session GetLocalSession() {
        Session s = (Session) session.get();
        if (s == null) {
            s = HibernateUtil.GetSessionFactory().openSession();
            HibernateUtil.session.set(s);
        }
        return s;
    }

    /**
     * Close active session in this thread.
     * In normal situation, this method should not be called.
     */
    @SuppressWarnings("unchecked")
    public static void CloseLocalSession() {
        try {
            Session s = (Session) session.get();
            if (s != null) {
                if (s.isOpen()) {
                    s.close();
                }
                session.set(null);
            }
        }
        catch (Exception ex) {
            LogUtil.Echo("Close hibernate session failed, " + ex,
                    HibernateUtil.class.getName(), LogLevelType.ERROR);
        }
    }
}