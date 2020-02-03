package com.redf.chatwebapp.controller.interfaces;

import com.redf.chatwebapp.dao.entities.UserEntity;

import java.util.List;


public interface TitleCreator {
    default String createTitle(List<UserEntity> users) {
        String title = "";
        for (int i = 0; i < users.size(); i++) {
            if ((i + 1) == users.size())
                title = title.concat(users.get(i).getUsername());
            else
                title = title.concat(users.get(i).getUsername() + ", ");
        }
        return title;
    }
}
