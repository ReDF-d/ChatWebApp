package com.redf.chatwebapp.messaging;

public class Message {
    private String content;

    public Message(String content) {
        setContent(content);
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}
