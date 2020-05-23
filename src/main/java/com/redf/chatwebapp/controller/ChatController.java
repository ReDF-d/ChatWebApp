package com.redf.chatwebapp.controller;

import com.redf.chatwebapp.controller.interfaces.viewBeautify.RoomBeautify;
import com.redf.chatwebapp.controller.interfaces.viewBeautify.RoomSanitizer;
import com.redf.chatwebapp.dao.FriendshipDAOImpl;
import com.redf.chatwebapp.dao.MessageDAOImpl;
import com.redf.chatwebapp.dao.entities.MessageEntity;
import com.redf.chatwebapp.dao.entities.RoomEntity;
import com.redf.chatwebapp.dao.entities.UserEntity;
import com.redf.chatwebapp.dao.repo.FriendshipEntityRepository;
import com.redf.chatwebapp.dao.repo.MessageEntityRepository;
import com.redf.chatwebapp.dao.repo.OnlineUserEntityRepository;
import com.redf.chatwebapp.dao.repo.RoomEntityRepository;
import com.redf.chatwebapp.dao.services.UserService;
import com.redf.chatwebapp.dao.utils.UserDetails;
import com.redf.chatwebapp.dto.ChatCreateDto;
import com.redf.chatwebapp.dto.ChatMemberDto;
import com.redf.chatwebapp.messaging.ChatMessage;
import com.redf.chatwebapp.messaging.EditChatTitleMessage;
import com.redf.chatwebapp.messaging.UserOnlineStatusChangeMessage;
import org.apache.commons.collections.CollectionUtils;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.inject.Singleton;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;


@Singleton
@Controller
@Transactional
@RequestMapping("/chat/{id}")
public class ChatController implements RoomSanitizer {

    private static boolean savePermitted = true;

    private MessageDAOImpl messageDAO;
    private RoomEntity room;
    private RoomEntityRepository roomEntityRepository;
    private ArrayList<RoomEntity> rooms;
    private FriendshipDAOImpl friendshipDAO;
    private MessageEntityRepository messageEntityRepository;
    private UserService userService;
    private List<UserEntity> onlineUsers;
    private List<UserEntity> offlineUsers;
    private FriendshipEntityRepository friendshipEntityRepository;
    private SimpMessagingTemplate messagingTemplate;
    private OnlineUserEntityRepository onlineUserEntityRepository;


    @Contract(pure = true)
    @Autowired
    public ChatController(MessageDAOImpl messageDAO, RoomEntity room, RoomEntityRepository roomEntityRepository, MessageEntityRepository messageEntityRepository,
                          FriendshipDAOImpl friendshipDAO, UserService userService, FriendshipEntityRepository friendshipEntityRepository,
                          SimpMessagingTemplate messagingTemplate, OnlineUserEntityRepository onlineUserEntityRepository) {
        setMessageDAO(messageDAO);
        setRoom(room);
        setRoomEntityRepository(roomEntityRepository);
        setMessageEntityRepository(messageEntityRepository);
        setFriendshipDao(friendshipDAO);
        setUserService(userService);
        setFriendshipEntityRepository(friendshipEntityRepository);
        setMessagingTemplate(messagingTemplate);
        setOnlineUserEntityRepository(onlineUserEntityRepository);
        getOnlineUserEntityRepository().setAllUsersOffline();
    }


    public ChatController() {
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
        messages = (ArrayList<MessageEntity>) getMessageDAO().getAllMessagesFromRoom(id);
        return messages;
    }


    @ModelAttribute("currentDate")
    public Calendar getCurrentDate() {
        return Calendar.getInstance();
    }


    @MessageMapping("/chat.editChatTitle.{id}")
    @SendTo("/topic/editChatTitle")
    public EditChatTitleMessage editChatTitle(@NotNull @Payload EditChatTitleMessage message, @DestinationVariable("id") String id) {
        RoomEntity room = getRoomEntityRepository().findRoomById(Integer.parseInt(message.getRoomId()));
        if (room.getId() != 1) {
            room.setTitle(message.getTitle());
            getRoomEntityRepository().save(room);
            return message;
        }
        return null;
    }


