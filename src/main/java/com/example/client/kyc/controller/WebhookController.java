package com.example.client.kyc.controller;

import com.example.client.kyc.model.WebhookNotification;
import com.example.client.kyc.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api")
public class WebhookController {

    private final ClientRepository clientRepository;

    @PostMapping(value = "/webhook", consumes = APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public void handleCustomerWebhookData(@RequestBody WebhookNotification webhookNotificationPayload) {
        log.info("Webhook Notification Received: {}", webhookNotificationPayload);

        // Process the incoming payload from the third party service
        //Save KYC the status to the client record
        // Return a response indicating successful processing
        updateClientRecord(webhookNotificationPayload);
    }

    @Async
    public void updateClientRecord(WebhookNotification webhookNotificationPayload) {
        try {
            var client = clientRepository.findById(webhookNotificationPayload.getClientId());
            if (client.isPresent()) {
                var updateClient = client.get();
                updateClient.setKycStatus(webhookNotificationPayload.getKycStatus());
                clientRepository.save(updateClient);
                log.info("Client record updated.");
            }
        } catch (Exception ex) {
            log.error("Error occurred while updating client record: {}", ex.getMessage());
        }
    }
}
