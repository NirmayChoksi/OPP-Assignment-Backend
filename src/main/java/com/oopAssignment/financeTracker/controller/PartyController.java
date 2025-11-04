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
import com.oopAssignment.financeTracker.dto.response.PartyResponse;
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
    public ResponseEntity<PartyResponse> createParty(
            @Valid @RequestBody CreatePartyRequest request,
            Authentication authentication) {

        String userId = (String) authentication.getPrincipal();
        return ResponseEntity.ok(partyService.createParty(userId, request));
    }

    @GetMapping
    public ResponseEntity<List<PartyResponse>> getAllParties(Authentication authentication) {
        String userId = (String) authentication.getPrincipal();
        return ResponseEntity.ok(partyService.getAllParties(userId));
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<List<PartyResponse>> getPartiesByType(
            @PathVariable String type,
            Authentication authentication) {

        String userId = (String) authentication.getPrincipal();
        return ResponseEntity.ok(partyService.getPartiesByType(userId, type));
    }
}
