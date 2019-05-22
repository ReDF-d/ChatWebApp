package com.redf.chatwebapp.dao;

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;
import org.jetbrains.annotations.Contract;

import javax.persistence.*;

@DynamicUpdate
@Entity
@Table(name = "users", schema = "public", catalog = "chatdb")
public class UserEntity {
    private int id;
    private String login;
    private String password;


    public UserEntity() {
    }


    public UserEntity(String login, String password) {
        setLogin(login);
        setPassword(password);
    }

    @Id
    @Column(name = "id", nullable = false)
    @GenericGenerator(name = "user_ids", strategy = "sequence", parameters = {
            @org.hibernate.annotations.Parameter(name = "user_ids", value = "user_ids"),
            @org.hibernate.annotations.Parameter(name = "allocationSize", value = "1"),
    })
    @GeneratedValue(generator = "user_ids", strategy = GenerationType.SEQUENCE)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "login", nullable = false, length = 64)
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

    @Contract(value = "null -> false", pure = true)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserEntity that = (UserEntity) o;

        if (id != that.id) return false;
        if (login != null ? !login.equals(that.login) : that.login != null) return false;
        return password != null ? password.equals(that.password) : that.password == null;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (login != null ? login.hashCode() : 0);
        result = 31 * result + (password != null ? password.hashCode() : 0);
        return result;
    }
}
