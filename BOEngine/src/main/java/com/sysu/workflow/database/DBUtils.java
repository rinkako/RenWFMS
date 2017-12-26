package com.sysu.workflow.database;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

import java.sql.*;

/**
 * Created by zhengshouzi on 2015/12/12.
 */
public class DBUtils {

    private static SessionFactory sessionFactory = null;
    private static Configuration cfg = new Configuration().configure();;
    private static ServiceRegistry serviceRegistry = serviceRegistry = new StandardServiceRegistryBuilder().applySettings(cfg.getProperties()).build();;

    public static SessionFactory  getSessionFactory(){


        if (sessionFactory ==null){
            sessionFactory = cfg.buildSessionFactory(serviceRegistry);
        }

        return sessionFactory;
    }
    public static void closeSession(Session session ){
        if (session!= null){
           if (session.isOpen()){
               session.close();
           }
        }
    }




    public static Connection getMysqlConnection() {

        String driver = "com.mysql.jdbc.Driver";

        String url = "jdbc:mysql://127.0.0.1:3306/crowdsourcing";

        String user = "root";

        String password = "zhengqiang";

        Connection connection = null;
        try {
            Class.forName(driver);
            try {
                connection = DriverManager.getConnection(url, user, password);
                if (connection != null) {
                    System.out.println("连接数据库成功");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return connection;
    }


    public static void closeAll(Connection connection, PreparedStatement ps, ResultSet rs) {

        try {
            if (rs != null) {
                rs.close();
                rs = null;
            }
            if (ps != null) {
                ps.close();
                ps = null;
            }
            if (connection != null) {
                connection.close();
                connection = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
