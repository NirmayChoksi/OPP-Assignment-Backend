package com.oopAssignment.financeTracker.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.oopAssignment.financeTracker.model.Bill;
import com.oopAssignment.financeTracker.service.BillService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/bills")
@RequiredArgsConstructor
public class BillController {

    private final BillService billService;

    @PostMapping
    public ResponseEntity<?> createBill(
            @RequestBody Bill bill,
            Authentication authentication
    ) {
        String userId = authentication.getPrincipal().toString();
        Bill saved = billService.createBill(bill, userId);
        return ResponseEntity.ok(saved);
    }
}
