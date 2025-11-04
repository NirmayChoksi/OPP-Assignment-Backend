package com.oopAssignment.financeTracker.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

        @ExceptionHandler(ResponseStatusException.class)
        public ResponseEntity<ApiError> handleResponseStatusException(ResponseStatusException ex,
                        HttpServletRequest request) {
                HttpStatusCode statusCode = ex.getStatusCode();

                String errorPhrase = (statusCode instanceof HttpStatus)
                                ? ((HttpStatus) statusCode).getReasonPhrase()
                                : statusCode.toString();

                ApiError error = new ApiError(
                                statusCode.value(),
                                errorPhrase,
                                ex.getReason() != null ? ex.getReason() : "Unexpected error",
                                request.getRequestURI());

                return new ResponseEntity<>(error, statusCode);
        }

        @ExceptionHandler(MethodArgumentNotValidException.class)
        public ResponseEntity<ApiError> handleValidationException(MethodArgumentNotValidException ex,
                        HttpServletRequest request) {
                Map<String, String> fieldErrors = new HashMap<>();
                ex.getBindingResult().getFieldErrors()
                                .forEach(error -> fieldErrors.put(error.getField(), error.getDefaultMessage()));

                ApiError error = new ApiError(
                                HttpStatus.BAD_REQUEST.value(),
                                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                                fieldErrors.toString(),
                                request.getRequestURI());

                return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }

        @ExceptionHandler(Exception.class)
        public ResponseEntity<ApiError> handleAllExceptions(Exception ex, HttpServletRequest request) {
                ApiError error = new ApiError(
                                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                                ex.getMessage(),
                                request.getRequestURI());

                return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        @ExceptionHandler(org.springframework.dao.DuplicateKeyException.class)
        public ResponseEntity<ApiError> handleDuplicateKey(org.springframework.dao.DuplicateKeyException ex,
                        HttpServletRequest request) {

                ApiError error = new ApiError(
                                HttpStatus.CONFLICT.value(),
                                HttpStatus.CONFLICT.getReasonPhrase(),
                                "Party with this contact number already exists for this user",
                                request.getRequestURI());

                return new ResponseEntity<>(error, HttpStatus.CONFLICT);
        }

}
