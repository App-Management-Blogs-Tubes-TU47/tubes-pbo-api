package com.manage_blog.api.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/landing")
public class Landing {

    @GetMapping
    public String landing() {
        return "Welcome to the Blog API!";
    }
}
