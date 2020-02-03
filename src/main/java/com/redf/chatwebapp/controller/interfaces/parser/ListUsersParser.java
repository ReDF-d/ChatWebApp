package com.redf.chatwebapp.controller.interfaces.parser;

import com.redf.chatwebapp.dao.entities.UserEntity;
import com.redf.chatwebapp.dao.services.UserService;
import com.redf.chatwebapp.dao.utils.UserDetails;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.ArrayList;
import java.util.List;

public interface ListUsersParser {

    default ArrayList<UserEntity> parseUsersWithPrincipal(@NotNull List<String> users, UserService userService) {
        ArrayList<UserEntity> members = new ArrayList<>();
        users.forEach(s ->
                members.add(userService.findById(Long.parseLong(s)))
        );
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserEntity principal = userService.findById(userDetails.getId());
        members.add(principal);
        return members;
    }


    default ArrayList<UserEntity> parseUsersWithoutPrincipal(@NotNull List<String> users, UserService userService) {
        ArrayList<UserEntity> members = new ArrayList<>();
        users.forEach(s ->
                members.add(userService.findById(Long.parseLong(s)))
        );
        return members;
    }
}
