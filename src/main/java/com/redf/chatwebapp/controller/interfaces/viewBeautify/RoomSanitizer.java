package com.redf.chatwebapp.controller.interfaces.viewBeautify;

import com.redf.chatwebapp.dao.entities.RoomEntity;
import com.redf.chatwebapp.dao.repo.MessageEntityRepository;

import java.util.ArrayList;
import java.util.List;

public interface RoomSanitizer {


    default void addRooms(ArrayList<RoomEntity> rooms, List<RoomBeautify> roomsBeautify, Long id, MessageEntityRepository messageEntityRepository) {
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
