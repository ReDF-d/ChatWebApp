package com.redf.chatwebapp.dao.utils;

import com.redf.chatwebapp.dao.entities.*;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.jetbrains.annotations.Contract;

import javax.inject.Singleton;
import java.util.Properties;

@Singleton
public class HibernateSessionFactory {
    private static SessionFactory sessionFactory = createHibernateSessionFactory();


    @Contract(pure = true)
    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    private static SessionFactory createHibernateSessionFactory() {
        Configuration configuration = new Configuration();
        Properties properties = new Properties();
        properties.setProperty(Environment.DRIVER, "org.postgresql.Driver");
        properties.setProperty(Environment.URL, "jdbc:postgresql://localhost:5432/blinkdb");
        properties.setProperty(Environment.USER, "root");
        properties.setProperty(Environment.PASS, "root");
        properties.setProperty(Environment.DIALECT, "org.hibernate.dialect.PostgreSQL9Dialect");
        properties.setProperty(Environment.SHOW_SQL, "false");
        properties.setProperty(Environment.CONNECTION_PROVIDER, "org.hibernate.hikaricp.internal.HikariCPConnectionProvider");
        properties.setProperty(Environment.C3P0_ACQUIRE_INCREMENT, "1");
        properties.setProperty(Environment.C3P0_IDLE_TEST_PERIOD, "60");
        properties.setProperty(Environment.C3P0_MIN_SIZE, "1");
        properties.setProperty(Environment.C3P0_MAX_SIZE, "10");
        properties.setProperty(Environment.C3P0_MAX_STATEMENTS, "15");
        properties.setProperty(Environment.CURRENT_SESSION_CONTEXT_CLASS, "thread");
        properties.setProperty(Environment.C3P0_TIMEOUT, "0");
        properties.setProperty(Environment.NON_CONTEXTUAL_LOB_CREATION, "true");
        configuration.setProperties(properties);
        configuration.addAnnotatedClass(UserEntity.class);
        configuration.addAnnotatedClass(MessageEntity.class);
        configuration.addAnnotatedClass(RoleEntity.class);
        configuration.addAnnotatedClass(BlockedUserEntity.class);
        configuration.addAnnotatedClass(FriendshipEntity.class);
        configuration.addAnnotatedClass(RoomEntity.class);
        configuration.addAnnotatedClass(EmailVerificationToken.class);
        configuration.addAnnotatedClass(OnlineUserEntity.class);
        return configuration.buildSessionFactory();
    }
}
