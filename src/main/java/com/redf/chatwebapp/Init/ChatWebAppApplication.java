package com.redf.chatwebapp.Init;

import com.redf.chatwebapp.Services.UserService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ChatWebAppApplication {

    public static void main(String[] args) {
        System.setProperty("com.sun.xml.bind.v2.bytecode.ClassTailor.noOptimize", "true");
        SpringApplication.run(ChatWebAppApplication.class, args);
        UserService.INSTANCE.saveUser(UserService.INSTANCE.createUser("root", "root"));
    }

}