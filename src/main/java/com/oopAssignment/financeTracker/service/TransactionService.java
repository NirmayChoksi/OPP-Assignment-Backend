package com.oopAssignment.financeTracker.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.oopAssignment.financeTracker.model.Transaction;
import com.oopAssignment.financeTracker.repository.TransactionRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;

    public Transaction createTransaction(Transaction tx) {
        return transactionRepository.save(tx);
    }

    public List<Transaction> getTransactionsByParty(String partyId) {
        return transactionRepository.findByPartyId(partyId);
    }
}
