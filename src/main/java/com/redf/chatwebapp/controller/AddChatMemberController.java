package com.redf.chatwebapp.controller;


import com.redf.chatwebapp.controller.interfaces.TitleCreator;
import com.redf.chatwebapp.controller.interfaces.parser.ListUsersParser;
import com.redf.chatwebapp.dao.RoomDAOImpl;
import com.redf.chatwebapp.dao.entities.RoomEntity;
import com.redf.chatwebapp.dao.entities.UserEntity;
import com.redf.chatwebapp.dao.repo.RoomEntityRepository;
import com.redf.chatwebapp.dao.services.UserService;
import com.redf.chatwebapp.dto.ChatMemberDto;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;

@Controller
@RequestMapping("/addChatMember/{id}")
public class AddChatMemberController implements ListUsersParser, TitleCreator {

    private RoomDAOImpl roomDAO;
    private UserService userService;
    private RoomEntityRepository roomEntityRepository;


    @Autowired
    AddChatMemberController(RoomDAOImpl roomDAO, UserService userService, RoomEntityRepository roomEntityRepository) {
        setRoomDAO(roomDAO);
        setUserService(userService);
        setRoomEntityRepository(roomEntityRepository);
    }


    @ModelAttribute("chatMemberDto")
    public ChatMemberDto getChatMemberDto() {
        return new ChatMemberDto();
    }


    @Contract(pure = true)
    private UserService getUserService() {
        return userService;
    }

    private void setUserService(UserService userService) {
        this.userService = userService;
    }


    @PostMapping
    public String createChat(@NotNull @ModelAttribute("chatMemberDto") ChatMemberDto chatDto, @PathVariable String id) {
        RoomEntity room = getRoomEntityRepository().findRoomById(Integer.parseInt(id));
        ArrayList<UserEntity> users = parseUsersWithoutPrincipal(chatDto.getUsers(), getUserService());
        String titleBefore = createTitle(room.getRoomMembers());
        if (titleBefore.equals(room.getTitle()))
            room.setTitle(titleBefore + ", " + createTitle(users));
        getRoomEntityRepository().save(room.addRoomMembers(users));
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
    private RoomEntityRepository getRoomEntityRepository() {
        return roomEntityRepository;
    }


    private void setRoomEntityRepository(RoomEntityRepository roomEntityRepository) {
        this.roomEntityRepository = roomEntityRepository;
    }

}
