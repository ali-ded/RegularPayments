package com.payments.regulatorymodule.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class JournalEntryService {
    private final RestTemplate restTemplate = new RestTemplate();
    private final Logger LOGGER = LoggerFactory.getLogger(JournalEntryService.class);

    public void writeOffPayment(long paymentId) {
        final String URI = "http://localhost:8081/api/journal-entry/write-off-payment";
        ResponseEntity<Void> response = restTemplate.exchange(URI + "/{paymentId}",
                HttpMethod.POST, null, Void.class, paymentId);
        LOGGER.info("{}/{}: {}", URI, paymentId, response.getStatusCode());
    }
}
