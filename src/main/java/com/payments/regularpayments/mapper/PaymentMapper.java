package com.payments.regularpayments.mapper;

import com.payments.regularpayments.model.dto.PaymentCreateDto;
import com.payments.regularpayments.model.dto.PaymentDto;
import com.payments.regularpayments.model.entity.BankAccountEntity;
import com.payments.regularpayments.model.entity.PaymentEntity;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PaymentMapper {

    @Mapping(source = "paymentCreateDto.creditAccount", target = "creditAccount.id")
    @Mapping(source = "paymentCreateDto.debitAccount", target = "debitAccount.id")
    PaymentEntity paymentDtoToPaymentEntity(PaymentCreateDto paymentCreateDto);

//    BankAccountEntity bankAccountIdToBankAccountEntity(Long id);

    @Mapping(source = "paymentEntity.creditAccount.id", target = "creditAccount")
    @Mapping(source = "paymentEntity.debitAccount.id", target = "debitAccount")
    PaymentDto paymentEntityToPaymentDto(PaymentEntity paymentEntity);

    @InheritInverseConfiguration
    PaymentEntity paymentDtoToPaymentEntity(PaymentDto paymentDto);
}
