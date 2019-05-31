package com.redf.chatwebapp.dao.entities;

import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.io.Serializable;

@DynamicUpdate
@Entity
@Table(name = "users", schema = "public", catalog = "d3vj1afn940bj7")
public class UserEntity implements Serializable {
    private String login;
    private String password;
    private RoleEntity role;

    public UserEntity() {
    }


    public UserEntity(String login, String password) {
        setLogin(login);
        setPassword(password);
    }


    @Basic
    @Column(name = "login", nullable = false, length = 64)
    @Id
    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    @Basic
    @Column(name = "password", nullable = false, length = 64)
    public String getPassword() {
        return password;
    }


    public void setPassword(String password) {
        this.password = password;
    }

    public RoleEntity getRole() {
        return role;
    }


    public void setRole(RoleEntity role) {
        this.role = role;
    }
}
