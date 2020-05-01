package com.redf.chatwebapp.dao;

import com.redf.chatwebapp.dao.entities.OnlineUserEntity;
import com.redf.chatwebapp.dao.repo.UserEntityRepository;
import com.redf.chatwebapp.dao.utils.TransactionHandler;
import org.jetbrains.annotations.Contract;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.inject.Singleton;


@Component
@Singleton
public class OnlineUserEntityDAOImpl implements OnlineUserEntityDAO, TransactionHandler {

    private OnlineUserEntity userEntity;
    private UserEntityRepository userEntityRepository;

    OnlineUserEntityDAOImpl() {
    }

    @Autowired
    OnlineUserEntityDAOImpl(OnlineUserEntity userEntity, UserEntityRepository userEntityRepository) {
        setUserEntity(userEntity);
        setUserEntityRepository(userEntityRepository);
    }

    @Override
    public OnlineUserEntity create(Long id, String isOnline, long lastSeen) {
        getUserEntity().setId(id);
        getUserEntity().setUser(getUserEntityRepository().findById(id).orElse(null));
        getUserEntity().setOnline(isOnline);
        getUserEntity().setLastSeen(lastSeen);
        return getUserEntity();
    }


    @Override
    public OnlineUserEntity createAndSave(Long id, String isOnline, long lastSeen) {
        return save(create(id, isOnline, lastSeen));
    }


    @Override
    public OnlineUserEntity save(OnlineUserEntity userEntity) {
        performTransaction(Transactions.SAVE, userEntity);
        return userEntity;
    }


    @Override
    public void update(OnlineUserEntity userEntity) {
        performTransaction(Transactions.UPDATE, userEntity);
    }


    @Override
    public void delete(OnlineUserEntity userEntity) {
        performTransaction(Transactions.DELETE, userEntity);
    }


    @Contract(pure = true)
    private OnlineUserEntity getUserEntity() {
        return userEntity;
    }


    private void setUserEntity(OnlineUserEntity userEntity) {
        this.userEntity = userEntity;
    }

    @Contract(pure = true)
    private UserEntityRepository getUserEntityRepository() {
        return userEntityRepository;
    }

    private void setUserEntityRepository(UserEntityRepository userEntityRepository) {
        this.userEntityRepository = userEntityRepository;
    }
}
