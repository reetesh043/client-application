package com.example.client.kyc.service;

import com.example.client.kyc.model.Client;
import com.example.client.kyc.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ClientService {

    private final ClientRepository clientRepository;

    private final KYCService kycService;

    public Client createAndValidateCustomerDetails(final Client request) {
        var response = clientRepository.save(request);
        log.info("Client record created successfully: {}", response);
        submitKycData(response);
        return response;
    }

    @Async
    private void submitKycData(final Client response) {
        kycService.submitDataForKyc(response);
    }
}
