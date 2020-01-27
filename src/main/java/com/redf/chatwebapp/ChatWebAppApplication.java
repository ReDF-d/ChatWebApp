package com.redf.chatwebapp;

import com.redf.chatwebapp.config.MultipartResolverConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@EnableJpaRepositories(basePackages = "com.redf.chatwebapp.dao")
@SpringBootApplication
@ComponentScan(basePackages = {"com.redf"}, excludeFilters = {@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = {MultipartResolverConfiguration.class})})
public class ChatWebAppApplication {
    public static void main(String[] args) {
        SpringApplication.run(ChatWebAppApplication.class, args);
    }

}