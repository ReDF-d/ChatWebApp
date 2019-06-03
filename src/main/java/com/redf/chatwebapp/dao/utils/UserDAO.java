package com.redf.chatwebapp.dao.utils;


import com.redf.chatwebapp.dao.entities.UserEntity;
import com.redf.chatwebapp.dto.UserRegistrationDto;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

public interface UserDAO {

    @Nullable
    @Contract(pure = true)
    static UserEntity findByUsername(String login) {
        return null;
    }

    @Nullable
    @Contract(pure = true)
    static UserEntity create(String login, String password, String role) {
        return null;
    }

    @Nullable
    @Contract(pure = true)
    static UserEntity create(UserRegistrationDto userRegistrationDto) {
        return null;
    }

    static void save(UserEntity user) {
    }

    static void update(UserEntity user) {
    }

    static void delete(UserEntity user) {
    }

    static void createAndSave(String login, String password, String role) {
    }

    static void createAndSave(UserRegistrationDto userRegistrationDto) {

    }
}