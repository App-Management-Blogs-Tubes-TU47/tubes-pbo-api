package com.manage_blog.api.controller;

import com.manage_blog.api.model.WebResponse;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.NonUniqueResultException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;

@Hidden
@Slf4j
@RestControllerAdvice
public class ErrorController {

//    @ExceptionHandler({
//            ConstraintViolationException.class,
//            MethodArgumentNotValidException.class,
//            IncorrectResultSizeDataAccessException.class
//    })
//    public ResponseEntity<WebResponse<String>> handleClientErrors(Exception ex) {
//        String message = switch (ex) {
//            case ConstraintViolationException e -> e.getMessage();
//            case MethodArgumentNotValidException e -> e.getBindingResult().getFieldErrors().stream()
//                    .map(err -> err.getField() + " " + err.getDefaultMessage())
//                    .findFirst()
//                    .orElse("Validation failed");
//            case IncorrectResultSizeDataAccessException e -> "Query returned more than one result.";
//            default -> "Bad Request";
//        };
//
//        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
//                .body(WebResponse.<String>builder()
//                        .status(HttpStatus.BAD_REQUEST.value())
//                        .message(message)
//                        .error(message)
//                        .build());
//    }
//
//    // Handle all other unexpected server errors (5xx)
//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<WebResponse<String>> handleAllOtherExceptions(Exception ex) {
//        log.error("Unexpected error", ex);
//        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                .body(WebResponse.<String>builder()
//                        .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
//                        .message("An unexpected error occurred. Please try again later.")
//                        .error("Unexpected server error: " + ex.getMessage())
//                        .build());
//    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<WebResponse<String>> apiException(ResponseStatusException ex) {
        HttpStatus status = (HttpStatus) ex.getStatusCode();
        return ResponseEntity.status(status)
                .body(WebResponse.<String>builder()
                        .status(status.value()) // Set status di body
                        .error(ex.getReason())
                        .message(ex.getReason())
                        .build());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<WebResponse<String>> constraintViolationException(ConstraintViolationException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(WebResponse.<String>builder()
                        .status(HttpStatus.BAD_REQUEST.value()) // Set status di body
                        .error(exception.getMessage())
                        .message("Validation error: " + exception.getMessage())
                        .build());
    }


    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<WebResponse<String>> handleUnsupportedMediaType(HttpMediaTypeNotSupportedException ex) {
        return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
                .body(WebResponse.<String>builder()
                        .status(HttpStatus.UNSUPPORTED_MEDIA_TYPE.value())
                        .error("Unsupported media type: " + ex.getContentType())
                        .message("The media type " + ex.getContentType() + " is not supported. Please use a supported media type.")
                        .build());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<WebResponse<String>> handleBadRequestBody(HttpMessageNotReadableException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(WebResponse.<String>builder()
                        .status(HttpStatus.BAD_REQUEST.value())
                        .error("Invalid request body: " + ex.getMessage())
                        .message("The request body is malformed or missing required fields. Please check your request and try again.")
                        .build());
    }

    @ExceptionHandler(MissingServletRequestPartException.class)
    public ResponseEntity<WebResponse<String>> handleMissingPart(MissingServletRequestPartException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(WebResponse.<String>builder()
                        .status(HttpStatus.BAD_REQUEST.value())
                        .error("Missing part: " + ex.getRequestPartName())
                        .message("The request is missing a required part: " + ex.getRequestPartName() + ". Please include it and try again.")
                        .build());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<WebResponse<String>> handleValidationErrors(MethodArgumentNotValidException ex) {
        String errorMsg = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + " " + error.getDefaultMessage())
                .findFirst()
                .orElse("Validation failed");

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(WebResponse.<String>builder()
                        .status(HttpStatus.BAD_REQUEST.value())
                        .error(errorMsg)
                        .message("Validation error: " + errorMsg)
                        .build());
    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity<WebResponse<String>> handleIOException(IOException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(WebResponse.<String>builder()
                        .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                        .error("I/O error occurred: " + ex.getMessage())
                        .message("An error occurred while processing your request. Please try again later.")
                        .build());
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<WebResponse<String>> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(WebResponse.<String>builder()
                        .status(HttpStatus.CONFLICT.value())
                        .error("Conflict: Duplicate data or constraint violation")
                        .message(ex.getMessage())
                        .build());
    }

    @ExceptionHandler({NonUniqueResultException.class, IncorrectResultSizeDataAccessException.class})
    public ResponseEntity<WebResponse<String>> handleNonUniqueResult(Exception ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(WebResponse.<String>builder()
                        .status(HttpStatus.BAD_REQUEST.value())
                        .error("Query returned more than one result: " + ex.getMessage())
                        .message(ex.getMessage())
                        .build());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<WebResponse<String>> handleAllOtherExceptions(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(WebResponse.<String>builder()
                        .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                        .error("Unexpected error occurred: " + ex.getMessage())
                        .message("An unexpected error occurred. Please try again later.")
                        .build());
    }



}