package com.redf.chatwebapp.dao.utils;


import com.redf.chatwebapp.dao.entities.UserEntity;

public interface UserDAO {

    UserEntity findByUsername(String login);

    UserEntity create(String login, String password);

    void save(UserEntity user);

    void update(UserEntity user);

    void delete(UserEntity user);
}
