package com.redf.chatwebapp.init;

import com.redf.chatwebapp.dao.UserEntity;
import com.redf.chatwebapp.dao.services.UserService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class ChatWebAppApplication {
    public static void main(String[] args) {
        SpringApplication.run(ChatWebAppApplication.class, args);
        UserEntity user = UserService.getInstance().createUser("root", "root");
        UserService.getInstance().saveUser(user);
        System.out.println("test");
    }


    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(ChatWebAppApplication.class);
    }

}