package com.oopAssignment.financeTracker.dto.response;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

import com.oopAssignment.financeTracker.model.BillType;
import com.oopAssignment.financeTracker.model.Party;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BillDetailsResponse {
    private String id;
    private BillType type;
    private BigDecimal amount;
    private String note;
    private Party party;
    private List<BillItemWithProductResponse> items;
    private Instant createdAt;
}
