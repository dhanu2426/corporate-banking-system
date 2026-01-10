package com.example.demo.service;


import com.example.demo.dto.ClientRequest;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.Client;
import com.example.demo.repository.ClientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClientServiceTest {
    
    @Mock
    private ClientRepository clientRepository;
    
    @InjectMocks
    private ClientService clientService;
    
    private Client testClient;
    private ClientRequest clientRequest;
    
    @BeforeEach
    void setUp() {
        // Setup test client
        testClient = new Client();
        testClient.setId("client123");
        testClient.setCompanyName("ABC Textiles Ltd");
        testClient.setIndustry("Manufacturing");
        testClient.setAddress("Mumbai");
        
        Client.PrimaryContact contact = new Client.PrimaryContact();
        contact.setName("Raghav Sharma");
        contact.setEmail("raghav@abc.com");
        contact.setPhone("9876543210");
        testClient.setPrimaryContact(contact);
        
        testClient.setAnnualTurnover(25000000.0);
        testClient.setDocumentsSubmitted(true);
        testClient.setRmId("rm123");
        
        // Setup client request DTO
        clientRequest = new ClientRequest();
        clientRequest.setCompanyName("ABC Textiles Ltd");
        clientRequest.setIndustry("Manufacturing");
        clientRequest.setAddress("Mumbai");
        
        ClientRequest.PrimaryContactDto contactDto = new ClientRequest.PrimaryContactDto();
        contactDto.setName("Raghav Sharma");
        contactDto.setEmail("raghav@abc.com");
        contactDto.setPhone("9876543210");
        clientRequest.setPrimaryContact(contactDto);
        
        clientRequest.setAnnualTurnover(25000000.0);
        clientRequest.setDocumentsSubmitted(true);
    }
    
    @Test
    void testCreateClient_Success() {
        // Arrange
        when(clientRepository.save(any(Client.class))).thenReturn(testClient);
        
        // Act
        Client result = clientService.createClient(clientRequest, "rm123");
        
        // Assert
        assertNotNull(result);
        assertEquals("ABC Textiles Ltd", result.getCompanyName());
        assertEquals("Manufacturing", result.getIndustry());
        assertEquals("Mumbai", result.getAddress());
        assertEquals("rm123", result.getRmId());
        assertEquals(25000000.0, result.getAnnualTurnover());
        assertTrue(result.getDocumentsSubmitted());
        
        verify(clientRepository, times(1)).save(any(Client.class));
    }
    
    @Test
    void testGetClientsByRm_Success() {
        // Arrange
        Client client1 = new Client();
        client1.setId("client1");
        client1.setCompanyName("Company 1");
        client1.setRmId("rm123");
        
        Client client2 = new Client();
        client2.setId("client2");
        client2.setCompanyName("Company 2");
        client2.setRmId("rm123");
        
        List<Client> clients = Arrays.asList(client1, client2);
        when(clientRepository.findByRmId(anyString())).thenReturn(clients);
        
        // Act
        List<Client> result = clientService.getClientsByRm("rm123");
        
        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Company 1", result.get(0).getCompanyName());
        assertEquals("Company 2", result.get(1).getCompanyName());
        
        verify(clientRepository, times(1)).findByRmId("rm123");
    }
    
    @Test
    void testGetClientsByRm_EmptyList() {
        // Arrange
        when(clientRepository.findByRmId(anyString())).thenReturn(Arrays.asList());
        
        // Act
        List<Client> result = clientService.getClientsByRm("rm999");
        
        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(clientRepository, times(1)).findByRmId("rm999");
    }
    
    @Test
    void testGetClientById_Success() {
        // Arrange
        when(clientRepository.findById(anyString())).thenReturn(Optional.of(testClient));
        
        // Act
        Client result = clientService.getClientById("client123");
        
        // Assert
        assertNotNull(result);
        assertEquals("client123", result.getId());
        assertEquals("ABC Textiles Ltd", result.getCompanyName());
        
        verify(clientRepository, times(1)).findById("client123");
    }
    
    @Test
    void testGetClientById_NotFound() {
        // Arrange
        when(clientRepository.findById(anyString())).thenReturn(Optional.empty());
        
        // Act & Assert
        ResourceNotFoundException exception = assertThrows(
            ResourceNotFoundException.class,
            () -> clientService.getClientById("nonexistent")
        );
        
        assertEquals("Client not found", exception.getMessage());
        verify(clientRepository, times(1)).findById("nonexistent");
    }
    
    @Test
    void testUpdateClient_Success() {
        // Arrange
        when(clientRepository.findById(anyString())).thenReturn(Optional.of(testClient));
        when(clientRepository.save(any(Client.class))).thenReturn(testClient);
        
        clientRequest.setCompanyName("Updated Company Name");
        clientRequest.setAnnualTurnover(30000000.0);
        
        // Act
        Client result = clientService.updateClient("client123", clientRequest, "rm123");
        
        // Assert
        assertNotNull(result);
        verify(clientRepository, times(1)).findById("client123");
        verify(clientRepository, times(1)).save(any(Client.class));
    }
    
    @Test
    void testUpdateClient_NotFound() {
        // Arrange
        when(clientRepository.findById(anyString())).thenReturn(Optional.empty());
        
        // Act & Assert
        ResourceNotFoundException exception = assertThrows(
            ResourceNotFoundException.class,
            () -> clientService.updateClient("nonexistent", clientRequest, "rm123")
        );
        
        assertEquals("Client not found", exception.getMessage());
        verify(clientRepository, times(1)).findById("nonexistent");
        verify(clientRepository, never()).save(any(Client.class));
    }
    
    @Test
    void testUpdateClient_WrongRM() {
        // Arrange
        when(clientRepository.findById(anyString())).thenReturn(Optional.of(testClient));
        
        // Act & Assert
        ResourceNotFoundException exception = assertThrows(
            ResourceNotFoundException.class,
            () -> clientService.updateClient("client123", clientRequest, "wrongRM")
        );
        
        assertEquals("Client not found", exception.getMessage());
        verify(clientRepository, times(1)).findById("client123");
        verify(clientRepository, never()).save(any(Client.class));
    }
    
    @Test
    void testSearchClients_ByCompanyName() {
        // Arrange
        List<Client> clients = Arrays.asList(testClient);
        when(clientRepository.findByCompanyNameContainingIgnoreCase(anyString())).thenReturn(clients);
        
        // Act
        List<Client> result = clientService.searchClients("ABC", null);
        
        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("ABC Textiles Ltd", result.get(0).getCompanyName());
        
        verify(clientRepository, times(1)).findByCompanyNameContainingIgnoreCase("ABC");
        verify(clientRepository, never()).findByIndustryIgnoreCase(anyString());
    }
    
    @Test
    void testSearchClients_ByIndustry() {
        // Arrange
        List<Client> clients = Arrays.asList(testClient);
        when(clientRepository.findByIndustryIgnoreCase(anyString())).thenReturn(clients);
        
        // Act
        List<Client> result = clientService.searchClients(null, "Manufacturing");
        
        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Manufacturing", result.get(0).getIndustry());
        
        verify(clientRepository, times(1)).findByIndustryIgnoreCase("Manufacturing");
        verify(clientRepository, never()).findByCompanyNameContainingIgnoreCase(anyString());
    }
    
    @Test
    void testSearchClients_NoFilters() {
        // Arrange
        List<Client> clients = Arrays.asList(testClient);
        when(clientRepository.findAll()).thenReturn(clients);
        
        // Act
        List<Client> result = clientService.searchClients(null, null);
        
        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        
        verify(clientRepository, times(1)).findAll();
    }
    
    @Test
    void testSearchClients_EmptyCompanyName() {
        // Arrange
        List<Client> clients = Arrays.asList(testClient);
        when(clientRepository.findAll()).thenReturn(clients);
        
        // Act
        List<Client> result = clientService.searchClients("", "");
        
        // Assert
        assertNotNull(result);
        verify(clientRepository, times(1)).findAll();
    }
}