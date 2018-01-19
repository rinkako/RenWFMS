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
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManagerFactory;

/**
 * Author: Rinkako
 * Date  : 2018/1/18
 * Usage : Common methods for hibernate.
 */
public class HibernateUtil {

    @Autowired
    private static SessionFactory sessionFactory;
    private static ThreadLocal session = new ThreadLocal();

    /*
    static {
        Configuration config = new Configuration().configure();
        StandardServiceRegistryBuilder sb = new StandardServiceRegistryBuilder().applySettings(config.getProperties());
        StandardServiceRegistry ssr = sb.build();
        HibernateUtil.sessionFactory = config.buildSessionFactory(ssr);
    }
    */

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
    public static Session GetLocalThreadSession() {
        Session s = (Session) session.get();
        if (s == null) {
            s = HibernateUtil.GetSessionFactory().getCurrentSession();
            HibernateUtil.session.set(s);
        }
        return s;
    }

    /**
     * Close active session in this thread.
     */
    @SuppressWarnings("unchecked")
    public static void CloseSession() {
        Session s = (Session) session.get();
        if (s != null) {
            session.set(null);
        }
    }
}