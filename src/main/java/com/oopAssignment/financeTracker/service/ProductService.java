package com.oopAssignment.financeTracker.service;

import com.oopAssignment.financeTracker.dto.request.ProductRequest;
import com.oopAssignment.financeTracker.dto.response.ProductResponse;
import com.oopAssignment.financeTracker.model.Product;
import com.oopAssignment.financeTracker.repository.ProductRepository;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    // ✅ Add Product
    public ProductResponse addProduct(String userId, ProductRequest request) {

        Product product = new Product();
        product.setUserId(userId);
        product.setName(request.getName());
        product.setQuantity(request.getQuantity());
        product.setPrice(request.getPrice()); // BigDecimal

        Product saved = productRepository.save(product);

        return convert(saved);
    }

    // ✅ Update product
    public ProductResponse updateProduct(String userId, String productId, ProductRequest request) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        if (!product.getUserId().equals(userId)) {
            throw new RuntimeException("Unauthorized request");
        }

        product.setName(request.getName());
        product.setQuantity(request.getQuantity());
        product.setPrice(request.getPrice());

        Product saved = productRepository.save(product);
        return convert(saved);
    }

    // ✅ Delete product
    public void deleteProduct(String userId, String productId) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        if (!product.getUserId().equals(userId)) {
            throw new RuntimeException("Unauthorized request");
        }

        productRepository.delete(product);
    }

    // ✅ Get all products for user
    public List<ProductResponse> getAllProducts(String userId) {
        return productRepository.findByUserId(userId)
                .stream()
                .map(this::convert)
                .toList();
    }

    private ProductResponse convert(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getQuantity(),
                product.getPrice()
        );
    }
}
