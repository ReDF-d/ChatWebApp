package com.redf.chatwebapp.dao;

import com.redf.chatwebapp.dao.entities.BlockedUserEntity;
import com.redf.chatwebapp.dao.entities.UserEntity;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

import java.sql.Timestamp;

public interface BlockedUserDAO {

    @Nullable
    @Contract(pure = true)
    BlockedUserEntity create(UserEntity user, Timestamp started, Timestamp ends);

    void save(BlockedUserEntity blockedUserEntity);

    void update(BlockedUserEntity blockedUserEntity);

    void delete(BlockedUserEntity blockedUserEntity);

    void createAndSave(UserEntity user, Timestamp started, Timestamp ends);
}
