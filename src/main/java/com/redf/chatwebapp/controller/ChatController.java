package com.redf.chatwebapp.controller;

import com.redf.chatwebapp.controller.interfaces.viewBeautify.RoomBeautify;
import com.redf.chatwebapp.controller.interfaces.viewBeautify.RoomBeautiyfier;
import com.redf.chatwebapp.dao.FriendshipDAOImpl;
import com.redf.chatwebapp.dao.MessageDAOImpl;
import com.redf.chatwebapp.dao.entities.MessageEntity;
import com.redf.chatwebapp.dao.entities.RoomEntity;
import com.redf.chatwebapp.dao.entities.UserEntity;
import com.redf.chatwebapp.dao.repo.FriendshipEntityRepository;
import com.redf.chatwebapp.dao.repo.MessageEntityRepository;
import com.redf.chatwebapp.dao.repo.RoomEntityRepository;
import com.redf.chatwebapp.dao.services.UserService;
import com.redf.chatwebapp.dao.utils.UserDetails;
import com.redf.chatwebapp.dto.ChatCreateDto;
import com.redf.chatwebapp.dto.ChatMemberDto;
import com.redf.chatwebapp.messaging.ChatMessage;
import org.apache.commons.collections.CollectionUtils;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


@Controller
@Transactional
@RequestMapping("/chat/{id}")
public class ChatController implements RoomBeautiyfier {

    private static boolean savePermitted = true;

    private MessageDAOImpl messageDAO;
    private RoomEntity room;
    private RoomEntityRepository roomEntityRepository;
    private ArrayList<RoomEntity> rooms;
    private FriendshipDAOImpl friendshipDAO;
    private MessageEntityRepository messageEntityRepository;
    private SessionRegistry sessionRegistry;
    private UserService userService;
    private ArrayList<UserEntity> onlineUsers;
    private ArrayList<UserEntity> offlineUsers;
    private FriendshipEntityRepository friendshipEntityRepository;


    @Contract(pure = true)
    @Autowired
    public ChatController(MessageDAOImpl messageDAO, RoomEntity room, RoomEntityRepository roomEntityRepository, MessageEntityRepository messageEntityRepository, FriendshipDAOImpl friendshipDAO, UserService userService, SessionRegistry sessionRegistry, FriendshipEntityRepository friendshipEntityRepository) {
        this.messageDAO = messageDAO;
        setRoom(room);
        setRoomEntityRepository(roomEntityRepository);
        setMessageEntityRepository(messageEntityRepository);
        setFriendshipDao(friendshipDAO);
        setUserService(userService);
        setSessionRegistry(sessionRegistry);
        setFriendshipEntityRepository(friendshipEntityRepository);
    }


    @MessageMapping("/chat.sendMessage.{id}")
    @SendTo("/topic/chat/{id}")
    public ChatMessage sendMessage(@Payload ChatMessage chatMessage, @DestinationVariable("id") String id) {
        return chatMessage;
    }


    @ModelAttribute("chatMemberDto")
    public ChatMemberDto getChatMemberDto() {
        return new ChatMemberDto();
    }


    @ModelAttribute("createDto")
    public ChatCreateDto getChatDto() {
        return new ChatCreateDto();
    }


    @ModelAttribute("messages")
    public ArrayList<MessageEntity> getChatHistory(@PathVariable int id) {
        ArrayList<MessageEntity> messages;
        messages = (ArrayList<MessageEntity>) messageDAO.getAllMessagesFromRoom(id);
        return messages;
    }


    @ModelAttribute("currentDate")
    public Calendar getCurrentDate() {
        return Calendar.getInstance();
    }


    @MessageMapping("/chat.saveMessage.{id}")
    public void saveMessage(@NotNull @Payload ChatMessage chatMessage, @DestinationVariable("id") String id) throws InterruptedException {
        if (chatMessage.getType().equals(ChatMessage.MessageType.CHAT)) {
            while (!savePermitted)
                Thread.sleep(1);
            savePermitted = false;
            String author = chatMessage.getSender().trim();
            String login = chatMessage.getLogin().trim();
            String messageText = chatMessage.getContent().trim();
            Timestamp timestamp = chatMessage.getTimestamp();
            RoomEntity roomEntity = getRoomEntityRepository().findRoomById(Integer.parseInt(id));
            messageDAO.createAndSave(author, login, messageText, timestamp, roomEntity);
            savePermitted = true;
        }
    }


    @MessageMapping("/chat.addUser.{id}")
    @SendTo("/topic/chat/{id}")
    public ChatMessage addUser(@NotNull @Payload ChatMessage chatMessage,
                               @NotNull SimpMessageHeaderAccessor headerAccessor, @DestinationVariable("id") String id) {
        headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
        return chatMessage;
    }


