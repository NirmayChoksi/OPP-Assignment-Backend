package com.oopAssignment.financeTracker.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.oopAssignment.financeTracker.model.Party;
import com.oopAssignment.financeTracker.model.PartyType;

public interface PartyRepository extends MongoRepository<Party, String> {
    List<Party> findByUserId(String userId);

    List<Party> findByUserIdAndType(String userId, PartyType type);
}
