/*
 * Project Ren @ 2018
 * Rinkako, Arianna, Gordan. SYSU SDCS.
 */
package org.sysu.renNameService.utility;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import java.util.HashSet;

/**
 * Author: Rinkako
 * Date  : 2018/1/18
 * Usage : Common methods for hibernate.
 */
public final class HibernateUtil {
    static {
        Configuration config = new Configuration().configure();
        StandardServiceRegistryBuilder sb = new StandardServiceRegistryBuilder().applySettings(config.getProperties());
        StandardServiceRegistry ssr = sb.build();
        sessionFactory = config.buildSessionFactory(ssr);
    }

    /**
     * Thread safe session factory
     */
    private static SessionFactory sessionFactory;

    /**
     * Maintain active session
     */
    private static HashSet<Session> activeSessionSet;

    /**
     * Get session factory, SessionFactory is thread safe.
     * @return session factory instance
     */
    public static synchronized SessionFactory GetSessionFactory() {
        return HibernateUtil.sessionFactory;
    }

    /**
     * Open a new session for hibernate and this session should be closed by caller.
     * @return hibernate session instance
     */
    public static synchronized Session OpenSession() {
        Session retSession = HibernateUtil.sessionFactory.openSession();
        HibernateUtil.activeSessionSet.add(retSession);
        return retSession;
    }

    /**
     * Close a active session.
     * @param session hibernate session
     */
    public static synchronized void CloseSession(Session session) {
        if (null != session) {
            session.close();
            HibernateUtil.activeSessionSet.remove(session);
        }
    }

    /**
     * Dispose all remaining session.
     * @return remaining session count
     */
    public static synchronized int DisposeAllRemainSession() {
        int retCount = HibernateUtil.activeSessionSet.size();
        for (Session s: HibernateUtil.activeSessionSet) {
            s.close();
        }
        HibernateUtil.activeSessionSet.clear();
        return retCount;
    }
}
