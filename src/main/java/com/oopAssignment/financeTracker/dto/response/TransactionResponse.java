package com.oopAssignment.financeTracker.dto.response;

import java.math.BigDecimal;

import com.oopAssignment.financeTracker.model.Transaction;
import com.oopAssignment.financeTracker.model.TransactionType;

import lombok.Data;

@Data
public class TransactionResponse {

    private String id;

    private String partyId;

    private String userId;

    private TransactionType type;

    private BigDecimal amount;

    private String note;

    public TransactionResponse(Transaction tx) {
        this.id = tx.getId();
        this.partyId = tx.getPartyId();
        this.userId = tx.getUserId();
        this.type = tx.getType();
        this.amount = tx.getAmount();
        this.note = tx.getNote();
    }
}
