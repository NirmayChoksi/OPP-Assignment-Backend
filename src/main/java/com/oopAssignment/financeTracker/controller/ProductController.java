package com.oopAssignment.financeTracker.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.oopAssignment.financeTracker.dto.request.ProductRequest;
import com.oopAssignment.financeTracker.dto.response.ApiResponse;
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
    public ResponseEntity<ApiResponse<ProductResponse>> addProduct(
            @RequestBody ProductRequest request,
            Authentication auth) {

        String userId = (String) auth.getPrincipal();
        ProductResponse data = productService.addProduct(userId, request);

        ApiResponse<ProductResponse> response = new ApiResponse<>(
                true,
                "Product added successfully",
                data);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductResponse>> updateProduct(
            @PathVariable String id,
            @RequestBody ProductRequest request,
            Authentication auth) {

        String userId = (String) auth.getPrincipal();
        ProductResponse data = productService.updateProduct(userId, id, request);

        ApiResponse<ProductResponse> response = new ApiResponse<>(
                true,
                "Product updated successfully",
                data);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteProduct(
            @PathVariable String id,
            Authentication auth) {

        String userId = (String) auth.getPrincipal();
        productService.deleteProduct(userId, id);

        ApiResponse<String> response = new ApiResponse<>(
                true,
                "Product deleted successfully",
                "Deleted");

        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ProductResponse>>> getProducts(Authentication auth) {
        String userId = (String) auth.getPrincipal();

        List<ProductResponse> data = productService.getAllProducts(userId);

        ApiResponse<List<ProductResponse>> response = new ApiResponse<>(
                true,
                "Products retrieved successfully",
                data);

        return ResponseEntity.ok(response);
    }
}
