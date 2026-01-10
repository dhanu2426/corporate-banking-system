package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class UpdateStatusRequest {
    
    @NotBlank(message = "Status is required")
    @Pattern(regexp = "Pending|Approved|Rejected", message = "Status must be Pending, Approved, or Rejected")
    private String status;
    
    private String remarks;
}