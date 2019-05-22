package com.redf.chatwebapp.init;

import com.redf.chatwebapp.dao.services.UserService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ChatWebAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(ChatWebAppApplication.class, args);
        UserService.getInstance().saveUser(UserService.getInstance().createUser("root", "root"));
    }

}