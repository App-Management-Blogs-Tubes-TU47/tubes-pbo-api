package com.manage_blog.api.model;

import com.manage_blog.api.entity.Users;
import com.manage_blog.api.enums.RoleEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponse {

    private String id;
    private String name;
    private String username;
    private String email;
    private RoleEnum role;
    private String profileUrl;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public UserResponse(Users user) {
        this.id = user.getId() != null ? user.getId().toString() : null;  // Assuming UUID for id
        this.name = user.getName();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.role = user.getRole();
        this.createdAt = user.getCreatedAt();
        this.updatedAt = user.getUpdatedAt();
    }

}
