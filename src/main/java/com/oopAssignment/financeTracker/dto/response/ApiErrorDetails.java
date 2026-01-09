package com.oopAssignment.financeTracker.dto.response;

import java.time.Instant;
import java.util.Map;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ApiErrorDetails {
    @Builder.Default
    private Instant timestamp = Instant.now();
    private int status;
    private String error;
    private String message;
    private String path;
    private Map<String, Object> errors;
}
