package com.redf.chatwebapp.dao;

import com.redf.chatwebapp.dao.entities.RoleEntity;
import com.redf.chatwebapp.dao.entities.UserEntity;
import com.redf.chatwebapp.dao.repo.RoleEntityRepository;
import com.redf.chatwebapp.dao.repo.UserEntityRepository;
import com.redf.chatwebapp.dao.utils.TransactionHandler;
import com.redf.chatwebapp.dto.UserRegistrationDto;
import com.redf.chatwebapp.dto.UserUpdateDto;
import com.redf.chatwebapp.security.BcryptPasswordEncoder;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Component
@Singleton
public class UserDAOImpl implements UserDAO, TransactionHandler {

    private UserEntityRepository userEntityRepository;
    private RoleEntityRepository roleEntityRepository;
    private UserEntity user;
    private BlockedUserDAOImpl blockedUserDAOImpl;


    @Contract(pure = true)
    @Autowired
    UserDAOImpl(UserEntityRepository userEntityRepository, UserEntity user, RoleEntityRepository roleEntityRepository, BlockedUserDAOImpl blockedUserDAOImpl) {
        setUserEntityRepository(userEntityRepository);
        setRoleEntityRepository(roleEntityRepository);
        setUser(user);
        setBlockedUserDAOImpl(blockedUserDAOImpl);
    }


    @Contract(pure = true)
    public UserDAOImpl() {
    }


    public UserEntity findById(@NotNull Long id) {
        return getUserEntityRepository().findById(id).orElse(null);
    }


    @NotNull
    public UserEntity create(@NotNull String login, @NotNull String password, @NotNull List<RoleEntity> roles, @NotNull String username) {
        password = BcryptPasswordEncoder.passwordEncoder().encode(password);
        getUser().setUsername(username);
        getUser().setLogin(login);
        getUser().setPassword(password);
        getUser().setRoles(roles);
        return getUser();
    }


    @Override
    public UserEntity create(@NotNull UserRegistrationDto userRegistrationDto) {
        getUser().setLogin(userRegistrationDto.getLogin());
        getUser().setPassword(BcryptPasswordEncoder.passwordEncoder().encode(userRegistrationDto.getPassword().trim()));
        getUser().setUsername(userRegistrationDto.getUsername());
        RoleEntity roleEntity = getRoleEntityRepository().findByRoleName("USER");
        List<RoleEntity> roles = new ArrayList<>();
        roles.add(roleEntity);
        getUser().setRoles(roles);
        getUser().setIsLocked(false);
        return getUser();
    }

    @Override
    public void createAndSave(@NotNull String login, @NotNull String password, @NotNull List<RoleEntity> roles, @NotNull String username) {
        performTransaction(Transactions.SAVE, create(login, password, roles, username));
    }


    @Override
    public void createAndSave(UserRegistrationDto userRegistrationDto) {
        save(create(userRegistrationDto));
    }

    @Override
    public void save(UserEntity user) {
        performTransaction(Transactions.SAVE, user);
    }


    @Override
    public void update(UserEntity user) {
        performTransaction(Transactions.UPDATE, user);
    }


    @Override
    public void update(@NotNull UserUpdateDto updateDto) {
        UserEntity userEntity;
        getUser().setId(updateDto.getId());
        getUser().setLogin(updateDto.getLogin());
        getUser().setUsername(updateDto.getUsername());
        if (!updateDto.getPassword().equals("")) {
            getUser().setPassword(BcryptPasswordEncoder.passwordEncoder().encode(updateDto.getPassword()).trim());
        } else {
            Optional<UserEntity> userOptional = getUserEntityRepository().findById(updateDto.getId());
            if (userOptional.isPresent())
                userEntity = userOptional.get();
            else
                throw new NullPointerException("User not found");
            getUser().setPassword(userEntity.getPassword());
        }
        List<RoleEntity> roles = new ArrayList<>(updateDto.getRoles());
        getUser().setRoles(roles);
        if (!getUser().getIsLocked()) {
            getUser().setIsLocked(updateDto.isMarkBanned());
            if (updateDto.isMarkBanned())
                getBlockedUserDAOImpl().createAndSave(getUser(), updateDto.getStarted(), updateDto.getEnds());
        } else
            getUser().setIsLocked(!updateDto.isMarkUnbanned());
        getUser().setEnabled(true);
        performTransaction(Transactions.UPDATE, getUser());
    }


    @Override
    public void delete(UserEntity user) {
        performTransaction(Transactions.DELETE, user);
    }


    public UserEntity findByLogin(String login) {
        return userEntityRepository.findByLogin(login);
    }

    public BlockedUserDAOImpl getBlockedUserDAOImpl() {
        return blockedUserDAOImpl;
    }

    public void setBlockedUserDAOImpl(BlockedUserDAOImpl blockedUserDAOImpl) {
        this.blockedUserDAOImpl = blockedUserDAOImpl;
    }

    public RoleEntityRepository getRoleEntityRepository() {
        return roleEntityRepository;
    }

    public void setRoleEntityRepository(RoleEntityRepository roleEntityRepository) {
        this.roleEntityRepository = roleEntityRepository;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public UserEntityRepository getUserEntityRepository() {
        return userEntityRepository;
    }

    public void setUserEntityRepository(UserEntityRepository userEntityRepository) {
        this.userEntityRepository = userEntityRepository;
    }
}