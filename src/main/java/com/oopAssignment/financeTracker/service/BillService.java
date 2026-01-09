package com.oopAssignment.financeTracker.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.oopAssignment.financeTracker.dto.request.CreateBillRequest;
import com.oopAssignment.financeTracker.dto.response.BillDetailsResponse;
import com.oopAssignment.financeTracker.dto.response.BillItemWithProductResponse;
import com.oopAssignment.financeTracker.dto.response.BillResponse;
import com.oopAssignment.financeTracker.dto.response.BillWithPartyResponse;
import com.oopAssignment.financeTracker.model.*;
import com.oopAssignment.financeTracker.repository.*;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BillService {

    private final ProductRepository productRepo;
    private final PartyRepository partyRepo;
    private final BillRepository billRepo;
    private final TransactionRepository transactionRepo;

    public BillResponse createBill(CreateBillRequest request, String userId) {

        Bill bill = new Bill();
        bill.setUserId(userId);
        bill.setPartyId(request.getPartyId());
        bill.setType(request.getType());
        bill.setNote(request.getNote());
        bill.setItems(request.getItems());
        bill.setAmount(request.getAmount());

        partyRepo.findById(bill.getPartyId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Party not found"));

        BigDecimal totalAmount = BigDecimal.ZERO;

        for (BillItem item : bill.getItems()) {

            Product product = productRepo.findById(item.getProductId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                            "Product not found: " + item.getProductId()));

            if (bill.getType() == BillType.PURCHASE) {
                product.setQuantity(product.getQuantity() + item.getQuantity());
            }

            if (bill.getType() == BillType.SALE) {
                if (product.getQuantity() < item.getQuantity()) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                            "Not enough stock for product: " + product.getName());
                }
                product.setQuantity(product.getQuantity() - item.getQuantity());
            }

            productRepo.save(product);

            // Set price and total for this item
            item.setPrice(product.getPrice());
            BigDecimal itemTotal = product.getPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
            item.setTotalAmount(itemTotal);

            totalAmount = totalAmount.add(itemTotal);
        }

        bill.setAmount(totalAmount);

        Bill savedBill = billRepo.save(bill);

        Transaction transaction = new Transaction();
        transaction.setUserId(userId);
        transaction.setPartyId(bill.getPartyId());
        transaction.setAmount(totalAmount);
        transaction.setNote("Auto-created for Bill: " + savedBill.getId());

        transaction.setType(
                bill.getType() == BillType.SALE ? TransactionType.YOU_GOT : TransactionType.YOU_GAVE);

        transactionRepo.save(transaction);

        return new BillResponse(savedBill);
    }

    public List<BillWithPartyResponse> getBillsByType(String userId, BillType type) {
        List<Bill> bills = billRepo.findByUserIdAndType(userId, type);

        return bills.stream().map(bill -> {
            Party party = partyRepo.findById(bill.getPartyId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Party not found for bill"));

            return new BillWithPartyResponse(
                    bill.getId(),
                    party,
                    bill.getType(),
                    bill.getAmount(),
                    bill.getNote(),
                    bill.getItems(),
                    bill.getCreatedAt());
        }).toList();
    }

    public BillDetailsResponse getBillById(String userId, String billId) {

        Bill bill = billRepo.findById(billId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Bill not found"));

        if (!bill.getUserId().equals(userId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Unauthorized access to bill");
        }

        Party party = partyRepo.findById(bill.getPartyId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Party not found"));

        List<BillItemWithProductResponse> itemsWithProduct = bill.getItems().stream()
                .map(item -> {
                    Product product = productRepo.findById(item.getProductId())
                            .orElseThrow(() -> new RuntimeException("Product not found: " + item.getProductId()));
                    return new BillItemWithProductResponse(
                            item.getProductId(),
                            item.getQuantity(),
                            item.getPrice(),
                            item.getTotalAmount(),
                            product);
                })
                .toList();

        return new BillDetailsResponse(
                bill.getId(),
                bill.getType(),
                bill.getAmount(),
                bill.getNote(),
                party,
                itemsWithProduct,
                bill.getCreatedAt());
    }

}
