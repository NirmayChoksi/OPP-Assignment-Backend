package com.oopAssignment.financeTracker.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.oopAssignment.financeTracker.dto.request.CreateBillRequest;
import com.oopAssignment.financeTracker.dto.response.ApiResponse;
import com.oopAssignment.financeTracker.dto.response.BillDetailsResponse;
import com.oopAssignment.financeTracker.dto.response.BillResponse;
import com.oopAssignment.financeTracker.dto.response.BillWithPartyResponse;
import com.oopAssignment.financeTracker.model.BillType;
import com.oopAssignment.financeTracker.service.BillService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/bills")
@RequiredArgsConstructor
public class BillController {

    private final BillService billService;

    @PostMapping
    public ResponseEntity<ApiResponse<BillResponse>> createBill(
            @RequestBody CreateBillRequest bill,
            Authentication authentication) {

        String userId = authentication.getPrincipal().toString();
        BillResponse data = billService.createBill(bill, userId);

        ApiResponse<BillResponse> response = new ApiResponse<>(
                true,
                "Bill created successfully",
                data);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<ApiResponse<List<BillWithPartyResponse>>> getBillsByType(
            @PathVariable BillType type,
            Authentication authentication) {

        String userId = authentication.getPrincipal().toString();
        List<BillWithPartyResponse> data = billService.getBillsByType(userId, type);

        ApiResponse<List<BillWithPartyResponse>> response = new ApiResponse<>(
                true,
                "Bills fetched successfully",
                data);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{billId}")
    public ResponseEntity<ApiResponse<BillDetailsResponse>> getBillById(
            @PathVariable String billId,
            Authentication authentication) {

        String userId = authentication.getPrincipal().toString();
        BillDetailsResponse data = billService.getBillById(userId, billId);

        ApiResponse<BillDetailsResponse> response = new ApiResponse<>(
                true,
                "Bill fetched successfully",
                data);

        return ResponseEntity.ok(response);
    }

}
