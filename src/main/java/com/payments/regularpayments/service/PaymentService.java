package com.payments.regularpayments.service;

import com.payments.regularpayments.exception.BankAccountNotFoundException;
import com.payments.regularpayments.exception.IdenticalBankAccountsException;
import com.payments.regularpayments.exception.PaymentNotFoundException;
import com.payments.regularpayments.mapper.PaymentMapper;
import com.payments.regularpayments.model.dto.PaymentCreateDto;
import com.payments.regularpayments.model.dto.PaymentDto;
import com.payments.regularpayments.model.entity.PaymentEntity;
import com.payments.regularpayments.repository.BankAccountRepository;
import com.payments.regularpayments.repository.PaymentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public PaymentDto save(PaymentCreateDto paymentCreateDto) throws BankAccountNotFoundException, IdenticalBankAccountsException {
        isCorrectBankAccounts(paymentCreateDto);
        PaymentEntity paymentEntity = paymentRepository.save(paymentMapper.paymentCreateDtoToPaymentEntity(paymentCreateDto));
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
        isCorrectBankAccounts(paymentDto);
        if (paymentRepository.existsById(paymentDto.getId())) {
            paymentRepository.save(paymentMapper.paymentDtoToPaymentEntity(paymentDto));
        } else {
            throw new PaymentNotFoundException("Невозможно обновить данные. По указанному идентификатору платеж не найден");
        }
    }
}
