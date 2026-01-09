package com.oopAssignment.financeTracker.dto.response;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

import com.oopAssignment.financeTracker.model.BillItem;
import com.oopAssignment.financeTracker.model.BillType;
import com.oopAssignment.financeTracker.model.Party;

import lombok.Data;

@Data
public class BillWithPartyResponse {
    private String id;
    private Party party;
    private BillType type;
    private BigDecimal amount;
    private String note;
    private List<BillItem> items;
    private Instant createdAt;

    public BillWithPartyResponse(String id, Party party, BillType type, BigDecimal amount,
            String note, List<BillItem> items, Instant createdAt) {
        this.id = id;
        this.party = party;
        this.type = type;
        this.amount = amount;
        this.note = note;
        this.items = items;
        this.createdAt = createdAt;
    }
}
