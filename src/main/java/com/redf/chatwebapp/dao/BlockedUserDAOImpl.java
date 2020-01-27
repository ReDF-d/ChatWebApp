package com.redf.chatwebapp.dao;


import com.redf.chatwebapp.dao.entities.BlockedUserEntity;
import com.redf.chatwebapp.dao.entities.UserEntity;
import com.redf.chatwebapp.dao.repo.BlockedUserEntityRepository;
import com.redf.chatwebapp.dao.utils.TransactionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.inject.Singleton;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;


@Component
@Singleton
public class BlockedUserDAOImpl implements BlockedUserDAO, TransactionHandler {
    private BlockedUserEntityRepository blockedUserEntityRepository;
    private BlockedUserEntity blockedUserEntity;

    BlockedUserDAOImpl() {
    }

    @Autowired
    BlockedUserDAOImpl(BlockedUserEntityRepository blockedUserEntityRepository, BlockedUserEntity blockedUserEntity) {
        setBlockedUserEntityRepository(blockedUserEntityRepository);
        setBlockedUserEntity(blockedUserEntity);
    }


    @NotNull
    @Override
    public BlockedUserEntity create(UserEntity user, Timestamp started, Timestamp ends) {
        getBlockedUserEntity().setUser(user);
        getBlockedUserEntity().setStarted(started);
        getBlockedUserEntity().setEnds(ends);
        return getBlockedUserEntity();
    }


    @Override
    public void createAndSave(UserEntity user, Timestamp started, Timestamp ends) {
        save(create(user, started, ends));
    }


    @Override
    public void save(@NotNull BlockedUserEntity blockedUserEntity) {
        performTransaction(TransactionHandler.Transactions.SAVE, blockedUserEntity);
    }


    @Override
    public void update(@NotNull BlockedUserEntity blockedUserEntity) {
        performTransaction(TransactionHandler.Transactions.UPDATE, blockedUserEntity);
    }


    @Override
    public void delete(@NotNull BlockedUserEntity blockedUserEntity) {
        performTransaction(TransactionHandler.Transactions.DELETE, blockedUserEntity);
    }


    private BlockedUserEntity getBlockedUserEntity() {
        return blockedUserEntity;
    }

    private void setBlockedUserEntity(BlockedUserEntity blockedUserEntity) {
        this.blockedUserEntity = blockedUserEntity;
    }

    private BlockedUserEntityRepository getBlockedUserEntityRepository() {
        return blockedUserEntityRepository;
    }

    private void setBlockedUserEntityRepository(BlockedUserEntityRepository blockedUserEntityRepository) {
        this.blockedUserEntityRepository = blockedUserEntityRepository;
    }
}