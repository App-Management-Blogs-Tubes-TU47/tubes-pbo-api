package com.manage_blog.api.controller;

import com.manage_blog.api.model.WebResponse;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@Hidden
@Slf4j
@RestControllerAdvice
public class ErrorController {

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<WebResponse<String>> apiException(ResponseStatusException exception) {
        HttpStatus status = (HttpStatus) exception.getStatusCode();
        return ResponseEntity.status(status)
                .body(WebResponse.<String>builder()
                        .status(status.value()) // Set status di body
                        .errors(exception.getReason())
                        .build());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<WebResponse<String>> constraintViolationException(ConstraintViolationException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(WebResponse.<String>builder()
                        .status(HttpStatus.BAD_REQUEST.value()) // Set status di body
                        .errors(exception.getMessage())
                        .build());
    }
}