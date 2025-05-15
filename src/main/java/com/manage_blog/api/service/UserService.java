package com.manage_blog.api.service;

import com.manage_blog.api.model.ListResponse;
import com.manage_blog.api.model.UserCreateRequest;
import com.manage_blog.api.model.UserResponse;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public interface UserService {

    ListResponse<List<UserResponse>> getUser(
            int page,
            int size,
            String search
    );

    UserResponse getUserByUsername(String username);

    UserResponse createUser(UserCreateRequest userCreateRequest) throws IOException;

    UserResponse updateUser(String username, UserCreateRequest userCreateRequest) throws IOException;

    void deleteUser(String username);
}
