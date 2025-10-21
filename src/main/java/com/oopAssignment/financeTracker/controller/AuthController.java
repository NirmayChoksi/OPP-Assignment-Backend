package com.oopAssignment.financeTracker.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.oopAssignment.financeTracker.dto.request.CreateUserRequest;
import com.oopAssignment.financeTracker.dto.request.VerifyOtpRequest;
import com.oopAssignment.financeTracker.dto.response.VerifyOtpResponse;
import com.oopAssignment.financeTracker.model.User;
import com.oopAssignment.financeTracker.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/create")
    public ResponseEntity<User> createOrUpdateUser(@Valid @RequestBody CreateUserRequest requestBody) {
        User user = userService.createOrUpdateUserByContact(requestBody.getContactNumber());
        return ResponseEntity.ok(user);
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<VerifyOtpResponse> verifyOtp(@Valid @RequestBody VerifyOtpRequest requestBody) {
        String token = userService.verifyOtp(
                requestBody.getContactNumber(),
                requestBody.getOtp());

        return ResponseEntity.ok(new VerifyOtpResponse(token));
    }

}
