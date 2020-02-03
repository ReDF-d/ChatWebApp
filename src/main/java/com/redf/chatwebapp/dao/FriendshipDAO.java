package com.redf.chatwebapp.dao;

import com.redf.chatwebapp.dao.entities.FriendshipEntity;
import com.redf.chatwebapp.dao.entities.UserEntity;

import java.util.ArrayList;


public interface FriendshipDAO {

    FriendshipEntity create(UserEntity firstUser, UserEntity secondUser, String status, int lastAction);

    void save(FriendshipEntity friendshipEntity);

    void update(FriendshipEntity friendshipEntity);

    void delete(FriendshipEntity friendshipEntity);

    void createAndSave(UserEntity firstUser, UserEntity secondUser, String status, int lastAction);

    ArrayList<UserEntity> getUserFriends(Long id);
}
