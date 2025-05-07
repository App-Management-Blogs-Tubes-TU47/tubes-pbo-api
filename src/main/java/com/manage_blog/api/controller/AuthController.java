package com.manage_blog.api.controller;

import com.manage_blog.api.model.AuthLoginRequest;
import com.manage_blog.api.model.AuthRegisterRequest;
import com.manage_blog.api.model.AuthResponse;
import com.manage_blog.api.model.WebResponse;
import com.manage_blog.api.service.AuthService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

//    =========================
//    Login
//    @Params username, password
//    =========================

    @PostMapping("/login")
    public WebResponse<AuthResponse> login(@RequestBody AuthLoginRequest authLoginRequest) {
        try {
            AuthResponse authResponse = authService.login(authLoginRequest);
            return WebResponse.<AuthResponse>builder()
                    .status(200)
                    .message("Success")
                    .data(authResponse)
                    .build();
        } catch (Exception e) {
            return WebResponse.<AuthResponse>builder()
                    .status(500)
                    .error(e.getMessage())
                    .build();
        }
    }

//    =========================
//    Register
//    @Params name, username, password, email
//    =========================

    @PostMapping("/register")
    public WebResponse<AuthResponse> register(@RequestBody AuthRegisterRequest auth) {
        try {
            AuthResponse authResponse = authService.register(auth);
            return WebResponse.<AuthResponse>builder()
                    .status(200)
                    .message("Success")
                    .data(authResponse)
                    .build();
        } catch (Exception e) {
            return WebResponse.<AuthResponse>builder()
                    .status(500)
                    .error(e.getMessage())
                    .build();
        }
    }

//    =========================
//    Logout
//    @Params token
//    =========================
    @PostMapping("/logout")
    public WebResponse<String> logout(@RequestBody String token) {
        try {
            authService.logout(token);
            return WebResponse.<String>builder()
                    .status(200)
                    .message("Success")
                    .data("Logout success")
                    .build();
        } catch (Exception e) {
            return WebResponse.<String>builder()
                    .status(500)
                    .error(e.getMessage())
                    .build();
        }
    }

}
