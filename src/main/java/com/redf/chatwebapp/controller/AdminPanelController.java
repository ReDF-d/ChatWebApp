package com.redf.chatwebapp.controller;


import com.redf.chatwebapp.dao.entities.UserEntity;
import com.redf.chatwebapp.dao.repo.UserEntityRepository;
import org.jetbrains.annotations.Contract;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;

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
        modelAndView.addObject("users", getUsers());
        return modelAndView;
    }


    @Contract(pure = true)
    private ArrayList<UserEntity> getUsers() {
        return users;
    }


    private void setUsers(ArrayList<UserEntity> users) {
        this.users = users;
    }
}
