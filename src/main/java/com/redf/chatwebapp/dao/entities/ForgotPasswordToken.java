package com.redf.chatwebapp.dao.entities;

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;
import org.jetbrains.annotations.Contract;
import org.springframework.stereotype.Component;

import javax.persistence.*;

@Component
@DynamicUpdate
@Entity
@Table(name = "forgot_password_token")
public class ForgotPasswordToken {
    private Long id;
    private String token;
    private UserEntity user;


    public ForgotPasswordToken() {
    }

    public ForgotPasswordToken(String token, UserEntity user) {
        setToken(token);
        setUser(user);
    }


    @Contract(pure = true)
    @Id
    @Column(name = "id")
    @GenericGenerator(name = "forgot_password_token_id_seq", strategy = "sequence", parameters = {
            @org.hibernate.annotations.Parameter(name = "forgot_password_token_id_seq", value = "forgot_password_token_id_seq"),
            @org.hibernate.annotations.Parameter(name = "allocationSize", value = "1"),
    })
    @GeneratedValue(generator = "forgot_password_token_id_seq", strategy = GenerationType.SEQUENCE)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    @OneToOne(targetEntity = UserEntity.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "user_id")
    @Contract(pure = true)
    public UserEntity getUser() {
        return user;
    }


    public void setUser(UserEntity user) {
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
