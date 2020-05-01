package com.redf.chatwebapp.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/forgotPasswordEmailSent")
public class ForgotPasswordEmailSentController {

    @GetMapping
    public String getForgotPasswordEmailSentPage() {
        return "forgotPasswordEmailSent";
    }
}
