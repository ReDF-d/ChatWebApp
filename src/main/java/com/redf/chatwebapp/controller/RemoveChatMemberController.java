package com.redf.chatwebapp.controller;

import com.redf.chatwebapp.dao.entities.RoomEntity;
import com.redf.chatwebapp.dao.entities.UserEntity;
import com.redf.chatwebapp.dao.repo.RoomEntityRepository;
import com.redf.chatwebapp.dao.services.UserService;
import com.redf.chatwebapp.messaging.RemoveRoomMemberMessage;
import org.jetbrains.annotations.Contract;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@Transactional
@RequestMapping("/removeChatMember")
public class RemoveChatMemberController {

    private RoomEntityRepository roomEntityRepository;
    private UserService userService;
    private SimpMessagingTemplate messagingTemplate;

    RemoveChatMemberController() {
    }


    @Autowired
    RemoveChatMemberController(RoomEntityRepository roomEntityRepository, UserService userService, SimpMessagingTemplate messagingTemplate) {
        setRoomEntityRepository(roomEntityRepository);
        setUserService(userService);
        setMessagingTemplate(messagingTemplate);
    }


    @PostMapping(params = {"memberId", "roomId"})
    @ResponseBody
    public Object removeChatMember(@RequestParam(value = "memberId") String memberId, @RequestParam(value = "roomId") String roomId) {
        RoomEntity room = getRoomEntityRepository().findRoomById(Integer.parseInt(roomId));
        UserEntity member = getUserService().findById(Long.parseLong(memberId));
        if (member != null && room != null)
            getRoomEntityRepository().deleteRoomMember(member.getId(), room.getId());
        getMessagingTemplate().convertAndSend("/topic/removeRoomMember", new RemoveRoomMemberMessage(memberId, roomId));
        return null;
    }


    @Contract(pure = true)
    private RoomEntityRepository getRoomEntityRepository() {
        return roomEntityRepository;
    }


    private void setRoomEntityRepository(RoomEntityRepository roomEntityRepository) {
        this.roomEntityRepository = roomEntityRepository;
    }


    @Contract(pure = true)
    private UserService getUserService() {
        return userService;
    }


    private void setUserService(UserService userService) {
        this.userService = userService;
    }


    @Contract(pure = true)
    private SimpMessagingTemplate getMessagingTemplate() {
        return messagingTemplate;
    }


    private void setMessagingTemplate(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }
}
