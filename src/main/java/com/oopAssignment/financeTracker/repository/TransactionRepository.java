package com.oopAssignment.financeTracker.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.oopAssignment.financeTracker.model.Transaction;

public interface TransactionRepository extends MongoRepository<Transaction, String> {
    List<Transaction> findByPartyId(String partyId);
}
