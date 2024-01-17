package com.example.client.kyc.service;

import com.example.client.kyc.model.Client;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class KYCService {

    RestTemplate restTemplate = new RestTemplate();

    @Value("${kycService.url}")
    private String kycServiceUrl;

    public void submitDataForKyc(final Client client) {
        HttpEntity<Client> request = new HttpEntity<>(client);
        restTemplate.exchange(kycServiceUrl, HttpMethod.POST, request, Void.class);
    }
}
