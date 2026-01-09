package com.oopAssignment.financeTracker.controller;

import java.io.ByteArrayInputStream;
import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.oopAssignment.financeTracker.dto.request.CreateTransactionRequest;
import com.oopAssignment.financeTracker.dto.response.ApiResponse;
import com.oopAssignment.financeTracker.dto.response.TransactionResponse;
import com.oopAssignment.financeTracker.service.PdfService;
import com.oopAssignment.financeTracker.service.TransactionService;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;
    private final PdfService pdfService;

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

    @GetMapping("/download")
    public ResponseEntity<byte[]> downloadTransactionsPdf(Authentication authentication) throws Exception {

        String userId = authentication.getPrincipal().toString();
        var transactions = transactionService.getTransactionsByUser(userId);
        ByteArrayInputStream bis = pdfService.generateTransactionsPdf(transactions);

        byte[] pdfBytes = bis.readAllBytes();

        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=transactions.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdfBytes);
    }

}
