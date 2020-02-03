package com.redf.chatwebapp.dto;

import java.util.ArrayList;


public class ChatCreateDto {

    private String title;
    private ArrayList<String> users;


    public String getTitle() {
        return title;
    }


    public void setTitle(String title) {
        this.title = title;
    }


    public ArrayList<String> getUsers() {
        return users;
    }


    public void setUsers(ArrayList<String> users) {
        this.users = users;
    }
}
