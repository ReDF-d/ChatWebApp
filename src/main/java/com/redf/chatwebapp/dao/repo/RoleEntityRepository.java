package com.redf.chatwebapp.dao.repo;

import com.redf.chatwebapp.dao.entities.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface RoleEntityRepository extends JpaRepository<RoleEntity, Long> {

    @Query("SELECT r from RoleEntity r where r.role=:name")
    RoleEntity findByRoleName(@Param("name") String name);
}

