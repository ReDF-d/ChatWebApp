package com.redf.chatwebapp.dao;

import com.redf.chatwebapp.dao.entities.MessageEntity;
import com.redf.chatwebapp.dao.entities.RoomEntity;
import com.redf.chatwebapp.dao.entities.UserEntity;
import com.redf.chatwebapp.dao.repo.MessageEntityRepository;
import com.redf.chatwebapp.dao.services.UserService;
import com.redf.chatwebapp.dao.utils.TransactionHandler;
import javassist.NotFoundException;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Singleton;
import java.sql.Timestamp;
import java.util.List;
import java.util.Set;


@Component
@Singleton
public class MessageDAOImpl implements MessageDAO, TransactionHandler {

    private UserService userService;
    private MessageEntity message;
    private MessageEntityRepository messageEntityRepository;


    @Contract(pure = true)
    @Autowired
    MessageDAOImpl(UserService userService, MessageEntity message, MessageEntityRepository messageEntityRepository) {
        setUserService(userService);
        setMessage(message);
        setMessageEntityRepository(messageEntityRepository);
    }


    MessageDAOImpl() {
    }


    @Override
    public Set<MessageEntity> findByAuthor(@NotNull String login) throws NotFoundException {
        UserEntity user = getUserService().findByLogin(login);
        if (user != null)
            return user.getMessages();
        else
            throw new NotFoundException("User not found");
    }


    @NotNull
    @Override
    public MessageEntity create(@NotNull String author, @NotNull String login, @NotNull String messageText, @NotNull Timestamp time, @NotNull RoomEntity room, @NotNull String messageType) {
        UserEntity authorEntity = userService.findByLogin(login);
        getMessage().setUsername(author);
        getMessage().setMessageText(messageText);
        getMessage().setTime(time);
        getMessage().setUser(authorEntity);
        getMessage().setRoomEntity(room);
        getMessage().setMessageType(messageType);
        return getMessage();
    }


    @Override
    public MessageEntity createAndSave(@NotNull String author, @NotNull String login, @NotNull String messageText, @NotNull Timestamp time, @NotNull RoomEntity room, @NotNull String messageType) {
        return save(create(author, login, messageText, time, room, messageType));
    }


    @Override
    public MessageEntity save(@NotNull MessageEntity message) {
        performTransaction(Transactions.SAVE, message);
        return message;
    }


    @Override
    public void update(@NotNull MessageEntity message) {
        performTransaction(Transactions.UPDATE, message);
    }


    @Override
    public void delete(@NotNull MessageEntity message) {
        performTransaction(Transactions.DELETE, message);
    }


    @Override
    @Transactional(readOnly = true)
    public List<MessageEntity> getAllMessagesFromRoom(int id) {
        return getMessageEntityRepository().findAllFromRoom(id);
    }

    @Contract(pure = true)
    private MessageEntityRepository getMessageEntityRepository() {
        return messageEntityRepository;
    }

    private void setMessageEntityRepository(MessageEntityRepository messageEntityRepository) {
        this.messageEntityRepository = messageEntityRepository;
    }

    @Contract(pure = true)
    private MessageEntity getMessage() {
        return message;
    }

    private void setMessage(MessageEntity message) {
        this.message = message;
    }

    @Contract(pure = true)
    private UserService getUserService() {
        return userService;
    }

    private void setUserService(UserService userService) {
        this.userService = userService;
    }
}