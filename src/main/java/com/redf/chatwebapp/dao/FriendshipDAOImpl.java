package com.redf.chatwebapp.dao;

import com.redf.chatwebapp.dao.entities.FriendshipEntity;
import com.redf.chatwebapp.dao.entities.UserEntity;
import com.redf.chatwebapp.dao.repo.FriendshipEntityRepository;
import com.redf.chatwebapp.dao.utils.TransactionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ejb.Singleton;
import javax.validation.constraints.NotNull;


@Component
@Singleton
public class FriendshipDAOImpl implements FriendshipDAO, TransactionHandler {

    private FriendshipEntityRepository friendshipEntityRepository;
    private FriendshipEntity friendshipEntity;


    FriendshipDAOImpl() {
    }


    @Autowired
    FriendshipDAOImpl(FriendshipEntityRepository friendshipEntityRepository, FriendshipEntity friendshipEntity) {
        setFriendshipEntityRepository(friendshipEntityRepository);
        setFriendshipEntity(friendshipEntity);
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

    private FriendshipEntityRepository getFriendshipEntityRepository() {
        return friendshipEntityRepository;
    }

    private void setFriendshipEntityRepository(FriendshipEntityRepository friendshipEntityRepository) {
        this.friendshipEntityRepository = friendshipEntityRepository;
    }

    private FriendshipEntity getFriendshipEntity() {
        return friendshipEntity;
    }

    private void setFriendshipEntity(FriendshipEntity friendshipEntity) {
        this.friendshipEntity = friendshipEntity;
    }
}