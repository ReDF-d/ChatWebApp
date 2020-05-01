package com.redf.chatwebapp.dao.repo;


import com.redf.chatwebapp.dao.entities.OnlineUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface OnlineUserEntityRepository extends JpaRepository<OnlineUserEntity, Long> {

    OnlineUserEntity findOnlineUserEntityById(@Param("id") Long id);


    @Query(value = "select id, is_online, last_seen from online_users join room_members on (room_members.user_id = online_users.id) where room_id = :id", nativeQuery = true)
    List<OnlineUserEntity> findAllUsersFromRoom(@Param("id") Long id);

    @Transactional
    @Modifying
    @Query(value = "update online_users set is_online = 'OFFLINE'", nativeQuery = true)
    void setAllUsersOffline();
}
