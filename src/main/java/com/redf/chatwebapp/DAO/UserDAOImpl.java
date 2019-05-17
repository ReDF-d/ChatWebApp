package com.redf.chatwebapp.DAO;

import com.redf.chatwebapp.Entities.Message;
import com.redf.chatwebapp.Entities.User;
import com.redf.chatwebapp.Utils.HibernateSessionFactory;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class UserDAOImpl implements UserDAO {


    public User findById(int id) {
        return HibernateSessionFactory.getSessionFactory().openSession().get(User.class, id);
    }


    @Override
    public User create(String login, String password) {
        return new User(login, password);
    }


    @Override
    public void save(User user) {
        Session session = HibernateSessionFactory.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        session.save(user);
        tx1.commit();
        session.close();
    }


    @Override
    public void update(User user) {
        Session session = HibernateSessionFactory.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        session.update(user);
        tx1.commit();
        session.close();
    }


    @Override
    public void delete(User user) {
        Session session = HibernateSessionFactory.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        session.delete(user);
        tx1.commit();
        session.close();
    }


    @Override
    public Message findMessageById(int id) {
        return HibernateSessionFactory.getSessionFactory().openSession().get(Message.class, id);
    }


    @Override
    public List<User> findAll() {
        return (List<User>) HibernateSessionFactory.getSessionFactory().openSession().createQuery("From User").list();
    }
}
