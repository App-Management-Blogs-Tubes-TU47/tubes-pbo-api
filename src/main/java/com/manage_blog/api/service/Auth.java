package com.manage_blog.api.service;

import com.manage_blog.api.model.AuthResponse;
import org.springframework.stereotype.Service;

@Service
public interface Auth {

    AuthResponse login(String username, String password);



    void logout(String token);
}
