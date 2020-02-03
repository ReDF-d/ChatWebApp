package com.redf.chatwebapp.controller.interfaces.viewBeautify;

import com.redf.chatwebapp.dao.entities.RoomEntity;
import com.redf.chatwebapp.dao.repo.MessageEntityRepository;

import java.util.ArrayList;

public interface RoomBeautiyfier {


    default void addRooms(ArrayList<RoomEntity> rooms, ArrayList<RoomBeautify> roomsBeautify, Long id, MessageEntityRepository messageEntityRepository) {
        rooms.forEach(roomEntity -> {
            if (roomEntity.getRoomType().equals("dialogue")) {
                roomEntity.getRoomMembers().forEach(userEntity -> {
                    if (!userEntity.getId().equals(id))
                        roomsBeautify.add(new RoomBeautify(roomEntity.getId(), userEntity.getUsername(), messageEntityRepository));
                });
            } else
                roomsBeautify.add(new RoomBeautify(roomEntity.getId(), roomEntity.getTitle(), messageEntityRepository));
        });
    }
}
