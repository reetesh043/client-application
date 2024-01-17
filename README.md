# client-application
Demo application to collect client KYC data and submit the record to KYC application to perform KYC.

## To implement the webhook in the spring boot Application, follow the below steps :


1. Initially, we will develop an API to collect customer data. Subsequently, we will send this data to another application(third party) to perform KYC on the customer's identity and personal details. The third-party application will then respond with a JSON response containing the result of KYC.

```java
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
```

2. Next, we will create another API, and this API URL is registered with the third-party application(client-kyc-application) where the customer data was sent. Upon successful verification, the third-party system will trigger an automatic call to our designated URL, furnishing a KYC status response. Alternatively, we can phrase it as the clients receiving automated notifications from the third party through the invocation of this registered URL, which is commonly referred to as the “Webhook URL.”
```java
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

        // Process the incoming payload from the third-party service
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
```

## Sample Request Response

1. Create Client Record:
```curl
curl --location 'http://localhost:8080/api/clients' \
--header 'Content-Type: application/json' \
--data-raw '{
    "name": "reetesh",
    "dob": "03-01-1990",
    "email": "reet.test@test.com",
    "address": "903, Tower Gateway, London, E17 2BJ",
    "taxId": "789321456"
}'
```
Response:

```json
{
    "clientId": 16,
    "name": "reetesh",
    "dob": "03-01-1990",
    "taxId": "789321456",
    "address": "903, Tower Gateway, London, E17 2BJ",
    "email": "reet.test@test.com",
    "kycStatus": null
}
```

2. Get Updated Client Record After KYC
```curl
curl --location 'http://localhost:8080/api/clients/16'
}'
```
Response:

```json
{
    "clientId": 16,
    "name": "reetesh",
    "dob": "03-01-1990",
    "taxId": "789321456",
    "address": "903, Tower Gateway, London, E17 2BJ",
    "email": "reet.test@test.com",
    "kycStatus": "KYC_COMPLETED"
}
