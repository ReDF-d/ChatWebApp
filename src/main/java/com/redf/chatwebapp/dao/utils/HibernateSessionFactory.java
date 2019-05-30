package com.redf.chatwebapp.dao.utils;

import com.redf.chatwebapp.dao.entities.MessageEntity;
import com.redf.chatwebapp.dao.entities.RoleEntity;
import com.redf.chatwebapp.dao.entities.UserEntity;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;

import java.util.Properties;


public class HibernateSessionFactory {
    private static Properties properties = new Properties();
    private static SessionFactory sessionFactory;
    private static Configuration configuration = new Configuration();

    static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            try {
                properties.setProperty(Environment.DRIVER, "org.postgresql.Driver");
                properties.setProperty(Environment.URL, "jdbc:postgresql://ec2-46-137-188-105.eu-west-1.compute.amazonaws.com:5432/d3vj1afn940bj7?sslmode=require");
                properties.setProperty(Environment.USER, "pmuvtffdkpbtan");
                properties.setProperty(Environment.PASS, "18494d4206cb8473befbbcdf0bbbad8ad187642e63450ac83c9bd35284988c14");
                properties.setProperty(Environment.DIALECT, "org.hibernate.dialect.PostgreSQL9Dialect");
                properties.setProperty(Environment.SHOW_SQL, "true");
                configuration.setProperties(properties);
                configuration.addAnnotatedClass(UserEntity.class);
                configuration.addAnnotatedClass(MessageEntity.class);
                configuration.addAnnotatedClass(RoleEntity.class);
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