package com.redf.chatwebapp.messaging;

import org.springframework.stereotype.Component;

import java.sql.Timestamp;

@Component
public class ChatMessage {
    private MessageType type;
    private String id;
    private String content;
    private String sender;
    private String login;
    private Timestamp timestamp;


    public MessageType getType() {
        return type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }


    public void setContent(String content) {
        this.content = content;
    }


    public String getSender() {
        return sender;
    }


    public void setSender(String sender) {
        this.sender = sender;
    }


    public Timestamp getTimestamp() {
        return timestamp;
    }


    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public enum MessageType {
        CHAT,
        JOIN,
        LEAVE
    }
}
