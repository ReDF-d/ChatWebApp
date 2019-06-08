package com.redf.chatwebapp.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
public class MvcConfig implements WebMvcConfigurer {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/home").setViewName("home");
        registry.addRedirectViewController("/", "home");
        registry.addViewController("/login").setViewName("login");
        registry.addViewController("/signup").setViewName("signup");
        registry.addViewController("/chat").setViewName("chat");
        registry.addViewController("/terms").setViewName("tou");
        registry.addViewController("/logout").setViewName("logout");
    }
}
