package com.redf.chatwebapp.dao.entities;

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;
import org.jetbrains.annotations.Contract;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;


@Component
@DynamicUpdate
@Entity
@Table(name = "roles")
public class RoleEntity extends AbstractEntity implements Serializable {

    private Long id;
    private String role;
    private List<UserEntity> userRoles;


    @Contract(pure = true)
    public RoleEntity() {
    }


    @Contract(pure = true)
    public RoleEntity(String role) {
        setRole(role);
    }


    @Id
    @Column(name = "id")
    @GenericGenerator(name = "role_ids", strategy = "sequence", parameters = {
            @org.hibernate.annotations.Parameter(name = "role_ids", value = "role_ids"),
            @org.hibernate.annotations.Parameter(name = "allocationSize", value = "1"),
    })
    @GeneratedValue(generator = "role_ids", strategy = GenerationType.SEQUENCE)
    Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String name) {
        this.role = name;
    }

    @ManyToMany(mappedBy = "roles")
    public List<UserEntity> getUserRoles() {
        return userRoles;
    }

    public void setUserRoles(List<UserEntity> user) {
        this.userRoles = user;
    }
}
