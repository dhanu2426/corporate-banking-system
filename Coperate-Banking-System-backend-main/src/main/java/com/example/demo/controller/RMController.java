package com.example.demo.controller;


import com.example.demo.dto.ClientRequest;
import com.example.demo.dto.CreditRequestDto;
import com.example.demo.model.Client;
import com.example.demo.model.CreditRequest;
import com.example.demo.security.JwtUtil;
import com.example.demo.service.ClientService;
import com.example.demo.service.CreditRequestService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rm")
public class RMController {
    
    @Autowired
    private ClientService clientService;
    
    @Autowired
    private CreditRequestService creditRequestService;
    
    @Autowired
    private JwtUtil jwtUtil;
    
    // Extract RM ID from JWT token
    private String extractRmId(String authHeader) {
        String token = authHeader.substring(7);
        return jwtUtil.extractUserId(token);
    }
    

    @PostMapping("/clients")
    public ResponseEntity<Client> createClient(
            @Valid @RequestBody ClientRequest request,
            @RequestHeader("Authorization") String authHeader) {
        
        String rmId = extractRmId(authHeader);
        Client client = clientService.createClient(request, rmId);
        return ResponseEntity.ok(client);
    }
    
    @GetMapping("/clients")
    public ResponseEntity<List<Client>> getMyClients(
            @RequestHeader("Authorization") String authHeader) {
        
        String rmId = extractRmId(authHeader);
        List<Client> clients = clientService.getClientsByRm(rmId);
        return ResponseEntity.ok(clients);
    }
    
    @GetMapping("/clients/{id}")
    public ResponseEntity<Client> getClientById(@PathVariable String id) {
        Client client = clientService.getClientById(id);
        return ResponseEntity.ok(client);
    }
    
    @PutMapping("/clients/{id}")
    public ResponseEntity<Client> updateClient(
            @PathVariable String id,
            @Valid @RequestBody ClientRequest request,
            @RequestHeader("Authorization") String authHeader) {
        
        String rmId = extractRmId(authHeader);
        Client client = clientService.updateClient(id, request, rmId);
        return ResponseEntity.ok(client);
    }
    

    @PostMapping("/credit-requests")
    public ResponseEntity<CreditRequest> createCreditRequest(
            @Valid @RequestBody CreditRequestDto dto,
            @RequestHeader("Authorization") String authHeader) {
        
        String rmId = extractRmId(authHeader);
        CreditRequest creditRequest = creditRequestService.createCreditRequest(dto, rmId);
        return ResponseEntity.ok(creditRequest);
    }
    
    @GetMapping("/credit-requests")
    public ResponseEntity<List<CreditRequest>> getMyCreditRequests(
            @RequestHeader("Authorization") String authHeader) {
        
        String rmId = extractRmId(authHeader);
        List<CreditRequest> requests = creditRequestService.getCreditRequestsByRm(rmId);
        return ResponseEntity.ok(requests);
    }
    
    @GetMapping("/credit-requests/{id}")
    public ResponseEntity<CreditRequest> getCreditRequestById(@PathVariable String id) {
        CreditRequest request = creditRequestService.getCreditRequestById(id);
        return ResponseEntity.ok(request);
    }
}