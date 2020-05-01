package com.redf.chatwebapp.controller;


import com.redf.chatwebapp.dao.entities.RoleEntity;
import com.redf.chatwebapp.dao.entities.UserEntity;
import com.redf.chatwebapp.dao.repo.UserEntityRepository;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/adminpanel")
public class AdminPanelController {

    private UserEntityRepository userEntityRepository;
    private ArrayList<UserEntity> users;

    @Autowired
    AdminPanelController(UserEntityRepository userEntityRepository) {
        setUserEntityRepository(userEntityRepository);
    }


    AdminPanelController() {
    }


    @Contract(pure = true)
    private UserEntityRepository getUserEntityRepository() {
        return userEntityRepository;
    }


    private void setUserEntityRepository(UserEntityRepository userEntityRepository) {
        this.userEntityRepository = userEntityRepository;
    }


    @GetMapping
    public ModelAndView getAdminPanel() {
        ModelAndView modelAndView = new ModelAndView("adminpanel");
        setUsers((ArrayList<UserEntity>) getUserEntityRepository().findAllOrderById());
        deleteRoleDuplicates(getUsers());
        modelAndView.addObject("users", getUsers());
        return modelAndView;
    }


    private void deleteRoleDuplicates(@NotNull List<UserEntity> users) {
        users.forEach(userEntity -> {
            if (userEntity.getRoles().size() > 2) {
                Set<RoleEntity> set = new HashSet<>(userEntity.getRoles());
                userEntity.getRoles().clear();
                userEntity.getRoles().addAll(set);
                getUserEntityRepository().save(userEntity);
            }
        });
    }


    @Contract(pure = true)
    private ArrayList<UserEntity> getUsers() {
        return users;
    }


    private void setUsers(ArrayList<UserEntity> users) {
        this.users = users;
    }
}
