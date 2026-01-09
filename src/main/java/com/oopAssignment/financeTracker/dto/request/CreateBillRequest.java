package com.oopAssignment.financeTracker.dto.request;

import java.math.BigDecimal;
import java.util.List;

import com.oopAssignment.financeTracker.model.BillItem;
import com.oopAssignment.financeTracker.model.BillType;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateBillRequest {

    @NotNull(message = "Party ID is required")
    private String partyId;

    @NotNull(message = "Bill type is required")
    private BillType type; // SALE or PURCHASE

    @NotNull(message = "Amount is required")
    private BigDecimal amount;

    private String note;

    @NotNull(message = "Items are required")
    private List<BillItem> items;
}
