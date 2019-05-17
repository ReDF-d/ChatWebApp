package com.redf.chatwebapp.DAO;

import com.redf.chatwebapp.Entities.Message;
import com.redf.chatwebapp.Entities.User;

import java.util.List;

public interface UserDAO {

    User findById(int id);

    User create(String login, String password);

    void save(User user);

    void update(User user);

    void delete(User user);

    Message findMessageById(int id);

    List<User> findAll();
}
