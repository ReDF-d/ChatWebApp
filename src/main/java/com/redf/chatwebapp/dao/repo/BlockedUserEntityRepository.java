package com.redf.chatwebapp.dao.repo;

import com.redf.chatwebapp.dao.entities.BlockedUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BlockedUserEntityRepository extends JpaRepository<BlockedUserEntity, Long> {
    @Query(value = "SELECT * from blocked_users where user_id = :id ORDER BY id desc limit 1", nativeQuery = true)
    BlockedUserEntity findLastBlocked(@Param("id") Long id);
}
