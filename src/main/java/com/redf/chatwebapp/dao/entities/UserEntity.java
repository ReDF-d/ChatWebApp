package com.redf.chatwebapp.dao.entities;

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.GenericGenerator;
import org.jetbrains.annotations.Contract;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Component
@DynamicUpdate
@Entity
@Table(name = "users")
public class UserEntity extends AbstractEntity implements Serializable {


    private Long id;
    private String login;
    private String password;
    private String username;
    private boolean isLocked;
    private List<RoleEntity> roles = new ArrayList<>();
    private Set<MessageEntity> messages = new HashSet<>();
    private Set<RoomEntity> rooms = new HashSet<>();
    private boolean enabled;
    private EmailVerificationToken emailVerificationToken;
    private OnlineUserEntity onlineUserEntity;


    @Contract(pure = true)
    public UserEntity() {
    }


    public UserEntity(String login, String password, String username, List<RoleEntity> roles) {
        setLogin(login);
        setPassword(password);
        setUsername(username);
        setRoles(roles);
        setEnabled(false);
    }


    @Column(name = "id", nullable = false)
    @Id
    @GenericGenerator(name = "user_ids", strategy = "sequence", parameters = {
            @org.hibernate.annotations.Parameter(name = "user_ids", value = "user_ids"),
            @org.hibernate.annotations.Parameter(name = "allocationSize", value = "1"),
    })
    @GeneratedValue(generator = "user_ids", strategy = GenerationType.SEQUENCE)
    public Long getId() {
        return id;
    }


    public void setId(Long id) {
        this.id = id;
    }


    @Column(name = "login", nullable = false, length = 64)
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


    @Column(name = "username", nullable = false)
    public String getUsername() {
        return username;
    }


    public void setUsername(String username) {
        this.username = username;
    }


    @Column(name = "locked")
    public boolean getIsLocked() {
        return isLocked;
    }


    public void setIsLocked(boolean isLocked) {
        this.isLocked = isLocked;
    }


    @OneToMany(mappedBy = "user")
    @Fetch(FetchMode.JOIN)
    public Set<MessageEntity> getMessages() {
        return messages;
    }


    public void setMessages(Set<MessageEntity> messages) {
        this.messages = messages;
    }


    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles",
            joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "role_id", referencedColumnName = "id")})
    public List<RoleEntity> getRoles() {
        return roles;
    }


    public void setRoles(List<RoleEntity> roles) {
        this.roles = roles;
    }


    @ManyToMany(mappedBy = "roomMembers")
    public Set<RoomEntity> getRooms() {
        return rooms;
    }


    public void setRooms(Set<RoomEntity> rooms) {
        this.rooms = rooms;
    }


    @Column(name = "enabled")
    public boolean isEnabled() {
        return enabled;
    }


    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }


    @OneToOne(mappedBy = "user")
    public EmailVerificationToken getEmailVerificationToken() {
        return emailVerificationToken;
    }


    public void setEmailVerificationToken(EmailVerificationToken emailVerificationToken) {
        this.emailVerificationToken = emailVerificationToken;
    }


    @OneToOne(cascade = CascadeType.ALL, mappedBy = "user")
    public OnlineUserEntity getOnlineUserEntity() {
        return onlineUserEntity;
    }


    public void setOnlineUserEntity(OnlineUserEntity onlineUserEntity) {
        this.onlineUserEntity = onlineUserEntity;
    }
}