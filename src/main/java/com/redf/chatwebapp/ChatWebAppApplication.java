package com.redf.chatwebapp;

import com.redf.chatwebapp.dao.utils.HibernateSessionFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class ChatWebAppApplication {
    public static void main(String[] args) {
        SpringApplication.run(ChatWebAppApplication.class, args);
        HibernateSessionFactory.getSessionFactory().openSession();
    }
}