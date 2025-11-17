package com.oopAssignment.financeTracker.service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.oopAssignment.financeTracker.dto.request.CreatePartyRequest;
import com.oopAssignment.financeTracker.dto.response.PartyDetailResponse;
import com.oopAssignment.financeTracker.dto.response.PartyListSummaryResponse;
import com.oopAssignment.financeTracker.dto.response.PartyResponse;
import com.oopAssignment.financeTracker.dto.response.PartyWithBalanceResponse;
import com.oopAssignment.financeTracker.model.Party;
import com.oopAssignment.financeTracker.model.PartyType;
import com.oopAssignment.financeTracker.model.Transaction;
import com.oopAssignment.financeTracker.model.TransactionType;
import com.oopAssignment.financeTracker.repository.PartyRepository;
import com.oopAssignment.financeTracker.repository.TransactionRepository;

@Service
public class PartyService {

    private final PartyRepository partyRepository;
    private final TransactionRepository transactionRepository;

    public PartyService(PartyRepository partyRepository, TransactionRepository transactionRepository) {
        this.partyRepository = partyRepository;
        this.transactionRepository = transactionRepository;
    }

    public PartyResponse createParty(String userId, CreatePartyRequest request) {

        PartyType partyType = parsePartyType(request.getType());

        if (partyRepository.existsByUserIdAndContactNumber(userId, request.getContactNumber())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "A party with this contact number already exists for this user.");
        }

        Party party = new Party(null, request.getName(), request.getContactNumber(), userId, partyType, null, null,
                request.getGstNumber(), null, null);

        Party saved = partyRepository.save(party);
        return new PartyResponse(saved);
    }

    public List<PartyResponse> getAllParties(String userId) {
        return partyRepository.findByUserId(userId)
                .stream()
                .map(PartyResponse::new)
                .collect(Collectors.toList());
    }

    public PartyListSummaryResponse getPartiesByType(String userId, PartyType type) {

        List<Party> parties = partyRepository.findByUserIdAndType(userId, type);

        BigDecimal totalYouGave = BigDecimal.ZERO;
        BigDecimal totalYouGot = BigDecimal.ZERO;

        List<PartyWithBalanceResponse> responseList = new ArrayList<>();

        for (Party party : parties) {

            List<Transaction> txList = transactionRepository.findByPartyId(party.getId());

            BigDecimal youGave = txList.stream()
                    .filter(tx -> tx.getType() == TransactionType.YOU_GAVE)
                    .map(Transaction::getAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            BigDecimal youGot = txList.stream()
                    .filter(tx -> tx.getType() == TransactionType.YOU_GOT)
                    .map(Transaction::getAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            BigDecimal balance = youGave.subtract(youGot);

            if (balance.compareTo(BigDecimal.ZERO) > 0) {
                totalYouGot = totalYouGot.add(balance);
            } else if (balance.compareTo(BigDecimal.ZERO) < 0) {
                totalYouGave = totalYouGave.add(balance.abs());
            }

            Instant lastTransactionAt = txList.stream()
                    .map(Transaction::getCreatedAt)
                    .max(Instant::compareTo)
                    .orElse(null);

            responseList.add(
                    new PartyWithBalanceResponse(party.getId(), party.getName(), party.getContactNumber(), youGave,
                            youGot, balance,
                            lastTransactionAt));
        }

        BigDecimal netBalance = totalYouGot.subtract(totalYouGave);

        return new PartyListSummaryResponse(
                totalYouGave,
                totalYouGot,
                netBalance,
                responseList);
    }

    public PartyDetailResponse getPartyWithTransactions(String userId, String partyId) {
        Party party = partyRepository.findById(partyId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Party not found"));

        if (!party.getUserId().equals(userId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not authorized to view this party");
        }

        List<Transaction> transactions = transactionRepository.findByPartyId(partyId);

        return new PartyDetailResponse(
                party.getId(),
                party.getName(),
                party.getContactNumber(),
                party.getType().name(),
                party.getGstNumber(),
                party.getCreatedAt(),
                party.getUpdatedAt(),
                transactions);
    }

    private PartyType parsePartyType(String type) {
        try {
            return PartyType.valueOf(type.trim().toUpperCase());
        } catch (Exception ex) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Type must be CUSTOMER or SUPPLIER");
        }
    }
}
