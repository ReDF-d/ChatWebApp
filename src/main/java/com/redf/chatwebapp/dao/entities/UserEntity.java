package com.redf.chatwebapp.dao.entities;

import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@DynamicUpdate
@Entity
@Table(name = "users", schema = "public", catalog = "d3vj1afn940bj7")
public class UserEntity implements Serializable {
    private String login;
    private String password;
    private String role;

    public UserEntity() {
    }


    public UserEntity(String login, String password, String role) {
        setLogin(login);
        setPassword(password);
        setRole(role);
    }



    @Column(name = "login", nullable = false, length = 64)
    @Id
    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }


    @Column(name = "password", nullable = false, length = 128)
    public String getPassword() {
        return password;
    }


    public void setPassword(String password) {
        this.password = password;
    }

    @Column(name = "role", nullable = false)
    public String getRole() {
        return role;
    }


    public void setRole(String role) {
        this.role = role;
    }
}
