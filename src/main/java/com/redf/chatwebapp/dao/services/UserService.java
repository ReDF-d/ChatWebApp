package com.redf.chatwebapp.dao.services;

import com.redf.chatwebapp.dao.entities.UserEntity;
import com.redf.chatwebapp.dao.utils.UserDAOImpl;
import org.jetbrains.annotations.Contract;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;


public class UserService implements UserDetailsService {

    private static final UserService INSTANCE = new UserService();
    private UserDAOImpl usersDao = new UserDAOImpl();


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
        usersDao.save(user);
    }

    public void deleteUser(UserEntity user) {
        usersDao.delete(user);
    }

    public void updateUser(UserEntity user) {
        usersDao.update(user);
    }

    public UserEntity createUser(String login, String password) {
        return usersDao.create(login, password);
    }


    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        UserEntity user = usersDao.findByUsername(login);
        User.UserBuilder builder = null;
        if (user != null) {

            builder = org.springframework.security.core.userdetails.User.withUsername(login);
            builder.password(user.getPassword());
            String role = user.getRole().getAuthority();
            builder.authorities(role);
        } else {
            throw new UsernameNotFoundException("User not found.");
        }
        return builder.build();
    }
}