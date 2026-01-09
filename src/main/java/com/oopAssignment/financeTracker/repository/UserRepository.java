package com.oopAssignment.financeTracker.repository;

import com.oopAssignment.financeTracker.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String> {
    User findByContactNumber(String contactNumber);
}
