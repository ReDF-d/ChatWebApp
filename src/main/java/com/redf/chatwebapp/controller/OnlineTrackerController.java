package com.redf.chatwebapp.controller;

import com.redf.chatwebapp.dao.OnlineUserEntityDAOImpl;
import com.redf.chatwebapp.dao.entities.OnlineUserEntity;
import com.redf.chatwebapp.dao.repo.OnlineUserEntityRepository;
import com.redf.chatwebapp.messaging.UserOnlineStatusChangeMessage;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.inject.Singleton;
import java.util.Calendar;

@Singleton
@Controller
@RequestMapping("/onlineTracker")
public class OnlineTrackerController {

    private OnlineUserEntityRepository onlineUserEntityRepository;
    private OnlineUserEntityDAOImpl onlineUserEntityDAO;
    private UserOnlineStatusChangeMessage userOnlineStatusChangeMessage;
    private boolean savePermitted = true;

    public OnlineTrackerController() {
    }


    @Autowired
    public OnlineTrackerController(OnlineUserEntityRepository onlineUserEntityRepository, UserOnlineStatusChangeMessage userOnlineStatusChangeMessage, OnlineUserEntityDAOImpl onlineUserEntityDAO) {
        setOnlineUserEntityRepository(onlineUserEntityRepository);
        setUserOnlineStatusChangeMessage(userOnlineStatusChangeMessage);
        setOnlineUserEntityDAO(onlineUserEntityDAO);
    }


    @MessageMapping("/onlineTracker.userConnected")
    @SendTo("/topic/onlineTracker")
    @ResponseBody
    public UserOnlineStatusChangeMessage setUserOnline(@NotNull @Payload UserOnlineStatusChangeMessage message) {
        return message;
    }


    @MessageMapping("/onlineTracker.userDisconnected")
    @SendTo("/topic/onlineTracker")
    @ResponseBody
    public UserOnlineStatusChangeMessage setUserOffline(@NotNull @Payload UserOnlineStatusChangeMessage message) throws InterruptedException {
        Long parsedId = Long.parseLong(message.getId());
        Thread.sleep(5500);
        OnlineUserEntity currentUser = getOnlineUserEntityRepository().findOnlineUserEntityById(parsedId);
        if (!currentUser.isOnline().equals("ONLINE")) {
            Thread.sleep(500);
            if (!currentUser.isOnline().equals("ONLINE")) {
                return message;
            }
        }
        return null;
    }


    @MessageMapping("/onlineTracker.saveUserStatus")
    public void saveUserStatus(@NotNull @Payload UserOnlineStatusChangeMessage message) throws InterruptedException {
        Long parsedId = Long.parseLong(message.getId());
        OnlineUserEntity currentUser;
        if (getOnlineUserEntityRepository().findOnlineUserEntityById(parsedId) != null) {
            currentUser = getOnlineUserEntityRepository().findOnlineUserEntityById(parsedId);
            currentUser.setLastSeen(Calendar.getInstance().getTimeInMillis());
            currentUser.setOnline(message.getStatus().toString());
            while (!savePermitted)
                Thread.sleep(1);
            savePermitted = false;
            getOnlineUserEntityDAO().update(currentUser);
            savePermitted = true;
        } else {
            while (!savePermitted)
                Thread.sleep(1);
            savePermitted = false;
            getOnlineUserEntityDAO().createAndSave(parsedId, message.getStatus().toString(), Calendar.getInstance().getTimeInMillis());
            savePermitted = true;
        }
    }


    @Contract(pure = true)
    private OnlineUserEntityRepository getOnlineUserEntityRepository() {
        return onlineUserEntityRepository;
    }


    private void setOnlineUserEntityRepository(OnlineUserEntityRepository onlineUserEntityRepository) {
        this.onlineUserEntityRepository = onlineUserEntityRepository;
    }


    @Contract(pure = true)
    private OnlineUserEntityDAOImpl getOnlineUserEntityDAO() {
        return onlineUserEntityDAO;
    }


    private void setOnlineUserEntityDAO(OnlineUserEntityDAOImpl onlineUserEntityDAO) {
        this.onlineUserEntityDAO = onlineUserEntityDAO;
    }


    @Contract(pure = true)
    private UserOnlineStatusChangeMessage getUserOnlineStatusChangeMessage() {
        return userOnlineStatusChangeMessage;
    }


    private void setUserOnlineStatusChangeMessage(UserOnlineStatusChangeMessage userOnlineStatusChangeMessage) {
        this.userOnlineStatusChangeMessage = userOnlineStatusChangeMessage;
    }
}
