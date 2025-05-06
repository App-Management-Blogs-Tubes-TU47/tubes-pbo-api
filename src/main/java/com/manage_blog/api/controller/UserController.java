package com.manage_blog.api.controller;

import com.manage_blog.api.model.ListResponse;
import com.manage_blog.api.model.UserCreateRequest;
import com.manage_blog.api.model.UserResponse;
import com.manage_blog.api.model.WebResponse;
import com.manage_blog.api.service.UserService;
import com.manage_blog.api.service.impl.UserServiceImpl;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private UserService userService;

    public UserController(UserServiceImpl userService) {
        this.userService = userService;
    }

//    =========================
//    Get All Users
//    @Params page, size, search
//    =========================
    @GetMapping
    public WebResponse<ListResponse<List<UserResponse>>> getAllUsers(
            @RequestParam (value = "page", required = false, defaultValue = "0") Integer page,
            @RequestParam (value = "size", required = false, defaultValue = "10") Integer size,
            @RequestParam (value = "search", required = false) String search
    ) {
        try {
            if (page < 1){
                page = 1;
            }
            if (size < 1){
                size = 10;
            }
            ListResponse<List<UserResponse>> listResponse = userService.getUser(
                    page,
                    size,
                    search
            );
            return WebResponse.<ListResponse<List<UserResponse>>>builder()
                    .status(200)
                    .message("Success")
                    .data(listResponse)
                    .build();
        } catch (Exception e) {
            return WebResponse.<ListResponse<List<UserResponse>>>builder()
                    .status(500)
                    .error(e.getMessage())
                    .build();
        }
    }

//    =========================
//    Get User By ID
//    @Params id
//    =========================
    @GetMapping("/{username}")
    public WebResponse<UserResponse> getUserByUsername(
            @PathVariable("username") String username
    ) {
        try {
            UserResponse userResponse = userService.getUserByUsername(username);
            return WebResponse.<UserResponse>builder()
                    .status(200)
                    .message("Success")
                    .data(userResponse)
                    .build();
        } catch (Exception e) {
            return WebResponse.<UserResponse>builder()
                    .status(500)
                    .error(e.getMessage())
                    .build();
        }
    }

//    =========================
//    Create User
//    @Body name, username, email, password, role
//    =========================
    @PostMapping(
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public WebResponse<UserResponse> createUser(
            @ModelAttribute UserCreateRequest user
    ) {
        try {
            UserResponse userResponse = userService.createUser(user);
            return WebResponse.<UserResponse>builder()
                    .status(200)
                    .message("Success")
                    .data(userResponse)
                    .build();
        } catch (Exception e) {
            return WebResponse.<UserResponse>builder()
                    .status(500)
                    .error(e.getMessage())
                    .build();
        }
    }

//    =========================
//    Update User
//    @Params id
//    @Body name, username, email, password, role
//    =========================
    @PatchMapping(value = "/{username}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public WebResponse<UserResponse> updateUser(
            @PathVariable("username") String username,
            @ModelAttribute UserCreateRequest userCreateRequest
    ) {
        try {
            UserResponse userResponse = userService.updateUser(username, userCreateRequest);
            return WebResponse.<UserResponse>builder()
                    .status(200)
                    .message("Success")
                    .data(userResponse)
                    .build();
        } catch (Exception e) {
            return WebResponse.<UserResponse>builder()
                    .status(500)
                    .error(e.getMessage())
                    .build();
        }
    }

//    =========================
//    Delete User
//    @Params id
//    =========================
    @DeleteMapping("/{username}")
    public WebResponse<String> deleteUser(
            @PathVariable("username") String username
    ) {
        try {
            userService.deleteUser(username);
            return WebResponse.<String>builder()
                    .status(200)
                    .message("Success")
                    .data("User with username " + username + " has been deleted")
                    .build();
        } catch (Exception e) {
            return WebResponse.<String>builder()
                    .status(500)
                    .error(e.getMessage())
                    .build();
        }
    }

}
