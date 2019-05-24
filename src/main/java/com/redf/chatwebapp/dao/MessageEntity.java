package com.redf.chatwebapp.dao;

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.sql.Timestamp;

@DynamicUpdate
@Entity
@Table(name = "messages", schema = "public", catalog = "d3vj1afn940bj7")
public class MessageEntity {
    private int messageId;
    private String messageText;
    private Timestamp time;
    private UserEntity user;


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


    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id", referencedColumnName = "id", nullable = false)
    public UserEntity getSender() {
        return user;
    }

    public void setSender(UserEntity user) {
        this.user = user;
    }
}
