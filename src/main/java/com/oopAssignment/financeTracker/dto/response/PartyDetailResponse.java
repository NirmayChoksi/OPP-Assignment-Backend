package com.oopAssignment.financeTracker.dto.response;

import java.time.Instant;
import java.util.List;

import com.oopAssignment.financeTracker.model.Transaction;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PartyDetailResponse {
    private String id;
    private String name;
    private String contactNumber;
    private String type;
    private String gstNumber;
    private Instant createdAt;
    private Instant updatedAt;
    private List<Transaction> transactions;
}