    @MessageMapping("/chat.saveAndSendMessage.{id}")
    @SendTo("/topic/chat/{id}")
    public ChatMessage saveAndSendMessage(@NotNull @Payload ChatMessage chatMessage, @DestinationVariable("id") String id) throws InterruptedException {
        if (chatMessage.getType().equals(ChatMessage.MessageType.CHAT)) {
            while (!savePermitted)
                Thread.sleep(1);
            savePermitted = false;
            String author = chatMessage.getSender().trim();
            String login = chatMessage.getLogin().trim();
            String messageText = chatMessage.getContent().trim();
            Timestamp timestamp = new Timestamp(System.currentTimeMillis() + 7200000);
            RoomEntity roomEntity = getRoomEntityRepository().findRoomById(Integer.parseInt(id));
            String messageType = "text";
            MessageEntity message = getMessageEntityRepository().save(getMessageDAO().create(author, login, messageText, timestamp, roomEntity, messageType));
            getMessageEntityRepository().flush();
            savePermitted = true;
            chatMessage.setMessageId(String.valueOf(message.getMessageId()));
            message = getMessageEntityRepository().findByMessageId(Integer.parseInt(chatMessage.getMessageId()));
            if (message != null)
                return chatMessage;
        }
        if (chatMessage.getType().equals(ChatMessage.MessageType.UPDATE)) {
            MessageEntity message = getMessageEntityRepository().findByMessageId(Integer.parseInt(chatMessage.getMessageId()));
            if (message != null) {
                message.setMessageText(chatMessage.getContent());
                getMessageEntityRepository().save(message);
                chatMessage.setMessageId(String.valueOf(message.getMessageId()));
                chatMessage.setContent(message.getMessageText());
                chatMessage.setTimestamp(new Timestamp(System.currentTimeMillis() + 7200000));
                return chatMessage;
            }
        }
        if (chatMessage.getType().equals(ChatMessage.MessageType.DELETE)) {
            MessageEntity message = getMessageEntityRepository().findByMessageId(Integer.parseInt(chatMessage.getMessageId()));
            if (message != null) {
                if (message.getMessageType().equals("text"))
                    getMessageEntityRepository().delete(message);
                else if (message.getMessageType().equals("image") || (message.getMessageType().equals("audio"))) {
                    File file = new File(message.getMessageText());
                    boolean del = file.delete();
                    getMessageEntityRepository().delete(message);
                }
                return chatMessage;
            }
        }
        return null;
    }


    @PostMapping(params = {"userId", "roomId"})
    public Object exitChat(@RequestParam(value = "userId") String userId, @RequestParam(value = "roomId") String roomId) {
        int parsedRoomId = Integer.parseInt(roomId.replaceAll("\\D+", ""));
        Long parsedUserId = Long.parseLong(userId);
        getRoomEntityRepository().deleteRoomMember(parsedUserId, parsedRoomId);
        if (getRoomEntityRepository().findRoomById(parsedRoomId).getRoomMembers().size() == 0)
            getRoomEntityRepository().deleteRoomEntityById(parsedRoomId);
        return null;
    }


