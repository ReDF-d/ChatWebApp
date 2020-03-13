package com.redf.chatwebapp.dao.repo;

import com.redf.chatwebapp.dao.entities.EmailVerificationToken;
import com.redf.chatwebapp.dao.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VerificationTokenRepository extends JpaRepository<EmailVerificationToken, Long> {

    EmailVerificationToken findByToken(String token);

    EmailVerificationToken findByUser(UserEntity user);
}
