package com.redf.chatwebapp.event;

import com.redf.chatwebapp.dao.entities.UserEntity;
import org.jetbrains.annotations.Contract;
import org.springframework.context.ApplicationEvent;

import java.util.Locale;

public class OnRegistrationCompleteEvent extends ApplicationEvent {
    private String appUrl;
    private UserEntity user;
    private Locale locale;

    public OnRegistrationCompleteEvent(UserEntity user, String appUrl, Locale locale) {
        super(user);
        setUser(user);
        setAppUrl(appUrl);
        setLocale(locale);
    }


    @Contract(pure = true)
    public String getAppUrl() {
        return appUrl;
    }


    private void setAppUrl(String appUrl) {
        this.appUrl = appUrl;
    }


    @Contract(pure = true)
    public UserEntity getUser() {
        return user;
    }

    private void setUser(UserEntity user) {
        this.user = user;
    }

    public Locale getLocale() {
        return locale;
    }

    private void setLocale(Locale locale) {
        this.locale = locale;
    }
}