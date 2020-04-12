package com.redf.chatwebapp.dao.entities;

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;


@Component
@DynamicUpdate
@Entity
@Table(name = "messages")
public class MessageEntity extends AbstractEntity implements Serializable {
    private int messageId;
    private String messageText;
    private Timestamp time;
    private UserEntity user;
    private String username;
    private RoomEntity room;
    private String messageType;


    public MessageEntity(String messageText, UserEntity user, Timestamp time, RoomEntity room, String messageType) {
        setMessageText(messageText);
        setUser(user);
        setTime(time);
        setRoomEntity(room);
        setUsername(user.getUsername());
        setMessageType(messageType);
    }


    public MessageEntity() {
    }


    @Id
    @Column(name = "message_id")
    @GenericGenerator(name = "message_ids", strategy = "sequence", parameters = {
            @org.hibernate.annotations.Parameter(name = "message_ids", value = "message_ids"),
            @org.hibernate.annotations.Parameter(name = "allocationSize", value = "1"),
    })
    @GeneratedValue(generator = "message_ids", strategy = GenerationType.SEQUENCE)
    public int getMessageId() {
        return messageId;
    }


    public void setMessageId(int messageId) {
        this.messageId = messageId;
    }


    @Basic
    @Column(name = "message_text", nullable = false, length = 1024)
    public String getMessageText() {
        return messageText;
    }


    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }


    @Basic
    @Column(name = "time", nullable = false)
    public Timestamp getTime() {
        return time;
    }


    public void setTime(Timestamp time) {
        this.time = time;
    }


    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    public UserEntity getUser() {
        return user;
    }


    public void setUser(UserEntity user) {
        this.user = user;
    }


    public String getUsername() {
        return username;
    }


    public void setUsername(String username) {
        this.username = username;
    }


    @ManyToOne
    @JoinColumn(name = "room_id", referencedColumnName = "id")
    public RoomEntity getRoomEntity() {
        return room;
    }


    public void setRoomEntity(RoomEntity room) {
        this.room = room;
    }

    @Basic
    @Column(name = "message_type")
    public String getMessageType() {
        return messageType;
    }


    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }
}