package com.oopAssignment.financeTracker.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
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

        private ResponseEntity<ApiResponse<ApiErrorDetails>> buildErrorResponse(
                        HttpStatus status,
                        String message,
                        String path,
                        Map<String, Object> errors) {

                ApiErrorDetails errorDetails = ApiErrorDetails.builder()
                                .status(status.value())
                                .error(status.getReasonPhrase())
                                .message(message)
                                .path(path)
                                .errors(errors)
                                .build();

                return new ResponseEntity<>(
                                new ApiResponse<>(false, message, errorDetails),
                                status);
        }

        @ExceptionHandler(ResponseStatusException.class)
        public ResponseEntity<ApiResponse<ApiErrorDetails>> handleResponseStatusException(
                        ResponseStatusException ex, HttpServletRequest request) {
                return buildErrorResponse(
                                HttpStatus.valueOf(ex.getStatusCode().value()),
                                ex.getReason(),
                                request.getRequestURI(),
                                null);
        }

        @ExceptionHandler(MethodArgumentNotValidException.class)
        public ResponseEntity<ApiResponse<ApiErrorDetails>> handleValidationException(
                        MethodArgumentNotValidException ex, HttpServletRequest request) {

                Map<String, Object> fieldErrors = new HashMap<>();
                ex.getBindingResult().getFieldErrors()
                                .forEach(err -> fieldErrors.put(err.getField(), err.getDefaultMessage()));

                return buildErrorResponse(
                                HttpStatus.BAD_REQUEST,
                                "Validation failed",
                                request.getRequestURI(),
                                fieldErrors);
        }

        @ExceptionHandler(DuplicateKeyException.class)
        public ResponseEntity<ApiResponse<ApiErrorDetails>> handleDuplicateKey(
                        DuplicateKeyException ex, HttpServletRequest request) {
                return buildErrorResponse(
                                HttpStatus.CONFLICT,
                                ex.getMessage(),
                                request.getRequestURI(),
                                null);
        }

        @ExceptionHandler(Exception.class)
        public ResponseEntity<ApiResponse<ApiErrorDetails>> handleAllExceptions(
                        Exception ex, HttpServletRequest request) {
                return buildErrorResponse(
                                HttpStatus.INTERNAL_SERVER_ERROR,
                                ex.getMessage(),
                                request.getRequestURI(),
                                null);
        }
}
