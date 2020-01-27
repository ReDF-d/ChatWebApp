package com.redf.chatwebapp.dao;


import com.redf.chatwebapp.dao.entities.RoleEntity;
import com.redf.chatwebapp.dao.entities.UserEntity;
import com.redf.chatwebapp.dto.UserRegistrationDto;
import com.redf.chatwebapp.dto.UserUpdateDto;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface UserDAO {

    UserEntity findById(Long id);


    @Nullable
    @Contract(pure = true)
    UserEntity create(String login, String password, List<RoleEntity> roles, String username);

    @Nullable
    @Contract(pure = true)
    UserEntity create(UserRegistrationDto userRegistrationDto);

    void save(UserEntity user);

    void update(UserEntity user);

    void update(UserUpdateDto userUpdateDto);

    void delete(UserEntity user);

    void createAndSave(String login, String password, List<RoleEntity> roles, String username);

    void createAndSave(UserRegistrationDto userRegistrationDto);
}