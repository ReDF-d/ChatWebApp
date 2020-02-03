package com.redf.chatwebapp.dao.repo;

import com.redf.chatwebapp.dao.entities.RoomEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface RoomEntityRepository extends JpaRepository<RoomEntity, Long> {

    @Query("select r from RoomEntity r where r.id = :id")
    RoomEntity findRoomById(@Param("id") int id);


    @Query("select r from RoomEntity r join r.roomMembers rm where rm.id in (:id1, :id2) and size(r.roomMembers) = 2 and r.roomType = 'dialogue'")
    RoomEntity findDialogueByMembers(@Param("id1") Long id1, @Param("id2") Long id2);


    @Query("select r from RoomEntity r join r.roomMembers rm where rm.id = :id")
    List<RoomEntity> findRoomsByMemberId(@Param("id") Long id);
}
