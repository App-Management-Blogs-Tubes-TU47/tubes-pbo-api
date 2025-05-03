package com.manage_blog.api.service.impl;

import com.manage_blog.api.entity.Users;
import com.manage_blog.api.model.ListResponse;
import com.manage_blog.api.model.PaginationResponse;
import com.manage_blog.api.model.UserCreateRequest;
import com.manage_blog.api.model.UserResponse;
import com.manage_blog.api.repository.UserRepository;
import com.manage_blog.api.service.UserService;
import com.manage_blog.api.service.ValidationService;
import com.manage_blog.api.utils.NullAwareBeanUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.*;

import java.util.List;

@Service
@Slf4j
public class UserServiceImpl implements UserService {


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ValidationService validationService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;



    //    ==========================
    //    Get All Users
    //    @Params page, size, search
    //    ==========================
    @Transactional
    public ListResponse<List<UserResponse>> getUser(
            int page,
            int size,
            String search
    ) {
        PageRequest pageRequest = PageRequest.of(page - 1, size);
        Page<Users> userPage = userRepository.findBySearch(search, pageRequest);

        List<UserResponse> userResponses = userPage.getContent().stream()
                .map(user -> new UserResponse(user))
                .toList();

        // Prepare pagination response
        PaginationResponse paginationResponse = new PaginationResponse();
        paginationResponse.setPage(page);
        paginationResponse.setSize(size);
        paginationResponse.setTotalRecords((int) userPage.getTotalElements());
        paginationResponse.setTotalPages(userPage.getTotalPages());

        return new ListResponse<>(userResponses, paginationResponse);
    }

    //    ==========================
    //    Get User By ID
    //    @Params id
    //    ==========================
    @Transactional(readOnly = true)
    public UserResponse getUserByUsername(
            String username
    ) {
        Users user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return new UserResponse(user);
    }


    //    ==========================
    //    Create User
    //    @Body name, username, email, password, role
    //    ==========================
    @Transactional
    public UserResponse createUser(
            UserCreateRequest u
    ) {
        Users user = new Users();
        user.setName(u.getName());
        user.setUsername(u.getUsername());
        user.setEmail(u.getEmail());

        user.setCreatedAt(String.valueOf(System.currentTimeMillis()));
        user.setUpdatedAt(String.valueOf(System.currentTimeMillis()));

        // Encrypt the password using bcrypt
        String encryptedPassword = passwordEncoder.encode(u.getPassword());
        user.setPassword(encryptedPassword);

        user.setRole(u.getRole());
        userRepository.save(user);

        return new UserResponse(
                user
        );
    }

    //    ==========================
    //    Update User
    //    @Params username
    //    @Body name, username, email, password, role
    //    ==========================
    @Transactional
    public UserResponse updateUser(
            String username,
            UserCreateRequest u
    ) {
        Users user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Update user details
        NullAwareBeanUtils.copyNonNullProperties(u, user);

        // Encrypt the password using bcrypt
        if(u.getPassword() != null){
            String encryptedPassword = passwordEncoder.encode(u.getPassword());
            user.setPassword(encryptedPassword);
        }

        user.setUpdatedAt(String.valueOf(System.currentTimeMillis()));

        userRepository.save(user);

        return new UserResponse(user);
    }

    //    ==========================
    //    Delete User
    //    @Params username
    //    ==========================

    @Transactional
    public void deleteUser(
            String username
    ) {
        Users user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setDeletedAt(String.valueOf(System.currentTimeMillis()));
        userRepository.save(user);

    }



}
