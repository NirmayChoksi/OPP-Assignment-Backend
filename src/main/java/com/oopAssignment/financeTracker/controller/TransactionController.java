package com.oopAssignment.financeTracker.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.oopAssignment.financeTracker.model.Transaction;
import com.oopAssignment.financeTracker.service.TransactionService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/transaction")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping
    public Transaction createTransaction(@RequestBody Transaction tx) {
        return transactionService.createTransaction(tx);
    }

    @GetMapping("/party/{partyId}")
    public List<Transaction> getByParty(@PathVariable String partyId) {
        return transactionService.getTransactionsByParty(partyId);
    }
}
