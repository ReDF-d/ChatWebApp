package com.redf.chatwebapp.dao.utils;

import com.redf.chatwebapp.dao.MessageEntity;
import com.redf.chatwebapp.dao.UserEntity;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;

import java.util.Properties;


public class HibernateSessionFactory {
    private static Properties properties = new Properties();
    private static SessionFactory sessionFactory;
    private static Configuration configuration = new Configuration();

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            try {
                properties.setProperty(Environment.DRIVER, "org.postgresql.Driver");
                properties.setProperty(Environment.URL, "jdbc:postgresql://localhost:5432/chatdb");
                properties.setProperty(Environment.USER, "root");
                properties.setProperty(Environment.PASS, "root");
                properties.setProperty(Environment.DIALECT, "org.hibernate.dialect.PostgreSQL9Dialect");
                properties.setProperty(Environment.SHOW_SQL, "true");
                configuration.setProperties(properties);
                configuration.addAnnotatedClass(UserEntity.class);
                configuration.addAnnotatedClass(MessageEntity.class);
                sessionFactory = configuration.buildSessionFactory();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return sessionFactory;
    }


    public static void shutdown() {
        sessionFactory.getCurrentSession().close();
    }
}