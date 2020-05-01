package com.redf.chatwebapp.dao;

import com.redf.chatwebapp.dao.entities.RoleEntity;
import com.redf.chatwebapp.dao.utils.TransactionHandler;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.inject.Singleton;


@Component
@Singleton
public class RoleDAOImpl implements RoleDAO, TransactionHandler {

    private RoleEntity role;


    @Autowired
    @Contract(pure = true)
    RoleDAOImpl(RoleEntity role) {
        setRole(role);
    }


    @Contract(pure = true)
    RoleDAOImpl() {
    }


    @NotNull
    @Override
    public RoleEntity create(@NotNull String role) {
        getRole().setRole(role);
        return getRole();
    }

    @Override
    public RoleEntity createAndSave(@NotNull String role) {
        return save(create(role));
    }


    @Override
    public RoleEntity save(@NotNull RoleEntity role) {
        performTransaction(Transactions.SAVE, role);
        return role;
    }

    @Override
    public void update(@NotNull RoleEntity role) {
        performTransaction(Transactions.UPDATE, role);
    }


    @Override
    public void delete(RoleEntity role) {
        performTransaction(Transactions.DELETE, role);
    }


    public RoleEntity getRole() {
        return role;
    }


    public void setRole(RoleEntity role) {
        this.role = role;
    }
}

