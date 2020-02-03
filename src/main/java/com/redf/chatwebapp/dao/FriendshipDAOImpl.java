package com.redf.chatwebapp.dao;

import com.redf.chatwebapp.dao.entities.FriendshipEntity;
import com.redf.chatwebapp.dao.entities.UserEntity;
import com.redf.chatwebapp.dao.repo.FriendshipEntityRepository;
import com.redf.chatwebapp.dao.services.UserService;
import com.redf.chatwebapp.dao.utils.TransactionHandler;
import com.redf.chatwebapp.dao.utils.UserDetails;
import org.jetbrains.annotations.Contract;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.ejb.Singleton;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;


@Component
@Singleton
public class FriendshipDAOImpl implements FriendshipDAO, TransactionHandler {

    private FriendshipEntityRepository friendshipEntityRepository;
    private FriendshipEntity friendshipEntity;
    private UserService userService;


    FriendshipDAOImpl() {
    }


    @Autowired
    FriendshipDAOImpl(FriendshipEntityRepository friendshipEntityRepository, FriendshipEntity friendshipEntity, UserService userService) {
        setFriendshipEntityRepository(friendshipEntityRepository);
        setFriendshipEntity(friendshipEntity);
        setUserService(userService);
    }


    @Override
    @NotNull
    public FriendshipEntity create(UserEntity firstUser, UserEntity secondUser, String status, int lastAction) {
        getFriendshipEntity().setFirstUser(firstUser);
        getFriendshipEntity().setSecondUser(secondUser);
        getFriendshipEntity().setStatus(status);
        getFriendshipEntity().setLastAction(lastAction);
        return getFriendshipEntity();
    }


    @Override
    public void createAndSave(UserEntity firstUser, UserEntity secondUser, String status, int lastAction) {
        save(create(firstUser, secondUser, status, lastAction));
    }


    @Override
    public void save(@NotNull FriendshipEntity friendshipEntity) {
        performTransaction(TransactionHandler.Transactions.SAVE, friendshipEntity);
    }


    @Override
    public void update(@NotNull FriendshipEntity friendshipEntity) {
        performTransaction(TransactionHandler.Transactions.UPDATE, friendshipEntity);
    }


    @Override
    public void delete(@NotNull FriendshipEntity friendshipEntity) {
        performTransaction(TransactionHandler.Transactions.DELETE, friendshipEntity);
    }


    @Contract(pure = true)
    private FriendshipEntityRepository getFriendshipEntityRepository() {
        return friendshipEntityRepository;
    }


    private void setFriendshipEntityRepository(FriendshipEntityRepository friendshipEntityRepository) {
        this.friendshipEntityRepository = friendshipEntityRepository;
    }


    @Override
    public ArrayList<UserEntity> getUserFriends(Long id) {
        ArrayList<UserEntity> friends = new ArrayList<>();
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserEntity principal = getUserService().findById(userDetails.getId());
        List<FriendshipEntity> friendshipEntities = getFriendshipEntityRepository().getUserFriends(id);
        friendshipEntities.forEach(friendshipEntity -> {
            if (!friendshipEntity.getFirstUser().getId().equals(principal.getId()))
                friends.add(friendshipEntity.getFirstUser());
            if (!friendshipEntity.getSecondUser().getId().equals(principal.getId()))
                friends.add(friendshipEntity.getSecondUser());
        });
        return friends;
    }


    @Contract(pure = true)
    private FriendshipEntity getFriendshipEntity() {
        return friendshipEntity;
    }


    private void setFriendshipEntity(FriendshipEntity friendshipEntity) {
        this.friendshipEntity = friendshipEntity;
    }


    @Contract(pure = true)
    private UserService getUserService() {
        return userService;
    }


    private void setUserService(UserService userService) {
        this.userService = userService;
    }
}