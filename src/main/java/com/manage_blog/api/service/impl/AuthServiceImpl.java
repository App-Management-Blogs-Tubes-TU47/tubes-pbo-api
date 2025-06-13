package com.manage_blog.api.service.impl;

import com.manage_blog.api.entity.Users;
import com.manage_blog.api.enums.RoleEnum;
import com.manage_blog.api.model.AuthLoginRequest;
import com.manage_blog.api.model.AuthRegisterRequest;
import com.manage_blog.api.model.AuthResponse;
import com.manage_blog.api.model.UserResponse;
import com.manage_blog.api.repository.UserRepository;
import com.manage_blog.api.service.AuthService;
import com.manage_blog.api.service.StorageService;
import com.manage_blog.api.utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class AuthServiceImpl implements AuthService {


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private StorageService storageService;

    @Override
    @Transactional
    public AuthResponse login(AuthLoginRequest a) {
        Users user = userRepository.findByUsername(a.getUsername())
                .orElseThrow(() -> new RuntimeException(
                        "User not found with username: " + a.getUsername()
                ));
        if (!passwordEncoder.matches(a.getPassword(), user.getPassword()))
            throw new RuntimeException("Invalid password");
        String token = jwtUtils.generateToken(user);
        UserResponse users = new UserResponse(user);
        if (user.getProfile() != null) {
            users.setProfileUrl(storageService.getFileUrl(user.getProfile()));
        } else {
            users.setProfileUrl(null);
        }
        AuthResponse authResponse = AuthResponse.builder()
                .token(token).user(users).build();
        return authResponse;
    }

    @Override
    @Transactional
    public AuthResponse register(AuthRegisterRequest a) {
        if (userRepository.findByUsername(a.getUsername()).isPresent()) {
            throw new RuntimeException("Username already exists");
        }
        Users user = Users.builder()
                .name(a.getName()).username(a.getUsername())
                .password(passwordEncoder.encode(a.getPassword()))
                .email(a.getEmail())
                .role(RoleEnum.WRITTER)
                .build();
        userRepository.save(user);
        String token = jwtUtils.generateToken(user);

        UserResponse users = new UserResponse(user);
        AuthResponse authResponse = AuthResponse.builder()
                .token(token).user(users).build();
        return authResponse;
    }

//    @Override
//    @Transactional
//    public void logout(String token) {
//
//        String username = jwtUtils.getUsernameFromToken(token);
//        Users user = userRepository.findByUsername(username)
//                .orElseThrow(() -> new RuntimeException("User not found with username: " + username));
//
//        if (jwtUtils.isTokenExpired(token)) {
//            throw new RuntimeException("Token expired");
//        }
//
//        if (!jwtUtils.validateToken(token, user)) {
//            throw new RuntimeException("Invalid token");
//        }
//
//    }
}
