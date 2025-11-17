package com.oopAssignment.financeTracker.model;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class BillItem {

    private String productId;

    private int quantity;

    private BigDecimal price;

    private BigDecimal totalAmount;
}
