package com.redf.chatwebapp.dao.entities;

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@DynamicUpdate
@Entity
@Table(name = "users", schema = "public", catalog = "d3vj1afn940bj7")
public class UserEntity {
    private int id;
    private String login;
    private String password;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "UserEntity")
    private Set<RoleEntity> authorities = new HashSet<>();

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
        this.password = new BCryptPasswordEncoder().encode(password);
    }


    public Set<RoleEntity> getAuthorities() {
        return authorities;
    }


    public void setAuthorities(Set<RoleEntity> authorities) {
        this.authorities = authorities;
    }
}
