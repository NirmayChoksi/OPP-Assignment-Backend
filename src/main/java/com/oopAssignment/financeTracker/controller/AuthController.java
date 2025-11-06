package com.oopAssignment.financeTracker.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.oopAssignment.financeTracker.dto.request.OtpRequest;
import com.oopAssignment.financeTracker.dto.request.VerifyOtpRequest;
import com.oopAssignment.financeTracker.dto.response.ApiResponse;
import com.oopAssignment.financeTracker.dto.response.OtpResponse;
import com.oopAssignment.financeTracker.dto.response.VerifyOtpResponse;
import com.oopAssignment.financeTracker.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {
        private final UserService userService;

        public AuthController(UserService userService) {
                this.userService = userService;
        }

        @PostMapping("/request-otp")
        public ResponseEntity<ApiResponse<OtpResponse>> requestOtp(
                        @Valid @RequestBody OtpRequest requestBody) {
                userService.generateOtpForContact(requestBody.getContactNumber());

                OtpResponse data = new OtpResponse(
                                requestBody.getContactNumber(),
                                "OTP has been sent successfully.");

                ApiResponse<OtpResponse> response = new ApiResponse<>(
                                true,
                                "OTP request successful",
                                data);

                return ResponseEntity.ok(response);
        }

        @PostMapping("/verify-otp")
        public ResponseEntity<ApiResponse<VerifyOtpResponse>> verifyOtp(
                        @Valid @RequestBody VerifyOtpRequest requestBody) {

                String token = userService.verifyOtp(
                                requestBody.getContactNumber(),
                                requestBody.getOtp());

                VerifyOtpResponse data = new VerifyOtpResponse(token);

                ApiResponse<VerifyOtpResponse> response = new ApiResponse<>(
                                true,
                                "OTP verified successfully",
                                data);

                return ResponseEntity.ok(response);
        }

        @PostMapping("/resend-otp")
        public ResponseEntity<ApiResponse<OtpResponse>> resendOtp(
                        @Valid @RequestBody OtpRequest requestBody) {

                userService.resendOtp(requestBody.getContactNumber());

                OtpResponse data = new OtpResponse(
                                requestBody.getContactNumber(),
                                "A new OTP has been sent successfully.");

                ApiResponse<OtpResponse> response = new ApiResponse<>(
                                true,
                                "OTP resent successfully",
                                data);

                return ResponseEntity.ok(response);
        }
}
