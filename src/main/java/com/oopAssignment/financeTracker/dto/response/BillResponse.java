package com.oopAssignment.financeTracker.dto.response;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

import com.oopAssignment.financeTracker.model.Bill;
import com.oopAssignment.financeTracker.model.BillItem;
import com.oopAssignment.financeTracker.model.BillType;

import lombok.Data;

@Data
public class BillResponse {
    private String id;
    private String partyId;
    private BillType type;
    private BigDecimal amount;
    private String note;
    private List<BillItem> items;
    private Instant createdAt;

    public BillResponse(Bill bill) {
        this.id = bill.getId();
        this.partyId = bill.getPartyId();
        this.type = bill.getType();
        this.amount = bill.getAmount();
        this.note = bill.getNote();
        this.items = bill.getItems();
        this.createdAt = bill.getCreatedAt();
    }
}
