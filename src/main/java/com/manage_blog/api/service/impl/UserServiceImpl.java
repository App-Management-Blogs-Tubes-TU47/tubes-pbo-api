package com.manage_blog.api.service.impl;

import com.manage_blog.api.entity.Users;
import com.manage_blog.api.model.ListResponse;
import com.manage_blog.api.model.PaginationResponse;
import com.manage_blog.api.model.UserCreateRequest;
import com.manage_blog.api.model.UserResponse;
import com.manage_blog.api.repository.UserRepository;
import com.manage_blog.api.service.StorageService;
import com.manage_blog.api.service.UserService;
import com.manage_blog.api.utils.NullAwareBeanUtils;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StorageService storageService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    //    ==========================
    //    Get All Users
    //    @Params page, size, search
    //    ==========================
    @Override
    @Transactional
    public ListResponse<List<UserResponse>> getUser(int page, int size, String search) {
        PageRequest pageRequest = PageRequest.of(page - 1, size);
        Page<Users> userPage = userRepository.findBySearch(search, pageRequest);
        List<UserResponse> userResponses = userPage.getContent().stream().map(u -> {
                    UserResponse response = new UserResponse(u);
                    if (u.getProfile() != null)
                        response.setProfileUrl(storageService.getFileUrl(u.getProfile()));
                    return response;
                }).toList();
        PaginationResponse paginationResponse = PaginationResponse.builder()
                .page(page).size(size).totalRecords((int) userPage.getTotalElements())
                .totalPages(userPage.getTotalPages()).build();
        return new ListResponse<>(userResponses, paginationResponse);
    }

    //    ==========================
    //    Get User By ID
    //    @Params id
    //    ==========================
    @Override
    @Transactional(readOnly = true)
    public UserResponse getUserByUsername(String username) {
        Users user = userRepository.findByUsername(username)
                .orElseThrow(
                        () -> new ResponseStatusException(
                                HttpStatus.NOT_FOUND, "User not found"
                        )
                );
        UserResponse response = new UserResponse(user);
        if (user.getProfile() != null)
            response.setProfileUrl(storageService.getFileUrl(user.getProfile()));
        return response;
    }


    //    ==========================
    //    Create User
    //    @Body name, username, email, password, role
    //    ==========================
    @Override
    @Transactional
    public UserResponse createUser(UserCreateRequest u) throws IOException  {
        if (userRepository.validateUnameEmailAlreadyExist(u.getEmail(), u.getUsername()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Username Or Email already exists");
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");
        LocalDate currentDate = LocalDate.now();
        String formattedDate = currentDate.format(formatter);
        String generatedPath = null;
        if(u.getProfile() != null) {
            generatedPath = "profile/" + formattedDate + "/" + u.getUsername() + u.getProfile().getContentType();
            storageService.uploadFile(generatedPath, u.getProfile().getInputStream(), u.getProfile().getContentType());
        }
        Users user = Users.builder().name(u.getName()).username(u.getUsername())
                .email(u.getEmail()).profile(generatedPath).role(u.getRole())
                .build();
        String encryptedPassword = passwordEncoder.encode(u.getPassword());
        user.setPassword(encryptedPassword);
        userRepository.save(user);
        UserResponse response = new UserResponse(user);
        if(user.getProfile() != null)
            response.setProfileUrl(storageService.getFileUrl(user.getProfile()));
        return response;
    }

    //    ==========================
    //    Update User
    //    @Params username
    //    @Body name, username, email, password, role
    //    ==========================
    @Override
    @Transactional
    public UserResponse updateUser(String username, UserCreateRequest u) throws IOException {
        Users user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"User not found"));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");
        LocalDate currentDate = LocalDate.now();
        String formattedDate = currentDate.format(formatter);
        String generatedPath = null;
        NullAwareBeanUtils.copyNonNullProperties(u, user);
        if(u.getProfile() != null) {
            generatedPath = "profile/" + formattedDate + "/" + u.getUsername() + u.getProfile().getContentType();
            storageService.uploadFile(generatedPath, u.getProfile().getInputStream(), u.getProfile().getContentType());
            user.setProfile(generatedPath);
        }
        if(u.getPassword() != null){
            String encryptedPassword = passwordEncoder.encode(u.getPassword());
            user.setPassword(encryptedPassword);
        }
        userRepository.save(user);
        return new UserResponse(user);
    }

    //    ==========================
    //    Delete User
    //    @Params username
    //    ==========================
    @Override
    @Transactional
    public void deleteUser(String username) {
        Users user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "User not found")
                );
        user.setDeletedAt(LocalDateTime.now());
        userRepository.save(user);
    }



}
