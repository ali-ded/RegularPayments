package com.payments.regularpayments.service;

import com.payments.regularpayments.exception.BankAccountNotFoundException;
import com.payments.regularpayments.exception.IdenticalBankAccountsException;
import com.payments.regularpayments.exception.PaymentNotFoundException;
import com.payments.regularpayments.mapper.PaymentMapper;
import com.payments.regularpayments.dto.PaymentCreateDto;
import com.payments.regularpayments.dto.PaymentDto;
import com.payments.regularpayments.model.PaymentEntity;
import com.payments.regularpayments.repository.BankAccountRepository;
import com.payments.regularpayments.repository.PaymentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final BankAccountRepository bankAccountRepository;
    private final PaymentMapper paymentMapper;
    private final Logger LOGGER = LoggerFactory.getLogger(PaymentService.class);

    @Autowired
    public PaymentService(PaymentRepository paymentRepository, BankAccountRepository bankAccountRepository, PaymentMapper paymentMapper) {
        this.paymentRepository = paymentRepository;
        this.bankAccountRepository = bankAccountRepository;
        this.paymentMapper = paymentMapper;
    }

    public PaymentDto create(PaymentCreateDto paymentCreateDto) throws BankAccountNotFoundException, IdenticalBankAccountsException {
        LOGGER.info("Create a new payment");
        isCorrectBankAccounts(paymentCreateDto);
        PaymentEntity paymentEntity = paymentRepository.save(paymentMapper.paymentCreateDtoToPaymentEntity(paymentCreateDto));
        return paymentMapper.paymentEntityToPaymentDto(paymentEntity);
    }

    public PaymentDto findById(long id) throws PaymentNotFoundException {
        LOGGER.info("Payment search by ID {}", id);
        PaymentEntity paymentEntity = paymentRepository.findById(id)
                .orElseThrow(() -> new PaymentNotFoundException("По заданному идентификатору платеж не найден"));
        return paymentMapper.paymentEntityToPaymentDto(paymentEntity);
    }

    private void isCorrectBankAccounts(PaymentCreateDto paymentDto) throws BankAccountNotFoundException, IdenticalBankAccountsException {
        if (paymentDto.getCreditAccount().equals(paymentDto.getDebitAccount())) {
            throw new IdenticalBankAccountsException("Одинаковый счет плательщика и получателя");
        }
        if (!bankAccountRepository.existsById(paymentDto.getCreditAccount())) {
            LOGGER.warn("Payer account number '{}' not found", paymentDto.getCreditAccount());
            throw new BankAccountNotFoundException("Номер счета плательщика не найден");
        }
        if (!bankAccountRepository.existsById(paymentDto.getDebitAccount())) {
            LOGGER.warn("Beneficiary account number '{}' not found", paymentDto.getDebitAccount());
            throw new BankAccountNotFoundException("Номер счета получателя не найден");
        }
    }

    public void update(PaymentDto paymentDto) throws PaymentNotFoundException, BankAccountNotFoundException, IdenticalBankAccountsException {
        LOGGER.info("Update payment data by ID {}", paymentDto.getId());
        isCorrectBankAccounts(paymentDto);
        if (paymentRepository.existsById(paymentDto.getId())) {
            paymentRepository.save(paymentMapper.paymentDtoToPaymentEntity(paymentDto));
        } else {
            throw new PaymentNotFoundException("Невозможно обновить данные. По указанному идентификатору платеж не найден");
        }
    }

    public List<PaymentDto> getPaymentEntitiesByPayerInn(long inn) {
        LOGGER.info("Search for a list of payments by payer's INN {}", inn);
        List<PaymentEntity> paymentEntities = paymentRepository.findPaymentEntitiesByPayerInn(inn);
        return paymentEntities.stream()
                .map(paymentMapper::paymentEntityToPaymentDto)
                .collect(Collectors.toList());
    }

    public List<PaymentDto> getPaymentEntitiesByRecipientInn(long inn) {
        LOGGER.info("Search for a list of payments by recipient's INN {}", inn);
        List<PaymentEntity> paymentEntities = paymentRepository.findPaymentEntitiesByRecipientInn(inn);
        return paymentEntities.stream()
                .map(paymentMapper::paymentEntityToPaymentDto)
                .collect(Collectors.toList());
    }
}
