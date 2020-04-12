package com.redf.chatwebapp.dao.repo;

import com.redf.chatwebapp.dao.entities.FriendshipEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FriendshipEntityRepository extends JpaRepository<FriendshipEntity, Long> {

    @Query("SELECT f from FriendshipEntity f where (f.firstUser.id=:id and f.secondUser.id=:id2) or (f.firstUser.id=:id2 and f.secondUser.id=:id)")
    FriendshipEntity findById(@Param("id") Long id, @Param("id2") Long id2);


    @Query("select f from FriendshipEntity f where (f.firstUser.id=:id or f.secondUser.id=:id) and f.status='friends'")
    List<FriendshipEntity> getUserFriends(@Param("id") Long id);

    @Query("select f from FriendshipEntity f where (f.firstUser.id=:id or f.secondUser.id=:id) and f.status='pending' and f.lastAction<>:id")
    List<FriendshipEntity> getUserFriendRequests(@Param("id") Long id);
}