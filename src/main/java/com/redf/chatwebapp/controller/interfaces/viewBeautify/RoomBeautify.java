package com.redf.chatwebapp.controller.interfaces.viewBeautify;

import com.redf.chatwebapp.dao.entities.MessageEntity;
import com.redf.chatwebapp.dao.repo.MessageEntityRepository;
import org.jetbrains.annotations.Contract;

import java.util.ArrayList;


public class RoomBeautify {

    private long id;
    private String title;
    private MessageEntity lastMessage;
    private MessageEntityRepository messageEntityRepository;


    public RoomBeautify(long id, String title, MessageEntityRepository messageEntityRepository) {
        setMessageEntityRepository(messageEntityRepository);
        setId(id);
        setTitle(title);
        ArrayList<MessageEntity> messages = (ArrayList<MessageEntity>) getMessageEntityRepository().findAllFromRoom((int) id);
        if (messages.size() != 0)
            setLastMessage(messages.get(messages.size() - 1));
        else
            setLastMessage(null);
    }

    @Contract(pure = true)
    public long getId() {
        return id;
    }

    private void setId(long id) {
        this.id = id;
    }

    @Contract(pure = true)
    public String getTitle() {
        return title;
    }

    private void setTitle(String title) {
        this.title = title;
    }

    public MessageEntity getLastMessage() {
        return lastMessage;
    }


    private void setLastMessage(MessageEntity lastMessage) {
        this.lastMessage = lastMessage;
    }


    @Contract(pure = true)
    private MessageEntityRepository getMessageEntityRepository() {
        return messageEntityRepository;
    }


    private void setMessageEntityRepository(MessageEntityRepository messageEntityRepository) {
        this.messageEntityRepository = messageEntityRepository;
    }
}
