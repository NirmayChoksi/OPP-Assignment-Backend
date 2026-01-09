package com.oopAssignment.financeTracker.dto.response;

import java.math.BigDecimal;

import com.oopAssignment.financeTracker.model.Product;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BillItemWithProductResponse {
    private String productId;
    private int quantity;
    private BigDecimal price;
    private BigDecimal totalAmount;
    private Product product;
}
