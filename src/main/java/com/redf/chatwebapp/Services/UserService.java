package com.redf.chatwebapp.Services;

import com.redf.chatwebapp.DAO.UserDAOImpl;
import com.redf.chatwebapp.Entities.Message;
import com.redf.chatwebapp.Entities.User;

import java.util.List;

public class UserService {

    public static final UserService INSTANCE = new UserService();
    private UserDAOImpl usersDao = new UserDAOImpl();

    private UserService() {
        try {
            if (INSTANCE != null)
                throw new IllegalAccessException();
        } catch (IllegalAccessException e) {
            System.out.println("Attempting to create another instance of UserService");
        }
    }


    public User findUser(int id) {
        return usersDao.findById(id);
    }


    public void saveUser(User user) {
        usersDao.save(user);
    }


    public void deleteUser(User user) {
        usersDao.delete(user);
    }


    public void updateUser(User user) {
        usersDao.update(user);
    }


    public List<User> findAllUsers() {
        return usersDao.findAll();
    }


    public Message findMessageById(int messageId) {
        return usersDao.findMessageById(messageId);
    }


    public User createUser(String login, String password) {
        return usersDao.create(login, password);
    }
}
