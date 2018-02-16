package org.sysu.workflow.restful.utility;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Author: Ariana
 * Date  : 2018/1/22
 * Usage : Common methods for hibernate.
 */
@Component
public class HibernateUtil {
    /**
     * Hibernate session factory instance, thread safe.
     */
    private static SessionFactory sessionFactory;

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
    public static Session GetLocalThreadSession() {
        return HibernateUtil.GetSessionFactory().getCurrentSession();
    }

}