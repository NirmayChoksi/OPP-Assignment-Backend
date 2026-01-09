package com.oopAssignment.financeTracker.model;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
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

    @CreatedDate
    private Instant createdAt;

    @LastModifiedDate
    private Instant updatedAt;
}
