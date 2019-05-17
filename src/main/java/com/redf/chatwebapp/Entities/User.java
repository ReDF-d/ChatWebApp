package com.redf.chatwebapp.Entities;

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Table;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@DynamicUpdate
@Table(appliesTo = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String login;

    private String password;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Message> messages;


    public User() {
    }


    public User(String login, String password) {
        setLogin(login);
        setPassword(password);
        messages = new ArrayList<>();
    }


    public String getLogin() {
        return login;
    }


    private void setLogin(String login) {
        this.login = login;
    }


    public String getPassword() {
        return password;
    }


    private void setPassword(String password) {
        this.password = new BCryptPasswordEncoder().encode(password);
    }


    public int getId() {
        return id;
    }


    public void addMessage(Message message) {
        messages.add(message);
    }


    public List<Message> getMessages() {
        return messages;
    }
}