    @GetMapping
    public ModelAndView getChatPage(@PathVariable String id, @ModelAttribute("messages") ArrayList<MessageEntity> messages, @ModelAttribute("currentDate") Calendar currentDate) {
        RoomEntity room = getRoomEntityRepository().findRoomById(Integer.parseInt(id));
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (room != null && checkMembership(room, userDetails.getId())) {
            setRoom(room);
            ArrayList<RoomBeautify> roomsBeautify = new ArrayList<>();
            setRooms((ArrayList<RoomEntity>) getRoomEntityRepository().findRoomsByMemberId(userDetails.getId()));
            addRooms(getRooms(), roomsBeautify, userDetails.getId(), getMessageEntityRepository());
            initOnlineAndOfflineLists(Integer.parseInt(id));
            ModelAndView modelAndView = new ModelAndView("chat");
            modelAndView.addObject("roomId", id);
            modelAndView.addObject("friends", getFriendshipDAO().getUserFriends(userDetails.getId(), getFriendshipEntityRepository().getUserFriends(userDetails.getId())));
            modelAndView.addObject("friendsToAdd", getFriendsToAddWithoutMembership(getFriendshipDAO().getUserFriends(userDetails.getId(), getFriendshipEntityRepository().getUserFriends(userDetails.getId())), getRoom()));
            modelAndView.addObject("chats", roomsBeautify);
            modelAndView.addObject("onlineUsers", getOnlineUsers());
            modelAndView.addObject("offlineUsers", getOfflineUsers());
            return modelAndView;
        } else
            return new ModelAndView("redirect:/home");
    }


    public RoomEntity getRoom() {
        return room;
    }


    private void setRoom(RoomEntity room) {
        this.room = room;
    }


    @Contract(pure = true)
    private List<UserEntity> getOnlineUsers() {
        return onlineUsers;
    }


    private void setOnlineUsers(ArrayList<UserEntity> users) {
        this.onlineUsers = users;
    }


    @Contract(pure = true)
    private RoomEntityRepository getRoomEntityRepository() {
        return roomEntityRepository;
    }


    private void setRoomEntityRepository(RoomEntityRepository roomEntityRepository) {
        this.roomEntityRepository = roomEntityRepository;
    }


    public boolean checkMembership(@NotNull RoomEntity room, Long id) {
        List<UserEntity> users = room.getRoomMembers();
        for (UserEntity user : users) {
            if (user.getId().equals(id)) {
                return true;
            }
        }
        return false;
    }


    @SuppressWarnings("unchecked")
    private ArrayList<UserEntity> getFriendsToAddWithoutMembership(ArrayList<UserEntity> friends, @NotNull RoomEntity room) {
        return (ArrayList<UserEntity>) CollectionUtils.subtract(friends, room.getRoomMembers());
    }


    public ArrayList<RoomEntity> getRooms() {
        return rooms;
    }


    public void setRooms(ArrayList<RoomEntity> rooms) {
        this.rooms = rooms;
    }


    @Contract(pure = true)
    private MessageEntityRepository getMessageEntityRepository() {
        return messageEntityRepository;
    }


    private void setMessageEntityRepository(MessageEntityRepository messageEntityRepository) {
        this.messageEntityRepository = messageEntityRepository;
    }


    @Contract(pure = true)
    private FriendshipDAOImpl getFriendshipDAO() {
        return friendshipDAO;
    }


    private void setFriendshipDao(FriendshipDAOImpl friendshipDao) {
        this.friendshipDAO = friendshipDao;
    }


    @SuppressWarnings(value = "unchecked")
    private void initOnlineAndOfflineLists(int id) {
        List<UserEntity> members = getRoomEntityRepository().findRoomById(id).getRoomMembers();
        List<Object> principals = getSessionRegistry().getAllPrincipals();
        List<UserEntity> loggedUsers = new ArrayList<>();
        principals.forEach(p -> {
            if (p instanceof UserDetails)
                loggedUsers.add(getUserService().findById(((UserDetails) p).getId()));
        });
        ArrayList<UserEntity> offlineUsers = (ArrayList<UserEntity>) CollectionUtils.subtract(members, loggedUsers);
        setOfflineUsers(offlineUsers);
        setOnlineUsers((ArrayList<UserEntity>) CollectionUtils.subtract(members, offlineUsers));
    }


    @Contract(pure = true)
    private SessionRegistry getSessionRegistry() {
        return sessionRegistry;
    }


    private void setSessionRegistry(SessionRegistry sessionRegistry) {
        this.sessionRegistry = sessionRegistry;
    }


    @Contract(pure = true)
    private UserService getUserService() {
        return userService;
    }


    private void setUserService(UserService userService) {
        this.userService = userService;
    }


    @Contract(pure = true)
    private ArrayList<UserEntity> getOfflineUsers() {
        return offlineUsers;
    }


    private void setOfflineUsers(ArrayList<UserEntity> offlineUsers) {
        this.offlineUsers = offlineUsers;
    }


    @Contract(pure = true)
    private FriendshipEntityRepository getFriendshipEntityRepository() {
        return friendshipEntityRepository;
    }

    private void setFriendshipEntityRepository(FriendshipEntityRepository friendshipEntityRepository) {
        this.friendshipEntityRepository = friendshipEntityRepository;
    }
}