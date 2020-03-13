package com.redf.chatwebapp.dao.utils;

import com.redf.chatwebapp.dao.entities.RoleEntity;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public class UserDetails implements org.springframework.security.core.userdetails.UserDetails {
    private Long id;
    private String login;
    private String password;
    private String username;
    private List<RoleEntity> roles;
    private boolean isAccountNonLocked;
    private boolean isCredentialsNonExpired;
    private boolean isAccountNonExpired;
    private boolean isEnabled;


    public UserDetails(@NotNull Long id, @NotNull String login, @NotNull String username, @NotNull String password, @NotNull List<RoleEntity> roles, boolean isLocked, boolean isEnabled) {
        setId(id);
        setLogin(login);
        setPassword(password);
        setUsername(username);
        setRoles(roles);
        setAccountNonLocked(isLocked);
        setEnabled(isEnabled);
    }


    @Override
    public boolean isAccountNonExpired() {
        return true;
    }


    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }


    @Override
    public boolean isEnabled() {
        return true;
    }


    @Override
    public boolean isAccountNonLocked() {
        return isAccountNonLocked;
    }


    private void setAccountNonLocked(boolean accountNonLocked) {
        isAccountNonLocked = accountNonLocked;
    }


    @Override
    public String getPassword() {
        return password;
    }


    public void setPassword(String password) {
        this.password = password;
    }


    public String getLogin() {
        return login;
    }


    public void setLogin(String login) {
        this.login = login;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        ArrayList<String> userRoles = new ArrayList<>();
        getRoles().forEach(r ->
                userRoles.add(r.getRole())
        );
        String[] arrayRoles = userRoles.toArray(new String[0]);
        return AuthorityUtils.createAuthorityList(arrayRoles);
    }


    @Override
    public String getUsername() {
        return username;
    }


    public void setUsername(String username) {
        this.username = username;
    }


    @Contract(pure = true)
    private List<RoleEntity> getRoles() {
        return roles;
    }


    public void setRoles(List<RoleEntity> roles) {
        this.roles = roles;
    }


    public Long getId() {
        return id;
    }


    public void setId(Long id) {
        this.id = id;
    }


    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }
}
