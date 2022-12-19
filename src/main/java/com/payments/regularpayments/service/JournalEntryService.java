package com.payments.regularpayments.service;

import com.payments.regularpayments.dto.JournalEntryDto;
import com.payments.regularpayments.exception.JournalEntryNotFoundException;
import com.payments.regularpayments.exception.PaymentNotFoundException;
import com.payments.regularpayments.mapper.JournalEntryMapper;
import com.payments.regularpayments.model.BankAccountEntity;
import com.payments.regularpayments.model.JournalEntryEntity;
import com.payments.regularpayments.model.PaymentEntity;
import com.payments.regularpayments.repository.BankAccountRepository;
import com.payments.regularpayments.repository.JournalEntryRepository;
import com.payments.regularpayments.repository.PaymentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class JournalEntryService {
    private final JournalEntryRepository journalEntryRepository;
    private final PaymentRepository paymentRepository;
    private final BankAccountRepository bankAccountRepository;
    private final JournalEntryMapper journalEntryMapper;
    private final Logger LOGGER = LoggerFactory.getLogger(JournalEntryService.class);

    @Autowired
    public JournalEntryService(JournalEntryRepository journalEntryRepository,
                               PaymentRepository paymentRepository,
                               BankAccountRepository bankAccountRepository,
                               JournalEntryMapper journalEntryMapper) {
        this.journalEntryRepository = journalEntryRepository;
        this.paymentRepository = paymentRepository;
        this.bankAccountRepository = bankAccountRepository;
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

    @Transactional
    public void writeOffPayment(long paymentId) throws PaymentNotFoundException, JournalEntryNotFoundException {
        LocalDateTime lastWriteOffDateTime = null;
        try {
            LOGGER.info("Getting the date and time of the last write-off of payment");
            lastWriteOffDateTime = lastWriteOff(paymentId).getPaymentDate();
        } catch (JournalEntryNotFoundException e) {
            LOGGER.info("There were no write-off for this payment");
        }
        LOGGER.info("Creating the new write-off of payment");
        JournalEntryDto journalEntryDto = create(paymentId);
        PaymentEntity paymentEntity = paymentRepository.findById(paymentId).orElseThrow(() ->
                new PaymentNotFoundException(String.format("Платеж по номеру %d не найден", paymentId)));
        Long writeOffPeriodInMinutes = paymentEntity.getWriteOffPeriod();
        LOGGER.info("Checking if a write-off is necessary");
        boolean isItTimeToWriteOffPayment = true;
        if (lastWriteOffDateTime != null) {
            isItTimeToWriteOffPayment = LocalDateTime.now()
                    .isAfter(lastWriteOffDateTime.plusMinutes(writeOffPeriodInMinutes));
        }
        if (isItTimeToWriteOffPayment) {
            LOGGER.info("Execution of a transaction in the amount of {}", paymentEntity.getTransactionAmount());
            BankAccountEntity payerBankAccount = paymentEntity.getCreditAccount();
            BankAccountEntity recipientBankAccount = paymentEntity.getDebitAccount();
            payerBankAccount.debit(paymentEntity.getTransactionAmount());
            recipientBankAccount.credit(paymentEntity.getTransactionAmount());
            bankAccountRepository.save(payerBankAccount);
            bankAccountRepository.save(recipientBankAccount);
        } else {
            LOGGER.info("At the moment there is no need to write off the payment");
            delete(journalEntryDto.getId());
        }
    }

    public JournalEntryDto lastWriteOff(long paymentId) throws JournalEntryNotFoundException {
        Page<JournalEntryEntity> journalEntryEntityPage = journalEntryRepository.getLastWriteOff(paymentId,
                PageRequest.of(0, 1));
        return journalEntryMapper.journalEntryEntityToJournalEntryDto(
                journalEntryEntityPage
                        .get()
                        .findFirst()
                        .orElseThrow(() -> new JournalEntryNotFoundException(
                                String.format("По номеру платежа %d ни одна проводка не зарегистрирована", paymentId))));
    }
}
