package com.redf.chatwebapp.controller;

import com.redf.chatwebapp.dao.entities.UserEntity;
import com.redf.chatwebapp.dao.services.UserService;
import com.redf.chatwebapp.dao.utils.UserDAOImpl;
import com.redf.chatwebapp.dto.UserRegistrationDto;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;


@Controller
@RequestMapping("/signup")
public class SignupController {


    @ModelAttribute("user")
    public UserRegistrationDto userRegistrationDto() {
        return new UserRegistrationDto();
    }

    @GetMapping
    public String signup() {
        return "signup";
    }

    @PostMapping
    public String registerUserAccount(@ModelAttribute("user") @Valid UserRegistrationDto userDto,
                                      BindingResult result) {

        UserEntity existing = UserDAOImpl.findByUsername(userDto.getLogin());
        if (existing != null)
            result.rejectValue("login", null, "Sorry, but there is already an account existing with that login");

        if (result.hasErrors()) {
            return "signup";
        }

        register(userDto);

        return "redirect:chat";
    }


    private void register(UserRegistrationDto userRegistrationDto) {
        UserService.getInstance().createAndSave(userRegistrationDto);
    }
}
