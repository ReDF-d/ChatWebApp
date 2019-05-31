package com.redf.chatwebapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/signup")
public class SignupController {
    public String singup() {
        return "/singup";
    }
}
