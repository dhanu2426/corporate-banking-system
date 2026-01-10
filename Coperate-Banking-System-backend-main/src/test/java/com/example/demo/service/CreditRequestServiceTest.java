package com.example.demo.service;


import com.example.demo.dto.CreditRequestDto;
import com.example.demo.dto.UpdateStatusRequest;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.CreditRequest;
import com.example.demo.repository.CreditRequestRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreditRequestServiceTest {
    
    @Mock
    private CreditRequestRepository creditRequestRepository;
    
    @InjectMocks
    private CreditRequestService creditRequestService;
    
    private CreditRequest testCreditRequest;
    private CreditRequestDto creditRequestDto;
    
    @BeforeEach
    void setUp() {
        // Setup test credit request
        testCreditRequest = new CreditRequest();
        testCreditRequest.setId("credit123");
        testCreditRequest.setClientId("client123");
        testCreditRequest.setSubmittedBy("rm123");
        testCreditRequest.setRequestAmount(5000000.0);
        testCreditRequest.setTenureMonths(24);
        testCreditRequest.setPurpose("Working capital requirement");
        testCreditRequest.setStatus("Pending");
        testCreditRequest.setRemarks("");
        testCreditRequest.setCreatedAt(LocalDateTime.now());
        
        // Setup credit request DTO
        creditRequestDto = new CreditRequestDto();
        creditRequestDto.setClientId("client123");
        creditRequestDto.setRequestAmount(5000000.0);
        creditRequestDto.setTenureMonths(24);
        creditRequestDto.setPurpose("Working capital requirement");
    }
    
    @Test
    void testCreateCreditRequest_Success() {
        // Arrange
        when(creditRequestRepository.save(any(CreditRequest.class))).thenReturn(testCreditRequest);
        
        // Act
        CreditRequest result = creditRequestService.createCreditRequest(creditRequestDto, "rm123");
        
        // Assert
        assertNotNull(result);
        assertEquals("client123", result.getClientId());
        assertEquals("rm123", result.getSubmittedBy());
        assertEquals(5000000.0, result.getRequestAmount());
        assertEquals(24, result.getTenureMonths());
        assertEquals("Working capital requirement", result.getPurpose());
        assertEquals("Pending", result.getStatus());
        assertEquals("", result.getRemarks());
        assertNotNull(result.getCreatedAt());
        
        verify(creditRequestRepository, times(1)).save(any(CreditRequest.class));
    }
    
    @Test
    void testGetCreditRequestsByRm_Success() {
        // Arrange
        CreditRequest request1 = new CreditRequest();
        request1.setId("credit1");
        request1.setSubmittedBy("rm123");
        request1.setStatus("Pending");
        
        CreditRequest request2 = new CreditRequest();
        request2.setId("credit2");
        request2.setSubmittedBy("rm123");
        request2.setStatus("Approved");
        
        List<CreditRequest> requests = Arrays.asList(request1, request2);
        when(creditRequestRepository.findBySubmittedBy(anyString())).thenReturn(requests);
        
        // Act
        List<CreditRequest> result = creditRequestService.getCreditRequestsByRm("rm123");
        
        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Pending", result.get(0).getStatus());
        assertEquals("Approved", result.get(1).getStatus());
        
        verify(creditRequestRepository, times(1)).findBySubmittedBy("rm123");
    }
    
    @Test
    void testGetCreditRequestsByRm_EmptyList() {
        // Arrange
        when(creditRequestRepository.findBySubmittedBy(anyString())).thenReturn(Arrays.asList());
        
        // Act
        List<CreditRequest> result = creditRequestService.getCreditRequestsByRm("rm999");
        
        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(creditRequestRepository, times(1)).findBySubmittedBy("rm999");
    }
    
    @Test
    void testGetAllCreditRequests_Success() {
        // Arrange
        CreditRequest request1 = new CreditRequest();
        request1.setId("credit1");
        
        CreditRequest request2 = new CreditRequest();
        request2.setId("credit2");
        
        List<CreditRequest> requests = Arrays.asList(request1, request2);
        when(creditRequestRepository.findAll()).thenReturn(requests);
        
        // Act
        List<CreditRequest> result = creditRequestService.getAllCreditRequests();
        
        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        
        verify(creditRequestRepository, times(1)).findAll();
    }
    
    @Test
    void testGetAllCreditRequests_EmptyList() {
        // Arrange
        when(creditRequestRepository.findAll()).thenReturn(Arrays.asList());
        
        // Act
        List<CreditRequest> result = creditRequestService.getAllCreditRequests();
        
        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(creditRequestRepository, times(1)).findAll();
    }
    
    @Test
    void testGetCreditRequestById_Success() {
        // Arrange
        when(creditRequestRepository.findById(anyString())).thenReturn(Optional.of(testCreditRequest));
        
        // Act
        CreditRequest result = creditRequestService.getCreditRequestById("credit123");
        
        // Assert
        assertNotNull(result);
        assertEquals("credit123", result.getId());
        assertEquals("client123", result.getClientId());
        assertEquals(5000000.0, result.getRequestAmount());
        
        verify(creditRequestRepository, times(1)).findById("credit123");
    }
    
    @Test
    void testGetCreditRequestById_NotFound() {
        // Arrange
        when(creditRequestRepository.findById(anyString())).thenReturn(Optional.empty());
        
        // Act & Assert
        ResourceNotFoundException exception = assertThrows(
            ResourceNotFoundException.class,
            () -> creditRequestService.getCreditRequestById("nonexistent")
        );
        
        assertEquals("Credit request not found", exception.getMessage());
        verify(creditRequestRepository, times(1)).findById("nonexistent");
    }
    
    @Test
    void testUpdateCreditRequestStatus_Approve() {
        // Arrange
        when(creditRequestRepository.findById(anyString())).thenReturn(Optional.of(testCreditRequest));
        when(creditRequestRepository.save(any(CreditRequest.class))).thenReturn(testCreditRequest);
        
        UpdateStatusRequest updateRequest = new UpdateStatusRequest();
        updateRequest.setStatus("Approved");
        updateRequest.setRemarks("Good credit history. Approved.");
        
        // Act
        CreditRequest result = creditRequestService.updateCreditRequestStatus("credit123", updateRequest);
        
        // Assert
        assertNotNull(result);
        assertEquals("Approved", result.getStatus());
        assertEquals("Good credit history. Approved.", result.getRemarks());
        
        verify(creditRequestRepository, times(1)).findById("credit123");
        verify(creditRequestRepository, times(1)).save(testCreditRequest);
    }
    
    @Test
    void testUpdateCreditRequestStatus_Reject() {
        // Arrange
        when(creditRequestRepository.findById(anyString())).thenReturn(Optional.of(testCreditRequest));
        when(creditRequestRepository.save(any(CreditRequest.class))).thenReturn(testCreditRequest);
        
        UpdateStatusRequest updateRequest = new UpdateStatusRequest();
        updateRequest.setStatus("Rejected");
        updateRequest.setRemarks("Insufficient collateral.");
        
        // Act
        CreditRequest result = creditRequestService.updateCreditRequestStatus("credit123", updateRequest);
        
        // Assert
        assertNotNull(result);
        assertEquals("Rejected", result.getStatus());
        assertEquals("Insufficient collateral.", result.getRemarks());
        
        verify(creditRequestRepository, times(1)).findById("credit123");
        verify(creditRequestRepository, times(1)).save(testCreditRequest);
    }
    
    @Test
    void testUpdateCreditRequestStatus_WithoutRemarks() {
        // Arrange
        when(creditRequestRepository.findById(anyString())).thenReturn(Optional.of(testCreditRequest));
        when(creditRequestRepository.save(any(CreditRequest.class))).thenReturn(testCreditRequest);
        
        UpdateStatusRequest updateRequest = new UpdateStatusRequest();
        updateRequest.setStatus("Approved");
        updateRequest.setRemarks(null);
        
        // Act
        CreditRequest result = creditRequestService.updateCreditRequestStatus("credit123", updateRequest);
        
        // Assert
        assertNotNull(result);
        assertEquals("Approved", result.getStatus());
        // Remarks should remain unchanged (empty in this case)
        
        verify(creditRequestRepository, times(1)).findById("credit123");
        verify(creditRequestRepository, times(1)).save(testCreditRequest);
    }
    
    @Test
    void testUpdateCreditRequestStatus_NotFound() {
        // Arrange
        when(creditRequestRepository.findById(anyString())).thenReturn(Optional.empty());
        
        UpdateStatusRequest updateRequest = new UpdateStatusRequest();
        updateRequest.setStatus("Approved");
        
        // Act & Assert
        ResourceNotFoundException exception = assertThrows(
            ResourceNotFoundException.class,
            () -> creditRequestService.updateCreditRequestStatus("nonexistent", updateRequest)
        );
        
        assertEquals("Credit request not found", exception.getMessage());
        verify(creditRequestRepository, times(1)).findById("nonexistent");
        verify(creditRequestRepository, never()).save(any(CreditRequest.class));
    }
    
    @Test
    void testUpdateCreditRequestStatus_PendingToPending() {
        // Arrange
        when(creditRequestRepository.findById(anyString())).thenReturn(Optional.of(testCreditRequest));
        when(creditRequestRepository.save(any(CreditRequest.class))).thenReturn(testCreditRequest);
        
        UpdateStatusRequest updateRequest = new UpdateStatusRequest();
        updateRequest.setStatus("Pending");
        updateRequest.setRemarks("Under review");
        
        // Act
        CreditRequest result = creditRequestService.updateCreditRequestStatus("credit123", updateRequest);
        
        // Assert
        assertNotNull(result);
        assertEquals("Pending", result.getStatus());
        assertEquals("Under review", result.getRemarks());
        
        verify(creditRequestRepository, times(1)).save(testCreditRequest);
    }
}