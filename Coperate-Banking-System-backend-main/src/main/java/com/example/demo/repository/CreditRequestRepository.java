package com.example.demo.repository;

import com.example.demo.model.CreditRequest;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CreditRequestRepository extends MongoRepository<CreditRequest, String> {
    List<CreditRequest> findBySubmittedBy(String submittedBy);
    List<CreditRequest> findByClientId(String clientId);
    List<CreditRequest> findByStatus(String status);
}