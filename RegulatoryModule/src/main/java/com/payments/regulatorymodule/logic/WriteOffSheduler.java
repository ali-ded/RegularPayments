package com.payments.regulatorymodule.logic;

import com.payments.regulatorymodule.dto.PaymentDto;
import com.payments.regulatorymodule.service.JournalEntryService;
import com.payments.regulatorymodule.service.PaymentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WriteOffSheduler {
    private final PaymentService paymentService;
    private final JournalEntryService journalEntryService;
    private final Logger LOGGER;

    @Autowired
    public WriteOffSheduler(PaymentService paymentService, JournalEntryService journalEntryService) {
        this.paymentService = paymentService;
        this.journalEntryService = journalEntryService;
        LOGGER = LoggerFactory.getLogger(WriteOffSheduler.class);
    }

    @Scheduled(cron = "${scheduler.interval.cron}")
    public void checkAndWriteOffPayments() {
        LOGGER.info("Getting a list of all payments");
        List<PaymentDto> paymentDtos = paymentService.getAllPayments();
        LOGGER.info("Check and write off for each payment");
        paymentDtos.forEach(paymentDto -> journalEntryService.writeOffPayment(paymentDto.getId()));
    }
}
