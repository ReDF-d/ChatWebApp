package com.redf.chatwebapp.messaging;

import org.springframework.stereotype.Component;

import java.sql.Timestamp;

@Component
public class ChatMessage {
    private String roomId;
    private MessageType type;
    private String id;
    private String content;
    private String sender;
    private String login;
    private Timestamp timestamp;
    private String messageId;

    public ChatMessage() {
    }


    public ChatMessage(String roomId, String messageId, MessageType type, String id, String content, String sender, String login, Timestamp timestamp) {
        setRoomId(roomId);
        setType(type);
        setId(id);
        setContent(content);
        setSender(sender);
        setLogin(login);
        setTimestamp(timestamp);
        setMessageId(messageId);
    }


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

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public enum MessageType {
        CHAT,
        IMAGE,
        UPDATE,
        DELETE
    }
}
