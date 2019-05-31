package com.redf.chatwebapp.dao.utils;

import com.redf.chatwebapp.dao.entities.UserEntity;
import com.redf.chatwebapp.dao.services.UserService;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;


public class UserDAOImpl implements UserDAO {


    public static UserEntity findByUsername(String login) {
        return HibernateSessionFactory.getSessionFactory().openSession().get(UserEntity.class, login);
    }


    @NotNull
    @Contract("_, _, _ -> new")
    public static UserEntity create(String login, String password, String role) {
        password = UserService.passwordEncoder().encode(password);
        return new UserEntity(login, password, role);
    }


    public static void createAndSave(String login, String password, String role) {
        save(create(login, password, role));
    }


    public static void save(UserEntity user) {
        Session session = HibernateSessionFactory.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        session.save(user);
        tx1.commit();
        session.close();
    }


    public static void update(UserEntity user) {
        Session session = HibernateSessionFactory.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        session.update(user);
        tx1.commit();
        session.close();
    }


    public static void delete(UserEntity user) {
        Session session = HibernateSessionFactory.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        session.delete(user);
        tx1.commit();
        session.close();
    }
}