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
@Table(name = "blocked_users")
public class BlockedUserEntity extends AbstractEntity implements Serializable {
    private Long id;
    private UserEntity user;
    private Timestamp started;
    private Timestamp ends;


    public BlockedUserEntity() {

    }


    public BlockedUserEntity(UserEntity user, Timestamp started, Timestamp ends) {
        setUser(user);
        setStarted(started);
        setEnds(ends);
    }


    @Id
    @Column(name = "id")
    @GenericGenerator(name = "blocked_users_id_seq", strategy = "sequence", parameters = {
            @org.hibernate.annotations.Parameter(name = "blocked_users_id_seq", value = "blocked_users_id_seq"),
            @org.hibernate.annotations.Parameter(name = "allocationSize", value = "1"),
    })
    @GeneratedValue(generator = "blocked_users_id_seq", strategy = GenerationType.SEQUENCE)
    Long getId() {
        return id;
    }

    void setId(Long id) {
        this.id = id;
    }


    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    public UserEntity getUser() {
        return user;
    }


    public void setUser(UserEntity user) {
        this.user = user;
    }


    public Timestamp getEnds() {
        return ends;
    }

    public void setEnds(Timestamp ends) {
        this.ends = ends;
    }

    public Timestamp getStarted() {
        return started;
    }

    public void setStarted(Timestamp started) {
        this.started = started;
    }
}
