package com.example.client.kyc.controller;

import com.example.client.kyc.model.Client;
import com.example.client.kyc.repository.ClientRepository;
import com.example.client.kyc.service.ClientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api")
public class ClientController {

    private final ClientService ClientService;

    private final ClientRepository clientRepository;


    @PostMapping("/clients")
    public ResponseEntity<Client> createClient(@RequestBody Client request) {

        var response = ClientService.createAndValidateCustomerDetails(request);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/clients/{clientId}")
    public ResponseEntity<Client> getClient(@PathVariable BigInteger clientId) {
        var response = clientRepository.findById(clientId);
        if (response.isPresent()) {
            return ResponseEntity.ok().body(response.get());
        }
        throw new RuntimeException("Client Not Found");
    }
}
