package com.manage_blog.api.entity;

import com.manage_blog.api.enums.RoleEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;

import java.util.UUID;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Where(clause = "deleted_at IS NULL")
public class Users extends BaseEntity {
    @Column(name= "name")
    private String name;

    @Column(name= "username", unique = true)
    private String username;

    @Column(name= "email", unique = true)
    private String email;

    @Column(name= "password")
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name= "role")
    private RoleEnum role;

    @Column(name = "profile", length = 500, nullable = true)
    private String profile;
}
