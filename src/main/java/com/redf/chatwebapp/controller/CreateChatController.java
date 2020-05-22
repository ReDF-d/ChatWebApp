package com.redf.chatwebapp.controller;


import com.redf.chatwebapp.controller.interfaces.TitleCreator;
import com.redf.chatwebapp.controller.interfaces.parser.ListUsersParser;
import com.redf.chatwebapp.dao.RoomDAOImpl;
import com.redf.chatwebapp.dao.entities.UserEntity;
import com.redf.chatwebapp.dao.services.UserService;
import com.redf.chatwebapp.dao.utils.UserDetails;
import com.redf.chatwebapp.dto.ChatCreateDto;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;


@Controller
@RequestMapping("/createChat")
public class CreateChatController implements ListUsersParser, TitleCreator {

    private RoomDAOImpl roomDAO;
    private UserService userService;


    @Autowired
    CreateChatController(RoomDAOImpl roomDAO, UserService userService) {
        setRoomDAO(roomDAO);
        setUserService(userService);
    }


    @ModelAttribute("createDto")
    public ChatCreateDto getChatDto() {
        return new ChatCreateDto();
    }


    @PostMapping
    public String createChat(@NotNull @ModelAttribute("createDto") ChatCreateDto chatDto) {
        String id;
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserEntity owner = getUserService().findById(userDetails.getId());
        ArrayList<String> userList = new ArrayList<>();
        if (chatDto.getUsers() == null)
            chatDto.setUsers(userList);
        if (chatDto.getTitle() != null && !chatDto.getTitle().equals(""))
            id = String.valueOf(getRoomDAO().createAndSave("group", parseUsersWithPrincipal(chatDto.getUsers(), getUserService()), chatDto.getTitle(), owner).getId());
        else {
            ArrayList<UserEntity> users = parseUsersWithPrincipal(chatDto.getUsers(), getUserService());
            id = String.valueOf(getRoomDAO().createAndSave("group", parseUsersWithPrincipal(chatDto.getUsers(), getUserService()), createTitle(users), owner).getId());
        }
        return "redirect:/chat/" + id;
    }


    @Contract(pure = true)
    private RoomDAOImpl getRoomDAO() {
        return roomDAO;
    }


    private void setRoomDAO(RoomDAOImpl roomDAO) {
        this.roomDAO = roomDAO;
    }


    @Contract(pure = true)
    private UserService getUserService() {
        return userService;
    }


    private void setUserService(UserService userService) {
        this.userService = userService;
    }
}
