package com.rest.data.demoservice.service;

import java.util.List;

import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import com.rest.data.demoservice.persistence.entity.Client;
import com.rest.data.demoservice.persistence.repository.ClientRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository clientRepository;

    public Client findClientById(String clientId) {
        return clientRepository.findById(clientId)
            .orElseThrow(() -> new ResourceNotFoundException("Client not found by provided id: " + clientId));
    }

    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }
}
