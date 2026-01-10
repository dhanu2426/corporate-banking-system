package com.example.demo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "clients")
public class Client {
    
    @Id
    private String id;
    
    private String companyName;
    private String industry;
    private String address;
    
    private PrimaryContact primaryContact;
    
    private Double annualTurnover;
    private Boolean documentsSubmitted = false;
    
    private String rmId; // Relationship Manager ID
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PrimaryContact {
        private String name;
        private String email;
        private String phone;
    }
}