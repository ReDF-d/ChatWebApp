package com.redf.chatwebapp.dao;

import com.redf.chatwebapp.dao.utils.HibernateSessionFactory;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class UserDAOImpl implements UserDAO {


    public UserEntity findById(int id) {
        return HibernateSessionFactory.getSessionFactory().openSession().get(UserEntity.class, id);
    }


    @Override
    public UserEntity create(String login, String password) {
        return new UserEntity(login, password);
    }


    @Override
    public void save(UserEntity user) {
        Session session = HibernateSessionFactory.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        session.save(user);
        tx1.commit();
        session.close();
    }


    @Override
    public void update(UserEntity user) {
        Session session = HibernateSessionFactory.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        session.update(user);
        tx1.commit();
        session.close();
    }


    @Override
    public void delete(UserEntity user) {
        Session session = HibernateSessionFactory.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        session.delete(user);
        tx1.commit();
        session.close();
    }


    @Override
    public MessageEntity findMessageById(int id) {
        return HibernateSessionFactory.getSessionFactory().openSession().get(MessageEntity.class, id);
    }


    @Override
    @SuppressWarnings(value = "unchecked")
    public List<UserEntity> findAll() {
        Session session = HibernateSessionFactory.getSessionFactory().openSession();
        return session.createQuery("From UserEntity").list();
    }
}
