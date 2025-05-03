package com.manage_blog.api.model;

import com.manage_blog.api.enums.RoleEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserCreateRequest {
    private String name;
    private String username;
    private String email;
    private String password;
    private RoleEnum role;
}
