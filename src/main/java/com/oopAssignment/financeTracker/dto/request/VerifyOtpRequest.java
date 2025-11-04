package com.oopAssignment.financeTracker.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class VerifyOtpRequest {
    @NotBlank(message = "Contact number is required")
    @Pattern(regexp = "^[+]?[0-9]{10,15}$", message = "Invalid contact number format")
    private String contactNumber;

    @NotBlank(message = "OTP is required")
    private String otp;
}
