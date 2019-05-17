package com.redf.chatwebapp.Entities;


import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Table;

import javax.persistence.*;
import java.time.LocalDateTime;

@DynamicUpdate
@Table(appliesTo = "messages")
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int message_id;

    private String message_text;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private int user_id;

    private LocalDateTime time;


    public Message() {
    }


    public Message(String message_text, int user_id) {
        setMessage_text(message_text);
        setTime(LocalDateTime.now());
        setUser_id(user_id);
    }

    public String getMessage_text() {
        return message_text;
    }

    private void setMessage_text(String message_text) {
        this.message_text = message_text;
    }


    public LocalDateTime getTime() {
        return time;
    }

    private void setTime(LocalDateTime time) {
        this.time = time;
    }


    public int getUser_id() {
        return user_id;
    }

    private void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getMessage_id() {
        return message_id;
    }
}
