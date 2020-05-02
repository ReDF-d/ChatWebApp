package com.redf.chatwebapp.dao.repo;

import com.redf.chatwebapp.dao.entities.ForgotPasswordToken;
import com.redf.chatwebapp.dao.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ForgotPasswordTokenRepository extends JpaRepository<ForgotPasswordToken, Long> {
    ForgotPasswordToken findByToken(String token);

    ForgotPasswordToken findByUser(UserEntity user);
}
