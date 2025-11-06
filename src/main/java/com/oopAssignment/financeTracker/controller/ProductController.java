package com.oopAssignment.financeTracker.controller;


import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.oopAssignment.financeTracker.dto.request.ProductRequest;
import com.oopAssignment.financeTracker.dto.response.ProductResponse;
import com.oopAssignment.financeTracker.service.ProductService;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public ResponseEntity<ProductResponse> addProduct(
            @RequestBody ProductRequest request,
            Authentication auth) {

        String userId = (String) auth.getPrincipal();
        return ResponseEntity.ok(productService.addProduct(userId, request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> updateProduct(
            @PathVariable String id,
            @RequestBody ProductRequest request,
            Authentication auth) {

        String userId = (String) auth.getPrincipal();
        return ResponseEntity.ok(productService.updateProduct(userId, id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(
            @PathVariable String id,
            Authentication auth) {

        String userId = (String) auth.getPrincipal();
        productService.deleteProduct(userId, id);
        return ResponseEntity.ok("Product deleted");
    }

    @GetMapping
    public ResponseEntity<List<ProductResponse>> getProducts(Authentication auth) {
        String userId = (String) auth.getPrincipal();
        return ResponseEntity.ok(productService.getAllProducts(userId));
    }
}
