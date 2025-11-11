package com.oopAssignment.financeTracker.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.oopAssignment.financeTracker.model.BillItem;
import com.oopAssignment.financeTracker.model.BillType;

import lombok.Data;

@Data
public class BillResponse {

    private String id;
    private BillType type;
    private String partyId;
    private BigDecimal amount;
    private String note;
    private List<BillItem> items;
    private LocalDateTime createdAt;

    // Summary
    private int totalItems;
    private BigDecimal totalAmount;
    private String balanceDirection; // YOU_GOT / YOU_GAVE / NONE
}
