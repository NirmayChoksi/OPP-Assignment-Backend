package com.oopAssignment.financeTracker.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.oopAssignment.financeTracker.dto.request.CreatePartyRequest;
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

        Party party = new Party(
                null,
                request.getName(),
                request.getContactNumber(),
                userId,
                partyType,
                request.getGstNumber());

        Party saved = partyRepository.save(party);
        return new PartyResponse(saved);
    }

    public List<PartyResponse> getAllParties(String userId) {
        return partyRepository.findByUserId(userId)
                .stream()
                .map(PartyResponse::new)
                .collect(Collectors.toList());
    }

    // public List<PartyResponse> getPartiesByType(String userId, String type) {

    // PartyType partyType = parsePartyType(type);

    // return partyRepository.findByUserIdAndType(userId, partyType)
    // .stream()
    // .map(PartyResponse::new)
    // .collect(Collectors.toList());
    // }

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

            // ✅ Accumulate totals
            totalYouGave = totalYouGave.add(youGave);
            totalYouGot = totalYouGot.add(youGot);

            responseList.add(
                    new PartyWithBalanceResponse(
                            party.getId(),
                            party.getName(),
                            party.getContactNumber(),
                            youGave,
                            youGot,
                            balance));
        }

        BigDecimal netBalance = totalYouGave.subtract(totalYouGot);

        return new PartyListSummaryResponse(
                totalYouGave,
                totalYouGot,
                netBalance,
                responseList);
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
