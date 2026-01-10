package com.example.demo.service;


import com.example.demo.dto.CreditRequestDto;
import com.example.demo.dto.UpdateStatusRequest;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.CreditRequest;
import com.example.demo.repository.CreditRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CreditRequestService {
    
    @Autowired
    private CreditRequestRepository creditRequestRepository;
    
    public CreditRequest createCreditRequest(CreditRequestDto dto, String rmId) {
        CreditRequest creditRequest = new CreditRequest();
        creditRequest.setClientId(dto.getClientId());
        creditRequest.setSubmittedBy(rmId);
        creditRequest.setRequestAmount(dto.getRequestAmount());
        creditRequest.setTenureMonths(dto.getTenureMonths());
        creditRequest.setPurpose(dto.getPurpose());
        creditRequest.setStatus("Pending");
        creditRequest.setRemarks("");
        creditRequest.setCreatedAt(LocalDateTime.now());
        
        return creditRequestRepository.save(creditRequest);
    }
    
    public List<CreditRequest> getCreditRequestsByRm(String rmId) {
        return creditRequestRepository.findBySubmittedBy(rmId);
    }
    
    public List<CreditRequest> getAllCreditRequests() {
        return creditRequestRepository.findAll();
    }
    
    public CreditRequest getCreditRequestById(String requestId) {
        return creditRequestRepository.findById(requestId)
                .orElseThrow(() -> new ResourceNotFoundException("Credit request not found"));
    }
    
    public CreditRequest updateCreditRequestStatus(String requestId, UpdateStatusRequest request) {
        CreditRequest creditRequest = creditRequestRepository.findById(requestId)
                .orElseThrow(() -> new ResourceNotFoundException("Credit request not found"));
        
        creditRequest.setStatus(request.getStatus());
        if (request.getRemarks() != null) {
            creditRequest.setRemarks(request.getRemarks());
        }
        
        return creditRequestRepository.save(creditRequest);
    }
}