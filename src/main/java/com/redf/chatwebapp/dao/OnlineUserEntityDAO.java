package com.redf.chatwebapp.dao;

import com.redf.chatwebapp.dao.entities.OnlineUserEntity;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;


public interface OnlineUserEntityDAO {

    @Nullable
    @Contract(pure = true)
    OnlineUserEntity create(Long id, String isOnline, long lastSeen);

    OnlineUserEntity save(OnlineUserEntity userEntity);

    void update(OnlineUserEntity userEntity);

    void delete(OnlineUserEntity userEntity);

    OnlineUserEntity createAndSave(Long id, String isOnline, long lastSeen);
}
