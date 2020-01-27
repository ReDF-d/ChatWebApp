package com.redf.chatwebapp.config;

import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
public class MvcConfig implements WebMvcConfigurer {

    @Override
    public void addViewControllers(@NotNull ViewControllerRegistry registry) {
        registry.addViewController("/home").setViewName("home");
        registry.addRedirectViewController("/", "home");
        registry.addViewController("/login").setViewName("login");
        registry.addViewController("/signup").setViewName("signup");
        registry.addViewController("/chat/{id}").setViewName("chat");
        registry.addViewController("/terms").setViewName("terms");
        registry.addViewController("/logout").setViewName("logout");
        registry.addViewController("/user/{user}").setViewName("profilepage");
        registry.addViewController("/user/edit").setViewName("editprofile");
        registry.addViewController("/adminpanel").setViewName("adminpanel");
        registry.addViewController("/adminpanel/edituser/{user}").setViewName("adminedituser");
        registry.addViewController("/404").setViewName("404");
    }


    @Override
    public void addResourceHandlers(final ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/media/**").addResourceLocations("file:media/");
    }
}
