package com.redf.chatwebapp.dao.services;


import com.redf.chatwebapp.dao.UserDAOImpl;
import com.redf.chatwebapp.dao.entities.BlockedUserEntity;
import com.redf.chatwebapp.dao.entities.RoleEntity;
import com.redf.chatwebapp.dao.entities.UserEntity;
import com.redf.chatwebapp.dao.repo.BlockedUserEntityRepository;
import com.redf.chatwebapp.dao.repo.UserEntityRepository;
import com.redf.chatwebapp.dto.UserRegistrationDto;
import com.redf.chatwebapp.dto.UserUpdateDto;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import javax.inject.Singleton;
import java.util.List;

@Component
@Singleton
public class UserService implements UserDetailsService {

    private UserDAOImpl userDAO;
    private UserEntityRepository userEntityRepository;
    private BlockedUserEntityRepository blockedUserEntityRepository;


    @Contract(pure = true)
    @Autowired
    UserService(UserDAOImpl userDAO, UserEntityRepository userEntityRepository, BlockedUserEntityRepository blockedUserEntityRepository) {
        setUserDAO(userDAO);
        setUserEntityRepository(userEntityRepository);
        setBlockedUserEntityRepository(blockedUserEntityRepository);
    }


    @Contract(pure = true)
    public UserService() {
    }


    public void saveUser(@NotNull UserEntity user) {
        getUserDAO().save(user);
    }


    public void deleteUser(@NotNull UserEntity user) {
        getUserDAO().delete(user);
    }


    public void updateUser(@NotNull UserEntity user) {
        getUserDAO().update(user);
    }


    public void updateUser(@NotNull UserUpdateDto updateDto) {
        getUserDAO().update(updateDto);
    }


    public void createAndSave(@NotNull String login, @NotNull String password, @NotNull List<RoleEntity> roles, @NotNull String username) {
        getUserDAO().createAndSave(login, password, roles, username);
    }


    public void createAndSave(@NotNull UserRegistrationDto userRegistrationDto) {
        getUserDAO().createAndSave(userRegistrationDto);
    }


    @NotNull
    public UserEntity createUser(@NotNull String login, @NotNull String password, @NotNull List<RoleEntity> roles, @NotNull String username) {
        return getUserDAO().create(login, password, roles, username);
    }


    @Override
    public UserDetails loadUserByUsername(@NotNull String login) throws UsernameNotFoundException {
        UserEntity user = getUserEntityRepository().findByLogin(login);
        if (user != null) {
            if (user.getIsLocked()) {
                BlockedUserEntity blockedUser = getBlockedUserEntityRepository().findLastBlocked(user.getId());
                if ((blockedUser.getEnds().getTime() - System.currentTimeMillis()) < 0) {
                    user.setIsLocked(false);
                    updateUser(user);
                    return new com.redf.chatwebapp.dao.utils.UserDetails(user.getId(), user.getLogin(), user.getUsername(), user.getPassword().trim(), user.getRoles(), !user.getIsLocked());
                }
            } else
                return new com.redf.chatwebapp.dao.utils.UserDetails(user.getId(), user.getLogin(), user.getUsername(), user.getPassword().trim(), user.getRoles(), !user.getIsLocked());
        }
        return null;
    }


    public UserEntity findByLogin(@NotNull String login) {
        return getUserDAO().findByLogin(login);
    }


    @Contract(pure = true)
    private UserDAOImpl getUserDAO() {
        return userDAO;
    }

    public UserEntity findById(@NotNull Long id) {
        return getUserDAO().findById(id);
    }


    private void setUserDAO(UserDAOImpl userDAO) {
        this.userDAO = userDAO;
    }


    @Contract(pure = true)
    private UserEntityRepository getUserEntityRepository() {
        return userEntityRepository;
    }


    private void setUserEntityRepository(UserEntityRepository userEntityRepository) {
        this.userEntityRepository = userEntityRepository;
    }


    @Contract(pure = true)
    private BlockedUserEntityRepository getBlockedUserEntityRepository() {
        return blockedUserEntityRepository;
    }


    private void setBlockedUserEntityRepository(BlockedUserEntityRepository blockedUserEntityRepository) {
        this.blockedUserEntityRepository = blockedUserEntityRepository;
    }
}