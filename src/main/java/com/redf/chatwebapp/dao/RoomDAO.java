package com.redf.chatwebapp.dao;

import com.redf.chatwebapp.dao.entities.RoomEntity;
import com.redf.chatwebapp.dao.entities.UserEntity;

import java.util.List;

public interface RoomDAO {

    RoomEntity create(String room);

    RoomEntity create(String room, List<UserEntity> roomMembers, String title);

    RoomEntity save(RoomEntity room);

    void update(RoomEntity room);

    void delete(RoomEntity room);

    RoomEntity createAndSave(String room);

    RoomEntity createAndSave(String room, List<UserEntity> roomMembers, String title);
}
