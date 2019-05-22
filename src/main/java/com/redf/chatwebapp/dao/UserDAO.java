package com.redf.chatwebapp.dao;


import java.util.List;

public interface UserDAO {

    UserEntity findById(int id);

    UserEntity create(String login, String password);

    void save(UserEntity user);

    void update(UserEntity user);

    void delete(UserEntity user);

    MessageEntity findMessageById(int id);

    List<UserEntity> findAll();
}
