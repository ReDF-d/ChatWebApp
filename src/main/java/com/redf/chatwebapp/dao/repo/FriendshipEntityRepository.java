package com.redf.chatwebapp.dao.repo;

import com.redf.chatwebapp.dao.entities.FriendshipEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface FriendshipEntityRepository extends JpaRepository<FriendshipEntity, Long> {

    //    @Query(value = "SELECT * from blocked_users where user_id = :id ORDER BY id desc limit 1", nativeQuery = true)
    @Query("SELECT f from FriendshipEntity f where f.firstUser.id=:id and f.secondUser.id=:id2 or f.firstUser.id=:id2 and f.secondUser.id=:id")
    FriendshipEntity findById(@Param("id") Long id, @Param("id2") Long id2);
}