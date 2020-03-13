package com.redf.chatwebapp.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/confirmEmail")
public class EmailConfirmController {

    @GetMapping
    public String getEmailConfirmPage() {
        return "confirmEmail";
    }
}
