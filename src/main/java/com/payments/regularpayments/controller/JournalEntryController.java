package com.payments.regularpayments.controller;

import com.payments.regularpayments.dto.JournalEntryDto;
import com.payments.regularpayments.exception.PaymentNotFoundException;
import com.payments.regularpayments.model.JournalEntryEntity;
import com.payments.regularpayments.service.JournalEntryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/journal-entry", produces = MediaType.APPLICATION_JSON_VALUE)
public class JournalEntryController {
    private final JournalEntryService journalEntryService;
    private final Logger LOGGER;

    public JournalEntryController(JournalEntryService journalEntryService) {
        this.journalEntryService = journalEntryService;
        LOGGER = LoggerFactory.getLogger(JournalEntryController.class);
    }

    @PostMapping("/create")
    public ResponseEntity<JournalEntryDto> createJournalEntry(@RequestParam("paymentId") final long paymentId) throws PaymentNotFoundException {
        JournalEntryDto journalEntryDto;
        try {
            journalEntryDto = journalEntryService.create(paymentId);
        } catch (PaymentNotFoundException e) {
            LOGGER.info("POST /create?paymentId={}: {}", paymentId, HttpStatus.NOT_FOUND);
            throw e;
        }
        LOGGER.info("POST /create?paymentId={}: {}", paymentId, HttpStatus.OK);
        return ResponseEntity.ok(journalEntryDto);
    }
}
