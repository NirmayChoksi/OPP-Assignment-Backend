package com.oopAssignment.financeTracker.model;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document(collection = "bills")
public class Bill {

    @Id
    private String id;

    private String userId;

    private BillType type;

    private String partyId;

    private BigDecimal amount;

    private String note;

    private List<BillItem> items;

    private long createdAt = System.currentTimeMillis();
}
