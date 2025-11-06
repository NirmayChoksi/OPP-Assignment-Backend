package com.oopAssignment.financeTracker.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OtpResponse {
    private String contactNumber;
    private String message;
}
