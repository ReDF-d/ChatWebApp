package com.redf.chatwebapp.dao;


import com.redf.chatwebapp.dao.entities.RoomEntity;
import com.redf.chatwebapp.dao.entities.UserEntity;
import com.redf.chatwebapp.dao.utils.TransactionHandler;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.inject.Singleton;
import java.util.List;

@Component
@Singleton
public class RoomDAOImpl implements RoomDAO, TransactionHandler {
    private RoomEntity room;


    @Autowired
    @Contract(pure = true)
    RoomDAOImpl(RoomEntity room) {
        this.room = room;
    }


    @Contract(pure = true)
    RoomDAOImpl() {
    }


    @NotNull
    @Override
    public RoomEntity create(@NotNull String roomType) {
        this.room.setRoomType(roomType);
        return this.room;
    }


    @NotNull
    @Override
    public RoomEntity create(@NotNull String roomType, @NotNull List<UserEntity> roomMembers) {
        this.room.setRoomType(roomType);
        this.room.setRoomMembers(roomMembers);
        return this.room;
    }


    @Override
    public RoomEntity createAndSave(@NotNull String roomType) {
        return save(create(roomType));
    }


    @Override
    public RoomEntity createAndSave(@NotNull String roomType, @NotNull List<UserEntity> roomMembers) {
        return save(create(roomType, roomMembers));
    }


    @Override
    public RoomEntity save(@NotNull RoomEntity room) {
        performTransaction(TransactionHandler.Transactions.SAVE, room);
        return room;
    }

    @Override
    public void update(@NotNull RoomEntity room) {
        performTransaction(TransactionHandler.Transactions.UPDATE, room);
    }


    @Override
    public void delete(@NotNull RoomEntity room) {
        performTransaction(TransactionHandler.Transactions.DELETE, room);
    }
}
