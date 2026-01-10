package com.example.demo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "creditRequests")
public class CreditRequest {
    
    @Id
    private String id;
    
    private String clientId;
    private String submittedBy; // RM user ID
    
    private Double requestAmount;
    private Integer tenureMonths;
    private String purpose;
    
    private String status = "Pending"; // Pending, Approved, Rejected
    private String remarks = "";
    
    @CreatedDate
    private LocalDateTime createdAt;
}