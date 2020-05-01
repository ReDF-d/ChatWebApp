package com.redf.chatwebapp.dao.repo;

import com.redf.chatwebapp.dao.entities.MessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface MessageEntityRepository extends JpaRepository<MessageEntity, Long> {

    @Query(value = "SELECT * from messages where room_id = :id ORDER BY time asc", nativeQuery = true)
    List<MessageEntity> findAllFromRoom(@Param("id") int id);


    MessageEntity findByMessageId(int id);
}
