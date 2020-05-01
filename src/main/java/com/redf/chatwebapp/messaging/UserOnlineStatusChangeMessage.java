package com.redf.chatwebapp.messaging;


import org.springframework.stereotype.Component;

@Component
public class UserOnlineStatusChangeMessage {


    private String id;
    private Status status;
    private String username;

    public String getId() {
        return id;
    }


    public void setId(String id) {
        this.id = id;
    }


    public Status getStatus() {
        return status;
    }


    public void setStatus(Status status) {
        this.status = status;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


    public enum Status {
        ONLINE,
        OFFLINE
    }
}
