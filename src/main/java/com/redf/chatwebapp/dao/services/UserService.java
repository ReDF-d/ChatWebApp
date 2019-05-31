package com.redf.chatwebapp.dao.services;


import com.redf.chatwebapp.dao.entities.UserEntity;
import com.redf.chatwebapp.dao.utils.UserDAOImpl;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;


public class UserService implements UserDetailsService {

    private static final UserService INSTANCE = new UserService();

    @NotNull
    @Contract(" -> new")
    @Bean
    public static BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    private UserService() {
        try {
            if (INSTANCE != null)
                throw new IllegalAccessException();
        } catch (IllegalAccessException e) {
            System.out.println("Attempting to create another instance of UserService");
        }
    }


    @Contract(pure = true)
    public static UserService getInstance() {
        return INSTANCE;
    }

    public void saveUser(UserEntity user) {
        UserDAOImpl.save(user);
    }


    public void deleteUser(UserEntity user) {
        UserDAOImpl.delete(user);
    }

    public void updateUser(UserEntity user) {
        UserDAOImpl.update(user);
    }

    public void createAndSave(String login, String password, String role) {
        UserDAOImpl.createAndSave(login, password, role);
    }

    @NotNull
    public UserEntity createUser(String login, String password, String role) {
        return UserDAOImpl.create(login, password, role);
    }

    @Transactional(readOnly = true)
    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        UserEntity user = UserDAOImpl.findByUsername(login);
        if (user != null) {
            return
                    User.builder()
                            .username(login.trim())
                            .password(user.getPassword().trim())
                            .authorities(user.getRole().trim()).build();
        } else {
            throw new UsernameNotFoundException("User not found.");
        }
    }
}