    @PostMapping
    @ResponseBody
    public Object saveAndSendFile(MultipartHttpServletRequest request, @DestinationVariable("id") String roomId) {
        try {
            final String senderId = request.getParameter("id");
            final String timestamp = request.getParameter("timestamp");
            final String PATH = "./media/file/" + roomId;
            Timestamp s;
            File directory = new File(PATH);
            if (!directory.exists()) {
                boolean created = directory.mkdir();
            }
            Iterator<String> iterator = request.getFileNames();
            MultipartFile file;
            while (iterator.hasNext()) {
                file = request.getFile(iterator.next());
                assert file != null;
                Path pathToFile = Paths.get(PATH, file.getOriginalFilename());
                Files.write(pathToFile, file.getBytes());
                while (!savePermitted)
                    Thread.sleep(1);
                savePermitted = false;
                UserEntity user = getUserService().findById(Long.parseLong(senderId));
                RoomEntity roomEntity = getRoomEntityRepository().findRoomById(Integer.parseInt(roomId));
                ChatMessage chatMessage = new ChatMessage();
                String contentType = Files.probeContentType(pathToFile);
                switch (contentType) {
                    case "image/jpeg":
                    case "image/png":
                    case "image/gif":
                        chatMessage = createAndSaveMessageFromType(user, pathToFile, timestamp, roomEntity, senderId, roomId, "image");
                        break;
                    case "audio/mpeg":
                        chatMessage = createAndSaveMessageFromType(user, pathToFile, timestamp, roomEntity, senderId, roomId, "audio");
                        break;
                    case "video/mp4":
                    case "video/webm":
                        chatMessage = createAndSaveMessageFromType(user, pathToFile, timestamp, roomEntity, senderId, roomId, "video");
                        break;
                }
                getMessagingTemplate().convertAndSend("/topic/chat/" + roomId, chatMessage);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }


    @Nullable
    @Contract("_, _, _, _, _, _, _ -> new")
    private ChatMessage createAndSaveMessageFromType(@NotNull UserEntity user, @NotNull Path pathToFile, String timestamp, RoomEntity roomEntity, String senderId, String roomId, String type) {
        MessageEntity message = getMessageDAO().createAndSave(user.getUsername(), user.getLogin(), pathToFile.toString(),
                parseTimestampFromMilliseconds(timestamp), roomEntity, type);
        savePermitted = true;
        switch (type) {
            case "image":
                return new ChatMessage(roomId, String.valueOf(message.getMessageId()), ChatMessage.MessageType.IMAGE,
                        senderId, pathToFile.toString(), user.getUsername(), user.getLogin(), parseTimestampFromMilliseconds(timestamp));
            case "audio":
                return new ChatMessage(roomId, String.valueOf(message.getMessageId()), ChatMessage.MessageType.AUDIO,
                        senderId, pathToFile.toString(), user.getUsername(), user.getLogin(), parseTimestampFromMilliseconds(timestamp));
            case "video":
                return new ChatMessage(roomId, String.valueOf(message.getMessageId()), ChatMessage.MessageType.VIDEO,
                        senderId, pathToFile.toString(), user.getUsername(), user.getLogin(), parseTimestampFromMilliseconds(timestamp));
        }
        return null;
    }


    @NotNull
    private Timestamp parseTimestampFromMilliseconds(String timestampInString) {
        Date date = new Date(Long.parseLong(timestampInString));
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        String formatted = format.format(date);
        Timestamp ret = Timestamp.valueOf(formatted);
        ret.setTime(ret.getTime() + 7200000);
        return ret;
    }


    @MessageMapping("/chat.addUser.{id}")
    @SendTo("/topic/chat/{id}")
    public ChatMessage addUser(@NotNull @Payload ChatMessage chatMessage,
                               @NotNull SimpMessageHeaderAccessor headerAccessor, @DestinationVariable("id") String id) {
        headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
        return chatMessage;
    }


    @PatchMapping
    public ModelAndView searchMessage(@RequestParam("searchMessage") String searchString, @PathVariable String id, @ModelAttribute("currentDate") Calendar currentDate) {
        if (searchString.equals(""))
            return new ModelAndView("redirect:/chat/" + Integer.parseInt(id));
        ModelAndView modelAndView;
        ArrayList<MessageEntity> searchResult = (ArrayList<MessageEntity>) getMessageEntityRepository().findByMessageTextFromRoom(searchString, Integer.parseInt(id));
        modelAndView = buildModelAndView(id);
        modelAndView.addObject("searchResult", searchResult);
        return modelAndView;
    }


    @GetMapping
    public ModelAndView getChatPage(@PathVariable String id, @ModelAttribute("messages") ArrayList<MessageEntity> messages, @ModelAttribute("currentDate") Calendar currentDate) {
        return buildModelAndView(id);
    }


    public ModelAndView buildModelAndView(String id) {
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
            modelAndView.addObject("roomType", room.getRoomType());
            modelAndView.addObject("title", room.getTitle());
            modelAndView.addObject("members", room.getRoomMembers());
            modelAndView.addObject("owner", room.getOwner());
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


    private void setOnlineUsers(List<UserEntity> users) {
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


    private void initOnlineAndOfflineLists(int id) {
        List<UserEntity> loggedUsers = new ArrayList<>();
        List<UserEntity> offlineUsers = new ArrayList<>();
        getOnlineUserEntityRepository().findAllUsersFromRoom((long) id).forEach(o -> {
            if (o.isOnline().equals("ONLINE"))
                loggedUsers.add(o.getUser());
            else
                offlineUsers.add(o.getUser());

        });
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserEntity principal = getUserService().findById(userDetails.getId());
        if (!loggedUsers.contains(principal)) {
            loggedUsers.add(principal);
            UserOnlineStatusChangeMessage message = new UserOnlineStatusChangeMessage();
            message.setId(principal.getId().toString());
            message.setStatus(UserOnlineStatusChangeMessage.Status.ONLINE);
            message.setUsername(principal.getUsername());
            sendStatusChangeMessage(Integer.toString(id), message);
        }
        offlineUsers.remove(principal);
        setOfflineUsers(offlineUsers);
        setOnlineUsers(loggedUsers);
    }


    private void sendStatusChangeMessage(String roomId, UserOnlineStatusChangeMessage message) {
        getMessagingTemplate().convertAndSend("/topic/chat/" + roomId, message);
    }


    @Contract(pure = true)
    private UserService getUserService() {
        return userService;
    }


    private void setUserService(UserService userService) {
        this.userService = userService;
    }


    @Contract(pure = true)
    private List<UserEntity> getOfflineUsers() {
        return offlineUsers;
    }


    private void setOfflineUsers(List<UserEntity> offlineUsers) {
        this.offlineUsers = offlineUsers;
    }


    @Contract(pure = true)
    private FriendshipEntityRepository getFriendshipEntityRepository() {
        return friendshipEntityRepository;
    }

    private void setFriendshipEntityRepository(FriendshipEntityRepository friendshipEntityRepository) {
        this.friendshipEntityRepository = friendshipEntityRepository;
    }

    @Contract(pure = true)
    private SimpMessagingTemplate getMessagingTemplate() {
        return messagingTemplate;
    }

    private void setMessagingTemplate(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }


    @Contract(pure = true)
    private OnlineUserEntityRepository getOnlineUserEntityRepository() {
        return onlineUserEntityRepository;
    }

    private void setOnlineUserEntityRepository(OnlineUserEntityRepository onlineUserEntityRepository) {
        this.onlineUserEntityRepository = onlineUserEntityRepository;
    }

    @Contract(pure = true)
    private MessageDAOImpl getMessageDAO() {
        return messageDAO;
    }

    private void setMessageDAO(MessageDAOImpl messageDAO) {
        this.messageDAO = messageDAO;
    }
}