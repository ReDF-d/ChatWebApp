package com.redf.chatwebapp.dao.entities;


import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Component
@DynamicUpdate
@Entity
@Table(name = "rooms")
public class RoomEntity extends AbstractEntity {
    private int id;
    private String roomType;
    private List<UserEntity> roomMembers = new ArrayList<>();
    private String title;


    public RoomEntity() {
    }


    public RoomEntity(int id, String roomType, List<UserEntity> members) {
        setId(id);
        setRoomType(roomType);
        setRoomMembers(members);
    }


    @Id
    @Column(name = "id")
    @GenericGenerator(name = "rooms_id_seq", strategy = "sequence", parameters = {
            @org.hibernate.annotations.Parameter(name = "rooms_id_seq", value = "rooms_id_seq"),
            @org.hibernate.annotations.Parameter(name = "allocationSize", value = "1"),
    })
    @GeneratedValue(generator = "rooms_id_seq", strategy = GenerationType.SEQUENCE)
    public int getId() {
        return id;
    }


    public void setId(int id) {
        this.id = id;
    }


    @Column(name = "room_type")
    public String getRoomType() {
        return roomType;
    }


    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    @Column(name = "title")
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "room_members",
            joinColumns = {@JoinColumn(name = "room_id")},
            inverseJoinColumns = {@JoinColumn(name = "user_id")})
    public List<UserEntity> getRoomMembers() {
        return roomMembers;
    }


    public void setRoomMembers(List<UserEntity> roomMembers) {
        this.roomMembers = roomMembers;
    }

    public RoomEntity addRoomMember(UserEntity user) {
        getRoomMembers().add(user);
        return this;
    }


    public RoomEntity addRoomMembers(List<UserEntity> users) {
        users.forEach(this::addRoomMember);
        return this;
    }
}
