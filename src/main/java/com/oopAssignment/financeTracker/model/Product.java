package com.oopAssignment.financeTracker.model;

import java.math.BigDecimal;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document(collection = "products")
public class Product {

    @Id
    private String id;

    private String name;

    private BigDecimal price;

    private int quantity;

    private String userId;
}
