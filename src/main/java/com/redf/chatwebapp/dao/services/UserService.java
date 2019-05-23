package com.redf.chatwebapp.dao.services;

import com.redf.chatwebapp.dao.MessageEntity;
import com.redf.chatwebapp.dao.UserDAOImpl;
import com.redf.chatwebapp.dao.UserEntity;
import org.jetbrains.annotations.Contract;

import java.util.List;

public class UserService {

    private static final UserService INSTANCE = new UserService();
    private UserDAOImpl usersDao = new UserDAOImpl();


    private UserService() {
        try {
            if (INSTANCE != null)
                throw new IllegalAccessException();
        } catch (IllegalAccessException e) {
            System.out.println("Attempting to create another instance of UserService");
        }
    }


    @Contract(pure = true)
    public static UserService getInstance() {
        return INSTANCE;
    }

    public UserEntity findUser(int id) {
        return usersDao.findById(id);
    }

    public void saveUser(UserEntity user) {
        usersDao.save(user);
    }

    public void deleteUser(UserEntity user) {
        usersDao.delete(user);
    }

    public void updateUser(UserEntity user) {
        usersDao.update(user);
    }

    public List<UserEntity> findAllUsers() {
        return usersDao.findAll();
    }

    public MessageEntity findMessageById(int messageId) {
        return usersDao.findMessageById(messageId);
    }

    public UserEntity createUser(String login, String password) {
        return usersDao.create(login, password);
    }
}
