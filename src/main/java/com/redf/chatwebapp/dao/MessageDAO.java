package com.redf.chatwebapp.dao;

import com.redf.chatwebapp.dao.entities.MessageEntity;
import com.redf.chatwebapp.dao.entities.RoomEntity;
import javassist.NotFoundException;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

import java.sql.Timestamp;
import java.util.List;
import java.util.Set;

public interface MessageDAO {
    @Nullable
    @Contract(pure = true)
    Set<MessageEntity> findByAuthor(String login) throws NotFoundException;

    @Nullable
    @Contract(pure = true)
    MessageEntity create(String messageText, String login, String author, Timestamp time, RoomEntity room);

    void save(MessageEntity message);

    void update(MessageEntity message);

    void delete(MessageEntity message);

    void createAndSave(String author, String login, String messageText, Timestamp time, RoomEntity room);

    List<MessageEntity> getAllMessagesFromRoom(int id);
}
