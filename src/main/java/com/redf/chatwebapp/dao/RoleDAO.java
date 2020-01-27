package com.redf.chatwebapp.dao;

import com.redf.chatwebapp.dao.entities.RoleEntity;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;


public interface RoleDAO {

    @Nullable
    @Contract(pure = true)
    RoleEntity create(String role);

    RoleEntity save(RoleEntity role);

    void update(RoleEntity role);

    void delete(RoleEntity role);

    RoleEntity createAndSave(String role);
}

