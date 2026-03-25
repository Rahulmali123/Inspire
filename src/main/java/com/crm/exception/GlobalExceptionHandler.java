package com.crm.exception;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice   // ✅ Better than @ControllerAdvice for REST APIs
@Slf4j                  // ✅ Logging enabled
public class GlobalExceptionHandler {

    // ==============================
    // 🔴 NO STAFF AVAILABLE
    // ==============================
    @ExceptionHandler(NoStaffAvailableException.class)
    public ResponseEntity<Map<String, Object>> handleNoStaff(NoStaffAvailableException ex) {

        log.error("No staff available: {}", ex.getMessage());

        return buildResponse(HttpStatus.BAD_REQUEST, "Staff Unavailable", ex.getMessage());
    }

    // ==============================
    // 🔴 RESOURCE NOT FOUND
    // ==============================
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleResourceNotFound(ResourceNotFoundException ex) {

        log.error("Resource not found: {}", ex.getMessage());

        return buildResponse(HttpStatus.NOT_FOUND, "Not Found", ex.getMessage());
    }

    // ==============================
    // 🔴 VALIDATION ERROR
    // ==============================
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationException(MethodArgumentNotValidException ex) {

        Map<String, String> validationErrors = new HashMap<>();

        ex.getBindingResult().getFieldErrors()
                .forEach(error -> validationErrors.put(error.getField(), error.getDefaultMessage()));

        log.error("Validation failed: {}", validationErrors);

        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalDateTime.now());
        response.put("status", HttpStatus.BAD_REQUEST.value());
        response.put("error", "Validation Failed");
        response.put("errors", validationErrors);

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    // ==============================
    // 🔴 ILLEGAL ARGUMENT (Custom Validation)
    // ==============================
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalArgument(IllegalArgumentException ex) {

        log.error("Invalid input: {}", ex.getMessage());

        return buildResponse(HttpStatus.BAD_REQUEST, "Bad Request", ex.getMessage());
    }

    // ==============================
    // 🔴 GENERIC EXCEPTION
    // ==============================
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGlobalException(Exception ex) {

        log.error("Unexpected error: ", ex);

        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", ex.getMessage());
    }

    // ==============================
    // ✅ COMMON RESPONSE BUILDER
    // ==============================
    private ResponseEntity<Map<String, Object>> buildResponse(HttpStatus status, String error, String message) {

        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", status.value());
        body.put("error", error);
        body.put("message", message);

        return new ResponseEntity<>(body, status);
    }
}