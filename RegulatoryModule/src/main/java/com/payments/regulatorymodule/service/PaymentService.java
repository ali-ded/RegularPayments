package com.payments.regulatorymodule.service;

import com.payments.regulatorymodule.dto.PaymentDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class PaymentService {
    private final RestTemplate restTemplate = new RestTemplate();
    private final Logger LOGGER = LoggerFactory.getLogger(PaymentService.class);

    public List<PaymentDto> getAllPayments() {
        final String URI = "http://localhost:8081/api/payment/get-all-payments";
        ResponseEntity<List<PaymentDto>> response = restTemplate.exchange(URI,
                HttpMethod.GET, null, new ParameterizedTypeReference<>(){});
        LOGGER.info("{}: {}", URI, response.getStatusCode());
        return response.getBody();
    }
}
