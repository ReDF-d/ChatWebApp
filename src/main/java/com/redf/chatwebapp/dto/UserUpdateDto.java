package com.redf.chatwebapp.dto;


import com.redf.chatwebapp.dao.entities.RoleEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Timestamp;
import java.util.List;

@Component
@FieldMatch.List({
        @FieldMatch(first = "password", second = "confirmPassword", message = "Пароли не совпадают"),
})
public class UserUpdateDto {

    private Long id;
    private String username;
    private String oldPassword;
    private String password;
    private String login;
    private String confirmPassword;
    private List<RoleEntity> roles;
    private boolean makeAdmin;
    private MultipartFile avatar;
    private boolean markBanned;
    private boolean markUnbanned;
    private Timestamp started;
    private Timestamp ends;
    private String dateTimeLocal;
    private String status;

    public UserUpdateDto() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<RoleEntity> getRoles() {
        return roles;
    }

    public void setRoles(List<RoleEntity> roles) {
        this.roles = roles;
    }

    public boolean isMakeAdmin() {
        return makeAdmin;
    }

    public void setMakeAdmin(boolean makeAdmin) {
        this.makeAdmin = makeAdmin;
    }

    public MultipartFile getAvatar() {
        return avatar;
    }

    public void setAvatar(MultipartFile avatar) {
        this.avatar = avatar;
    }

    public boolean isMarkBanned() {
        return markBanned;
    }

    public void setMarkBanned(boolean markBanned) {
        this.markBanned = markBanned;
    }

    public boolean isMarkUnbanned() {
        return markUnbanned;
    }

    public void setMarkUnbanned(boolean markUnbanned) {
        this.markUnbanned = markUnbanned;
    }

    public Timestamp getStarted() {
        return started;
    }

    public void setStarted(Timestamp started) {
        this.started = started;
    }

    public Timestamp getEnds() {
        return ends;
    }

    public void setEnds(Timestamp ends) {
        this.ends = ends;
    }

    public String getDateTimeLocal() {
        return dateTimeLocal;
    }

    public void setDateTimeLocal(String dateTimeLocal) {
        this.dateTimeLocal = dateTimeLocal;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
