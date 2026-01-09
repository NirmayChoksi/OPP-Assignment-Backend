package com.oopAssignment.financeTracker.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.oopAssignment.financeTracker.model.Bill;
import com.oopAssignment.financeTracker.model.BillType;

public interface BillRepository extends MongoRepository<Bill, String> {

    List<Bill> findByUserId(String userId);

    List<Bill> findByUserIdAndType(String userId, BillType type);

}
