package com.example.demo.controller;


import com.example.demo.dto.UpdateStatusRequest;
import com.example.demo.model.CreditRequest;
import com.example.demo.service.CreditRequestService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/analyst")
public class AnalystController {
    
    @Autowired
    private CreditRequestService creditRequestService;
    
    @GetMapping("/credit-requests")
    public ResponseEntity<List<CreditRequest>> getAllCreditRequests() {
        List<CreditRequest> requests = creditRequestService.getAllCreditRequests();
        return ResponseEntity.ok(requests);
    }
    
    @GetMapping("/credit-requests/{id}")
    public ResponseEntity<CreditRequest> getCreditRequestById(@PathVariable String id) {
        CreditRequest request = creditRequestService.getCreditRequestById(id);
        return ResponseEntity.ok(request);
    }
    
    @PutMapping("/credit-requests/{id}")
    public ResponseEntity<CreditRequest> updateCreditRequestStatus(
            @PathVariable String id,
            @Valid @RequestBody UpdateStatusRequest request) {
        
        CreditRequest creditRequest = creditRequestService.updateCreditRequestStatus(id, request);
        return ResponseEntity.ok(creditRequest);
    }
}