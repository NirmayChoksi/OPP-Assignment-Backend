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

import com.oopAssignment.financeTracker.dto.request.CreatePartyRequest;
import com.oopAssignment.financeTracker.dto.response.ApiResponse;
import com.oopAssignment.financeTracker.dto.response.PartyListSummaryResponse;
import com.oopAssignment.financeTracker.dto.response.PartyResponse;
import com.oopAssignment.financeTracker.model.PartyType;
import com.oopAssignment.financeTracker.service.PartyService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/parties")
public class PartyController {

    private final PartyService partyService;

    public PartyController(PartyService partyService) {
        this.partyService = partyService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<PartyResponse>> createParty(
            @Valid @RequestBody CreatePartyRequest request,
            Authentication authentication) {

        String userId = authentication.getPrincipal().toString();

        PartyResponse data = partyService.createParty(userId, request);

        ApiResponse<PartyResponse> response = new ApiResponse<>(
                true,
                "Party created successfully",
                data);

        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<PartyResponse>>> getAllParties(Authentication authentication) {
        String userId = authentication.getPrincipal().toString();

        List<PartyResponse> data = partyService.getAllParties(userId);

        ApiResponse<List<PartyResponse>> response = new ApiResponse<>(
                true,
                "Parties retrieved successfully",
                data);

        return ResponseEntity.ok(response);
    }

    // @GetMapping("/type/{type}")
    // public ResponseEntity<ApiResponse<List<PartyResponse>>> getPartiesByType(
    // @PathVariable String type,
    // Authentication authentication) {

    // String userId = authentication.getPrincipal().toString();

    // List<PartyResponse> data = partyService.getPartiesByType(userId, type);

    // ApiResponse<List<PartyResponse>> response = new ApiResponse<>(
    // true,
    // "Parties filtered by type retrieved successfully",
    // data);

    // return ResponseEntity.ok(response);
    // }

    @GetMapping("/type/{type}")
    public ResponseEntity<ApiResponse<PartyListSummaryResponse>> getPartiesByType(
            @PathVariable PartyType type,
            Authentication authentication) {

        String userId = authentication.getPrincipal().toString();

        PartyListSummaryResponse data = partyService.getPartiesByType(userId, type);

        ApiResponse<PartyListSummaryResponse> response = new ApiResponse<>(
                true,
                "Parties fetched successfully",
                data);

        return ResponseEntity.ok(response);
    }

}
