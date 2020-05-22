package com.redf.chatwebapp.dao;

import com.redf.chatwebapp.dao.entities.RoomEntity;
import com.redf.chatwebapp.dao.entities.UserEntity;

import java.util.List;

public interface RoomDAO {

    RoomEntity create(String room, UserEntity owner);

    RoomEntity create(String room, List<UserEntity> roomMembers, String title, UserEntity owner);

    RoomEntity save(RoomEntity room);

    void update(RoomEntity room);

    void delete(RoomEntity room);

    RoomEntity createAndSave(String room, UserEntity owner);

    RoomEntity createAndSave(String room, List<UserEntity> roomMembers, String title, UserEntity owner);
}
