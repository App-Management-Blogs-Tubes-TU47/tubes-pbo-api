package com.manage_blog.api.service;

import com.manage_blog.api.model.AuthLoginRequest;
import com.manage_blog.api.model.AuthRegisterRequest;
import com.manage_blog.api.model.AuthResponse;
import org.springframework.stereotype.Service;

@Service
public interface AuthService {

    AuthResponse login(AuthLoginRequest authLoginRequest);

    AuthResponse register(AuthRegisterRequest authRegisterRequest);

    void logout(String token);
}
