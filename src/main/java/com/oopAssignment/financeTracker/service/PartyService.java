package com.oopAssignment.financeTracker.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.oopAssignment.financeTracker.dto.request.CreatePartyRequest;
import com.oopAssignment.financeTracker.dto.response.PartyResponse;
import com.oopAssignment.financeTracker.model.Party;
import com.oopAssignment.financeTracker.repository.PartyRepository;

@Service
public class PartyService {

    private final PartyRepository partyRepository;

    public PartyService(PartyRepository partyRepository) {
        this.partyRepository = partyRepository;
    }

    public PartyResponse createParty(String userId, CreatePartyRequest request) {
        if (!request.getType().equalsIgnoreCase("CUSTOMER") &&
            !request.getType().equalsIgnoreCase("SUPPLIER")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Type must be CUSTOMER or SUPPLIER");
        }

        Party party = new Party(
                request.getName(),
                request.getContactNumber(),
                userId,
                request.getType().toUpperCase(),
                request.getGstNumber()
        );

        Party saved = partyRepository.save(party);
        return new PartyResponse(saved);
    }

    public List<PartyResponse> getAllParties(String userId) {
        return partyRepository.findByUserId(userId)
                .stream()
                .map(PartyResponse::new)
                .collect(Collectors.toList());
    }

    public List<PartyResponse> getPartiesByType(String userId, String type) {
        return partyRepository.findByUserIdAndType(userId, type.toUpperCase())
                .stream()
                .map(PartyResponse::new)
                .collect(Collectors.toList());
    }
}
