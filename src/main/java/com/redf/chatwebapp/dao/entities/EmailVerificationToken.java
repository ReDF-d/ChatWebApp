package com.redf.chatwebapp.dao.entities;


import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;
import org.jetbrains.annotations.Contract;
import org.springframework.stereotype.Component;

import javax.persistence.*;


@Component
@DynamicUpdate
@Entity
@Table(name = "email_verification_tokens")
public class EmailVerificationToken {

    private Long id;
    private String token;
    private UserEntity user;


    public EmailVerificationToken(String token, UserEntity user) {
        setToken(token);
        setUser(user);
    }


    public EmailVerificationToken() {
    }


    @Contract(pure = true)
    @Id
    @Column(name = "id")
    @GenericGenerator(name = "email_verification_tokens_id_seq", strategy = "sequence", parameters = {
            @org.hibernate.annotations.Parameter(name = "email_verification_tokens_id_seq", value = "email_verification_tokens_id_seq"),
            @org.hibernate.annotations.Parameter(name = "allocationSize", value = "1"),
    })
    @GeneratedValue(generator = "email_verification_tokens_id_seq", strategy = GenerationType.SEQUENCE)
    private Long getId() {
        return id;
    }


    private void setId(Long id) {
        this.id = id;
    }


    @Contract(pure = true)
    private String getToken() {
        return token;
    }


    private void setToken(String token) {
        this.token = token;
    }

    @OneToOne(targetEntity = UserEntity.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "user_id")
    @Contract(pure = true)
    public UserEntity getUser() {
        return user;
    }


    private void setUser(UserEntity user) {
        this.user = user;
    }
}
