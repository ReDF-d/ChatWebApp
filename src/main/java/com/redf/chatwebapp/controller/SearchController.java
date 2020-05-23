package com.redf.chatwebapp.controller;


import com.redf.chatwebapp.dao.entities.UserEntity;
import com.redf.chatwebapp.dao.repo.UserEntityRepository;
import org.jetbrains.annotations.Contract;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;

@Controller
@RequestMapping("/search")
public class SearchController {
    private UserEntityRepository userEntityRepository;



    @Autowired
    SearchController(UserEntityRepository userEntityRepository) {
        setUserEntityRepository(userEntityRepository);
    }

    SearchController() {
    }

    @PostMapping
    public ModelAndView searchUsers(@RequestParam(value = "searchUsers", required = false) String searchString) {
        ModelAndView modelAndView = new ModelAndView("searchUsers");
        ArrayList<UserEntity> searchResult = ((ArrayList<UserEntity>) getUserEntityRepository().findByUsernameContainingIgnoreCase(searchString));
        if (searchResult.size() == 1)
            return new ModelAndView("redirect:/user/" + searchResult.get(0).getId());
        modelAndView.addObject("searchResult", searchResult);
        modelAndView.addObject("searchString", searchString);
        return modelAndView;
    }


    @Contract(pure = true)
    private UserEntityRepository getUserEntityRepository() {
        return userEntityRepository;
    }


    private void setUserEntityRepository(UserEntityRepository userEntityRepository) {
        this.userEntityRepository = userEntityRepository;
    }

}
