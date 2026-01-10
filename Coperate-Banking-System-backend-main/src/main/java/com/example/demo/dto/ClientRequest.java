package com.example.demo.dto;


import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class ClientRequest {
    
    @NotBlank(message = "Company name is required")
    private String companyName;
    
    @NotBlank(message = "Industry is required")
    private String industry;
    
    @NotBlank(message = "Address is required")
    private String address;
    
    @NotNull(message = "Primary contact is required")
    private PrimaryContactDto primaryContact;
    
    @NotNull(message = "Annual turnover is required")
    @Positive(message = "Annual turnover must be positive")
    private Double annualTurnover;
    
    private Boolean documentsSubmitted = false;
    
    @Data
    public static class PrimaryContactDto {
        @NotBlank(message = "Contact name is required")
        private String name;
        
        @NotBlank(message = "Contact email is required")
        @Email(message = "Contact email should be valid")
        private String email;
        
        @NotBlank(message = "Contact phone is required")
        @Pattern(regexp = "^[0-9]{10}$", message = "Phone number should be 10 digits")
        private String phone;
    }
}