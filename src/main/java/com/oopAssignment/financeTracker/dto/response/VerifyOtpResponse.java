package com.oopAssignment.financeTracker.dto.response;

public class VerifyOtpResponse {
    private String token;

    public VerifyOtpResponse(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }
}
