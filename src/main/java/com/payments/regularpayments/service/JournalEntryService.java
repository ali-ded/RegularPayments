package com.payments.regularpayments.service;

import com.payments.regularpayments.dto.JournalEntryDto;
import com.payments.regularpayments.exception.JournalEntryNotFoundException;
import com.payments.regularpayments.exception.PaymentNotFoundException;
import com.payments.regularpayments.mapper.JournalEntryMapper;
import com.payments.regularpayments.model.JournalEntryEntity;
import com.payments.regularpayments.model.PaymentEntity;
import com.payments.regularpayments.repository.JournalEntryRepository;
import com.payments.regularpayments.repository.PaymentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class JournalEntryService {
    private final JournalEntryRepository journalEntryRepository;
    private final PaymentRepository paymentRepository;
    private final JournalEntryMapper journalEntryMapper;
    private final Logger LOGGER = LoggerFactory.getLogger(JournalEntryService.class);

    @Autowired
    public JournalEntryService(JournalEntryRepository journalEntryRepository,
                               PaymentRepository paymentRepository, JournalEntryMapper journalEntryMapper) {
        this.journalEntryRepository = journalEntryRepository;
        this.paymentRepository = paymentRepository;
        this.journalEntryMapper = journalEntryMapper;
    }

    public JournalEntryDto create(long paymentId) throws PaymentNotFoundException {
        LOGGER.info("Creating journal entry with payment id # {}", paymentId);
        PaymentEntity paymentEntity = paymentRepository
                .findById(paymentId)
                .orElseThrow(() -> new PaymentNotFoundException("По заданному идентификатору платеж не найден"));
        JournalEntryEntity journalEntryEntity = journalEntryRepository.save(new JournalEntryEntity(paymentEntity));
        return journalEntryMapper.journalEntryEntityToJournalEntryDto(journalEntryEntity);
    }

    public void delete(long journalEntryId) throws JournalEntryNotFoundException {
        LOGGER.info("Deleting journal entry with id # {}", journalEntryId);
        if (journalEntryRepository.storno(journalEntryId, LocalDateTime.now()) != 1) {
            throw new JournalEntryNotFoundException(String.format(
                    "По идентификатору %d проводка не найдена", journalEntryId));
        }
    }

    public JournalEntryDto get(long journalEntryId) throws JournalEntryNotFoundException {
        LOGGER.info("Search journal entry with id # {}", journalEntryId);
        JournalEntryEntity journalEntryEntity = journalEntryRepository
                .findById(journalEntryId)
                .orElseThrow(() -> new JournalEntryNotFoundException("По заданному идентификатору проводка не найдена"));
        return journalEntryMapper.journalEntryEntityToJournalEntryDto(journalEntryEntity);
    }

    public List<JournalEntryDto> getAll(long paymentId) {
        LOGGER.info("Search all journal entries");
        List<JournalEntryEntity> journalEntryEntities = journalEntryRepository.findJournalEntryEntitiesByPaymentId(paymentId);
        return journalEntryEntities.stream()
                .map(journalEntryMapper::journalEntryEntityToJournalEntryDto)
                .collect(Collectors.toList());
    }
}
