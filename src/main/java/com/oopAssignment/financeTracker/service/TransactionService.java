package com.oopAssignment.financeTracker.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.oopAssignment.financeTracker.dto.request.CreateTransactionRequest;
import com.oopAssignment.financeTracker.dto.response.TransactionResponse;
import com.oopAssignment.financeTracker.model.Party;
import com.oopAssignment.financeTracker.model.Transaction;
import com.oopAssignment.financeTracker.model.TransactionType;
import com.oopAssignment.financeTracker.repository.PartyRepository;
import com.oopAssignment.financeTracker.repository.TransactionRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final PartyRepository partyRepository;

    public TransactionResponse createTransaction(String userId, CreateTransactionRequest request) {

        Party party = partyRepository.findById(request.getPartyId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Party not found"));

        if (!party.getUserId().equals(userId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "You are not allowed to add transactions to this party");
        }

        TransactionType type = parseTransactionType(request.getType());

        Transaction tx = new Transaction();
        tx.setPartyId(request.getPartyId());
        tx.setUserId(userId);
        tx.setType(type);
        tx.setAmount(request.getAmount());
        tx.setNote(request.getNote());

        Transaction saved = transactionRepository.save(tx);

        return new TransactionResponse(saved);
    }

    public List<TransactionResponse> getTransactionsByParty(String userId, String partyId) {

        Party party = partyRepository.findById(partyId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Party not found"));

        if (!party.getUserId().equals(userId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "You are not allowed to view transactions of this party");
        }

        return transactionRepository.findByPartyId(partyId)
                .stream()
                .map(TransactionResponse::new)
                .toList();
    }

    private TransactionType parseTransactionType(String type) {
        try {
            return TransactionType.valueOf(type.trim().toUpperCase());
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Type must be YOU_GAVE or YOU_GOT");
        }
    }
}
