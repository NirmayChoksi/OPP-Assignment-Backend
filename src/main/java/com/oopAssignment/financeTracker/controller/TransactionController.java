package com.oopAssignment.financeTracker.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.oopAssignment.financeTracker.dto.request.CreateTransactionRequest;
import com.oopAssignment.financeTracker.dto.response.ApiResponse;
import com.oopAssignment.financeTracker.dto.response.TransactionResponse;
import com.oopAssignment.financeTracker.service.TransactionService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping
    public ResponseEntity<ApiResponse<TransactionResponse>> createTransaction(
            @Valid @RequestBody CreateTransactionRequest request,
            Authentication authentication) {

        String userId = authentication.getPrincipal().toString();

        TransactionResponse data = transactionService.createTransaction(userId, request);

        ApiResponse<TransactionResponse> response = new ApiResponse<>(
                true,
                "Transaction created successfully",
                data);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/party/{partyId}")
    public ResponseEntity<ApiResponse<List<TransactionResponse>>> getByParty(
            @PathVariable String partyId,
            Authentication authentication) {

        String userId = authentication.getPrincipal().toString();

        List<TransactionResponse> data = transactionService.getTransactionsByParty(userId, partyId);

        ApiResponse<List<TransactionResponse>> response = new ApiResponse<>(
                true,
                "Transactions retrieved successfully",
                data);

        return ResponseEntity.ok(response);
    }
}
