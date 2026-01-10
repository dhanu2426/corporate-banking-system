package com.example.demo.service;

import com.example.demo.dto.ClientRequest;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.Client;
import com.example.demo.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClientService {
    
    @Autowired
    private ClientRepository clientRepository;
    
    public Client createClient(ClientRequest request, String rmId) {
        Client client = new Client();
        client.setCompanyName(request.getCompanyName());
        client.setIndustry(request.getIndustry());
        client.setAddress(request.getAddress());
        
        Client.PrimaryContact contact = new Client.PrimaryContact();
        contact.setName(request.getPrimaryContact().getName());
        contact.setEmail(request.getPrimaryContact().getEmail());
        contact.setPhone(request.getPrimaryContact().getPhone());
        client.setPrimaryContact(contact);
        
        client.setAnnualTurnover(request.getAnnualTurnover());
        client.setDocumentsSubmitted(request.getDocumentsSubmitted());
        client.setRmId(rmId);
        
        return clientRepository.save(client);
    }
    
    public List<Client> getClientsByRm(String rmId) {
        return clientRepository.findByRmId(rmId);
    }
    
    public Client getClientById(String clientId) {
        return clientRepository.findById(clientId)
                .orElseThrow(() -> new ResourceNotFoundException("Client not found"));
    }
    
    public Client updateClient(String clientId, ClientRequest request, String rmId) {
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new ResourceNotFoundException("Client not found"));
        
        // Check if the client belongs to this RM
        if (!client.getRmId().equals(rmId)) {
            throw new ResourceNotFoundException("Client not found");
        }
        
        client.setCompanyName(request.getCompanyName());
        client.setIndustry(request.getIndustry());
        client.setAddress(request.getAddress());
        
        Client.PrimaryContact contact = new Client.PrimaryContact();
        contact.setName(request.getPrimaryContact().getName());
        contact.setEmail(request.getPrimaryContact().getEmail());
        contact.setPhone(request.getPrimaryContact().getPhone());
        client.setPrimaryContact(contact);
        
        client.setAnnualTurnover(request.getAnnualTurnover());
        client.setDocumentsSubmitted(request.getDocumentsSubmitted());
        
        return clientRepository.save(client);
    }
    
    public List<Client> searchClients(String companyName, String industry) {
        if (companyName != null && !companyName.isEmpty()) {
            return clientRepository.findByCompanyNameContainingIgnoreCase(companyName);
        } else if (industry != null && !industry.isEmpty()) {
            return clientRepository.findByIndustryIgnoreCase(industry);
        }
        return clientRepository.findAll();
    }
}