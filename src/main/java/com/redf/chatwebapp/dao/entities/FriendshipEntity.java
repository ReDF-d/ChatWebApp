package com.redf.chatwebapp.dao.entities;


import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.stereotype.Component;

import javax.persistence.*;

@Component
@DynamicUpdate
@Entity
@Table(name = "friendship")
public class FriendshipEntity extends AbstractEntity {

    private Long id;
    private UserEntity firstUser;
    private UserEntity secondUser;
    private String status;
    private int lastAction;


    public FriendshipEntity() {

    }


    public FriendshipEntity(UserEntity firstUser, UserEntity secondUser, String status, int lastAction) {
        setFirstUser(firstUser);
        setSecondUser(secondUser);
        setStatus(status);
        setLastAction(lastAction);
    }


    @Column(name = "id", nullable = false)
    @Id
    @GenericGenerator(name = "friendship_id_seq", strategy = "sequence", parameters = {
            @org.hibernate.annotations.Parameter(name = "friendship_id_seq", value = "friendship_id_seq"),
            @org.hibernate.annotations.Parameter(name = "allocationSize", value = "1"),
    })
    @GeneratedValue(generator = "friendship_id_seq", strategy = GenerationType.SEQUENCE)
    public Long getId() {
        return id;
    }


    public void setId(Long id) {
        this.id = id;
    }


    @ManyToOne
    @JoinColumn(name = "user_id_1", referencedColumnName = "id")
    public UserEntity getFirstUser() {
        return firstUser;
    }


    public void setFirstUser(UserEntity firstUser) {
        this.firstUser = firstUser;
    }


    @ManyToOne
    @JoinColumn(name = "user_id_2", referencedColumnName = "id")
    public UserEntity getSecondUser() {
        return secondUser;
    }


    public void setSecondUser(UserEntity secondUser) {
        this.secondUser = secondUser;
    }


    public String getStatus() {
        return status;
    }


    public void setStatus(String status) {
        this.status = status;
    }

    @Column(name = "last_action")
    public int getLastAction() {
        return lastAction;
    }

    public void setLastAction(int lastAction) {
        this.lastAction = lastAction;
    }
}
