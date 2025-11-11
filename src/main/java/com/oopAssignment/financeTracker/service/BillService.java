package com.oopAssignment.financeTracker.service;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import com.oopAssignment.financeTracker.model.Bill;
import com.oopAssignment.financeTracker.model.BillItem;
import com.oopAssignment.financeTracker.model.BillType;
import com.oopAssignment.financeTracker.model.Party;
import com.oopAssignment.financeTracker.model.Product;
import com.oopAssignment.financeTracker.model.Transaction;
import com.oopAssignment.financeTracker.model.TransactionType;
import com.oopAssignment.financeTracker.repository.BillRepository;
import com.oopAssignment.financeTracker.repository.PartyRepository;
import com.oopAssignment.financeTracker.repository.ProductRepository;
import com.oopAssignment.financeTracker.repository.TransactionRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BillService {

    private final ProductRepository productRepo;
    private final PartyRepository partyRepo;
    private final BillRepository billRepo;
    private final TransactionRepository transactionRepo;

    public Bill createBill(Bill bill, String userId) {

        // ✅ Attach userId from Authentication token
        bill.setUserId(userId);

        // ✅ Validate party
        Party party = partyRepo.findById(bill.getPartyId())
                .orElseThrow(() -> new RuntimeException("Party not found"));

        BigDecimal totalAmount = BigDecimal.ZERO;

        // ✅ Process bill items
        for (BillItem item : bill.getItems()) {

            Product product = productRepo.findById(item.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found: " + item.getProductId()));

            // ✅ Update stock based on bill type
            if (bill.getType() == BillType.PURCHASE) {
                // Purchase → Add stock
                product.setQuantity(product.getQuantity() + item.getQuantity());
            }

            if (bill.getType() == BillType.SALE) {
                // Sale → Reduce stock (but prevent negative)
                if (product.getQuantity() < item.getQuantity()) {
                    throw new RuntimeException("Not enough stock for product: " + product.getName());
                }
                product.setQuantity(product.getQuantity() - item.getQuantity());
            }

            // ✅ Save updated product stock
            productRepo.save(product);

            // ✅ Set price & calculate totals
            item.setPrice(product.getPrice());
            BigDecimal itemTotal = product.getPrice()
                    .multiply(BigDecimal.valueOf(item.getQuantity()));

            item.setTotalAmount(itemTotal);

            totalAmount = totalAmount.add(itemTotal);
        }

        // ✅ Set bill total
        bill.setAmount(totalAmount);

        // ✅ Save Bill first
        Bill savedBill = billRepo.save(bill);

        // ✅ Create corresponding Transaction for the Party
        Transaction transaction = new Transaction();
        transaction.setUserId(userId);
        transaction.setPartyId(bill.getPartyId());
        transaction.setAmount(totalAmount);
        transaction.setNote("Auto-created for Bill: " + savedBill.getId());

        // ✅ Determine transaction type based on bill type
        if (bill.getType() == BillType.SALE) {
            transaction.setType(TransactionType.YOU_GOT);
        } else if (bill.getType() == BillType.PURCHASE) {
            transaction.setType(TransactionType.YOU_GAVE);
        } else {
            transaction.setType(TransactionType.YOU_GAVE); // Default for EXPENSE or others
        }

        // ✅ Save transaction
        transactionRepo.save(transaction);

        return savedBill;
    }
}
