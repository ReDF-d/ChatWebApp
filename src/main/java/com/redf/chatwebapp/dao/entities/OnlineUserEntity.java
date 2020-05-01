package com.redf.chatwebapp.dao.entities;


import org.hibernate.annotations.DynamicUpdate;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.io.Serializable;

@Component
@DynamicUpdate
@Entity
@Table(name = "online_users")
public class OnlineUserEntity extends AbstractEntity implements Serializable {

    private Long id;
    private UserEntity user;
    private String isOnline;
    private long lastSeen;


    public OnlineUserEntity() {
    }


    public OnlineUserEntity(UserEntity user, String isOnline, long lastSeen) {
        setUser(user);
        setOnline(isOnline);
        setLastSeen(lastSeen);
    }


    @MapsId
    @OneToOne(targetEntity = UserEntity.class, fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "id", referencedColumnName = "id")
    public UserEntity getUser() {
        return user;
    }


    public void setUser(UserEntity user) {
        this.user = user;
    }


    @Column(name = "is_online")
    public String isOnline() {
        return isOnline;
    }


    public void setOnline(String online) {
        isOnline = online;
    }

    @Column(name = "last_seen")
    public long getLastSeen() {
        return lastSeen;
    }


    public void setLastSeen(long lastSeen) {
        this.lastSeen = lastSeen;
    }

    @Id
    @Column(name = "id", unique = true, nullable = false)
    public Long getId() {
        return id;
    }


    public void setId(Long id) {
        this.id = id;
    }
}
