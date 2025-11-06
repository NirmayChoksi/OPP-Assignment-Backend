package com.oopAssignment.financeTracker.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

import com.oopAssignment.financeTracker.dto.response.ApiErrorDetails;
import com.oopAssignment.financeTracker.dto.response.ApiResponse;

import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

        // ✅ Handle ResponseStatusException (manual exceptions thrown in services)
        @ExceptionHandler(ResponseStatusException.class)
        public ResponseEntity<ApiResponse<ApiErrorDetails>> handleResponseStatusException(
                        ResponseStatusException ex,
                        HttpServletRequest request) {

                HttpStatusCode statusCode = ex.getStatusCode();
                HttpStatus status = HttpStatus.valueOf(statusCode.value());

                ApiErrorDetails errorDetails = new ApiErrorDetails(
                                null, // timestamp auto-filled via field default
                                status.value(),
                                status.getReasonPhrase(),
                                ex.getReason() != null ? ex.getReason() : "Unexpected error",
                                request.getRequestURI(),
                                null);

                ApiResponse<ApiErrorDetails> response = new ApiResponse<>(
                                false,
                                ex.getReason(),
                                errorDetails);

                return new ResponseEntity<>(response, status);
        }

        // ✅ Handle @Valid validation errors
        @ExceptionHandler(MethodArgumentNotValidException.class)
        public ResponseEntity<ApiResponse<ApiErrorDetails>> handleValidationException(
                        MethodArgumentNotValidException ex,
                        HttpServletRequest request) {

                Map<String, String> fieldErrors = new HashMap<>();
                ex.getBindingResult().getFieldErrors()
                                .forEach(error -> fieldErrors.put(error.getField(), error.getDefaultMessage()));

                ApiErrorDetails errorDetails = new ApiErrorDetails(
                                null,
                                HttpStatus.BAD_REQUEST.value(),
                                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                                "Validation failed",
                                request.getRequestURI(),
                                fieldErrors);

                ApiResponse<ApiErrorDetails> response = new ApiResponse<>(
                                false,
                                "Validation failed",
                                errorDetails);

                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        // ✅ Handle Duplicate Key errors
        @ExceptionHandler(DuplicateKeyException.class)
        public ResponseEntity<ApiResponse<ApiErrorDetails>> handleDuplicateKey(
                        DuplicateKeyException ex,
                        HttpServletRequest request) {

                ApiErrorDetails errorDetails = new ApiErrorDetails(
                                null,
                                HttpStatus.CONFLICT.value(),
                                HttpStatus.CONFLICT.getReasonPhrase(),
                                "User with this contact number already exists.",
                                request.getRequestURI(),
                                null);

                ApiResponse<ApiErrorDetails> response = new ApiResponse<>(
                                false,
                                "Conflict error",
                                errorDetails);

                return new ResponseEntity<>(response, HttpStatus.CONFLICT);
        }

        // ✅ Catch all unexpected exceptions
        @ExceptionHandler(Exception.class)
        public ResponseEntity<ApiResponse<ApiErrorDetails>> handleAllExceptions(
                        Exception ex,
                        HttpServletRequest request) {

                ApiErrorDetails errorDetails = new ApiErrorDetails(
                                null,
                                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                                ex.getMessage(),
                                request.getRequestURI(),
                                null);

                ApiResponse<ApiErrorDetails> response = new ApiResponse<>(
                                false,
                                "Internal server error",
                                errorDetails);

                return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
}
