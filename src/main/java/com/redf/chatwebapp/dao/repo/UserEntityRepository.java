package com.redf.chatwebapp.dao.repo;

import com.redf.chatwebapp.dao.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface UserEntityRepository extends JpaRepository<UserEntity, Long> {

    @Query("SELECT u from UserEntity u where u.login=:login")
    UserEntity findByLogin(@Param("login") String login);
}
