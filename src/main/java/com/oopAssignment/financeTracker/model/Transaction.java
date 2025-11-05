package com.oopAssignment.financeTracker.model;

import java.time.LocalDate;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document(collection = "transactions")
public class Transaction {

    @Id
    private String id;

    private String partyId;  // reference to Party document

    private TransactionType type; // YOU_GAVE or YOU_GOT

    private double amount;

    private LocalDate date = LocalDate.now();

    private String note;
}
